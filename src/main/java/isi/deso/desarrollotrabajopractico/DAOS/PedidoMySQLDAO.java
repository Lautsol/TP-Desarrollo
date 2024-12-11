
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.modelo.Cliente;
import isi.deso.desarrollotrabajopractico.modelo.Estado;
import isi.deso.desarrollotrabajopractico.modelo.MercadoPago;
import isi.deso.desarrollotrabajopractico.modelo.Pago;
import isi.deso.desarrollotrabajopractico.modelo.Pedido;
import isi.deso.desarrollotrabajopractico.modelo.TipoDePago;
import isi.deso.desarrollotrabajopractico.modelo.Transferencia;
import isi.deso.desarrollotrabajopractico.modelo.Vendedor;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PedidoMySQLDAO implements PedidoDAO {
    
    private static Connection con;
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/grupo11";
    private static final String USER = "root";
    private static final String PASSWORD = "grupo11";
    private static PedidoMySQLDAO instancia; 
    
    private PedidoMySQLDAO() {
    }
    
    public static PedidoMySQLDAO obtenerInstancia() {
        
        if(instancia == null){
            instancia = new PedidoMySQLDAO();
        }
        
        return instancia;
    }
    
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        
        con = null;
        try {
            Class.forName(driver);
            con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw e; 
        }
        return con;
    }
    
    public int crearPedido(Pedido pedido) {
        
        String sqlPedido = "INSERT INTO grupo11.pedidos (id_cliente, id_vendedor, total, tipo_pago, estado, id_pago) VALUES (?, ?, ?, ?, ?, ?)";

        int pedidoId = -1; // Inicializa el id con un valor por defecto

        try (Connection connection = getConnection(); 
             PreparedStatement pstmtPedido = connection.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {

            pstmtPedido.setInt(1, pedido.getCliente().getId());
            pstmtPedido.setInt(2, pedido.getVendedor().getId());
            pstmtPedido.setDouble(3, pedido.getTotal());
            pstmtPedido.setString(4, pedido.getTipoPago().toString());
            pstmtPedido.setString(5, pedido.getEstado().toString());
            pstmtPedido.setNull(6, java.sql.Types.INTEGER); 

            pstmtPedido.executeUpdate();

            try (ResultSet generatedKeys = pstmtPedido.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pedidoId = generatedKeys.getInt(1); 
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, "Error al crear el pedido", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, "Clase no encontrada", ex);
        }

        return pedidoId; 
    }

    public void actualizarPedido(Pedido pedido) {
        
        String sqlPedido = "UPDATE grupo11.pedidos SET total = ?, tipo_pago = ?, estado = ?, id_pago = ? WHERE id = ?";
      
        try (Connection connection = getConnection(); 
             PreparedStatement pstmtPedido = connection.prepareStatement(sqlPedido)) {

            pstmtPedido.setDouble(1, pedido.getTotal());
            pstmtPedido.setString(2, pedido.getTipoPago().toString());
            pstmtPedido.setString(3, pedido.getEstado().toString());
            pstmtPedido.setInt(4, pedido.getPago().getId());
            pstmtPedido.setInt(5, pedido.getId_pedido());
            
            pstmtPedido.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
    
    public ArrayList<Pedido> obtenerTodosLosPedidos() {
        
        ArrayList<Pedido> pedidos = new ArrayList<>();
        
        String sql = "SELECT p.id, p.id_cliente, p.id_vendedor, p.total, p.tipo_pago, p.estado, p.id_pago, pa.monto, pa.fecha, "
                   + "c.nombre AS cliente_nombre, c.cbu AS cliente_cbu, c.cuit AS cliente_cuit, "
                   + "v.nombre AS vendedor_nombre "
                   + "FROM pedidos p "
                   + "LEFT JOIN pagos pa ON p.id_pago = pa.id "
                   + "LEFT JOIN clientes c ON p.id_cliente = c.id "  
                   + "LEFT JOIN vendedores v ON p.id_vendedor = v.id "  
                   + "ORDER BY p.id"; // LEFT JOIN para que no se excluyan los pedidos sin pago

        try (Connection connection = getConnection(); 
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId_pedido(rs.getInt("id"));

                // Crear cliente si existe
                Integer clienteId = rs.getInt("id_cliente");
                if (rs.wasNull()) {
                    pedido.setCliente(null);  
                } else {
                    String clienteNombre = rs.getString("cliente_nombre");
                    Long clienteCbu = rs.getLong("cliente_cbu");
                    Long clienteCuit = rs.getLong("cliente_cuit");
                    Cliente cliente = new Cliente();
                    cliente.setId(clienteId);
                    cliente.setNombre(clienteNombre);
                    cliente.setCbu(clienteCbu);
                    cliente.setCuit(clienteCuit);
                    pedido.setCliente(cliente);
                }

                // Crear vendedor si existe
                Integer vendedorId = rs.getInt("id_vendedor");
                if (rs.wasNull()) {
                    pedido.setVendedor(null);  
                } else {
                    String vendedorNombre = rs.getString("vendedor_nombre");
                    Vendedor vendedor = new Vendedor();
                    vendedor.setId(vendedorId);
                    vendedor.setNombre(vendedorNombre);
                    pedido.setVendedor(vendedor);
                }

                pedido.setTotal(rs.getDouble("total"));
                pedido.setTipoPago(TipoDePago.valueOf(rs.getString("tipo_pago")));
                pedido.setEstado(Estado.valueOf(rs.getString("estado")));

                // Manejar el pago si existe
                Integer idPago = rs.getObject("id_pago", Integer.class);
                if (idPago == null) {
                    pedido.setFormaDePago(null);
                } else {
                    String tipoPago = rs.getString("tipo_pago");
                    Pago pago = null;
                    double monto = rs.getDouble("monto");
                    Date fecha = rs.getDate("fecha");
                    LocalDate fechaPago = fecha.toLocalDate();

                    if ("TRANSFERENCIA".equalsIgnoreCase(tipoPago)) {
                        Transferencia transferencia = new Transferencia();
                        transferencia.setId(idPago);
                        transferencia.setMonto(monto);
                        transferencia.setFecha(fechaPago);
                        pago = transferencia;

                    } else if ("MERCADOPAGO".equalsIgnoreCase(tipoPago)) {
                        MercadoPago mercadoPago = new MercadoPago();
                        mercadoPago.setId(idPago);
                        mercadoPago.setMonto(monto);
                        mercadoPago.setFecha(fechaPago);
                        pago = mercadoPago;
                    }

                    pedido.setFormaDePago(pago);
                }

                pedidos.add(pedido);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pedidos;
    }

    public void eliminarPedido(int id) {
        
        String sql = "DELETE FROM pedidos WHERE id = ?";  

        try (Connection connection = getConnection(); 
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public ArrayList<Pedido> buscarPedidosPorCliente(int id_cliente) {
        
        ArrayList<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.id, p.id_cliente, p.id_vendedor, p.total, p.tipo_pago, p.estado, p.id_pago, pa.monto, pa.fecha, "
                   + "c.nombre AS cliente_nombre, c.cbu AS cliente_cbu, c.cuit AS cliente_cuit, "
                   + "v.nombre AS vendedor_nombre "
                   + "FROM pedidos p "
                   + "LEFT JOIN pagos pa ON p.id_pago = pa.id "
                   + "LEFT JOIN clientes c ON p.id_cliente = c.id "  
                   + "LEFT JOIN vendedores v ON p.id_vendedor = v.id "
                   + "WHERE p.id_cliente = ?"; 

        try (Connection connection = getConnection(); 
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id_cliente);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = new Pedido();
                    pedido.setId_pedido(rs.getInt("id"));

                    Integer clienteId = rs.getInt("id_cliente");
                    if (rs.wasNull()) {
                        pedido.setCliente(null);
                    } else {
                        String clienteNombre = rs.getString("cliente_nombre");
                        Long clienteCbu = rs.getLong("cliente_cbu");
                        Long clienteCuit = rs.getLong("cliente_cuit");
                        Cliente cliente = new Cliente();
                        cliente.setId(clienteId);
                        cliente.setNombre(clienteNombre);
                        cliente.setCbu(clienteCbu);
                        cliente.setCuit(clienteCuit);
                        pedido.setCliente(cliente);
                    }

                    // Crear vendedor si existe
                    Integer vendedorId = rs.getInt("id_vendedor");
                    if (rs.wasNull()) {
                        pedido.setVendedor(null);
                    } else {
                        String vendedorNombre = rs.getString("vendedor_nombre");
                        Vendedor vendedor = new Vendedor();
                        vendedor.setId(vendedorId);
                        vendedor.setNombre(vendedorNombre);
                        pedido.setVendedor(vendedor);
                    }

                    pedido.setTotal(rs.getDouble("total"));
                    pedido.setTipoPago(TipoDePago.valueOf(rs.getString("tipo_pago")));
                    pedido.setEstado(Estado.valueOf(rs.getString("estado")));

                    // Manejar el pago si existe
                    Integer idPago = rs.getObject("id_pago", Integer.class);
                    if (idPago == null) {
                        pedido.setFormaDePago(null);
                    } else {
                        String tipoPago = rs.getString("tipo_pago");
                        Pago pago = null;
                        double monto = rs.getDouble("monto");
                        Date fecha = rs.getDate("fecha");
                        LocalDate fechaPago = fecha.toLocalDate();

                        if ("TRANSFERENCIA".equalsIgnoreCase(tipoPago)) {
                            Transferencia transferencia = new Transferencia();
                            transferencia.setId(idPago);
                            transferencia.setMonto(monto);
                            transferencia.setFecha(fechaPago);
                            pago = transferencia;
                        } else if ("MERCADOPAGO".equalsIgnoreCase(tipoPago)) {
                            MercadoPago mercadoPago = new MercadoPago();
                            mercadoPago.setId(idPago);
                            mercadoPago.setMonto(monto);
                            mercadoPago.setFecha(fechaPago);
                            pago = mercadoPago;
                        }

                        pedido.setFormaDePago(pago);
                    }

                    pedidos.add(pedido);
                }
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pedidos;
    }

    public ArrayList<Pedido> buscarPedidosPorVendedor(int id_vendedor) {
        
        ArrayList<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.id, p.id_cliente, p.id_vendedor, p.total, p.tipo_pago, p.estado, p.id_pago, pa.monto, pa.fecha, "
                   + "c.nombre AS cliente_nombre, c.cbu AS cliente_cbu, c.cuit AS cliente_cuit, "
                   + "v.nombre AS vendedor_nombre "
                   + "FROM pedidos p "
                   + "LEFT JOIN pagos pa ON p.id_pago = pa.id "
                   + "LEFT JOIN clientes c ON p.id_cliente = c.id "  
                   + "LEFT JOIN vendedores v ON p.id_vendedor = v.id "
                   + "WHERE p.id_vendedor = ?"; 

        try (Connection connection = getConnection(); 
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id_vendedor);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = new Pedido();
                    pedido.setId_pedido(rs.getInt("id"));

                    // Crear cliente si existe
                    Integer clienteId = rs.getInt("id_cliente");
                    if (rs.wasNull()) {
                        pedido.setCliente(null);
                    } else {
                        String clienteNombre = rs.getString("cliente_nombre");
                        Long clienteCbu = rs.getLong("cliente_cbu");
                        Long clienteCuit = rs.getLong("cliente_cuit");
                        Cliente cliente = new Cliente();
                        cliente.setId(clienteId);
                        cliente.setNombre(clienteNombre);
                        cliente.setCbu(clienteCbu);
                        cliente.setCuit(clienteCuit);
                        pedido.setCliente(cliente);
                    }
                    
                    Integer vendedorId = rs.getInt("id_vendedor");
                    if (rs.wasNull()) {
                        pedido.setVendedor(null);
                    } else {
                        String vendedorNombre = rs.getString("vendedor_nombre");
                        Vendedor vendedor = new Vendedor();
                        vendedor.setId(vendedorId);
                        vendedor.setNombre(vendedorNombre);
                        pedido.setVendedor(vendedor);
                    }

                    pedido.setTotal(rs.getDouble("total"));
                    pedido.setTipoPago(TipoDePago.valueOf(rs.getString("tipo_pago")));
                    pedido.setEstado(Estado.valueOf(rs.getString("estado")));

                    // Manejar el pago si existe
                    Integer idPago = rs.getObject("id_pago", Integer.class);
                    if (idPago == null) {
                        pedido.setFormaDePago(null);
                    } else {
                        String tipoPago = rs.getString("tipo_pago");
                        Pago pago = null;
                        double monto = rs.getDouble("monto");
                        Date fecha = rs.getDate("fecha");
                        LocalDate fechaPago = fecha.toLocalDate();

                        if ("TRANSFERENCIA".equalsIgnoreCase(tipoPago)) {
                            Transferencia transferencia = new Transferencia();
                            transferencia.setId(idPago);
                            transferencia.setMonto(monto);
                            transferencia.setFecha(fechaPago);
                            pago = transferencia;
                        } else if ("MERCADOPAGO".equalsIgnoreCase(tipoPago)) {
                            MercadoPago mercadoPago = new MercadoPago();
                            mercadoPago.setId(idPago);
                            mercadoPago.setMonto(monto);
                            mercadoPago.setFecha(fechaPago);
                            pago = mercadoPago;
                        }

                        pedido.setFormaDePago(pago);
                    }

                    pedidos.add(pedido);
                }
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pedidos;
    }

    public Pedido buscarPedidoPorID(int id) {
        Pedido pedido = null;
        String sql = "SELECT p.id, p.id_cliente, p.id_vendedor, p.total, p.tipo_pago, p.estado, p.id_pago, pa.monto, pa.fecha, "
                   + "c.nombre AS cliente_nombre, c.cbu AS cliente_cbu, c.cuit AS cliente_cuit, "
                   + "v.nombre AS vendedor_nombre "
                   + "FROM pedidos p "
                   + "LEFT JOIN pagos pa ON p.id_pago = pa.id "
                   + "LEFT JOIN clientes c ON p.id_cliente = c.id "
                   + "LEFT JOIN vendedores v ON p.id_vendedor = v.id "
                   + "WHERE p.id = ?"; 

        try (Connection connection = getConnection(); 
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    pedido = new Pedido();
                    pedido.setId_pedido(rs.getInt("id"));

                    // Crear cliente si existe
                    Integer clienteId = rs.getInt("id_cliente");
                    if (rs.wasNull()) {
                        pedido.setCliente(null);
                    } else {
                        String clienteNombre = rs.getString("cliente_nombre");
                        Long clienteCbu = rs.getLong("cliente_cbu");
                        Long clienteCuit = rs.getLong("cliente_cuit");
                        Cliente cliente = new Cliente();
                        cliente.setId(clienteId);
                        cliente.setNombre(clienteNombre);
                        cliente.setCbu(clienteCbu);
                        cliente.setCuit(clienteCuit);
                        pedido.setCliente(cliente);
                    }

                    // Crear vendedor si existe
                    Integer vendedorId = rs.getInt("id_vendedor");
                    if (rs.wasNull()) {
                        pedido.setVendedor(null);
                    } else {
                        String vendedorNombre = rs.getString("vendedor_nombre");
                        Vendedor vendedor = new Vendedor();
                        vendedor.setId(vendedorId);
                        vendedor.setNombre(vendedorNombre);
                        pedido.setVendedor(vendedor);
                    }

                    pedido.setTotal(rs.getDouble("total"));
                    pedido.setTipoPago(TipoDePago.valueOf(rs.getString("tipo_pago")));
                    pedido.setEstado(Estado.valueOf(rs.getString("estado")));

                    // Manejar el pago si existe
                    Integer idPago = rs.getObject("id_pago", Integer.class);
                    if (idPago == null) {
                        pedido.setFormaDePago(null);
                    } else {
                        String tipoPago = rs.getString("tipo_pago");
                        Pago pago = null;
                        double monto = rs.getDouble("monto");
                        Date fecha = rs.getDate("fecha");
                        LocalDate fechaPago = fecha != null ? fecha.toLocalDate() : null;

                        if ("TRANSFERENCIA".equalsIgnoreCase(tipoPago)) {
                            Transferencia transferencia = new Transferencia();
                            transferencia.setId(idPago);
                            transferencia.setMonto(monto);
                            transferencia.setFecha(fechaPago);
                            pago = transferencia;
                        } else if ("MERCADOPAGO".equalsIgnoreCase(tipoPago)) {
                            MercadoPago mercadoPago = new MercadoPago();
                            mercadoPago.setId(idPago);
                            mercadoPago.setMonto(monto);
                            mercadoPago.setFecha(fechaPago);
                            pago = mercadoPago;
                        }

                        pedido.setFormaDePago(pago);
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pedido;
    }

}

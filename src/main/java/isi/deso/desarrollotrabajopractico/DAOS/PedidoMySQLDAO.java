
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.Estado;
import isi.deso.desarrollotrabajopractico.MercadoPago;
import isi.deso.desarrollotrabajopractico.Pago;
import isi.deso.desarrollotrabajopractico.Pedido;
import isi.deso.desarrollotrabajopractico.TipoDePago;
import isi.deso.desarrollotrabajopractico.Transferencia;
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
    
    public void crearPedido(Pedido pedido) {
        
        String sqlPedido = "INSERT INTO grupo11.pedidos (id, id_cliente, id_vendedor, total, tipo_pago, estado, id_pago) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection(); 
            PreparedStatement pstmtPedido = connection.prepareStatement(sqlPedido)   
        ) {
            pstmtPedido.setInt(1, pedido.getId_pedido());
            pstmtPedido.setInt(2, pedido.getCliente().getId());
            pstmtPedido.setInt(3, pedido.getVendedor().getId());
            pstmtPedido.setDouble(4, pedido.getTotal());
            pstmtPedido.setString(5, pedido.getTipoPago().toString());
            pstmtPedido.setString(6, pedido.getEstado().toString());
            pstmtPedido.setNull(7, java.sql.Types.INTEGER);
            pstmtPedido.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, "Error al crear el pedido", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, "Clase no encontrada", ex);
        }
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
      String sql = "SELECT p.id, p.id_cliente, p.id_vendedor, p.total, p.tipo_pago, p.estado, p.id_pago, pa.monto, pa.fecha "
                 + "FROM pedidos p "
                 + "LEFT JOIN pagos pa ON p.id_pago = pa.id"; // LEFT JOIN para que no se excluyan los pedidos sin pago

      try (Connection connection = getConnection(); 
           Statement stmt = connection.createStatement();
           ResultSet rs = stmt.executeQuery(sql)) {

          while (rs.next()) {
              Pedido pedido = new Pedido(
                      rs.getInt("id"),
                      FactoryDAO.getClienteDAO().buscarClientePorID(rs.getInt("id_cliente")), 
                      FactoryDAO.getVendedorDAO().buscarVendedorPorID(rs.getInt("id_vendedor"))
              );

              pedido.setTotal(rs.getDouble("total"));
              pedido.setTipoPago(TipoDePago.valueOf(rs.getString("tipo_pago")));
              pedido.setEstado(Estado.valueOf(rs.getString("estado")));

              Integer idPago = rs.getObject("id_pago", Integer.class); // Usar getObject para manejar el caso de null
              if (idPago == null) {
                  pedido.setFormaDePago(null);
              } else {
                  // Si id_pago no es null, se recupera el tipo de pago y se crea el pago adecuado
                  String tipoPago = rs.getString("tipo_pago");
                  Pago pago = null;
                  double monto = rs.getDouble("monto");
                  Date fecha = rs.getDate("fecha");
                  LocalDate fechaPago = fecha.toLocalDate();

                  // Crear el objeto de tipo adecuado según el tipo de pago
                  if ("TRANSFERENCIA".equalsIgnoreCase(tipoPago)) {
                      Transferencia transferencia = new Transferencia(pedido.getCliente().getCbu(), pedido.getCliente().getCuit(), fechaPago);
                      transferencia.setId(idPago);
                      transferencia.setMonto(monto);
                      pago = transferencia;
                      
                  } else if ("MercadoPago".equalsIgnoreCase(tipoPago)) {
                      MercadoPago mercadoPago = new MercadoPago(pedido.getCliente().getAlias(), fechaPago);
                      mercadoPago.setId(idPago);
                      mercadoPago.setMonto(monto);
                  }

                  // Asignar el pago correspondiente al pedido
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

            // Establecemos el parámetro de la consulta (el id del pedido)
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
        String sql = "SELECT p.id, p.id_cliente, p.id_vendedor, p.total, p.tipo_pago, p.estado, p.id_pago, pa.monto, pa.fecha "
                     + "FROM pedidos p "
                     + "LEFT JOIN pagos pa ON p.id_pago = pa.id WHERE p.id_cliente = ?"; // LEFT JOIN para incluir pedidos sin pago

        try (Connection connection = getConnection(); 
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id_cliente);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = new Pedido(
                            rs.getInt("id"),
                            FactoryDAO.getClienteDAO().buscarClientePorID(rs.getInt("id_cliente")), 
                            FactoryDAO.getVendedorDAO().buscarVendedorPorID(rs.getInt("id_vendedor"))
                    );

                    pedido.setTotal(rs.getDouble("total"));
                    pedido.setTipoPago(TipoDePago.valueOf(rs.getString("tipo_pago")));
                    pedido.setEstado(Estado.valueOf(rs.getString("estado")));

                    Integer idPago = rs.getObject("id_pago", Integer.class); // Manejar null con getObject
                    if (idPago == null) {
                        pedido.setFormaDePago(null); // Si no hay pago, asignar null
                    } else {
                        // Si hay un pago, se recupera el tipo de pago y se crea el objeto adecuado
                        String tipoPago = rs.getString("tipo_pago");
                        Pago pago = null;
                        double monto = rs.getDouble("monto");
                        Date fecha = rs.getDate("fecha");
                        LocalDate fechaPago = fecha != null ? fecha.toLocalDate() : null;

                        // Crear el pago adecuado según el tipo de pago
                        if ("TRANSFERENCIA".equalsIgnoreCase(tipoPago)) {
                            Transferencia transferencia = new Transferencia(pedido.getCliente().getCbu(), pedido.getCliente().getCuit(), fechaPago);
                            transferencia.setId(idPago);
                            transferencia.setMonto(monto);
                            pago = transferencia;
                        } else if ("MercadoPago".equalsIgnoreCase(tipoPago)) {
                            MercadoPago mercadoPago = new MercadoPago(pedido.getCliente().getAlias(), fechaPago);
                            mercadoPago.setId(idPago);
                            mercadoPago.setMonto(monto);
                            pago = mercadoPago;
                        }

                        // Asignar el pago correspondiente
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
        String sql = "SELECT p.id, p.id_cliente, p.id_vendedor, p.total, p.tipo_pago, p.estado, p.id_pago, pa.monto, pa.fecha "
                     + "FROM pedidos p "
                     + "LEFT JOIN pagos pa ON p.id_pago = pa.id WHERE p.id_vendedor = ?"; // LEFT JOIN para incluir pedidos sin pago

        try (Connection connection = getConnection(); 
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id_vendedor);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = new Pedido(
                            rs.getInt("id"),
                            FactoryDAO.getClienteDAO().buscarClientePorID(rs.getInt("id_cliente")), 
                            FactoryDAO.getVendedorDAO().buscarVendedorPorID(rs.getInt("id_vendedor"))
                    );

                    pedido.setTotal(rs.getDouble("total"));
                    pedido.setTipoPago(TipoDePago.valueOf(rs.getString("tipo_pago")));
                    pedido.setEstado(Estado.valueOf(rs.getString("estado")));

                    Integer idPago = rs.getObject("id_pago", Integer.class); // Manejar null con getObject
                    if (idPago == null) {
                        pedido.setFormaDePago(null); // Si no hay pago, asignar null
                    } else {
                        String tipoPago = rs.getString("tipo_pago");
                        Pago pago = null;
                        double monto = rs.getDouble("monto");
                        Date fecha = rs.getDate("fecha");
                        LocalDate fechaPago = fecha != null ? fecha.toLocalDate() : null;

                        if ("TRANSFERENCIA".equalsIgnoreCase(tipoPago)) {
                            Transferencia transferencia = new Transferencia(pedido.getCliente().getCbu(), pedido.getCliente().getCuit(), fechaPago);
                            transferencia.setId(idPago);
                            transferencia.setMonto(monto);
                            pago = transferencia;
                        } else if ("MercadoPago".equalsIgnoreCase(tipoPago)) {
                            MercadoPago mercadoPago = new MercadoPago(pedido.getCliente().getAlias(), fechaPago);
                            mercadoPago.setId(idPago);
                            mercadoPago.setMonto(monto);
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
        String sql = "SELECT p.id, p.id_cliente, p.id_vendedor, p.total, p.tipo_pago, p.estado, p.id_pago, pa.monto, pa.fecha "
                     + "FROM pedidos p "
                     + "LEFT JOIN pagos pa ON p.id_pago = pa.id WHERE p.id = ?"; // LEFT JOIN para incluir pedidos sin pago

        try (Connection connection = getConnection(); 
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    pedido = new Pedido(
                            rs.getInt("id"),
                            FactoryDAO.getClienteDAO().buscarClientePorID(rs.getInt("id_cliente")), 
                            FactoryDAO.getVendedorDAO().buscarVendedorPorID(rs.getInt("id_vendedor"))
                    );

                    pedido.setTotal(rs.getDouble("total"));
                    pedido.setTipoPago(TipoDePago.valueOf(rs.getString("tipo_pago")));
                    pedido.setEstado(Estado.valueOf(rs.getString("estado")));

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
                            Transferencia transferencia = new Transferencia(pedido.getCliente().getCbu(), pedido.getCliente().getCuit(), fechaPago);
                            transferencia.setId(idPago);
                            transferencia.setMonto(monto);
                            pago = transferencia;
                        } else if ("MercadoPago".equalsIgnoreCase(tipoPago)) {
                            MercadoPago mercadoPago = new MercadoPago(pedido.getCliente().getAlias(), fechaPago);
                            mercadoPago.setId(idPago);
                            mercadoPago.setMonto(monto);
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

    public int obtenerID() {
        
        String consulta = "SELECT MAX(id) AS ultimo_id FROM grupo11.pedidos";
        int nuevoID = 1; 

        try (Connection connection = getConnection();  
             PreparedStatement stmt = connection.prepareStatement(consulta);
             ResultSet rs = stmt.executeQuery()) { 

            if (rs.next()) {
                int ultimoID = rs.getInt("ultimo_id");
                nuevoID = ultimoID + 1;
            }

        } catch (SQLException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return nuevoID;
    }

}

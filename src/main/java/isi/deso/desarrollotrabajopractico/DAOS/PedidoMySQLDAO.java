
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.Estado;
import isi.deso.desarrollotrabajopractico.Pedido;
import isi.deso.desarrollotrabajopractico.TipoDePago;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        
        try {
            Connection connection = getConnection();

            Statement stmt = connection.createStatement();

            String sqlPedido = "INSERT INTO grupo11.pedidos (id, id_cliente, id_vendedor, total, tipo_pago, estado) VALUES (?, ?, ?, ?, ?, ?)";
            try(PreparedStatement pstmtPedido = connection.prepareStatement(sqlPedido)) {
                pstmtPedido.setInt(1, pedido.getId_pedido());
                pstmtPedido.setInt(2, pedido.getCliente().getId());
                pstmtPedido.setInt(3, pedido.getVendedor().getId());
                pstmtPedido.setDouble(4, pedido.getTotal());
                pstmtPedido.setString(5, pedido.getTipoPago().toString());
                pstmtPedido.setString(6, pedido.getEstado().toString());
                pstmtPedido.executeUpdate();
            }
               
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    public void actualizarPedido(Pedido pedido) {
        
        String sqlPedido = "UPDATE grupo11.pedidos SET total = ?, tipo_pago = ?, estado = ? WHERE id = ?";
      
        try (Connection connection = getConnection(); 
             PreparedStatement pstmtPedido = connection.prepareStatement(sqlPedido)) {

            pstmtPedido.setDouble(1, pedido.getTotal());
            pstmtPedido.setString(2, pedido.getTipoPago().toString());
            pstmtPedido.setString(3, pedido.getEstado().toString());
            pstmtPedido.setInt(4, pedido.getId_pedido());
           
            pstmtPedido.executeUpdate();
            connection.close();

        } catch (SQLException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
    
    public ArrayList<Pedido> obtenerTodosLosPedidos() {
        
        ArrayList<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";
        
        try (Connection connection = getConnection(); 
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterar sobre los resultados de la consulta
            while (rs.next()) {
                // Crear un nuevo objeto Pedido y llenar con los datos de la tabla
                Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        FactoryDAO.getClienteDAO().buscarClientePorID(rs.getInt("id_cliente")), 
                        FactoryDAO.getVendedorDAO().buscarVendedorPorID(rs.getInt("id_vendedor"))
                );
                
                pedido.setTotal(rs.getDouble("total"));
                pedido.setTipoPago(TipoDePago.valueOf(rs.getString("tipo_pago")));
                pedido.setEstado(Estado.valueOf(rs.getString("estado")));
                
                pedidos.add(pedido);
                
            }
            connection.close();

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
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public ArrayList<Pedido> buscarPedidosPorCliente(int id_cliente) {
        
        ArrayList<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE id_cliente = ?";  

        try (Connection connection = getConnection(); 
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Establecer el parámetro del nombre en la consulta
            pstmt.setInt(1, id_cliente);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Iterar sobre los resultados de la consulta
                while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        FactoryDAO.getClienteDAO().buscarClientePorID(rs.getInt("id_cliente")), 
                        FactoryDAO.getVendedorDAO().buscarVendedorPorID(rs.getInt("id_vendedor"))
                );
                
                pedido.setTotal(rs.getDouble("total"));
                pedido.setTipoPago(TipoDePago.valueOf(rs.getString("tipo_pago")));
                pedido.setEstado(Estado.valueOf(rs.getString("estado")));
                
                pedidos.add(pedido);
                
            }
        }

        } catch (SQLException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pedidos;
    }
    
    public ArrayList<Pedido> buscarPedidosPorVendedor(int id_vendedor) {
        
        ArrayList<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE id_vendedor = ?";  

        try (Connection connection = getConnection(); 
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Establecer el parámetro del nombre en la consulta
            pstmt.setInt(1, id_vendedor);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Iterar sobre los resultados de la consulta
                while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        FactoryDAO.getClienteDAO().buscarClientePorID(rs.getInt("id_cliente")), 
                        FactoryDAO.getVendedorDAO().buscarVendedorPorID(rs.getInt("id_vendedor"))
                );
                
                pedido.setTotal(rs.getDouble("total"));
                pedido.setTipoPago(TipoDePago.valueOf(rs.getString("tipo_pago")));
                pedido.setEstado(Estado.valueOf(rs.getString("estado")));
                
                pedidos.add(pedido);
                
            }
        }

        } catch (SQLException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pedidos;
    }
    
    public Pedido buscarPedidoPorID(int id) {
        
        Pedido pedido = null;  
        String sql = "SELECT * FROM pedidos WHERE id = ?";  

        try (Connection connection = getConnection(); 
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Establecer el parámetro del ID en la consulta
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Si se encuentra un resultado, crear el objeto Pedido
                if (rs.next()) {
                  pedido = new Pedido(
                        rs.getInt("id"),
                        FactoryDAO.getClienteDAO().buscarClientePorID(rs.getInt("id_cliente")), 
                        FactoryDAO.getVendedorDAO().buscarVendedorPorID(rs.getInt("id_vendedor"))
                );
                
                pedido.setTotal(rs.getDouble("total"));
                pedido.setTipoPago(TipoDePago.valueOf(rs.getString("tipo_pago")));
                pedido.setEstado(Estado.valueOf(rs.getString("estado")));
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pedido; 
    }
    
     public int obtenerID() {
        
        String sql = "SELECT COUNT(*) FROM pedidos";
        int ID = 0;
        
        try (Connection connection = getConnection();  
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                    rs.next();
                    int count = rs.getInt(1);  
                    ID = count + 1; 
            }

        } catch (SQLException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ID;  
    }
    
}

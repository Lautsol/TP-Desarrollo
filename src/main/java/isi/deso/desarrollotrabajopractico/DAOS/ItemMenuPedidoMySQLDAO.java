
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.modelo.Pedido;
import isi.deso.desarrollotrabajopractico.modelo.PedidoDetalle;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemMenuPedidoMySQLDAO implements ItemMenuPedidoDAO {
    
    private static Connection con;
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/grupo11";
    private static final String USER = "root";
    private static final String PASSWORD = "grupo11";
    private static ItemMenuPedidoMySQLDAO instancia; 
    
    private ItemMenuPedidoMySQLDAO() {
    }
    
    public static ItemMenuPedidoMySQLDAO obtenerInstancia() {
        
        if(instancia == null){
            instancia = new ItemMenuPedidoMySQLDAO();
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
    
    public void agregarItemsPedido(Pedido pedido) {
        
        String sqlItemsVendedor = "INSERT INTO itemsMenuPedido (idPedido, idItem, cantidad) VALUES (?, ?, ?)";

        try (Connection connection = getConnection(); 
            PreparedStatement pstmtItemsPedido = connection.prepareStatement(sqlItemsVendedor)  
        ) {
            for (PedidoDetalle pd : pedido.getPedidoDetalle()) {
                pstmtItemsPedido.setInt(1, pedido.getId_pedido());
                pstmtItemsPedido.setInt(2, pd.getProducto().getId());
                pstmtItemsPedido.setInt(3, pd.getCantidad());
                pstmtItemsPedido.executeUpdate();
            }
        } catch (SQLException e) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, "Error al agregar ítems al pedido", e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, "Clase no encontrada", ex);
        }
    }
}


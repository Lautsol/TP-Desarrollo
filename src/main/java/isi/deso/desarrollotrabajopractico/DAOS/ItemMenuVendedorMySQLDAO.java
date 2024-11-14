
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.ItemMenu;
import isi.deso.desarrollotrabajopractico.Vendedor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemMenuVendedorMySQLDAO implements ItemMenuVendedorDAO {
    
    private static Connection con;
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/grupo11";
    private static final String USER = "root";
    private static final String PASSWORD = "grupo11";
    private static ItemMenuVendedorMySQLDAO instancia; 
    
    private ItemMenuVendedorMySQLDAO() {
    }
    
    public static ItemMenuVendedorMySQLDAO obtenerInstancia() {
        
        if(instancia == null){
            instancia = new ItemMenuVendedorMySQLDAO();
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
    
    
    public void agregarItemsVendedor(Vendedor vendedor) {
        Connection connection = null;
        PreparedStatement pstmtItemsVendedor = null;

        try {
            
            connection = getConnection();

            String sqlItemsVendedor = "INSERT INTO itemsMenuVendedor (idVendedor, idItem) VALUES (?, ?)";
            pstmtItemsVendedor = connection.prepareStatement(sqlItemsVendedor);

            for (ItemMenu item : vendedor.getItems()) {
                pstmtItemsVendedor.setInt(1, vendedor.getId());  
                pstmtItemsVendedor.setInt(2, item.getId());      
                pstmtItemsVendedor.executeUpdate();               
            }
        
        } catch (SQLException e) {
            e.printStackTrace();  
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            
        } finally {
        
        try {
                pstmtItemsVendedor.close();
                connection.close();
                
        } catch (SQLException e) {
                e.printStackTrace();
        } 
    }
  }
    
    public boolean itemMenuDeVendedor(int idVendedor, int idItem) {
        
        String sql = "SELECT 1 FROM itemsMenuVendedor WHERE idVendedor = ? AND idItem = ?";
    
        try (Connection connection = getConnection(); 
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
        
        pstmt.setInt(1, idVendedor);
        pstmt.setInt(2, idItem);
        
        // Ejecutar la consulta
        ResultSet rs = pstmt.executeQuery();
        
        // Si hay un resultado, el ItemMenu existe
        if (rs.next()) {
            return true; 
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
        
    } catch (ClassNotFoundException ex) {
        Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    
        return false; 

    } 
}


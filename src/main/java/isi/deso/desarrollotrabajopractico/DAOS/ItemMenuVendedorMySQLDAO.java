
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.Alcohol;
import isi.deso.desarrollotrabajopractico.Gaseosa;
import isi.deso.desarrollotrabajopractico.ItemMenu;
import isi.deso.desarrollotrabajopractico.Plato;
import isi.deso.desarrollotrabajopractico.Vendedor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

            String sqlItemsVendedor = "INSERT IGNORE INTO itemsMenuVendedor (idVendedor, idItem) VALUES (?, ?)";
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
    
    public ArrayList<ItemMenu> obtenerItemsMenuDeVendedor(Vendedor vendedor) {
        ArrayList<ItemMenu> itemsMenu = new ArrayList<>();
    
        String[] tipos = {"PLATO", "GASEOSA", "ALCOHOL"};
    
        for (String tipo : tipos) {
        
            String sql = "SELECT im.* " +
                        "FROM grupo11.itemsMenuVendedor imv " +
                        "JOIN grupo11.itemsMenu im ON imv.idItem = im.id " +
                        "WHERE imv.idVendedor = ? AND im.tipo_item = ?";
        
            try (Connection connection = getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)) {

                pstmt.setInt(1, vendedor.getId());  
                pstmt.setString(2, tipo);  
            
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        ItemMenu itemMenu = null;
                    
                        if (tipo.equals("PLATO")) {
                            itemMenu = new Plato(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("descripcion"),
                                rs.getDouble("precio"),
                                FactoryDAO.getCategoriaDAO().buscarCategoria(rs.getInt("id_categoria")),
                                0,
                                0,
                                false,
                                false,
                                false
                            );
                        } else if (tipo.equals("GASEOSA")) {
                            itemMenu = new Gaseosa(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("descripcion"),
                                rs.getDouble("precio"),
                                FactoryDAO.getCategoriaDAO().buscarCategoria(rs.getInt("id_categoria")),
                                0
                            );
                        } else if (tipo.equals("ALCOHOL")) {
                            itemMenu = new Alcohol(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("descripcion"),
                                rs.getDouble("precio"),
                                FactoryDAO.getCategoriaDAO().buscarCategoria(rs.getInt("id_categoria")),
                                0,
                                0
                            );
                        }
                    
                        if (itemMenu != null) {
                            itemsMenu.add(itemMenu);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace(); 
            } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            
            }
        }
    
        return itemsMenu;
}

}


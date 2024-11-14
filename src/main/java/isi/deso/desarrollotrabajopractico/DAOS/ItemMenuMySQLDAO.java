
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.Alcohol;
import isi.deso.desarrollotrabajopractico.Gaseosa;
import isi.deso.desarrollotrabajopractico.ItemMenu;
import isi.deso.desarrollotrabajopractico.Plato;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemMenuMySQLDAO implements ItemMenuDAO {
    
    private static Connection con;
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/grupo11";
    private static final String USER = "root";
    private static final String PASSWORD = "grupo11";
    private static ItemMenuMySQLDAO instancia; 

    private ItemMenuMySQLDAO() {
    }
    
    public static ItemMenuMySQLDAO obtenerInstancia() {
        
        if(instancia == null){
            instancia = new ItemMenuMySQLDAO();
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
    
    public void crearItemMenu(ItemMenu itemMenu) {
        
        try {
            Connection connection = getConnection();

            Statement stmt = connection.createStatement();

            String sqlItemMenu = "INSERT INTO grupo11.itemsMenu (id, tipo_item, nombre, descripcion, precio, id_categoria, item) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try(PreparedStatement pstmtItemMenu = connection.prepareStatement(sqlItemMenu)) {
                pstmtItemMenu.setInt(1, itemMenu.getId());
                pstmtItemMenu.setString(2, itemMenu.getCategoria().getTipo_item().toString());
                pstmtItemMenu.setString(3, itemMenu.getNombre());
                pstmtItemMenu.setString(4, itemMenu.getDescripcion());
                pstmtItemMenu.setDouble(5, itemMenu.getPrecio());
                pstmtItemMenu.setInt(6, itemMenu.getCategoria().getId());
                pstmtItemMenu.setString(7, itemMenu.getClass().getSimpleName().toUpperCase());
                pstmtItemMenu.executeUpdate();
            }
            
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    public void actualizarItemMenu(ItemMenu itemMenu) {
        
        String sqlItemMenu = "UPDATE grupo11.itemsMenu SET tipo_item = ?, nombre = ?, descripcion = ?, precio = ?, id_categoria = ?, item = ? WHERE id = ?";
      
        try (Connection connection = getConnection(); 
             PreparedStatement pstmtItemMenu = connection.prepareStatement(sqlItemMenu)) {

            pstmtItemMenu.setString(1, itemMenu.getCategoria().getTipo_item().toString());
            pstmtItemMenu.setString(2, itemMenu.getNombre());
            pstmtItemMenu.setString(3, itemMenu.getDescripcion());
            pstmtItemMenu.setDouble(4, itemMenu.getPrecio());
            pstmtItemMenu.setInt(5, itemMenu.getCategoria().getId());
            pstmtItemMenu.setString(6, itemMenu.getClass().getSimpleName().toUpperCase());
            pstmtItemMenu.setInt(7, itemMenu.getId());
           
            pstmtItemMenu.executeUpdate();
            connection.close();

        } catch (SQLException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
    
    public ArrayList<ItemMenu> obtenerTodosLosItemsMenu() {
        ArrayList<ItemMenu> itemsMenu = new ArrayList<>();
    
        // Consultas SQL para los diferentes tipos de items
        String sql = "SELECT * FROM itemsMenu WHERE item = ?";  

        try (Connection connection = getConnection()) {

        // Obtener los items de tipo comida, gaseosa y alcohol
        String[] tipos = {"PLATO", "GASEOSA", "ALCOHOL"};
        for (String tipo : tipos) {
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, tipo);  // Establecer el tipo de item a buscar
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
            }
        }

        } catch (SQLException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return itemsMenu;
}

    public void eliminarItemMenu(int id) {
        
        String sql = "DELETE FROM itemsMenu WHERE id = ?";  

        try (Connection connection = getConnection(); 
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Establecemos el parámetro de la consulta (el id del item menu)
            stmt.setInt(1, id);

            stmt.executeUpdate();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public ArrayList<ItemMenu> buscarItemsMenuPorNombre(String nombre) {
        ArrayList<ItemMenu> itemsMenu = new ArrayList<>();
    
        String sqlComida = "SELECT * FROM itemsMenu WHERE nombre LIKE ? AND item = 'PLATO'";
        String sqlGaseosa = "SELECT * FROM itemsMenu WHERE nombre LIKE ? AND item = 'GASEOSA'";
        String sqlAlcohol = "SELECT * FROM itemsMenu WHERE nombre LIKE ? AND item = 'ALCOHOL'";

        try (Connection connection = getConnection()) {
        
            // Obtener los items de tipo comida
            try (PreparedStatement pstmtComida = connection.prepareStatement(sqlComida)) {
                pstmtComida.setString(1, nombre + "%"); // Establecer el nombre en la consulta
                try (ResultSet rsComida = pstmtComida.executeQuery()) {
                    while (rsComida.next()) {
                        ItemMenu itemMenu = new Plato(
                            rsComida.getInt("id"),
                            rsComida.getString("nombre"),
                            rsComida.getString("descripcion"),
                            rsComida.getDouble("precio"),
                            FactoryDAO.getCategoriaDAO().buscarCategoria(rsComida.getInt("id_categoria")),   
                            0,
                            0,
                            false,
                            false, 
                            false
                        );
                        itemsMenu.add(itemMenu);
                    }
                }
            }

            // Obtener los items de tipo gaseosa
            try (PreparedStatement pstmtGaseosa = connection.prepareStatement(sqlGaseosa)) {
                pstmtGaseosa.setString(1, nombre + "%"); // Establecer el nombre en la consulta
                try (ResultSet rsGaseosa = pstmtGaseosa.executeQuery()) {
                    while (rsGaseosa.next()) {
                        ItemMenu itemMenu = new Gaseosa(
                            rsGaseosa.getInt("id"),
                            rsGaseosa.getString("nombre"),
                            rsGaseosa.getString("descripcion"),
                            rsGaseosa.getDouble("precio"),
                            FactoryDAO.getCategoriaDAO().buscarCategoria(rsGaseosa.getInt("id_categoria")),
                            0
                        );
                        itemsMenu.add(itemMenu);
                    }
                }
            }

            // Obtener los items de tipo alcohol
            try (PreparedStatement pstmtAlcohol = connection.prepareStatement(sqlAlcohol)) {
                pstmtAlcohol.setString(1, nombre + "%"); // Establecer el nombre en la consulta
                try (ResultSet rsAlcohol = pstmtAlcohol.executeQuery()) {
                    while (rsAlcohol.next()) {
                        ItemMenu itemMenu = new Alcohol(
                            rsAlcohol.getInt("id"),
                            rsAlcohol.getString("nombre"),
                            rsAlcohol.getString("descripcion"),
                            rsAlcohol.getDouble("precio"),
                            FactoryDAO.getCategoriaDAO().buscarCategoria(rsAlcohol.getInt("id_categoria")),
                            0,
                            0
                        );
                        itemsMenu.add(itemMenu);
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return itemsMenu;
    }

    
    public ItemMenu buscarItemMenuPorID(int id) {
        ItemMenu itemMenu = null;
        String sqlComida = "SELECT * FROM itemsMenu WHERE id = ? AND item = 'PLATO'";
        String sqlGaseosa = "SELECT * FROM itemsMenu WHERE id = ? AND item = 'GASEOSA'";
        String sqlAlcohol = "SELECT * FROM itemsMenu WHERE id = ? AND item = 'ALCOHOL'";

        try (Connection connection = getConnection()) {

            try (PreparedStatement pstmtComida = connection.prepareStatement(sqlComida)) {
                pstmtComida.setInt(1, id); 
                try (ResultSet rsComida = pstmtComida.executeQuery()) {
                    if (rsComida.next()) {
                        itemMenu = new Plato(
                            rsComida.getInt("id"),
                            rsComida.getString("nombre"),
                            rsComida.getString("descripcion"),
                            rsComida.getDouble("precio"),
                            FactoryDAO.getCategoriaDAO().buscarCategoria(rsComida.getInt("id_categoria")),   
                            0,
                            0,
                            false,
                            false, 
                            false
                        );
                    }
                }
            }

            if (itemMenu == null) {
                try (PreparedStatement pstmtGaseosa = connection.prepareStatement(sqlGaseosa)) {
                    pstmtGaseosa.setInt(1, id); 
                    try (ResultSet rsGaseosa = pstmtGaseosa.executeQuery()) {
                        if (rsGaseosa.next()) {
                            itemMenu = new Gaseosa(
                                rsGaseosa.getInt("id"),
                                rsGaseosa.getString("nombre"),
                                rsGaseosa.getString("descripcion"),
                                rsGaseosa.getDouble("precio"),
                                FactoryDAO.getCategoriaDAO().buscarCategoria(rsGaseosa.getInt("id_categoria")),
                                0
                            );
                        }
                    }
                }
            }

            if (itemMenu == null) {
                try (PreparedStatement pstmtAlcohol = connection.prepareStatement(sqlAlcohol)) {
                    pstmtAlcohol.setInt(1, id); 
                    try (ResultSet rsAlcohol = pstmtAlcohol.executeQuery()) {
                        if (rsAlcohol.next()) {
                            itemMenu = new Alcohol(
                                rsAlcohol.getInt("id"),
                                rsAlcohol.getString("nombre"),
                                rsAlcohol.getString("descripcion"),
                                rsAlcohol.getDouble("precio"),
                                FactoryDAO.getCategoriaDAO().buscarCategoria(rsAlcohol.getInt("id_categoria")),
                                0,
                                0
                            );
                        }
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return itemMenu;
    }

    
    public int obtenerID() {
        
        String sql = "SELECT COUNT(*) FROM itemsMenu";
        int ID = 0;
        
        try (Connection connection = getConnection();  
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                int count = rs.getInt(1);  
                ID = count + 1; 
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ID;  
    }
    
    
}

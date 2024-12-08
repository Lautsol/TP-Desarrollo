
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.modelo.Alcohol;
import isi.deso.desarrollotrabajopractico.modelo.Gaseosa;
import isi.deso.desarrollotrabajopractico.modelo.ItemMenu;
import isi.deso.desarrollotrabajopractico.modelo.Plato;
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
    
    public int crearItemMenu(ItemMenu itemMenu) {
        String sqlItemMenu = "INSERT INTO grupo11.itemsMenu (tipo_item, nombre, descripcion, precio, id_categoria, item) VALUES (?, ?, ?, ?, ?, ?)";

        int itemMenuId = -1; // Inicializa el id con un valor por defecto

        try (Connection connection = getConnection(); 
             PreparedStatement pstmtItemMenu = connection.prepareStatement(sqlItemMenu, Statement.RETURN_GENERATED_KEYS)) {

            pstmtItemMenu.setString(1, itemMenu.getCategoria().getTipo_item().toString());
            pstmtItemMenu.setString(2, itemMenu.getNombre());
            pstmtItemMenu.setString(3, itemMenu.getDescripcion());
            pstmtItemMenu.setDouble(4, itemMenu.getPrecio());
            pstmtItemMenu.setInt(5, itemMenu.getCategoria().getId());
            pstmtItemMenu.setString(6, itemMenu.getClass().getSimpleName().toUpperCase());

            pstmtItemMenu.executeUpdate();

            try (ResultSet generatedKeys = pstmtItemMenu.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    itemMenuId = generatedKeys.getInt(1); 
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, "Error al crear el ítem del menú", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, "Clase no encontrada", ex);
        }

        return itemMenuId; 
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

        } catch (SQLException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemMenuMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
    
    public ArrayList<ItemMenu> obtenerTodosLosItemsMenu() {
        ArrayList<ItemMenu> itemsMenu = new ArrayList<>();

        String sql = "SELECT * FROM itemsMenu WHERE item IN (?, ?, ?) ORDER BY id";  

        try (Connection connection = getConnection()) {

            String[] tipos = {"PLATO", "GASEOSA", "ALCOHOL"};

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, tipos[0]);
                pstmt.setString(2, tipos[1]);
                pstmt.setString(3, tipos[2]);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        ItemMenu itemMenu = null;

                        // Crear el objeto correspondiente según el tipo de item
                        if (rs.getString("item").equals("PLATO")) {
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
                        } else if (rs.getString("item").equals("GASEOSA")) {
                            itemMenu = new Gaseosa(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("descripcion"),
                                rs.getDouble("precio"),
                                FactoryDAO.getCategoriaDAO().buscarCategoria(rs.getInt("id_categoria")),
                                0
                            );
                        } else if (rs.getString("item").equals("ALCOHOL")) {
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
    
}

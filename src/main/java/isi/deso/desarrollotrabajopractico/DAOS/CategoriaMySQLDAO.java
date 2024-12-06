
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.Categoria;
import isi.deso.desarrollotrabajopractico.TipoItem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoriaMySQLDAO implements CategoriaDAO {
    
    private static Connection con;
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/grupo11";
    private static final String USER = "root";
    private static final String PASSWORD = "grupo11";
    private static CategoriaMySQLDAO instancia; 

    private CategoriaMySQLDAO() {
    }
    
    public static CategoriaMySQLDAO obtenerInstancia() {
        
        if(instancia == null){
            instancia = new CategoriaMySQLDAO();
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
    
    public Categoria buscarCategoria(int id) {
        
        Categoria categoria = null;  
        String sql = "SELECT * FROM categorias WHERE id = ?";  

        try (Connection connection = getConnection(); 
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Establecer el parámetro del ID en la consulta
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Si se encuentra un resultado, crear el objeto Categoria
                if (rs.next()) {
                    categoria = new Categoria(
                        rs.getInt("id"),
                        rs.getString("descripcion"),
                        TipoItem.valueOf(rs.getString("tipo_item"))
                    );
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(CategoriaMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CategoriaMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return categoria; 
    }
    
    public ArrayList<Categoria> obtenerTodasLasCategorias() {
        
        ArrayList<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categorias";  

        try (Connection connection = getConnection(); 
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Categoria categoria = new Categoria(
                        rs.getInt("id"),
                        rs.getString("descripcion"),
                        TipoItem.valueOf(rs.getString("tipo_item"))
                    );
                    categorias.add(categoria);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(CategoriaMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CategoriaMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return categorias; 
    }

}

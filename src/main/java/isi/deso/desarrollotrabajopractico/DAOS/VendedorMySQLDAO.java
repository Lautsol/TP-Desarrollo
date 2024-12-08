
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.modelo.Vendedor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VendedorMySQLDAO implements VendedorDAO {
    
    private static Connection con;
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/grupo11";
    private static final String USER = "root";
    private static final String PASSWORD = "grupo11";
    private static VendedorMySQLDAO instancia; 

    private VendedorMySQLDAO() {
    }
    
    public static VendedorMySQLDAO obtenerInstancia() {
        
        if(instancia == null){
            instancia = new VendedorMySQLDAO();
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
    
    public int crearVendedor(Vendedor vendedor) {
        String sqlVendedor = "INSERT INTO grupo11.vendedores (nombre, direccion) VALUES (?, ?)";

        int vendedorId = -1; // Inicializa el id con un valor por defecto

        try (Connection connection = getConnection(); 
             PreparedStatement pstmtVendedor = connection.prepareStatement(sqlVendedor, Statement.RETURN_GENERATED_KEYS)) {

            pstmtVendedor.setString(1, vendedor.getNombre());
            pstmtVendedor.setString(2, vendedor.getDireccion());
            pstmtVendedor.executeUpdate();

            try (ResultSet generatedKeys = pstmtVendedor.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vendedorId = generatedKeys.getInt(1); 
                }
            }

        } catch (SQLException e) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, "Error al crear el vendedor", e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, "Clase no encontrada", ex);
        }

        return vendedorId; 
    }

    public void actualizarVendedor(Vendedor vendedor) {
        
        String sqlVendedor = "UPDATE grupo11.vendedores SET nombre = ?, direccion = ? WHERE id = ?";
      
        try (Connection connection = getConnection(); 
             PreparedStatement pstmtVendedor = connection.prepareStatement(sqlVendedor)) {

            pstmtVendedor.setString(1, vendedor.getNombre());
            pstmtVendedor.setString(2, vendedor.getDireccion());
            pstmtVendedor.setInt(3, vendedor.getId());
           
            pstmtVendedor.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
    
    public ArrayList<Vendedor> obtenerTodosLosVendedores() {
        
        ArrayList<Vendedor> vendedores = new ArrayList<>();
        String sql = "SELECT * FROM vendedores ORDER BY id";
        
        try (Connection connection = getConnection(); 
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vendedor vendedor = new Vendedor(
                        rs.getInt("id"), 
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        null
                );
                
                vendedores.add(vendedor);
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vendedores;
        
    }
    
    public void eliminarVendedor(int id) {
        
        String sql = "DELETE FROM vendedores WHERE id = ?";  

        try (Connection connection = getConnection(); 
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Establecemos el parámetro de la consulta 
            stmt.setInt(1, id);

            stmt.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public ArrayList<Vendedor> buscarVendedoresPorNombre(String nombre) {
        
        ArrayList<Vendedor> vendedores = new ArrayList<>();
        String sql = "SELECT * FROM vendedores WHERE nombre LIKE ?";  

        try (Connection connection = getConnection(); 
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Establecer el parámetro del nombre en la consulta
            pstmt.setString(1, nombre + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Vendedor vendedor = new Vendedor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        null
                    );
                    vendedores.add(vendedor);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vendedores;
    }
    
    public Vendedor buscarVendedorPorID(int id) {
        
        Vendedor vendedor = null;  
        String sql = "SELECT * FROM vendedores WHERE id = ?";  

        try (Connection connection = getConnection(); 
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    vendedor = new Vendedor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        null
                    );
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return vendedor; 
    }

}

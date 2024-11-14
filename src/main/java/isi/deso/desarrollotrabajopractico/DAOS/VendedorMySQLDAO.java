
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.ItemMenu;
import isi.deso.desarrollotrabajopractico.Vendedor;
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
    
    public void crearVendedor(Vendedor vendedor) {
        Connection connection = null;
        PreparedStatement pstmtVendedor = null;

        try {
            
            connection = getConnection();

            // Primero, insertar el vendedor
            String sqlVendedor = "INSERT INTO grupo11.vendedores (id, nombre, direccion) VALUES (?, ?, ?)";
            pstmtVendedor = connection.prepareStatement(sqlVendedor);
            pstmtVendedor.setInt(1, vendedor.getId());
            pstmtVendedor.setString(2, vendedor.getNombre());
            pstmtVendedor.setString(3, vendedor.getDireccion());
            pstmtVendedor.executeUpdate();  

        
        } catch (SQLException e) {
            e.printStackTrace();  
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
            
        } finally {
        
        try {
                pstmtVendedor.close();
                connection.close();
                
        } catch (SQLException e) {
                e.printStackTrace();
        } 
    }
}

    public void actualizarVendedor(Vendedor vendedor) {
        
        String sqlVendedor = "UPDATE grupo11.vendedores SET nombre = ?, direccion = ? WHERE id = ?";
      
        try (Connection connection = getConnection(); 
             PreparedStatement pstmtVendedor = connection.prepareStatement(sqlVendedor)) {

            pstmtVendedor.setString(1, vendedor.getNombre());
            pstmtVendedor.setString(2, vendedor.getDireccion());
            pstmtVendedor.setInt(3, vendedor.getId());
           
            pstmtVendedor.executeUpdate();
            connection.close();

        } catch (SQLException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
    
    public ArrayList<Vendedor> obtenerTodosLosVendedores() {
        
        ArrayList<Vendedor> vendedores = new ArrayList<>();
        String sql = "SELECT * FROM vendedores";
        
        try (Connection connection = getConnection(); 
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterar sobre los resultados de la consulta
            while (rs.next()) {
                // Crear un nuevo objeto Vendedor y llenar con los datos de la tabla
                Vendedor vendedor = new Vendedor(
                        rs.getInt("id"), 
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        null
                );
                
                vendedores.add(vendedor);
                
            }
            connection.close();

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

            // Establecemos el parámetro de la consulta (el id del vendedor)
            stmt.setInt(1, id);

            stmt.executeUpdate();
            connection.close();
            
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
                // Iterar sobre los resultados de la consulta
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

            // Establecer el parámetro del ID en la consulta
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Si se encuentra un resultado, crear el objeto Vendedor
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
    
    public boolean existeID(int id) {
        
        String sql = "SELECT COUNT(*) FROM vendedores WHERE id = ?";
        boolean existe = false;

        try (Connection connection = getConnection();  
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);  // Obtener el conteo de resultados
                    existe = (count > 0);  // Si count es mayor que 0, el ID existe
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendedorMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return existe;  
    }
    
}

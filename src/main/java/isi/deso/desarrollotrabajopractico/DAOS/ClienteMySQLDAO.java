
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.Cliente;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteMySQLDAO implements ClienteDAO {
    private static Connection con;
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/grupo11";
    private static final String USER = "root";
    private static final String PASSWORD = "grupo11";
    private static ClienteMySQLDAO instancia; 

    private ClienteMySQLDAO() {
    }
    
    public static ClienteMySQLDAO obtenerInstancia() {
        
        if(instancia == null){
            instancia = new ClienteMySQLDAO();
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
    
    public void crearCliente(Cliente cliente) {
        
         try {
            Connection connection = getConnection();

            Statement stmt = connection.createStatement();

            String sqlCliente = "INSERT INTO grupo11.clientes (id, nombre, cuit, email, direccion, cbu, alias) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try(PreparedStatement pstmtCliente = connection.prepareStatement(sqlCliente)) {
                pstmtCliente.setInt(1, cliente.getId());
                pstmtCliente.setString(2, cliente.getNombre());
                pstmtCliente.setLong(3, cliente.getCuit());
                pstmtCliente.setString(4, cliente.getEmail());
                pstmtCliente.setString(5, cliente.getDireccion());
                pstmtCliente.setObject(6, cliente.getCbu(), java.sql.Types.BIGINT);
                pstmtCliente.setString(7, cliente.getAlias());
                pstmtCliente.executeUpdate();
            }
               
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    public void actualizarCliente(Cliente cliente) {
        
        String sqlCliente = "UPDATE grupo11.clientes SET nombre = ?, cuit = ?, email = ?, direccion = ?, cbu = ?, alias = ? WHERE id = ?";

        try (Connection connection = getConnection(); 
             PreparedStatement pstmtCliente = connection.prepareStatement(sqlCliente)) {

            pstmtCliente.setString(1, cliente.getNombre());
            pstmtCliente.setLong(2, cliente.getCuit());
            pstmtCliente.setString(3, cliente.getEmail());
            pstmtCliente.setString(4, cliente.getDireccion());
            pstmtCliente.setObject(5, cliente.getCbu(), java.sql.Types.BIGINT);
            pstmtCliente.setString(6, cliente.getAlias());
            pstmtCliente.setInt(7, cliente.getId());
            
            pstmtCliente.executeUpdate();
            connection.close();

        } catch (SQLException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
     
    public ArrayList<Cliente> obtenerTodosLosClientes() {
        
        ArrayList<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        
        try (Connection connection = getConnection(); 
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterar sobre los resultados de la consulta
            while (rs.next()) {
                // Crear un nuevo objeto Cliente y llenar con los datos de la tabla
                Cliente cliente = new Cliente(
                        rs.getInt("id"), 
                        rs.getLong("cuit"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        null,
                        rs.getObject("cbu", Long.class),
                        rs.getString("alias")
                );
        
                clientes.add(cliente);
                
            }
            connection.close();

        } catch (SQLException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clientes;
        
    }
    
    public void eliminarCliente(int id) {
        
        String sql = "DELETE FROM clientes WHERE id = ?";  

        try (Connection connection = getConnection(); 
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Establecemos el parámetro de la consulta (el id del cliente)
            stmt.setInt(1, id);

            stmt.executeUpdate();
            connection.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public ArrayList<Cliente> buscarClientesPorNombre(String nombre) {
        
        ArrayList<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE nombre LIKE ?";  

        try (Connection connection = getConnection(); 
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Establecer el parámetro del nombre en la consulta
            pstmt.setString(1, nombre + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                // Iterar sobre los resultados de la consulta
                while (rs.next()) {
                    Cliente cliente = new Cliente(
                        rs.getInt("id"), 
                        rs.getLong("cuit"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        null,
                        rs.getLong("cbu"),
                        rs.getString("alias")
                    );
                    clientes.add(cliente);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clientes;
    }

    
    public Cliente buscarClientePorID(int id) {
        
        Cliente cliente = null;  
        String sql = "SELECT * FROM clientes WHERE id = ?";  

        try (Connection connection = getConnection(); 
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Establecer el parámetro del ID en la consulta
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Si se encuentra un resultado, crear el objeto Cliente
                if (rs.next()) {
                    cliente = new Cliente(
                        rs.getInt("id"), 
                        rs.getLong("cuit"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        null,
                        rs.getLong("cbu"),
                        rs.getString("alias")
                    );
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cliente; 
    }
    
    public int obtenerID() {
        
        String sql = "SELECT COUNT(*) FROM clientes";
        int ID = 0;
        
        try (Connection connection = getConnection();  
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                int count = rs.getInt(1);  
                ID = count + 1; 
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ID;  
    }
 

    public Cliente buscarClientePorCuit(long cuit) {
        
        Cliente cliente = null;  
        String sql = "SELECT * FROM clientes WHERE cuit = ?";  

        try (Connection connection = getConnection(); 
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Establecer el parámetro del ID en la consulta
            pstmt.setLong(1, cuit);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Si se encuentra un resultado, crear el objeto Cliente
                if (rs.next()) {
                    cliente = new Cliente(
                        rs.getInt("id"), 
                        rs.getLong("cuit"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        null,
                        rs.getLong("cbu"),
                        rs.getString("alias")
                    );
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cliente; 
    }
}




package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.modelo.Cliente;
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
    
    public int crearCliente(Cliente cliente) {
        
        String sqlCliente = "INSERT INTO grupo11.clientes (nombre, cuit, email, direccion, cbu, alias) VALUES (?, ?, ?, ?, ?, ?)";
        int clienteId = -1;

        try (Connection connection = getConnection();
             PreparedStatement pstmtCliente = connection.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {

            pstmtCliente.setString(1, cliente.getNombre());
            pstmtCliente.setLong(2, cliente.getCuit());
            pstmtCliente.setString(3, cliente.getEmail());
            pstmtCliente.setString(4, cliente.getDireccion());
            pstmtCliente.setObject(5, cliente.getCbu(), java.sql.Types.BIGINT);
            pstmtCliente.setString(6, cliente.getAlias());

            pstmtCliente.executeUpdate();

            try (ResultSet generatedKeys = pstmtCliente.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    clienteId = generatedKeys.getInt(1); // Obtiene el id generado
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, "Error en la creación del cliente", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, "Clase no encontrada", ex);
        }

        return clienteId;
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

        } catch (SQLException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClienteMySQLDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
     
    public ArrayList<Cliente> obtenerTodosLosClientes() {
        
        ArrayList<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY id";
        
        try (Connection connection = getConnection(); 
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
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

            stmt.setInt(1, id);

            stmt.executeUpdate();
            
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

            pstmt.setString(1, nombre + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
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

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
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

    public Cliente buscarClientePorCuit(long cuit) {
        
        Cliente cliente = null;  
        String sql = "SELECT * FROM clientes WHERE cuit = ?";  

        try (Connection connection = getConnection(); 
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setLong(1, cuit);

            try (ResultSet rs = pstmt.executeQuery()) {
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




package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.Pago;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PagoMySQLDAO implements PagoDAO {
    
    private static Connection con;
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/grupo11";
    private static final String USER = "root";
    private static final String PASSWORD = "grupo11";
    private static PagoMySQLDAO instancia; 

    private PagoMySQLDAO() {
    }
    
    public static PagoMySQLDAO obtenerInstancia() {
        
        if(instancia == null){
            instancia = new PagoMySQLDAO();
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

    public void registrarPago(Pago pago) {
        String sqlPago = "INSERT INTO grupo11.pagos (idPedido, total, fecha) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement pstmtPago = connection.prepareStatement(sqlPago, Statement.RETURN_GENERATED_KEYS)) {

            pstmtPago.setInt(1, pago.getId_pedido());
            pstmtPago.setDouble(2, pago.getMonto());
            pstmtPago.setObject(3, pago.getFecha());  

            pstmtPago.executeUpdate();

            // Obtener el ID generado automáticamente
            try (ResultSet generatedKeys = pstmtPago.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pago.setId(generatedKeys.getInt(1));  
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PagoMySQLDAO.class.getName()).log(Level.SEVERE, "Error en la creación del pago", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PagoMySQLDAO.class.getName()).log(Level.SEVERE, "Clase no encontrada", ex);
        }
    }

}

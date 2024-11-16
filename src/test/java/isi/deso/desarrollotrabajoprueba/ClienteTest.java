
package isi.deso.desarrollotrabajoprueba;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteTest {

    private static Connection connection;

    @BeforeAll
    static void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/grupo11", "root", "grupo11");
    }
    
    @AfterAll
    static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testIdNoNulos() throws Exception {
        String sql = "SELECT COUNT(*) FROM clientes WHERE id IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                assertEquals(0, count, "No debe haber registros con id nulo");
            }
        }
    }

    @Test
    public void testCuitNoDuplicado() throws Exception {
        String sql = "SELECT cuit, COUNT(*) FROM clientes GROUP BY cuit HAVING COUNT(*) > 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            assertFalse(rs.next(), "No debe haber cuits duplicados en la tabla");
        }
    }

    @Test
    public void testNombreNoNulo() throws Exception {
        String sql = "SELECT COUNT(*) FROM clientes WHERE nombre IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                assertEquals(0, count, "No debe haber registros con nombre nulo");
            }
        }
    }

    @Test
    public void testEmailNoNulo() throws Exception {
        String sql = "SELECT COUNT(*) FROM clientes WHERE email IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                assertEquals(0, count, "No debe haber registros con email nulo");
            }
        }
    }

    @Test
    public void testDireccionNoNulo() throws Exception {
        String sql = "SELECT COUNT(*) FROM clientes WHERE direccion IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                assertEquals(0, count, "No debe haber registros con direccion nula");
            }
        }
    }
}


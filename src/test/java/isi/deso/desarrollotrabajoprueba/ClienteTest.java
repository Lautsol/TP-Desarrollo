
package isi.deso.desarrollotrabajoprueba;

import isi.deso.desarrollotrabajopractico.Cliente;
import isi.deso.desarrollotrabajopractico.DAOS.ClienteMySQLDAO;
import isi.deso.desarrollotrabajopractico.DAOS.FactoryDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClienteTest {
    
    ClienteMySQLDAO clienteDAO;
    Connection connection;
        
    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        clienteDAO = (ClienteMySQLDAO) FactoryDAO.getClienteDAO();
        connection = clienteDAO.getConnection();
        limpiarTablaClientes();
    }   
        
    @AfterEach
    void tearDown() throws SQLException {
        limpiarTablaClientes();
        connection.close();
    }    
    
    // Método para limpiar la tabla de clientes
    private void limpiarTablaClientes() throws SQLException {
        String sql = "DELETE FROM grupo11.clientes";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
        
    @Test 
    void testCrearCliente() {
        
        Cliente cliente = new Cliente(20, 1234, "Juan Pérez", "jperez@gmail.com", "La Rioja 1250", null, 1222L, "juan01");
        
        clienteDAO.crearCliente(cliente);
        
        // Verificar que el cliente fue insertado en la base de datos
        Cliente clienteRecuperado = clienteDAO.buscarClientePorID(20);
        assertNotNull(clienteRecuperado);
        assertEquals(clienteRecuperado.getNombre(), cliente.getNombre());
        assertEquals(clienteRecuperado.getCuit(), cliente.getCuit());
        assertEquals(clienteRecuperado.getEmail(), cliente.getEmail());
        
    }
    
    @Test
    void testActualizarCliente() {
        
        Cliente cliente = new Cliente(20, 1234, "Juan Pérez", "jperez@gmail.com", "La Rioja 1250", null, 1222L, "juan01");
        clienteDAO.crearCliente(cliente);
        
        // Verificar que el cliente fue insertado
        Cliente clienteRecuperado = clienteDAO.buscarClientePorID(20);
        assertNotNull(clienteRecuperado);
        
        Cliente clienteActualizado = new Cliente(20, 1233, "Martín Pérez", "mperez@gmail.com", "San Juan 1250", null, 1252L, "juan01");
        clienteDAO.actualizarCliente(clienteActualizado);
        
        clienteRecuperado = clienteDAO.buscarClientePorID(20);
        assertEquals(clienteRecuperado.getCuit(), clienteActualizado.getCuit());
        assertEquals(clienteRecuperado.getNombre(), clienteActualizado.getNombre());
        assertEquals(clienteRecuperado.getEmail(), clienteActualizado.getEmail());
        assertEquals(clienteRecuperado.getDireccion(), clienteActualizado.getDireccion());
        assertEquals(clienteRecuperado.getCbu(), clienteActualizado.getCbu());
        assertEquals(clienteRecuperado.getAlias(), clienteActualizado.getAlias());
        
    }
    
    @Test
    void testEliminarCliente() {
        
        Cliente cliente = new Cliente(20, 1234, "Juan Pérez", "jperez@gmail.com", "La Rioja 1250", null, 1222L, "juan01");
        clienteDAO.crearCliente(cliente);
        
        // Verificar que el cliente fue insertado
        Cliente clienteRecuperado = clienteDAO.buscarClientePorID(20);
        assertNotNull(clienteRecuperado);
        
        clienteDAO.eliminarCliente(20);
        
        clienteRecuperado = clienteDAO.buscarClientePorID(20);
        // Verificar que el cliente fue eliminado
        assertNull(clienteRecuperado);
        
    }
    
    @Test
    void testBuscarClientesPorNombre() {
        
        Cliente cliente1 = new Cliente(20, 1234, "Pedro Sánchez", "pedrosanchez@gmail.com", "La Rioja 1250", null, 1222L, "pedro01");
        clienteDAO.crearCliente(cliente1);
        
        Cliente cliente2 = new Cliente(21, 1111, "Pedro Sánchez", "psanchez@gmail.com", "San Martín 1400", null, 2032L, "pedro02");
        clienteDAO.crearCliente(cliente2);
        
        // Verificar que los clientes fueron insertados
        Cliente clienteRecuperado1 = clienteDAO.buscarClientePorID(20);
        assertNotNull(clienteRecuperado1);
        
        Cliente clienteRecuperado2 = clienteDAO.buscarClientePorID(21);
        assertNotNull(clienteRecuperado2);
        
        ArrayList<Cliente> busquedaClientes = clienteDAO.buscarClientesPorNombre("Pedro Sánchez");
        
        assertNotNull(busquedaClientes.get(0));
        assertNotNull(busquedaClientes.get(1));
        assertEquals(busquedaClientes.get(0).getNombre(), cliente1.getNombre());
        assertEquals(busquedaClientes.get(1).getNombre(), cliente2.getNombre());
        
    }
    
    @Test
    void testBuscarClientesPorID() {
        
        Cliente cliente = new Cliente(20, 1234, "Pedro Sánchez", "pedrosanchez@gmail.com", "La Rioja 1250", null, 1222L, "pedro01");
        clienteDAO.crearCliente(cliente);
        
        // Verificar que el cliente fue insertado
        Cliente clienteRecuperado = clienteDAO.buscarClientePorID(20);
        assertNotNull(clienteRecuperado);
        
        clienteRecuperado = clienteDAO.buscarClientePorID(20);
        
        assertNotNull(clienteRecuperado);
        assertEquals(clienteRecuperado.getNombre(), cliente.getNombre());
        assertEquals(clienteRecuperado.getCuit(), cliente.getCuit());
        
    }
        
}
    


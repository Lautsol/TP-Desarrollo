
package isi.deso.desarrollotrabajoprueba;
import isi.deso.desarrollotrabajopractico.Cliente;
import isi.deso.desarrollotrabajopractico.Controladores.ClienteController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class ClienteControllerTest {

    private static ClienteController clienteController;  
    private static Connection connection;  

    @BeforeAll
    static void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/grupo11", "root", "grupo11");
        clienteController = new ClienteController();
    }
    
    @AfterAll
    static void tearDownAfterClass() throws SQLException {
        
        List<Integer> clientesAEliminar = Arrays.asList(30, 31, 32, 33, 34, 35);  

        String queryDelete = "DELETE FROM clientes WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/grupo11", "root", "grupo11");
             PreparedStatement stmt = connection.prepareStatement(queryDelete)) {

            for (int clienteId : clientesAEliminar) {
                stmt.setInt(1, clienteId);  
                stmt.executeUpdate();
            }
            
            connection.close();
        }
    }

    @Test
    void testCrearCliente() throws SQLException {
        
        String nombre = "Juan Perez";
        int id = 30;
        long cuit = 20304050607L;
        String alias = "juanp";
        Long cbu = 123456789012L;
        String email = "juan@mail.com";
        String direccion = "La Rioja 1250";

        clienteController.crearCliente(nombre, id, cuit, alias, cbu, email, direccion);

        // Verificar si el cliente fue creado correctamente
        Cliente clienteCreado = clienteController.buscarCliente(id);
        assertNotNull(clienteCreado);
        assertEquals(id, clienteCreado.getId());
        assertEquals(nombre, clienteCreado.getNombre());
        assertEquals(alias, clienteCreado.getAlias());
        assertEquals(cbu, clienteCreado.getCbu());
        assertEquals(email, clienteCreado.getEmail());
        assertEquals(direccion, clienteCreado.getDireccion());
    }

    @Test
    void testActualizarCliente() throws SQLException {
        
        String nombre = "Juan Perez";
        int id = 31;
        long cuit = 20304050607L;
        String alias = "juanp";
        Long cbu = 123456789012L;
        String email = "juan@mail.com";
        String direccion = "La Rioja 1250";

        clienteController.crearCliente(nombre, id, cuit, alias, cbu, email, direccion);

        // Actualizar la información del cliente
        String nuevoNombre = "Juan Carlos Perez";
        String nuevoAlias = "juan_carlos";
        Long nuevoCbu = 987654321098L;
        String nuevoEmail = "juancarlos@mail.com";
        String nuevaDireccion = "San Martín 1340";

        clienteController.actualizarCliente(nuevoNombre, id, cuit, nuevoAlias, nuevoCbu, nuevoEmail, nuevaDireccion);

        // Verificar que los cambios se han realizado correctamente
        Cliente clienteActualizado = clienteController.buscarCliente(id);
        assertNotNull(clienteActualizado);
        assertEquals(nuevoNombre, clienteActualizado.getNombre());
        assertEquals(nuevoAlias, clienteActualizado.getAlias());
        assertEquals(nuevoCbu, clienteActualizado.getCbu());
        assertEquals(nuevoEmail, clienteActualizado.getEmail());
        assertEquals(nuevaDireccion, clienteActualizado.getDireccion());
    }

    @Test
    void testEliminarCliente() throws SQLException {
        
        String nombre = "Juan Perez";
        int id = 32;
        long cuit = 20304050607L;
        String alias = "juanp";
        Long cbu = 123456789012L;
        String email = "juan@correo.com";
        String direccion = "La Rioja 1250";

        clienteController.crearCliente(nombre, id, cuit, alias, cbu, email, direccion);

        // Verificar que el cliente fue creado
        Cliente clienteAntesDeEliminar = clienteController.buscarCliente(id);
        assertNotNull(clienteAntesDeEliminar);

        clienteController.eliminarCliente(id);

        // Verificar que el cliente ha sido eliminado
        Cliente clienteEliminado = clienteController.buscarCliente(id);
        assertNull(clienteEliminado);
    }

    @Test
    void testBuscarClientePorId() throws SQLException {
        
        String nombre = "Juan Perez";
        int id = 33; 
        long cuit = 20304050607L;
        String alias = "juanp";
        Long cbu = 123456789012L;
        String email = "juan@correo.com";
        String direccion = "La Rioja 1250";

        clienteController.crearCliente(nombre, id, cuit, alias, cbu, email, direccion);

        Cliente clienteBuscado = clienteController.buscarCliente(id);

        // Verificar que el cliente fue encontrado
        assertNotNull(clienteBuscado);
        assertEquals(id, clienteBuscado.getId());
        assertEquals(nombre, clienteBuscado.getNombre());
        assertEquals(alias, clienteBuscado.getAlias());
        assertEquals(cbu, clienteBuscado.getCbu());
        assertEquals(email, clienteBuscado.getEmail());
        assertEquals(direccion, clienteBuscado.getDireccion());
    }

    @Test
    void testListarClientesPorNombre() throws SQLException {
        
        clienteController.crearCliente("Juan Perez", 34, 20304050607L, "juanp", 123456789012L, "juan@mail.com", "La Rioja 1250");
        clienteController.crearCliente("Maria Lopez", 35, 20304050608L, "marial", 123456789013L, "maria@mail.com", "San Martín 1340");

        List<Cliente> clientes = clienteController.listarClientes("Juan Perez");

        // Verificar que los clientes listados coinciden con el nombre
        assertNotNull(clientes);
        assertEquals(1, clientes.size());  // Debería haber solo un cliente con el nombre "Juan Perez"
        assertEquals("Juan Perez", clientes.get(0).getNombre());
    }
}


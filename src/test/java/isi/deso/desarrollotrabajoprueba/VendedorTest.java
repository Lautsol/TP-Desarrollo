
package isi.deso.desarrollotrabajoprueba;

import isi.deso.desarrollotrabajopractico.DAOS.VendedorMySQLDAO;
import isi.deso.desarrollotrabajopractico.DAOS.FactoryDAO;
import isi.deso.desarrollotrabajopractico.Vendedor;
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

public class VendedorTest {
    
    VendedorMySQLDAO vendedorDAO;
    Connection connection;
        
    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        vendedorDAO = (VendedorMySQLDAO) FactoryDAO.getVendedorDAO();
        connection = vendedorDAO.getConnection();
        limpiarTablaVendedores();
    }   
        
    @AfterEach
    void tearDown() throws SQLException {
        limpiarTablaVendedores();
        connection.close();
    }    
    
    // Método para limpiar la tabla de vendedores
    private void limpiarTablaVendedores() throws SQLException {
        String sql = "DELETE FROM grupo11.vendedores";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
        
    @Test 
    void testCrearVendedor() {
        
        Vendedor vendedor = new Vendedor(20, "Juan Pérez", "La Rioja 1250", null);
        
        vendedorDAO.crearVendedor(vendedor);
        
        // Verificar que el vendedor fue insertado en la base de datos
        Vendedor vendedorRecuperado = vendedorDAO.buscarVendedorPorID(20);
        assertNotNull(vendedorRecuperado);
        assertEquals(vendedorRecuperado.getNombre(), vendedor.getNombre());
        assertEquals(vendedorRecuperado.getDireccion(), vendedor.getDireccion());
        
    }
    
    @Test
    void testActualizarCliente() {
        Vendedor vendedor = new Vendedor(20, "Juan Pérez", "La Rioja 1250", null);
        
        vendedorDAO.crearVendedor(vendedor);
        
        // Verificar que el vendedor fue insertado en la base de datos
        Vendedor vendedorRecuperado = vendedorDAO.buscarVendedorPorID(20);
        assertNotNull(vendedorRecuperado);
        
        Vendedor vendedorActualizado = new Vendedor(20, "Martín Pérez", "San Juan 1250", null);
        vendedorDAO.actualizarVendedor(vendedorActualizado);
        
        vendedorRecuperado = vendedorDAO.buscarVendedorPorID(20);
        assertEquals(vendedorRecuperado.getNombre(), vendedorActualizado.getNombre());
        assertEquals(vendedorRecuperado.getDireccion(), vendedorActualizado.getDireccion());
        
    }
    
    @Test
    void testEliminarVendedor() {
        
        Vendedor vendedor = new Vendedor(20, "Juan Pérez", "La Rioja 1250", null);
        
        vendedorDAO.crearVendedor(vendedor);
        
        // Verificar que el vendedor fue insertado en la base de datos
        Vendedor vendedorRecuperado = vendedorDAO.buscarVendedorPorID(20);
        assertNotNull(vendedorRecuperado);
        
        vendedorDAO.eliminarVendedor(20);
        
        vendedorRecuperado = vendedorDAO.buscarVendedorPorID(20);
        // Verificar que el vendedor fue eliminado
        assertNull(vendedorRecuperado);
        
    }
    
    @Test
    void testBuscarVendedoresPorNombre() {
        
        Vendedor vendedor1 = new Vendedor(20, "Juan Pérez", "La Rioja 1250", null);
        Vendedor vendedor2 = new Vendedor(21, "Juan Pérez", "San Juan 1400", null);
        
        vendedorDAO.crearVendedor(vendedor1);
        vendedorDAO.crearVendedor(vendedor2);
        
        // Verificar que los vendedores fueron insertados en la base de datos
        Vendedor vendedorRecuperado1 = vendedorDAO.buscarVendedorPorID(20);
        assertNotNull(vendedorRecuperado1);
        
        Vendedor vendedorRecuperado2 = vendedorDAO.buscarVendedorPorID(21);
        assertNotNull(vendedorRecuperado2);
        
        ArrayList<Vendedor> busquedaVendedores = vendedorDAO.buscarVendedoresPorNombre("Juan Pérez");
        
        assertNotNull(busquedaVendedores.get(0));
        assertNotNull(busquedaVendedores.get(1));
        assertEquals(busquedaVendedores.get(0).getNombre(), vendedor1.getNombre());
        assertEquals(busquedaVendedores.get(1).getNombre(), vendedor2.getNombre());
        
    }
    
    @Test
    void testBuscarVendedoresPorID() {
        
        Vendedor vendedor = new Vendedor(20, "Pedro Sánchez", "La Rioja 1250", null);
        vendedorDAO.crearVendedor(vendedor);
        
        // Verificar que el vendedor fue insertado
        Vendedor vendedorRecuperado = vendedorDAO.buscarVendedorPorID(20);
        assertNotNull(vendedorRecuperado);
        
        vendedorRecuperado = vendedorDAO.buscarVendedorPorID(20);
        
        assertNotNull(vendedorRecuperado);
        assertEquals(vendedorRecuperado.getNombre(), vendedor.getNombre());
        assertEquals(vendedorRecuperado.getDireccion(), vendedor.getDireccion());
        
    }
        
}
    


package isi.deso.desarrollotrabajoprueba;

import isi.deso.desarrollotrabajopractico.Categoria;
import isi.deso.desarrollotrabajopractico.DAOS.CategoriaMySQLDAO;
import isi.deso.desarrollotrabajopractico.ItemMenu;
import isi.deso.desarrollotrabajopractico.DAOS.ItemMenuMySQLDAO;
import isi.deso.desarrollotrabajopractico.DAOS.FactoryDAO;
import isi.deso.desarrollotrabajopractico.Plato;
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

public class ItemTest {
    
    ItemMenuMySQLDAO itemMenuDAO;
    CategoriaMySQLDAO categoriaDAO;
    Connection connection;
    Connection connection1;
        
    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        itemMenuDAO = (ItemMenuMySQLDAO) FactoryDAO.getItemMenuDAO();
        categoriaDAO = (CategoriaMySQLDAO) FactoryDAO.getCategoriaDAO();
        connection = itemMenuDAO.getConnection();
        connection1 = categoriaDAO.getConnection();
        limpiarTablaItemsMenu();
    }   
        
    @AfterEach
    void tearDown() throws SQLException {
        limpiarTablaItemsMenu();
        connection.close();
        connection1.close();
    }    
    
    // Método para limpiar la tabla de itemsMenu
    private void limpiarTablaItemsMenu() throws SQLException {
        String sql = "DELETE FROM grupo11.itemsMenu";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
        
    @Test 
    void testCrearItemMenu() {
        
        Categoria categoria = categoriaDAO.buscarCategoria(1);
        ItemMenu itemMenu;
        
        itemMenu = new Plato(); 
        itemMenu.setId(20);
        itemMenu.setNombre("Pizza");
        itemMenu.setDescripcion("Pizza napolitana");
        itemMenu.setPrecio(22);
        itemMenu.setCategoria(categoria);
        
        itemMenuDAO.crearItemMenu(itemMenu);
        
        // Verificar que el itemMenu fue insertado en la base de datos
        ItemMenu itemMenuRecuperado = itemMenuDAO.buscarItemMenuPorID(20);
        assertNotNull(itemMenuRecuperado);
        assertEquals(itemMenuRecuperado.getNombre(), itemMenu.getNombre());
        assertEquals(itemMenuRecuperado.getPrecio(), itemMenu.getPrecio());
        assertEquals(itemMenuRecuperado.getDescripcion(), itemMenu.getDescripcion());
    }
    
    @Test
    void testActualizarItemMenu() {
        
        Categoria categoria = categoriaDAO.buscarCategoria(1);
        
        Plato itemMenu = new Plato(); 
        itemMenu.setId(20);
        itemMenu.setNombre("Pizza");
        itemMenu.setDescripcion("Pizza");
        itemMenu.setPrecio(22);
        itemMenu.setCategoria(categoria);
        
        itemMenuDAO.crearItemMenu(itemMenu);
        
        // Verificar que el itemMenu fue insertado en la base de datos
        ItemMenu itemMenuRecuperado = itemMenuDAO.buscarItemMenuPorID(20);
        
        Plato itemMenuActualizado = new Plato(); 
        itemMenuActualizado.setId(20);
        itemMenuActualizado.setNombre("Pizza");
        itemMenuActualizado.setDescripcion("Pizza");
        itemMenuActualizado.setPrecio(40);
        itemMenuActualizado.setCategoria(categoria);
        
        itemMenuDAO.actualizarItemMenu(itemMenuActualizado);
        
        itemMenuRecuperado = itemMenuDAO.buscarItemMenuPorID(20);
        assertEquals(itemMenuRecuperado.getNombre(), itemMenuActualizado.getNombre());
        assertEquals(itemMenuRecuperado.getPrecio(), itemMenuActualizado.getPrecio());
    }
    
    @Test
    void testEliminarItemMenu() {
        
        Categoria categoria = categoriaDAO.buscarCategoria(1);
        
        Plato itemMenu = new Plato(); 
        itemMenu.setId(20);
        itemMenu.setNombre("Pizza");
        itemMenu.setDescripcion("Pizza");
        itemMenu.setPrecio(22);
        itemMenu.setCategoria(categoria);
        
        itemMenuDAO.crearItemMenu(itemMenu);
        
        // Verificar que el itemMenu fue insertado en la base de datos
        ItemMenu itemMenuRecuperado = itemMenuDAO.buscarItemMenuPorID(20);
        
        itemMenuDAO.eliminarItemMenu(20);
        
        itemMenuRecuperado = itemMenuDAO.buscarItemMenuPorID(20);
        // Verificar que el itemMenu fue eliminado
        assertNull(itemMenuRecuperado);
    }
    
    @Test
    void testBuscarItemsMenuPorNombre() {
        
        Categoria categoria = categoriaDAO.buscarCategoria(1);
        
        Plato itemMenu1 = new Plato(); 
        itemMenu1.setId(20);
        itemMenu1.setNombre("Pizza");
        itemMenu1.setDescripcion("Pizza muzzarella");
        itemMenu1.setPrecio(22);
        itemMenu1.setCategoria(categoria);
        
        itemMenuDAO.crearItemMenu(itemMenu1);
        
        Plato itemMenu2 = new Plato(); 
        itemMenu2.setId(21);
        itemMenu2.setNombre("Pizza");
        itemMenu2.setDescripcion("Pizza napolitana");
        itemMenu2.setPrecio(30);
        itemMenu2.setCategoria(categoria);
        
        itemMenuDAO.crearItemMenu(itemMenu2);
        
        // Verificar que los itemsMenu fueron insertados
        ItemMenu itemMenuRecuperado1 = itemMenuDAO.buscarItemMenuPorID(20);
        assertNotNull(itemMenuRecuperado1);
        
        ItemMenu itemMenuRecuperado2 = itemMenuDAO.buscarItemMenuPorID(21);
        assertNotNull(itemMenuRecuperado2);
        
        ArrayList<ItemMenu> busquedaItemsMenu = itemMenuDAO.buscarItemsMenuPorNombre("Pizza");
        
        assertNotNull(busquedaItemsMenu.get(0));
        assertNotNull(busquedaItemsMenu.get(1));
        assertEquals(busquedaItemsMenu.get(0).getNombre(), itemMenu1.getNombre());
        assertEquals(busquedaItemsMenu.get(1).getNombre(), itemMenu2.getNombre());
        assertEquals(busquedaItemsMenu.get(0).getDescripcion(), itemMenu1.getDescripcion());
        assertEquals(busquedaItemsMenu.get(1).getDescripcion(), itemMenu2.getDescripcion());
    }
    
    @Test
    void testBuscarItemsMenuPorID() {
        
        Categoria categoria = categoriaDAO.buscarCategoria(1);
        
        Plato itemMenu = new Plato(); 
        itemMenu.setId(20);
        itemMenu.setNombre("Pizza");
        itemMenu.setDescripcion("Pizza");
        itemMenu.setPrecio(22);
        itemMenu.setCategoria(categoria);
        
        itemMenuDAO.crearItemMenu(itemMenu);
        
        // Verificar que el itemMenu fue insertado en la base de datos
        ItemMenu itemMenuRecuperado = itemMenuDAO.buscarItemMenuPorID(20);
        
        assertNotNull(itemMenuRecuperado);
        assertEquals(itemMenuRecuperado.getNombre(), itemMenu.getNombre());
        assertEquals(itemMenuRecuperado.getPrecio(), itemMenu.getPrecio());
        assertEquals(itemMenuRecuperado.getDescripcion(), itemMenu.getDescripcion());
    }
}

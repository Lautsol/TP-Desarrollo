package isi.deso.desarrollotrabajoprueba;

import isi.deso.desarrollotrabajopractico.modelo.ItemMenu;
import isi.deso.desarrollotrabajopractico.Controladores.ItemMenuController;
import isi.deso.desarrollotrabajopractico.modelo.TipoItem;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class ItemMenuControllerTest {

    private static ItemMenuController itemMenuController;  
    private static Connection connection;
    private static List<Integer> itemsAEliminar;
    
    @BeforeAll
    static void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/grupo11", "root", "grupo11");
        itemMenuController = new ItemMenuController();
        itemsAEliminar = new ArrayList<>(); 
    }
    
    @AfterAll
    static void tearDownAfterClass() throws SQLException {

        String queryDelete = "DELETE FROM itemsmenu WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/grupo11", "root", "grupo11");
             PreparedStatement stmt = connection.prepareStatement(queryDelete)) {

            for (int itemId : itemsAEliminar) {
                stmt.setInt(1, itemId);  
                stmt.executeUpdate();
            }
            
            connection.close();
        }
    }

    @Test
    void testCrearItemMenu() throws SQLException {
        
        String nombre = "Pizza Margherita";
        String descripcion = "Pizza clásica con tomate, mozzarella y albahaca.";
        double precio = 8.99;
        int idCategoria = 1; 
        String tipoItem = "PLATO";

        int id = itemMenuController.crearItemMenu(nombre, descripcion, precio, idCategoria, tipoItem);

        ItemMenu itemCreado = itemMenuController.buscarItemMenu(id);
        assertNotNull(itemCreado);
        assertEquals(id, itemCreado.getId());
        assertEquals(nombre, itemCreado.getNombre());
        assertEquals(descripcion, itemCreado.getDescripcion());
        assertEquals(precio, itemCreado.getPrecio());
        assertEquals(idCategoria, itemCreado.getCategoria().getId());
        
        itemsAEliminar.add(id);
    }

    @Test
    void testActualizarItemMenu() throws SQLException {
        
        String nombre = "Pizza Margherita";
        String descripcion = "Pizza clásica con tomate, mozzarella y albahaca.";
        double precio = 8.99;
        int idCategoria = 1;
        String tipoItem = "PLATO";

        int id = itemMenuController.crearItemMenu(nombre, descripcion, precio, idCategoria, tipoItem);

        String nuevoNombre = "Pizza Diavola";
        String nuevaDescripcion = "Pizza con salami picante y mozzarella.";
        double nuevoPrecio = 10.99;
        int nuevaCategoria = 2; 
        String nuevoTipoItem = "PLATO";

        itemMenuController.actualizarItemMenu(id, TipoItem.COMIDA, nuevoNombre, nuevaDescripcion, nuevoPrecio, nuevaCategoria, nuevoTipoItem);

        // Verificar que los cambios se han realizado correctamente
        ItemMenu itemActualizado = itemMenuController.buscarItemMenu(id);
        assertNotNull(itemActualizado);
        assertEquals(nuevoNombre, itemActualizado.getNombre());
        assertEquals(nuevaDescripcion, itemActualizado.getDescripcion());
        assertEquals(nuevoPrecio, itemActualizado.getPrecio());
        assertEquals(nuevaCategoria, itemActualizado.getCategoria().getId());
        
        itemsAEliminar.add(id);
    }

    @Test
    void testEliminarItemMenu() throws SQLException {
        
        String nombre = "Pizza Funghi";
        String descripcion = "Pizza con champiñones y mozzarella.";
        double precio = 9.99;
        int idCategoria = 1;
        String tipoItem = "PLATO";

        int id = itemMenuController.crearItemMenu(nombre, descripcion, precio, idCategoria, tipoItem);

        ItemMenu itemAntesDeEliminar = itemMenuController.buscarItemMenu(id);
        assertNotNull(itemAntesDeEliminar);

        itemMenuController.eliminarItemMenu(id);

        // Verificar que el item ha sido eliminado
        ItemMenu itemEliminado = itemMenuController.buscarItemMenu(id);
        assertNull(itemEliminado);
    }

    @Test
    void testBuscarItemMenuPorId() throws SQLException {
        
        String nombre = "Pizza Napolitana";
        String descripcion = "Pizza clásica con anchoas, alcaparras y aceitunas.";
        double precio = 9.50;
        int idCategoria = 1;
        String tipoItem = "PLATO";

        int id = itemMenuController.crearItemMenu(nombre, descripcion, precio, idCategoria, tipoItem);

        ItemMenu itemBuscado = itemMenuController.buscarItemMenu(id);

        // Verificar que el item fue encontrado
        assertNotNull(itemBuscado);
        assertEquals(id, itemBuscado.getId());
        assertEquals(nombre, itemBuscado.getNombre());
        assertEquals(descripcion, itemBuscado.getDescripcion());
        assertEquals(precio, itemBuscado.getPrecio());
        assertEquals(idCategoria, itemBuscado.getCategoria().getId());
        
        itemsAEliminar.add(id);
    }

    @Test
    void testListarItemsMenuPorNombre() throws SQLException {
        
        int id1 = itemMenuController.crearItemMenu("Pizza Cuatro Quesos", "Pizza con cuatro tipos de queso.", 10.50, 1, "PLATO");
        int id2 = itemMenuController.crearItemMenu("Pizza Vegetariana", "Pizza con vegetales frescos.", 9.50, 1, "PLATO");

        List<ItemMenu> items = itemMenuController.listarItemsMenu("Pizza Cuatro Quesos");

        // Verificar que los items listados coinciden con el nombre
        assertNotNull(items);
        assertEquals(1, items.size());  // Debería haber solo un item con el nombre "Pizza Cuatro Quesos"
        assertEquals("Pizza Cuatro Quesos", items.get(0).getNombre());
        
        itemsAEliminar.add(id1);
        itemsAEliminar.add(id2);
    }
}

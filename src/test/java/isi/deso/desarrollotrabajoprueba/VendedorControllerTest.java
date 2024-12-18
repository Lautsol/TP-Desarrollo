package isi.deso.desarrollotrabajoprueba;

import isi.deso.desarrollotrabajopractico.modelo.Categoria;
import isi.deso.desarrollotrabajopractico.modelo.Vendedor;
import isi.deso.desarrollotrabajopractico.Controladores.VendedorController;
import isi.deso.desarrollotrabajopractico.modelo.Gaseosa;
import isi.deso.desarrollotrabajopractico.modelo.ItemMenu;
import isi.deso.desarrollotrabajopractico.modelo.Plato;
import isi.deso.desarrollotrabajopractico.modelo.TipoItem;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class VendedorControllerTest {

    private static VendedorController vendedorController;  
    private static Connection connection;  
    private static List<Integer> vendedoresAEliminar;

    @BeforeAll
    static void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/grupo11", "root", "grupo11");
        vendedorController = new VendedorController();
        vendedoresAEliminar = new ArrayList<>();
    }

    @AfterAll
    static void tearDownAfterClass() throws SQLException {

        String queryDelete = "DELETE FROM vendedores WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/grupo11", "root", "grupo11");
             PreparedStatement stmt = connection.prepareStatement(queryDelete)) {

            for (int vendedorId : vendedoresAEliminar) {
                stmt.setInt(1, vendedorId);  
                stmt.executeUpdate();
            }

            connection.close();
        }
    }

    @Test
    void testCrearVendedor() throws SQLException {
        
        String nombre = "Carlos Gómez";
        String direccion = "Av. Ramírez 123";
        ArrayList<ItemMenu> itemsMenu = new ArrayList();
        Categoria categoria1 = new Categoria(1, "Pizza", TipoItem.COMIDA);
        Plato item1 = new Plato(10,"Pizza Margherita","Pizza clásica con tomate, mozzarella y albahaca.",8.99,categoria1,10,1,false,false,false);
        itemsMenu.add(item1);

        int id = vendedorController.crearVendedor(nombre, direccion, itemsMenu);

        // Verificar si el vendedor fue creado correctamente
        Vendedor vendedorCreado = vendedorController.buscarPorIdVendedor(id);
        assertNotNull(vendedorCreado);
        assertEquals(id, vendedorCreado.getId());
        assertEquals(nombre, vendedorCreado.getNombre());
        assertEquals(direccion, vendedorCreado.getDireccion());
        
        vendedoresAEliminar.add(id);
    }

    @Test
    void testActualizarVendedor() throws SQLException {
        
        String nombre = "Carlos Gómez";
        String direccion = "Av. Ramírez 123";
        ArrayList<ItemMenu> itemsMenu = new ArrayList();
        Categoria categoria1 = new Categoria(1, "Pizza", TipoItem.COMIDA);
        Plato item1 = new Plato(10,"Pizza Margherita","Pizza clásica con tomate, mozzarella y albahaca.",8.99,categoria1,10,1,false,false,false);
        itemsMenu.add(item1);

        int id = vendedorController.crearVendedor(nombre, direccion, itemsMenu);

        String nuevoNombre = "Carlos Alberto Gómez";
        String nuevaDireccion = "San Juan 456";
        ArrayList<ItemMenu> nuevoitemMenu = new ArrayList();
        Categoria categoria2 = new Categoria(1, "", TipoItem.BEBIDA);
        Gaseosa item2 = new Gaseosa(9,"Coca Cola","Coca Cola clásica",5.99,categoria2,10);
        itemsMenu.add(item2);
        Vendedor vendedorActualizar = new Vendedor();
        vendedorActualizar.setNombre(nuevoNombre);
        vendedorActualizar.setId(id);
        vendedorActualizar.setDireccion(nuevaDireccion);

        vendedorController.actualizarVendedor(vendedorActualizar, nuevoitemMenu);

        // Verificar que los cambios se han realizado correctamente
        Vendedor vendedorActualizado = vendedorController.buscarPorIdVendedor(id);
        assertNotNull(vendedorActualizado);
        assertEquals(nuevoNombre, vendedorActualizado.getNombre());
        assertEquals(nuevaDireccion, vendedorActualizado.getDireccion());
        assertEquals(id, vendedorActualizado.getId());
       
        vendedoresAEliminar.add(id);
    }

    @Test
    void testEliminarVendedor() throws SQLException {
        
        String nombre = "Carlos Gómez";
        String direccion = "Av. Ramírez 123";
        ArrayList<ItemMenu> itemsMenu = new ArrayList();
        Categoria categoria1 = new Categoria(1, "Pizza", TipoItem.COMIDA);
        Plato item1 = new Plato(10,"Pizza Margherita","Pizza clásica con tomate, mozzarella y albahaca.",8.99,categoria1,10,1,false,false,false);
        itemsMenu.add(item1);

        int id = vendedorController.crearVendedor(nombre, direccion, itemsMenu);

        Vendedor vendedorAntesDeEliminar = vendedorController.buscarPorIdVendedor(id);
        assertNotNull(vendedorAntesDeEliminar);

        vendedorController.eliminarVendedor(id);

        // Verificar que el vendedor ha sido eliminado
        Vendedor vendedorEliminado = vendedorController.buscarPorIdVendedor(id);
        assertNull(vendedorEliminado);
    }

    @Test
    void testBuscarVendedorPorId() throws SQLException {
        
        String nombre = "Carlos Gómez";
        String direccion = "Av. Ramírez 123";
        ArrayList<ItemMenu> itemsMenu = new ArrayList();
        Categoria categoria1 = new Categoria(1, "Pizza", TipoItem.COMIDA);
        Plato item1 = new Plato(10,"Pizza Margherita","Pizza clásica con tomate, mozzarella y albahaca.",8.99,categoria1,10,1,false,false,false);
        itemsMenu.add(item1);

        int id = vendedorController.crearVendedor(nombre, direccion, itemsMenu);

        Vendedor vendedorBuscado = vendedorController.buscarPorIdVendedor(id);

        // Verificar que el vendedor fue encontrado
        assertNotNull(vendedorBuscado);
        assertEquals(id, vendedorBuscado.getId());
        assertEquals(nombre, vendedorBuscado.getNombre());
        assertEquals(direccion, vendedorBuscado.getDireccion());
        
        vendedoresAEliminar.add(id);
    }

    @Test
    void testListarVendedoresPorNombre() throws SQLException {
        ArrayList<ItemMenu> itemsMenu = new ArrayList();
        Categoria categoria1 = new Categoria(1, "Pizza", TipoItem.COMIDA);
        Plato item1 = new Plato(10,"Pizza Margherita","Pizza clásica con tomate, mozzarella y albahaca.",8.99,categoria1,10,1,false,false,false);
        itemsMenu.add(item1);
        
        ArrayList<ItemMenu> itemsMenu2 = new ArrayList();
        Categoria categoria2 = new Categoria(1, "", TipoItem.BEBIDA);
        Gaseosa item2 = new Gaseosa(9,"Coca Cola","Coca Cola clásica",5.99,categoria2,10);
        itemsMenu2.add(item2);
        int id1 = vendedorController.crearVendedor("Marcelo López", "Av. Ramírez 123", itemsMenu);
        int id2 = vendedorController.crearVendedor("Ana Pérez", "Mendoza 1350", itemsMenu2);

        List<Vendedor> vendedores = vendedorController.listarVendedores("Marcelo López");

        // Verificar que los vendedores listados coinciden con el nombre
        assertNotNull(vendedores);
        assertEquals(1, vendedores.size());  // Debería haber solo un vendedor con el nombre "Marcelo López"
        assertEquals("Marcelo López", vendedores.get(0).getNombre());
        
        vendedoresAEliminar.add(id1);
        vendedoresAEliminar.add(id2);
    }
}


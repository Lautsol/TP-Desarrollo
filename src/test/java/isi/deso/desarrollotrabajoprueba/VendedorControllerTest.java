package isi.deso.desarrollotrabajoprueba;

import isi.deso.desarrollotrabajopractico.Categoria;
import isi.deso.desarrollotrabajopractico.Vendedor;
import isi.deso.desarrollotrabajopractico.Controladores.VendedorController;
import isi.deso.desarrollotrabajopractico.Gaseosa;
import isi.deso.desarrollotrabajopractico.ItemMenu;
import isi.deso.desarrollotrabajopractico.Plato;
import isi.deso.desarrollotrabajopractico.TipoItem;
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

    @BeforeAll
    static void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/grupo11", "root", "grupo11");
        vendedorController = new VendedorController();
    }

    @AfterAll
    static void tearDownAfterClass() throws SQLException {
        
        List<Integer> vendedoresAEliminar = Arrays.asList(40, 41, 42, 43, 44, 45);  

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
        int id = 40;
        String direccion = "Av. Siempre Viva 123";
        ArrayList<ItemMenu> itemsMenu = new ArrayList();
        Categoria categoria1 = new Categoria(1, "Pizza", TipoItem.COMIDA);
        Plato item1 = new Plato(10,"Pizza Margherita","Pizza clásica con tomate, mozzarella y albahaca.",8.99,categoria1,10,1,false,false,false);
        itemsMenu.add(item1);

        vendedorController.crearVendedor(nombre, id, direccion, itemsMenu);

        // Verificar si el vendedor fue creado correctamente
        Vendedor vendedorCreado = vendedorController.buscarPorIdVendedor(id);
        assertNotNull(vendedorCreado);
        assertEquals(id, vendedorCreado.getId());
        assertEquals(nombre, vendedorCreado.getNombre());
        assertEquals(direccion, vendedorCreado.getDireccion());

    }

    @Test
    void testActualizarVendedor() throws SQLException {
        
        String nombre = "Carlos Gómez";
        int id = 41;
        String direccion = "Av. Siempre Viva 123";
        ArrayList<ItemMenu> itemsMenu = new ArrayList();
        Categoria categoria1 = new Categoria(1, "Pizza", TipoItem.COMIDA);
        Plato item1 = new Plato(10,"Pizza Margherita","Pizza clásica con tomate, mozzarella y albahaca.",8.99,categoria1,10,1,false,false,false);
        itemsMenu.add(item1);

        vendedorController.crearVendedor(nombre, id, direccion, itemsMenu);

        String nuevoNombre = "Carlos Alberto Gómez";
        String nuevaDireccion = "Calle Ficticia 456";
        ArrayList<ItemMenu> nuevoitemMenu = new ArrayList();
        Categoria categoria2 = new Categoria(1, "", TipoItem.BEBIDA);
        Gaseosa item2 = new Gaseosa(9,"Coca Cola","Coca Cola clásica",5.99,categoria2,10);
        itemsMenu.add(item2);

        vendedorController.actualizarVendedor(nuevoNombre, id, nuevaDireccion, nuevoitemMenu);

        // Verificar que los cambios se han realizado correctamente
        Vendedor vendedorActualizado = vendedorController.buscarPorIdVendedor(id);
        assertNotNull(vendedorActualizado);
        assertEquals(nuevoNombre, vendedorActualizado.getNombre());
        assertEquals(nuevaDireccion, vendedorActualizado.getDireccion());
        assertEquals(id, vendedorActualizado.getId());
       
    }

    @Test
    void testEliminarVendedor() throws SQLException {
        
        String nombre = "Carlos Gómez";
        int id = 42;
        String direccion = "Av. Siempre Viva 123";
        ArrayList<ItemMenu> itemsMenu = new ArrayList();
        Categoria categoria1 = new Categoria(1, "Pizza", TipoItem.COMIDA);
        Plato item1 = new Plato(10,"Pizza Margherita","Pizza clásica con tomate, mozzarella y albahaca.",8.99,categoria1,10,1,false,false,false);
        itemsMenu.add(item1);

        vendedorController.crearVendedor(nombre, id, direccion, itemsMenu);

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
        int id = 43;
        String direccion = "Av. Siempre Viva 123";
        ArrayList<ItemMenu> itemsMenu = new ArrayList();
        Categoria categoria1 = new Categoria(1, "Pizza", TipoItem.COMIDA);
        Plato item1 = new Plato(10,"Pizza Margherita","Pizza clásica con tomate, mozzarella y albahaca.",8.99,categoria1,10,1,false,false,false);
        itemsMenu.add(item1);

         vendedorController.crearVendedor(nombre, id, direccion, itemsMenu);

        Vendedor vendedorBuscado = vendedorController.buscarPorIdVendedor(id);

        // Verificar que el vendedor fue encontrado
        assertNotNull(vendedorBuscado);
        assertEquals(id, vendedorBuscado.getId());
        assertEquals(nombre, vendedorBuscado.getNombre());
        assertEquals(direccion, vendedorBuscado.getDireccion());
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
        vendedorController.crearVendedor("Marcelo López", 44, "Av. Siempre Viva 123", itemsMenu);
        vendedorController.crearVendedor("Ana Pérez", 45, "Francia 1000", itemsMenu2);

        List<Vendedor> vendedores = vendedorController.listarVendedores("Marcelo López");

        // Verificar que los vendedores listados coinciden con el nombre
        assertNotNull(vendedores);
        assertEquals(1, vendedores.size());  // Debería haber solo un vendedor con el nombre "Marcelo López"
        assertEquals("Marcelo López", vendedores.get(0).getNombre());
    }
}


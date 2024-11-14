
package isi.deso.desarrollotrabajoprueba;

import isi.deso.desarrollotrabajopractico.Categoria;
import isi.deso.desarrollotrabajopractico.Gaseosa;
import isi.deso.desarrollotrabajopractico.Plato;
import isi.deso.desarrollotrabajopractico.TipoItem;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ItemMenuTest {

@Test
public void testEsPlatoYEsBebida() {   

Categoria categoria1 = new Categoria(1,"Hamburguesa",TipoItem.COMIDA);    
Categoria categoria2 = new Categoria(2,"Coca Cola",TipoItem.BEBIDA);   

Plato plato = new Plato(1, "Hamburguesa", "Con queso", 10.0, categoria1, 0.3, 2, false, false, false);       
Gaseosa bebida = new Gaseosa(2, "Coca Cola", "Bebida gaseosa", 5.0, categoria2, 0.5);
        
// Validamos que Comida retorna true en esComida y false en esBebida
        assertTrue(plato.esComida(), "Comida debería retornar true en esComida");
        assertFalse(plato.esBebida(), "Comida debería retornar false en esBebida");

// Validamos que Bebida retorna false en esComida y true en esBebida
        assertTrue(bebida.esComida(), "Bebida debería retornar false en esComida");
        assertFalse(bebida.esBebida(), "Bebida debería retornar true en esBebida");
    }

@Test
    public void testAptoVegano() {
    Categoria categoria1 = new Categoria(3,"Ensalada Vegana",TipoItem.COMIDA);    
    Categoria categoria2 = new Categoria(4,"Bife",TipoItem.COMIDA);
    Categoria categoria3 = new Categoria(5,"Agua",TipoItem.BEBIDA);  

    Plato comidaVegana = new Plato(3, "Ensalada Vegana", "Con lechuga y tomate", 8.0, categoria1, 0.2, 1, true, true, false);
    Plato comidaNoVegana = new Plato(4, "Bife", "Carne de res", 15.0, categoria2, 0.4, 3, false, false, false);
    Gaseosa bebida = new Gaseosa(5,"Agua", "Agua Mineral", 2.0, categoria3, 0.5);

// Verificamos que una comida vegana retorne true en aptoVegano y una no vegana retorne false
        assertTrue(comidaVegana.aptoVegano(),"Comida vegana debería ser apta para veganos");
        assertFalse(comidaNoVegana.aptoVegano(),"Comida no vegana no debería ser apta para veganos");

// Verificamos que una bebida sea apta para veganos
        assertTrue(bebida.aptoVegano(),"Bebida debería ser apta para veganos");
    }

@Test
public void testPeso() {
Categoria categoria1 = new Categoria(6,"Sandwich",TipoItem.COMIDA);
Categoria categoria2 = new Categoria(7,"Jugo",TipoItem.BEBIDA);  

Plato comida = new Plato(6, "Sándwich", "Sándwich de verduras", 5.0, categoria1, 0.25, 1, true, true, false);
Gaseosa bebida = new Gaseosa(7, "Jugo de Naranja", "Natural", 3.0, categoria2, 0.3);

// Verificamos el peso de Comida y Bebida
        assertEquals(0.25, comida.peso(), 0.01, "El peso de la comida debería ser 0.25");
        assertEquals(0.3, bebida.peso(), 0.01, "El peso de la bebida debería ser 0.3");
    }
}


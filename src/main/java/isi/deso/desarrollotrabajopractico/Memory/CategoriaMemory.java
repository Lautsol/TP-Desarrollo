
package isi.deso.desarrollotrabajopractico.Memory;

import isi.deso.desarrollotrabajopractico.modelo.Categoria;
import isi.deso.desarrollotrabajopractico.modelo.TipoItem;
import java.util.ArrayList;

public class CategoriaMemory {
    public static ArrayList<Categoria> listaCategorias = new ArrayList();
    static {
        listaCategorias.add(new Categoria(1, "Pizza", TipoItem.COMIDA));
        listaCategorias.add(new Categoria(2, "Hamburguesa", TipoItem.COMIDA));
        listaCategorias.add(new Categoria(3, "Refresco", TipoItem.BEBIDA));
        listaCategorias.add(new Categoria(4, "Agua", TipoItem.BEBIDA));
    }
}

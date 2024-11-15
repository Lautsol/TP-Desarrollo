
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.Categoria;
import java.util.ArrayList;

public interface CategoriaDAO {
    
    public Categoria buscarCategoria(int id);
    public ArrayList<Categoria> obtenerTodasLasCategorias();
}

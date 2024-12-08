
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.modelo.Categoria;
import java.util.ArrayList;

public interface CategoriaDAO {
    
    public Categoria buscarCategoria(int id);
    public ArrayList<Categoria> obtenerTodasLasCategorias();
}


package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.modelo.Vendedor;
import java.util.ArrayList;

public interface VendedorDAO {
    
    public int crearVendedor(Vendedor vendedor);
    public void actualizarVendedor(Vendedor vendedor);
    public void eliminarVendedor(int id);
    public ArrayList<Vendedor> buscarVendedoresPorNombre(String nombre);
    public Vendedor buscarVendedorPorID(int id);
    public ArrayList<Vendedor> obtenerTodosLosVendedores();
    
}

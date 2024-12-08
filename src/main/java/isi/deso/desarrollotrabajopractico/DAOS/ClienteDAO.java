
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.modelo.Cliente;
import java.util.ArrayList;

public interface ClienteDAO {
    
    public int crearCliente(Cliente cliente);
    public void actualizarCliente(Cliente cliente);
    public void eliminarCliente(int id);
    public ArrayList<Cliente> buscarClientesPorNombre(String nombre);
    public Cliente buscarClientePorID(int id);
    public ArrayList<Cliente> obtenerTodosLosClientes();
     
}

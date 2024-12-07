
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.Cliente;
import java.util.ArrayList;

public interface ClienteDAO {
    
    public void crearCliente(Cliente cliente);
    public void actualizarCliente(Cliente cliente);
    public void eliminarCliente(int id);
    public ArrayList<Cliente> buscarClientesPorNombre(String nombre);
    public Cliente buscarClientePorID(int id);
    public ArrayList<Cliente> obtenerTodosLosClientes();
     
}

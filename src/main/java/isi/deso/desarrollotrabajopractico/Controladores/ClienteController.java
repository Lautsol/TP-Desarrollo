
package isi.deso.desarrollotrabajopractico.Controladores;

import isi.deso.desarrollotrabajopractico.modelo.Cliente;
import isi.deso.desarrollotrabajopractico.DAOS.ClienteMySQLDAO;
import isi.deso.desarrollotrabajopractico.DAOS.FactoryDAO;
import isi.deso.desarrollotrabajopractico.Interfaces.CrearCliente;
import isi.deso.desarrollotrabajopractico.Interfaces.ListaDeClientes;
import isi.deso.desarrollotrabajopractico.Interfaces.ModificarCliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ClienteController implements ActionListener {

    private ListaDeClientes listaDeClientes;
    private CrearCliente interfazCrearCliente;
    private ModificarCliente interfazModificarCliente;

    public ClienteController() {
       
    }

    public ClienteController(ListaDeClientes listaDeClientes) {
        this.listaDeClientes = listaDeClientes;
    }

    public ClienteController(CrearCliente interfazCrearCliente) {
        this.interfazCrearCliente = interfazCrearCliente;
    }

    public ClienteController(ModificarCliente interfazModificarCliente) {
        this.interfazModificarCliente = interfazModificarCliente;
    }

    public void setCrearCliente(CrearCliente crearCliente) {
        this.interfazCrearCliente = crearCliente;
    }

    public void setModificarCliente(ModificarCliente modificarCliente) {
        this.interfazModificarCliente = modificarCliente;
    }

    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        Object source = e.getSource();

        if (comando.equals("Crear nuevo cliente")) {
            interfazCrearCliente = new CrearCliente();  
            interfazCrearCliente.setControlador(this);
            setCrearCliente(interfazCrearCliente); 
        } 
        
        else if (comando.equals("Editar")) {
           interfazModificarCliente = new ModificarCliente();  
           interfazModificarCliente.setControlador(this);
           setModificarCliente(interfazModificarCliente);  
           interfazModificarCliente.getjTextField4().setEditable(false);
           
           if (listaDeClientes.getjTable1().isEditing()) {
            listaDeClientes.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
           }
           
           int row = listaDeClientes.getjTable1().getSelectedRow(); // Obtener la fila seleccionada
               
           String nombre = (String) listaDeClientes.getModelo().getValueAt(row, 0);
           int id = (Integer) listaDeClientes.getModelo().getValueAt(row, 1);
           long cuit = (Long) listaDeClientes.getModelo().getValueAt(row, 2);
           String alias = (String) listaDeClientes.getModelo().getValueAt(row, 3);
           Object cbu;
           if(listaDeClientes.getModelo().getValueAt(row, 4) == null) cbu = "";
           else cbu = (Long) listaDeClientes.getModelo().getValueAt(row, 4);
           String email = (String) listaDeClientes.getModelo().getValueAt(row, 5);
           String direccion = (String) listaDeClientes.getModelo().getValueAt(row, 6);
                    
           interfazModificarCliente.getjTextField1().setText(nombre); 
           interfazModificarCliente.getjTextField2().setText(String.valueOf(cuit)); 
           interfazModificarCliente.getjTextField3().setText(email); 
           interfazModificarCliente.getjTextField4().setText(String.valueOf(id)); 
           interfazModificarCliente.getjTextField5().setText(direccion);
           interfazModificarCliente.getjTextField6().setText(String.valueOf(cbu));
           interfazModificarCliente.getjTextField7().setText(alias); 
        
    }
        
        else if (source == listaDeClientes.getjTextField1()) {
            String texto = listaDeClientes.getjTextField1().getText();

        // Intentar convertir el texto a un int
        try {
            int idCliente = Integer.parseInt(texto);
            
            // Llamar al método de búsqueda por ID
            Cliente cliente = buscarCliente(idCliente);
            if (cliente != null) {
                actualizarTablaConCliente(cliente);
            } 
            else {
                listaDeClientes.mostrarMensaje();
                restablecerTablaConDatosOriginales();
            }
            
        } catch (NumberFormatException ex) {
            
            // Si no se puede convertir a int, se asume que es un String
            String nombreCliente = texto;
            
            // Llamar al método de búsqueda por nombre
            List<Cliente> clientes = listarClientes(nombreCliente);
            
            if(!clientes.isEmpty()){
            actualizarTabla(clientes);
            }
            else{
                listaDeClientes.mostrarMensaje();
                restablecerTablaConDatosOriginales();
                }
            }
                
        }
        
        else if (comando.equals("Eliminar")) {
            
            if (listaDeClientes.getjTable1().isEditing()) {
            listaDeClientes.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
            }
            
            if(listaDeClientes.confirmarAccion()) {
                int row = listaDeClientes.getjTable1().getSelectedRow();

                int id = (Integer) listaDeClientes.getModelo().getValueAt(row, 1);
                eliminarCliente(id);
                listaDeClientes.getModelo().removeRow(row);
            }
       
      } 
        
        else if (comando.equals("Crear")) {
            
            if(validarCamposVacios(interfazCrearCliente)) interfazCrearCliente.mostrarMensajeCamposVacios();
            else if (!validarTiposDeDatos(interfazCrearCliente)) interfazCrearCliente.mostrarMensajeDatosInvalidos();
            else {
            String nombre = interfazCrearCliente.getjTextField1().getText();
            long cuit = Long.parseLong(interfazCrearCliente.getjTextField2().getText());
            String email = interfazCrearCliente.getjTextField3().getText();
            String direccion = interfazCrearCliente.getjTextField5().getText();
            Long cbu;
            if(interfazCrearCliente.getjTextField6().getText().trim().isEmpty()) cbu = null;
            else cbu = Long.valueOf(interfazCrearCliente.getjTextField6().getText());
            String alias;
            if(interfazCrearCliente.getjTextField7().getText().trim().isEmpty()) alias = null;
            else alias = interfazCrearCliente.getjTextField7().getText();
            
            ClienteMySQLDAO clienteMySQLDAO = (ClienteMySQLDAO) FactoryDAO.getClienteDAO();
            if(clienteMySQLDAO.buscarClientePorCuit(cuit) != null) interfazCrearCliente.mostrarMensajeCuit();
            else if(interfazCrearCliente.confirmarAccion()) {
                crearCliente(nombre, cuit, alias, cbu, email, direccion);
                interfazCrearCliente.dispose();
                restablecerTablaConDatosOriginales();
            }
          }
        } 

        else if (comando.equals("Guardar")) {
            
            if(validarCamposVaciosModificar(interfazModificarCliente)) interfazModificarCliente.mostrarMensajeCamposVacios();
            else if (!validarTiposDeDatosModificar(interfazModificarCliente)) interfazModificarCliente.mostrarMensajeDatosInvalidos();
            else if(interfazModificarCliente.confirmarAccion()) {
                String nombre = interfazModificarCliente.getjTextField1().getText();
                long cuit = Long.parseLong(interfazModificarCliente.getjTextField2().getText());
                String email = interfazModificarCliente.getjTextField3().getText();
                int id = Integer.parseInt(interfazModificarCliente.getjTextField4().getText());
                String direccion = interfazModificarCliente.getjTextField5().getText();
                Long cbu;
                if(interfazModificarCliente.getjTextField6().getText().trim().isEmpty()) cbu = null;
                else cbu = Long.valueOf(interfazModificarCliente.getjTextField6().getText());
                String alias;
                if(interfazModificarCliente.getjTextField7().getText().trim().isEmpty()) alias = null;
                else alias = interfazModificarCliente.getjTextField7().getText();
            
                actualizarCliente(nombre, id, cuit, alias, cbu, email, direccion);

                interfazModificarCliente.dispose();
                restablecerTablaConDatosOriginales();
            }
        }
        
        else if (comando.equals("CancelarCrear")) {
            interfazCrearCliente.dispose();  
        } 
        
        else if (comando.equals("CancelarModificar")) {
            interfazModificarCliente.dispose();  
        }
        
        else if(comando.equals("Reiniciar la búsqueda")) {
            listaDeClientes.getjTextField1().setText("Buscar por nombre o por ID");
            restablecerTablaConDatosOriginales();
        }
  }

    public int crearCliente(String nombre, long cuit, String alias, Long cbu, String email, String direccion) {
        
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setCuit(cuit);
        cliente.setAlias(alias);
        cliente.setCbu(cbu);
        cliente.setEmail(email);
        cliente.setDireccion(direccion);

        //ClienteMemory.listaClientes.add(cliente);
        return FactoryDAO.getClienteDAO().crearCliente(cliente);
    }

    public void actualizarCliente(String nombre, int id, long cuit, String alias, Long cbu, String email, String direccion){
        
        /*
        for (Cliente cliente : ClienteMemory.listaClientes) {
            if (cliente.getId() == id) {
                cliente.setNombre(nombre);
                cliente.setCuit(cuit);
                cliente.setAlias(alias);
                cliente.setCbu(cbu);
                cliente.setEmail(email);
                cliente.setDireccion(direccion);
            }
        }
        */
        
        Cliente cliente = FactoryDAO.getClienteDAO().buscarClientePorID(id);
        cliente.setNombre(nombre);
        cliente.setCuit(cuit);
        cliente.setAlias(alias);
        cliente.setCbu(cbu);
        cliente.setEmail(email);
        cliente.setDireccion(direccion);
        
        FactoryDAO.getClienteDAO().actualizarCliente(cliente);
        
            
    }

    public void eliminarCliente(int id) {
        
        /*Iterator<Cliente> iterador = ClienteMemory.listaClientes.iterator();
        while (iterador.hasNext()) {
            Cliente c = iterador.next();
            if (c.getId() == id) {
                iterador.remove();
            }
        }*/
        
        FactoryDAO.getClienteDAO().eliminarCliente(id);
        
    }

    public Cliente buscarCliente(int id) {
        
        /*
        for (Cliente cliente : ClienteMemory.listaClientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;  
    }
        */
        
        return FactoryDAO.getClienteDAO().buscarClientePorID(id);
    }

    public ArrayList<Cliente> listarClientes(String nombre) {
        
        /*
        ArrayList<Cliente> clientesFiltrados = new ArrayList<>();
        for (Cliente cliente : ClienteMemory.listaClientes) {
            if (cliente.getNombre().equals(nombre)) {
                clientesFiltrados.add(cliente);
            }
        }
        return clientesFiltrados;
    }
        */
        
        return FactoryDAO.getClienteDAO().buscarClientesPorNombre(nombre);
    }

    private void actualizarTablaConCliente(Cliente cliente) {
        // Limpia la tabla actual
        listaDeClientes.getModelo().setRowCount(0); 

        // Agregar el cliente a la tabla
        Object[] rowData = {
            cliente.getNombre(),
            cliente.getId(),
            cliente.getCuit(),
            cliente.getAlias(),
            cliente.getCbu(),
            cliente.getEmail(),
            cliente.getDireccion()
        };
        listaDeClientes.getModelo().addRow(rowData);
    }

    private void actualizarTabla(List<Cliente> clientes) {
        // Limpia la tabla actual
        listaDeClientes.getModelo().setRowCount(0); 

        // Agregar los clientes a la tabla
        for (Cliente cliente : clientes) {
            Object[] rowData = {
                cliente.getNombre(),
                cliente.getId(),
                cliente.getCuit(),
                cliente.getAlias(),
                cliente.getCbu(),
                cliente.getEmail(),
                cliente.getDireccion()
            };
            listaDeClientes.getModelo().addRow(rowData);
        }
    }

    // Método para cargar los datos originales en la tabla
    public void cargarDatosOriginalesEnTabla() {
        
        /*
        for (Cliente cliente : ClienteMemory.listaClientes) {
                Object[] rowData = {
                cliente.getNombre(),
                cliente.getId(),
                cliente.getCuit(),
                cliente.getAlias(),
                cliente.getCbu(),
                cliente.getEmail(),
                cliente.getDireccion()
            };
        
            listaDeClientes.getModelo().addRow(rowData);
        }
    }
        */
        
        ClienteMySQLDAO clienteMySQLDAO = (ClienteMySQLDAO) FactoryDAO.getClienteDAO();
        for (Cliente cliente : clienteMySQLDAO.obtenerTodosLosClientes()) {
                Object[] rowData = {
                cliente.getNombre(),
                cliente.getId(),
                cliente.getCuit(),
                cliente.getAlias(),
                cliente.getCbu(),
                cliente.getEmail(),
                cliente.getDireccion()
            };
        
            listaDeClientes.getModelo().addRow(rowData);
        }
    }

    private void restablecerTablaConDatosOriginales() {
        listaDeClientes.getModelo().setRowCount(0); // Limpiar la tabla actual
        cargarDatosOriginalesEnTabla(); // Volver a cargar los datos originales
    }
    
    private boolean validarCamposVacios(CrearCliente interfaz) {
        
        boolean vacio = false;
        
        if(interfaz.getjTextField1().getText().trim().isEmpty() || 
           interfaz.getjTextField2().getText().trim().isEmpty() ||
           interfaz.getjTextField3().getText().trim().isEmpty() ||
           interfaz.getjTextField5().getText().trim().isEmpty() ||
           (interfaz.getjTextField6().getText().trim().isEmpty() &&
           interfaz.getjTextField7().getText().trim().isEmpty())) vacio = true;
        
        return vacio;
    }
    
    private boolean validarTiposDeDatos(CrearCliente interfaz) {
        
        boolean correcto = true;
        
        if(!interfaz.getjTextField1().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+") || 
           !interfaz.getjTextField2().getText().matches("\\d+") ||
           !interfaz.getjTextField3().getText().matches("^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)+$") ||
           !interfaz.getjTextField5().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s]+") ||
           (!interfaz.getjTextField6().getText().trim().isEmpty() && !interfaz.getjTextField6().getText().matches("\\d+"))) correcto = false;
        
        return correcto;
    }
    
    private boolean validarCamposVaciosModificar(ModificarCliente interfaz) {
        
        boolean vacio = false;
        
        if(interfaz.getjTextField1().getText().trim().isEmpty() || 
           interfaz.getjTextField2().getText().trim().isEmpty() ||
           interfaz.getjTextField3().getText().trim().isEmpty() ||
           interfaz.getjTextField5().getText().trim().isEmpty() ||
           (interfaz.getjTextField6().getText().trim().isEmpty() &&
           interfaz.getjTextField7().getText().trim().isEmpty())) vacio = true;
        
        return vacio;
        
    }
    
    private boolean validarTiposDeDatosModificar(ModificarCliente interfaz) {
        
        boolean correcto = true;
        
        
        if(!interfaz.getjTextField1().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+") || 
           !interfaz.getjTextField2().getText().matches("\\d+") ||
           !interfaz.getjTextField3().getText().matches("^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)+$") ||
           !interfaz.getjTextField5().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s]+") ||
          (!interfaz.getjTextField6().getText().trim().isEmpty() && !interfaz.getjTextField6().getText().matches("\\d+"))) correcto = false;
        
        return correcto;
    }
    
    public boolean verificarID(int id_cliente) {
        
        boolean existe = false;
        /*
        for (Cliente cliente : ClienteMemory.listaClientes) {
            if(cliente.getId() == id_cliente) existe = true; 
        }
        */
        
        ClienteMySQLDAO clienteMySQLDAO = (ClienteMySQLDAO) FactoryDAO.getClienteDAO();
        existe = clienteMySQLDAO.buscarClientePorID(id_cliente) != null;
        return existe;
    }
    
 }
 
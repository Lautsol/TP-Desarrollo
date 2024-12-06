
package isi.deso.desarrollotrabajopractico.Controladores;

import isi.deso.desarrollotrabajopractico.DAOS.FactoryDAO;
import isi.deso.desarrollotrabajopractico.DAOS.ItemMenuMySQLDAO;
import isi.deso.desarrollotrabajopractico.DAOS.ItemMenuVendedorMySQLDAO;
import isi.deso.desarrollotrabajopractico.DAOS.VendedorMySQLDAO;
import isi.deso.desarrollotrabajopractico.Interfaces.CrearVendedor;
import isi.deso.desarrollotrabajopractico.Interfaces.ItemsMenuPedido;
import isi.deso.desarrollotrabajopractico.Interfaces.ListaDeVendedores;
import isi.deso.desarrollotrabajopractico.Interfaces.ModificarVendedor;
import isi.deso.desarrollotrabajopractico.ItemMenu;
import isi.deso.desarrollotrabajopractico.ProductoDeOtroVendedorException;
import isi.deso.desarrollotrabajopractico.Vendedor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

public class VendedorController implements ActionListener, WindowListener {
    private ListaDeVendedores listaDeVendedores;
    private CrearVendedor interfazCrearVendedor;
    private ModificarVendedor interfazModificarVendedor;
    private ItemsMenuPedido interfazItemsMenuPedido;
    private ArrayList<ItemMenu> itemsMenu;
    
    public VendedorController(){}
    
    public VendedorController(ListaDeVendedores listaDeVendedores) {
        this.listaDeVendedores = listaDeVendedores;
    }

    public VendedorController(CrearVendedor interfazCrearVendedor) {
        this.interfazCrearVendedor = interfazCrearVendedor;
    }

    public VendedorController(ModificarVendedor interfazModificarVendedor) {
        this.interfazModificarVendedor = interfazModificarVendedor;
    }
    
    public void setCrearVendedor(CrearVendedor crearVendedor) {
        this.interfazCrearVendedor = crearVendedor;
    }
    
    public void setModificarVendedor(ModificarVendedor modificarVendedor) {
        this.interfazModificarVendedor = modificarVendedor;
    }
    
    public void setItemsMenuPedido(ItemsMenuPedido interfazItemsMenuPedido) {
        this.interfazItemsMenuPedido = interfazItemsMenuPedido;
    }
     
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        Object source = e.getSource();

        if (comando.equals("Crear nuevo vendedor")) {
            
            itemsMenu = new ArrayList<>();
            interfazCrearVendedor = new CrearVendedor();  
            interfazCrearVendedor.setControlador(this);
            setCrearVendedor(interfazCrearVendedor); 
            int idVendedor = obtenerID();
            interfazCrearVendedor.getjTextField4().setText(String.valueOf(idVendedor));
            interfazCrearVendedor.getjTextField4().setEditable(false);
            
        } 
    
        else if(comando.equals("Editar")) {
            
           interfazModificarVendedor = new ModificarVendedor();  
           interfazModificarVendedor.setControlador(this);
           setModificarVendedor(interfazModificarVendedor); 
           interfazModificarVendedor.getjTextField4().setEditable(false);
           
           if (listaDeVendedores.getjTable1().isEditing()) {
            listaDeVendedores.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
           }
           
           int row = listaDeVendedores.getjTable1().getSelectedRow(); // Obtener la fila seleccionada
           
           String nombre = (String) listaDeVendedores.getModelo().getValueAt(row, 0);
           int id = (Integer) listaDeVendedores.getModelo().getValueAt(row, 1);
           String direccion = (String) listaDeVendedores.getModelo().getValueAt(row, 2);
                    
           interfazModificarVendedor.getjTextField1().setText(nombre); 
           interfazModificarVendedor.getjTextField4().setText(String.valueOf(id)); 
           interfazModificarVendedor.getjTextField5().setText(direccion);   
        }
     
        else if (source == listaDeVendedores.getjTextField1()) {
            String texto = listaDeVendedores.getjTextField1().getText();

        // Intentar convertir el texto a un int
        try {
            int idVendedor = Integer.parseInt(texto);
            
            // Llamar al método de búsqueda por ID
            Vendedor vendedor = buscarPorIdVendedor(idVendedor);
            if (vendedor != null) {
                actualizarTablaConVendedor(vendedor);
            } 
            else {
                listaDeVendedores.mostrarMensaje();
                restablecerTablaConDatosOriginales();
            }
            
        } catch (NumberFormatException ex) {
            
            // Si no se puede convertir a int, se asume que es un String
            String nombreVendedor = texto;
            
            // Llamar al método de búsqueda por nombre
            List<Vendedor> vendedores = listarVendedores(nombreVendedor);
            
            if(!vendedores.isEmpty()){
            actualizarTabla(vendedores);
            }
            else{
                listaDeVendedores.mostrarMensaje();
                restablecerTablaConDatosOriginales();
                }   
        }
      }
     
        else if(comando.equals("Eliminar")) {
            
            if (listaDeVendedores.getjTable1().isEditing()) {
            listaDeVendedores.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
            }
            
            int row = listaDeVendedores.getjTable1().getSelectedRow();
            
            int id = (Integer) listaDeVendedores.getModelo().getValueAt(row, 1);
            eliminarVendedor(id);
            listaDeVendedores.getModelo().removeRow(row);
          
        } 
     
        else if (comando.equals("Crear")) {
            
            if(validarCamposVacios(interfazCrearVendedor)) interfazCrearVendedor.mostrarMensajeCamposVacios();
            else if (!validarTiposDeDatos(interfazCrearVendedor)) interfazCrearVendedor.mostrarMensajeDatosInvalidos();
            else {
            String nombre = interfazCrearVendedor.getjTextField1().getText();
            int id = Integer.parseInt(interfazCrearVendedor.getjTextField4().getText());
            String direccion = interfazCrearVendedor.getjTextField5().getText();
            
            if(itemsMenu == null || itemsMenu.isEmpty()) interfazCrearVendedor.mostrarMensajeItem();
            else{
            crearVendedor(nombre, id, direccion, itemsMenu);
            listaDeVendedores.agregarVendedorALaTabla(nombre, id, direccion);
            itemsMenu = new ArrayList<>();
            interfazCrearVendedor.dispose();
            }
          }
        } 
        
        else if (comando.equals("Agregar items")) {
            itemsMenu = new ArrayList<>();
            interfazItemsMenuPedido = new ItemsMenuPedido(this);
            interfazItemsMenuPedido.ocultarColumna();
            setItemsMenuPedido(interfazItemsMenuPedido);
            cargarDatosOriginalesEnTablaItems();
        }
        
        else if (comando.equals("Agregar")) {
            
            if (interfazItemsMenuPedido.getjTable1().isEditing()) {
            interfazItemsMenuPedido.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
            }
           
            int row = interfazItemsMenuPedido.getjTable1().getSelectedRow(); // Obtener la fila seleccionada
           
            int id = (Integer) interfazItemsMenuPedido.getModelo().getValueAt(row, 0);
            ItemMenu item = (new ItemMenuController()).buscarItemMenu(id);
            
            boolean agregado = false;
            
            if(!itemsMenu.isEmpty()) {
                for (ItemMenu itemMenu : itemsMenu) {
                    if (itemMenu.getId() == item.getId()) {
                        agregado = true;
                        break; 
                    }
                }
            }
            
            if(interfazModificarVendedor != null) {
                int idVendedor = Integer.parseInt(interfazModificarVendedor.getjTextField4().getText());
                if(yaTieneItem(idVendedor, id)) agregado = true;
            }
            
            if(!agregado) {
                itemsMenu.add(item);
                interfazItemsMenuPedido.mostrarMensajeExitoso();
            }
            else interfazItemsMenuPedido.mostrarMensajeError("El vendedor ya tiene ese item.");
            
        }

        else if (comando.equals("Guardar")) {
            
            if(validarCamposVaciosModificar(interfazModificarVendedor)) interfazModificarVendedor.mostrarMensajeCamposVacios();
            else if (!validarTiposDeDatosModificar(interfazModificarVendedor)) interfazModificarVendedor.mostrarMensajeDatosInvalidos();
            else {
            int row = listaDeVendedores.getjTable1().getSelectedRow(); // Obtener la fila seleccionada
            
            String nombre = interfazModificarVendedor.getjTextField1().getText();
            int id = Integer.parseInt(interfazModificarVendedor.getjTextField4().getText());
            String direccion = interfazModificarVendedor.getjTextField5().getText();
            
            actualizarVendedor(nombre, id, direccion, itemsMenu);
            
            listaDeVendedores.getModelo().setValueAt(nombre, row, 0); 
            listaDeVendedores.getModelo().setValueAt(id, row, 1);         
            listaDeVendedores.getModelo().setValueAt(direccion, row, 2); 
            itemsMenu = new ArrayList<>();
            interfazModificarVendedor.dispose();
            interfazModificarVendedor = null;
            }
        } 
        
        else if (comando.equals("CancelarCrear")) {
            interfazCrearVendedor.dispose();  

        } 
        
        else if (comando.equals("CancelarModificar")) {
            interfazModificarVendedor.dispose();  
        }
        
        else if(comando.equals("Reiniciar la búsqueda")) {
            restablecerTablaConDatosOriginales();
            listaDeVendedores.getjTextField1().setText("Buscar por nombre o por ID");
        }
    }

    private void restablecerTablaConDatosOriginales() {
      listaDeVendedores.getModelo().setRowCount(0); // Limpiar la tabla actual
      cargarDatosOriginalesEnTabla(); // Volver a cargar los datos originales
    }
      
    public void cargarDatosOriginalesEnTabla() {
      /*
        for (Vendedor vendedor : VendedorMemory.listaVendedores) {
              Object[] rowData = {
              vendedor.getNombre(),
              vendedor.getId(),
              vendedor.getDireccion()
          };
        
          listaDeVendedores.getModelo().addRow(rowData);
      }
      */
      
    VendedorMySQLDAO vendedorMySQLDAO = (VendedorMySQLDAO) FactoryDAO.getVendedorDAO();
        for (Vendedor vendedor : vendedorMySQLDAO.obtenerTodosLosVendedores()) {
              Object[] rowData = {
              vendedor.getNombre(),
              vendedor.getId(),
              vendedor.getDireccion()
          };
        
          listaDeVendedores.getModelo().addRow(rowData);
      }
      
    }
    
    private void actualizarTablaConVendedor(Vendedor vendedor) {
        // Limpia la tabla actual
        listaDeVendedores.getModelo().setRowCount(0); 

        // Agregar el vendedor a la tabla
        Object[] rowData = {
            vendedor.getNombre(),
            vendedor.getId(),
            vendedor.getDireccion()
        };
        listaDeVendedores.getModelo().addRow(rowData);
    }
    
    public void crearVendedor(String nombre, int id, String direccion, ArrayList<ItemMenu> itemsMenu) {
        
        Vendedor vendedor = new Vendedor();
        vendedor.setNombre(nombre);
        vendedor.setId(id);
        vendedor.setDireccion(direccion);
        vendedor.setItems(itemsMenu);

        // VendedorMemory.listaVendedores.add(vendedor);  
        FactoryDAO.getVendedorDAO().crearVendedor(vendedor);
        FactoryDAO.getItemMenuVendedorDAO().agregarItemsVendedor(vendedor);
    }
    
    public void eliminarVendedor(int id) {
        
        /*
        Iterator<Vendedor> iterador = VendedorMemory.listaVendedores.iterator();
        while (iterador.hasNext()) {
            Vendedor c = iterador.next();
            if (c.getId() == id) {
                iterador.remove();
            }
        }
        */
        
        FactoryDAO.getVendedorDAO().eliminarVendedor(id);
    }
     
    public Vendedor buscarPorIdVendedor(int id){
       
        /*
        for (Vendedor vendedor : VendedorMemory.listaVendedores) {
            if (vendedor.getId() == id) {
                return vendedor;
            }
        }
        return null;  
        */
        
        return FactoryDAO.getVendedorDAO().buscarVendedorPorID(id);
    }
     
    public ArrayList<Vendedor> listarVendedores(String nombre) {
        
        /*
        ArrayList<Vendedor> vendedorFiltrados = new ArrayList<>();
        for (Vendedor vendedor : VendedorMemory.listaVendedores) {
            if (vendedor.getNombre().equals(nombre)) {
                vendedorFiltrados.add(vendedor);
            }
        }
        return vendedorFiltrados;
        */
        
        return FactoryDAO.getVendedorDAO().buscarVendedoresPorNombre(nombre);
    }
    
    private void actualizarTabla(List<Vendedor> vendedores) {
        // Limpia la tabla actual
        listaDeVendedores.getModelo().setRowCount(0); 

        // Agregar los vendedores a la tabla
        for (Vendedor vendedor : vendedores) {
            Object[] rowData = {
                vendedor.getNombre(),
                vendedor.getId(),
                vendedor.getDireccion()
            };
            listaDeVendedores.getModelo().addRow(rowData);
        }
    }
    
    public void actualizarVendedor(String nombre, int id, String direccion, ArrayList<ItemMenu> itemMenu) {
        
        /*
        for (Vendedor vendedor : VendedorMemory.listaVendedores) {
            if (vendedor.getId() == id) {
                vendedor.setNombre(nombre);
                vendedor.setDireccion(direccion);
            }
        }
        */
        
        Vendedor vendedor = FactoryDAO.getVendedorDAO().buscarVendedorPorID(id);
        vendedor.setNombre(nombre);
        vendedor.setDireccion(direccion);
        ArrayList<ItemMenu> items = FactoryDAO.getItemMenuVendedorDAO().obtenerItemsMenuDeVendedor(vendedor);
        vendedor.setItems(items);
        vendedor.agregarItems(itemMenu);
        
        FactoryDAO.getVendedorDAO().actualizarVendedor(vendedor);
        FactoryDAO.getItemMenuVendedorDAO().agregarItemsVendedor(vendedor);
        
    }
    
    private boolean validarCamposVacios(CrearVendedor interfaz) {
        
        boolean vacio = false;
        
        if(interfaz.getjTextField4().getText().trim().isEmpty() || 
           interfaz.getjTextField1().getText().trim().isEmpty() ||
           interfaz.getjTextField5().getText().trim().isEmpty()) vacio = true;
        
        return vacio;
    }
    
    private boolean validarTiposDeDatos(CrearVendedor interfaz) {
        
        boolean correcto = true;
        
        if(!interfaz.getjTextField4().getText().matches("\\d+") || 
           !interfaz.getjTextField1().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+") ||
           !interfaz.getjTextField5().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s]+")) correcto = false;
        
        return correcto;
    }
    
    private boolean validarCamposVaciosModificar(ModificarVendedor interfaz) {
        
        boolean vacio = false;
        
        if(interfaz.getjTextField1().getText().trim().isEmpty() ||
           interfaz.getjTextField5().getText().trim().isEmpty()) vacio = true;
        
        return vacio;
    }
    
    private boolean validarTiposDeDatosModificar(ModificarVendedor interfaz) {
        
        boolean correcto = true;
        
        if(!interfaz.getjTextField1().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+") ||
           !interfaz.getjTextField5().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s]+")) correcto = false;
        
        return correcto;
    }
    
    public boolean verificarID(int id_vendedor) {
        
        boolean existe = false;
        /*
        for (Vendedor vendedor : VendedorMemory.listaVendedores) {
            if(vendedor.getId() == id_vendedor) existe = true; 
        }
        
        return existe;
       */
        
        VendedorMySQLDAO vendedorMySQLDAO = (VendedorMySQLDAO) FactoryDAO.getVendedorDAO();
        existe = vendedorMySQLDAO.buscarVendedorPorID(id_vendedor) != null;
        return existe;
    }
    
    public void cargarDatosOriginalesEnTablaItems() {
        
        /*
        for (ItemMenu itemMenu : ItemMenuMemory.listaItemMenu) {
                Object[] rowData = {
                itemMenu.getId(),
                itemMenu.getCategoria().getTipo_item().toString(),
                itemMenu.getNombre(),
                itemMenu.getDescripcion(),
                itemMenu.getPrecio(),
                itemMenu.getCategoria().getId()
            };
        
            listaDeItemMenu.getModelo().addRow(rowData);
        }
        */
        
        ItemMenuMySQLDAO itemMenuMySQLDAO = (ItemMenuMySQLDAO) FactoryDAO.getItemMenuDAO();
        for (ItemMenu itemMenu : itemMenuMySQLDAO.obtenerTodosLosItemsMenu()) {
                Object[] rowData = {
                itemMenu.getId(),
                itemMenu.getCategoria().getTipo_item().toString(),
                itemMenu.getNombre(),
                itemMenu.getDescripcion(),
                itemMenu.getPrecio(),
                itemMenu.getCategoria().getId()
            };
        
            interfazItemsMenuPedido.getModelo().addRow(rowData);
        }
    }
    
    private int obtenerID() {
        VendedorMySQLDAO vendedorMySQLDAO = (VendedorMySQLDAO) FactoryDAO.getVendedorDAO();
        return vendedorMySQLDAO.obtenerID();
    }
    
    private boolean yaTieneItem(int id_item, int id_vendedor) {
        ItemMenuVendedorMySQLDAO itemMenuVendedorMySQLDAO = (ItemMenuVendedorMySQLDAO) FactoryDAO.getItemMenuVendedorDAO();
        return itemMenuVendedorMySQLDAO.buscarItemMenuPorIDyVendedor(id_item, id_vendedor) != null;
    }

    public void verificarProducto(int idVendedor, int idProducto) throws ProductoDeOtroVendedorException {
        ItemMenuVendedorMySQLDAO itemsMenuVendedorMySQLDAO = (ItemMenuVendedorMySQLDAO) FactoryDAO.getItemMenuVendedorDAO();
        if(!itemsMenuVendedorMySQLDAO.itemMenuDeVendedor(idVendedor, idProducto)) throw new ProductoDeOtroVendedorException();
    }

    public void windowOpened(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void windowClosing(WindowEvent e) {
            itemsMenu = new ArrayList<>();
            interfazModificarVendedor.dispose();
            interfazModificarVendedor = null;
    }

    public void windowClosed(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void windowIconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void windowDeiconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void windowActivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void windowDeactivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}






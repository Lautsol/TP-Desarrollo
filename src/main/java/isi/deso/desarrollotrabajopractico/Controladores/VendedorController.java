
package isi.deso.desarrollotrabajopractico.Controladores;

import isi.deso.desarrollotrabajopractico.DAOS.FactoryDAO;
import isi.deso.desarrollotrabajopractico.DAOS.VendedorMySQLDAO;
import isi.deso.desarrollotrabajopractico.Interfaces.CrearVendedor;
import isi.deso.desarrollotrabajopractico.Interfaces.ItemsMenuPedido;
import isi.deso.desarrollotrabajopractico.Interfaces.ListaDeVendedores;
import isi.deso.desarrollotrabajopractico.Interfaces.ModificarVendedor;
import isi.deso.desarrollotrabajopractico.Interfaces.VerItems;
import isi.deso.desarrollotrabajopractico.modelo.ItemMenu;
import isi.deso.desarrollotrabajopractico.modelo.Vendedor;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class VendedorController implements ActionListener, WindowListener {
    private ListaDeVendedores listaDeVendedores;
    private CrearVendedor interfazCrearVendedor;
    private ModificarVendedor interfazModificarVendedor;
    private ItemsMenuPedido interfazItemsMenuPedido;
    private ArrayList<ItemMenu> itemsMenu;
    private ArrayList<ItemMenu> itemsMenuEliminar;
    private VerItems interfazItemsVendedor;
    
    public VendedorController(){
    
    }
    
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
    
    public void setVerItemsVendedor(VerItems interfazItemsVendedor) {
        this.interfazItemsVendedor = interfazItemsVendedor;
    }
     
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        Object source = e.getSource();

        if(comando.equals("Crear nuevo vendedor")) {
            
            interfazModificarVendedor = null; 
            itemsMenu = new ArrayList<>();
            interfazCrearVendedor = new CrearVendedor();  
            interfazCrearVendedor.setControlador(this);
            setCrearVendedor(interfazCrearVendedor); 
        } 
    
        else if(comando.equals("Editar")) {
            
           itemsMenu = new ArrayList<>();
           itemsMenuEliminar = new ArrayList<>();
           interfazModificarVendedor = new ModificarVendedor();  
           interfazModificarVendedor.setControlador(this);
           setModificarVendedor(interfazModificarVendedor); 
           interfazModificarVendedor.getjTextField4().setEditable(false);
           
           if(listaDeVendedores.getjTable1().isEditing()) {
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
     
        else if(source == listaDeVendedores.getjTextField1()) {
            
            String texto = listaDeVendedores.getjTextField1().getText();

        // Intentar convertir el texto a un int
        try {
            int idVendedor = Integer.parseInt(texto);
            
            // Llamar al método de búsqueda por ID
            Vendedor vendedor = buscarPorIdVendedor(idVendedor);
            if(vendedor != null) {
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
            
            if(listaDeVendedores.getjTable1().isEditing()) {
            listaDeVendedores.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
            }
            
            if(listaDeVendedores.confirmarAccion()) {
                int row = listaDeVendedores.getjTable1().getSelectedRow();

                int id = (Integer) listaDeVendedores.getModelo().getValueAt(row, 1);
                eliminarVendedor(id);
                listaDeVendedores.getModelo().removeRow(row);
            }
          
        } 
     
        else if(comando.equals("Crear")) {
            
            if(validarCamposVacios(interfazCrearVendedor)) interfazCrearVendedor.mostrarMensajeCamposVacios();
            else if(!validarTiposDeDatos(interfazCrearVendedor)) interfazCrearVendedor.mostrarMensajeDatosInvalidos();
            else {
            String nombre = interfazCrearVendedor.getjTextField1().getText();
            String direccion = interfazCrearVendedor.getjTextField5().getText();
            
            if(itemsMenu == null || itemsMenu.isEmpty()) interfazCrearVendedor.mostrarMensajeItem();
            else if(interfazCrearVendedor.confirmarAccion()) {
                crearVendedor(nombre, direccion, itemsMenu);
                restablecerTablaConDatosOriginales();
                itemsMenu = new ArrayList<>();
                interfazCrearVendedor.dispose();
            }
          }
        } 
        
        else if(comando.equals("Agregar items")) {
            
            interfazItemsMenuPedido = new ItemsMenuPedido(this);
            interfazItemsMenuPedido.ocultarColumna();
            setItemsMenuPedido(interfazItemsMenuPedido);
            ArrayList<ItemMenu> listaItems = FactoryDAO.getItemMenuDAO().obtenerTodosLosItemsMenu();
            
            if(interfazModificarVendedor != null) {
                Vendedor vendedor = new Vendedor();
                int id = Integer.parseInt(interfazModificarVendedor.getjTextField4().getText());
                vendedor.setId(id);
                ArrayList<ItemMenu> listaItemsVendedor = FactoryDAO.getItemMenuVendedorDAO().obtenerItemsMenuDeVendedor(vendedor);
                 
                Iterator<ItemMenu> iterator = listaItems.iterator();
    
                while(iterator.hasNext()) {
                    ItemMenu item = iterator.next();

                    // Verificar si el ítem ya está en la lista de items del vendedor
                    boolean itemYaAsociado = false;
                    for(ItemMenu itemVendedor : listaItemsVendedor) {
                        if(item.getId() == itemVendedor.getId()) {
                            itemYaAsociado = true;
                            break;
                        }
                    }

                    if(itemYaAsociado) {
                        iterator.remove();
                    }
                }
            }
            
            cargarDatosOriginalesEnTablaItems(listaItems, interfazItemsMenuPedido);
        }
        
        else if(comando.equals("Agregar")) {
            
            if(interfazItemsMenuPedido.getjTable1().isEditing()) {
            interfazItemsMenuPedido.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
            }
           
            int row = interfazItemsMenuPedido.getjTable1().getSelectedRow(); // Obtener la fila seleccionada
           
            int id = (Integer) interfazItemsMenuPedido.getModelo().getValueAt(row, 0);
            ItemMenu item = (new ItemMenuController()).buscarItemMenu(id);
            
            boolean agregado = false;
            
            if(!itemsMenu.isEmpty()) {
                for(ItemMenu itemMenu : itemsMenu) {
                    if(itemMenu.getId() == item.getId()) {
                        agregado = true;
                        break; 
                    }
                }
            }
            
            if(!agregado) {
                itemsMenu.add(item);
                interfazItemsMenuPedido.mostrarMensajeExitoso();
            }
            else interfazItemsMenuPedido.mostrarMensajeError("El item ya fue agregado.");
            
        }
        
        else if(comando.equals("Ver items del vendedor")) {
            
            interfazItemsVendedor = new VerItems(); 
            interfazItemsVendedor.setControladorVendedor(this);
            setVerItemsVendedor(interfazItemsVendedor); 
            interfazItemsVendedor.ocultarColumnas();
            interfazItemsVendedor.getjLabel1().setText("Items del vendedor");
            
            if(interfazModificarVendedor != null) {
                Vendedor vendedor = new Vendedor();
                int id = Integer.parseInt(interfazModificarVendedor.getjTextField4().getText());
                vendedor.setId(id);
                ArrayList<ItemMenu> listaItemsVendedor = FactoryDAO.getItemMenuVendedorDAO().obtenerItemsMenuDeVendedor(vendedor);

                Iterator<ItemMenu> iterator = listaItemsVendedor.iterator();
                while(iterator.hasNext()) {
                    ItemMenu item = iterator.next();
                    for(ItemMenu itemEliminar : itemsMenuEliminar) {
                        if(itemEliminar.getId() == item.getId()) {
                            iterator.remove();
                            break;
                        }
                    }
                }

                listaItemsVendedor.addAll(itemsMenu);
                cargarDatosOriginalesEnTablaItems(listaItemsVendedor, interfazItemsVendedor);
            }
            
            else cargarDatosOriginalesEnTablaItems(itemsMenu, interfazItemsVendedor);
        }
        
        else if(comando.equals("EliminarItem")) {
            
            if(interfazItemsVendedor.getjTable1().isEditing()) {
            interfazItemsVendedor.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
            }
            
            if(interfazItemsVendedor.confirmarAccion("¿Está seguro de que desea eliminar el item menú del vendedor?")) {
                int row = interfazItemsVendedor.getjTable1().getSelectedRow();

                int id = (Integer) interfazItemsVendedor.getModelo().getValueAt(row, 0);
                ItemMenu item = (new ItemMenuController()).buscarItemMenu(id);
                
                Iterator<ItemMenu> iterator = itemsMenu.iterator();
                    
                    while(iterator.hasNext()) {
                        ItemMenu itemMenu = iterator.next();
                        if(itemMenu.getId() == item.getId()) {
                            iterator.remove();
                            break;
                        }
                    }
                    
                if(interfazModificarVendedor != null) itemsMenuEliminar.add(item);
                
                interfazItemsVendedor.getModelo().removeRow(row);
            }
        }

        else if(comando.equals("Guardar")) {
            
            if(validarCamposVaciosModificar(interfazModificarVendedor)) interfazModificarVendedor.mostrarMensajeCamposVacios();
            else if(!validarTiposDeDatosModificar(interfazModificarVendedor)) interfazModificarVendedor.mostrarMensajeDatosInvalidos();
            int id = Integer.parseInt(interfazModificarVendedor.getjTextField4().getText());
            Vendedor vendedor = new Vendedor();
            vendedor.setId(id);
            if(itemsMenuEliminar.size() == FactoryDAO.getItemMenuVendedorDAO().obtenerItemsMenuDeVendedor(vendedor).size() && itemsMenu.isEmpty()) interfazModificarVendedor.mostrarMensajeItem();
            else if(interfazModificarVendedor.confirmarAccion()) {
                String nombre = interfazModificarVendedor.getjTextField1().getText();
                String direccion = interfazModificarVendedor.getjTextField5().getText();

                vendedor.setNombre(nombre);
                vendedor.setDireccion(direccion);
                actualizarVendedor(vendedor, itemsMenu);
                vendedor.setId(id);
                FactoryDAO.getItemMenuVendedorDAO().eliminarItemsVendedor(vendedor, itemsMenuEliminar);

                itemsMenu = new ArrayList<>();
                interfazModificarVendedor.dispose();
                restablecerTablaConDatosOriginales();
                interfazModificarVendedor = null;
            }
        } 
        
        else if(comando.equals("CancelarCrear")) {
            interfazCrearVendedor.dispose();  

        } 
        
        else if(comando.equals("CancelarModificar")) {
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
        for(Vendedor vendedor : VendedorMemory.listaVendedores) {
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
    
    public int crearVendedor(String nombre, String direccion, ArrayList<ItemMenu> itemsMenu) {
        
        Vendedor vendedor = new Vendedor();
        vendedor.setNombre(nombre);
        vendedor.setDireccion(direccion);
        vendedor.setItems(itemsMenu);

        // VendedorMemory.listaVendedores.add(vendedor);  
        int id = FactoryDAO.getVendedorDAO().crearVendedor(vendedor);
        vendedor.setId(id);
        FactoryDAO.getItemMenuVendedorDAO().agregarItemsVendedor(vendedor);
        return id;
    }
    
    public void eliminarVendedor(int id) {
        
        /*
        Iterator<Vendedor> iterador = VendedorMemory.listaVendedores.iterator();
        while(iterador.hasNext()) {
            Vendedor c = iterador.next();
            if(c.getId() == id) {
                iterador.remove();
            }
        }
        */
        
        FactoryDAO.getVendedorDAO().eliminarVendedor(id);
    }
     
    public Vendedor buscarPorIdVendedor(int id){
       
        /*
        for(Vendedor vendedor : VendedorMemory.listaVendedores) {
            if(vendedor.getId() == id) {
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
        for(Vendedor vendedor : VendedorMemory.listaVendedores) {
            if(vendedor.getNombre().equals(nombre)) {
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
        for(Vendedor vendedor : vendedores) {
            Object[] rowData = {
                vendedor.getNombre(),
                vendedor.getId(),
                vendedor.getDireccion()
            };
            listaDeVendedores.getModelo().addRow(rowData);
        }
    }
    
    public void actualizarVendedor(Vendedor vendedorActualizado, ArrayList<ItemMenu> itemMenu) {
        
        /*
        for(Vendedor vendedor : VendedorMemory.listaVendedores) {
            if(vendedor.getId() == id) {
                vendedor.setNombre(nombre);
                vendedor.setDireccion(direccion);
            }
        }
        */
        
        Vendedor vendedor = FactoryDAO.getVendedorDAO().buscarVendedorPorID(vendedorActualizado.getId());
        vendedor.setNombre(vendedorActualizado.getNombre());
        vendedor.setDireccion(vendedorActualizado.getDireccion());
        ArrayList<ItemMenu> items = FactoryDAO.getItemMenuVendedorDAO().obtenerItemsMenuDeVendedor(vendedor);
        vendedor.setItems(items);
        vendedor.agregarItems(itemMenu);
        
        FactoryDAO.getVendedorDAO().actualizarVendedor(vendedor);
        FactoryDAO.getItemMenuVendedorDAO().agregarItemsVendedor(vendedor);
        
    }
    
    private boolean validarCamposVacios(CrearVendedor interfaz) {
        
        boolean vacio = false;
        Border defaultBorder = interfaz.getBordeTexto();

        if (interfaz.getjTextField1().getText().trim().isEmpty()) {
            interfaz.getjTextField1().setBorder(BorderFactory.createLineBorder(Color.RED));
            vacio = true;
        } else {
            interfaz.getjTextField1().setBorder(defaultBorder);
        }

        if (interfaz.getjTextField5().getText().trim().isEmpty()) {
            interfaz.getjTextField5().setBorder(BorderFactory.createLineBorder(Color.RED));
            vacio = true;
        } else {
            interfaz.getjTextField5().setBorder(defaultBorder);
        }

        return vacio;
    }

    private boolean validarTiposDeDatos(CrearVendedor interfaz) {
        
        boolean correcto = true;
        Border defaultBorder = interfaz.getBordeTexto();

        if (!interfaz.getjTextField1().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            interfaz.getjTextField1().setBorder(BorderFactory.createLineBorder(Color.RED));
            correcto = false;
        } else {
            interfaz.getjTextField1().setBorder(defaultBorder);
        }

        if (!interfaz.getjTextField5().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s]+")) {
            interfaz.getjTextField5().setBorder(BorderFactory.createLineBorder(Color.RED));
            correcto = false;
        } else {
            interfaz.getjTextField5().setBorder(defaultBorder);
        }

        return correcto;
    }

    private boolean validarCamposVaciosModificar(ModificarVendedor interfaz) {
        
        boolean vacio = false;
        Border defaultBorder = interfaz.getBordeTexto();

        if (interfaz.getjTextField1().getText().trim().isEmpty()) {
            interfaz.getjTextField1().setBorder(BorderFactory.createLineBorder(Color.RED));
            vacio = true;
        } else {
            interfaz.getjTextField1().setBorder(defaultBorder);
        }

        if (interfaz.getjTextField5().getText().trim().isEmpty()) {
            interfaz.getjTextField5().setBorder(BorderFactory.createLineBorder(Color.RED));
            vacio = true;
        } else {
            interfaz.getjTextField5().setBorder(defaultBorder);
        }

        return vacio;
    }

    private boolean validarTiposDeDatosModificar(ModificarVendedor interfaz) {
        
        boolean correcto = true;
        Border defaultBorder = interfaz.getBordeTexto();

        if (!interfaz.getjTextField1().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            interfaz.getjTextField1().setBorder(BorderFactory.createLineBorder(Color.RED));
            correcto = false;
        } else {
            interfaz.getjTextField1().setBorder(defaultBorder);
        }

        if (!interfaz.getjTextField5().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s]+")) {
            interfaz.getjTextField5().setBorder(BorderFactory.createLineBorder(Color.RED));
            correcto = false;
        } else {
            interfaz.getjTextField5().setBorder(defaultBorder);
        }

        return correcto;
    }
    
    public boolean verificarID(int id_vendedor) {
        
        boolean existe = false;
        /*
        for(Vendedor vendedor : VendedorMemory.listaVendedores) {
            if(vendedor.getId() == id_vendedor) existe = true; 
        }
        
        return existe;
       */
        
        VendedorMySQLDAO vendedorMySQLDAO = (VendedorMySQLDAO) FactoryDAO.getVendedorDAO();
        existe = vendedorMySQLDAO.buscarVendedorPorID(id_vendedor) != null;
        return existe;
    }
    
    public void cargarDatosOriginalesEnTablaItems(ArrayList<ItemMenu> listaItems, ItemsMenuPedido interfaz) {
        
        /*
        for(ItemMenu itemMenu : ItemMenuMemory.listaItemMenu) {
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
        
        for(ItemMenu itemMenu : listaItems) {
                Object[] rowData = {
                itemMenu.getId(),
                itemMenu.getCategoria().getTipo_item().toString(),
                itemMenu.getNombre(),
                itemMenu.getDescripcion(),
                itemMenu.getPrecio(),
                itemMenu.getCategoria().getDescripcion()
            };
            interfaz.getModelo().addRow(rowData);
        }
    }
    
    public void cargarDatosOriginalesEnTablaItems(ArrayList<ItemMenu> listaItems, VerItems interfaz) {
        
        for(ItemMenu itemMenu : listaItems) {
                Object[] rowData = {
                itemMenu.getId(),
                itemMenu.getCategoria().getTipo_item().toString(),
                itemMenu.getNombre(),
                itemMenu.getDescripcion(),
                itemMenu.getPrecio(),
                itemMenu.getCategoria().getDescripcion()
            };
            interfaz.getModelo().addRow(rowData);
        }
    }
    
    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        itemsMenu = new ArrayList<>();
        interfazModificarVendedor = null;
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
    
}






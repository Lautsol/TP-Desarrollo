
package isi.deso.desarrollotrabajopractico.Interfaces;

import isi.deso.desarrollotrabajopractico.Controladores.PedidoController;
import isi.deso.desarrollotrabajopractico.Controladores.VendedorController;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class ItemsMenuPedido extends javax.swing.JFrame {

    private PedidoController controladorPedido;
    private VendedorController controladorVendedor;
    
    public ItemsMenuPedido(PedidoController controlador) {
        initComponents();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        controladorPedido = controlador;
        
       // Configurar las columnas para que no se puedan redimensionar
       for (int i = 0; i < jTable1.getColumnModel().getColumnCount(); i++) {
           jTable1.getColumnModel().getColumn(i).setResizable(false);
       }

       // Crear un renderizador personalizado para la columna de acciones
       jTable1.getColumnModel().getColumn(7).setCellRenderer(new ActionCellRenderer());

       // Crear un editor personalizado para la columna de acciones
       jTable1.getColumnModel().getColumn(7).setCellEditor(new ActionCellEditor(jTable1));
        
    }
    
   public ItemsMenuPedido(VendedorController controlador) {
        initComponents();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        controladorVendedor = controlador;
        
       // Configurar las columnas para que no se puedan redimensionar
       for (int i = 0; i < jTable1.getColumnModel().getColumnCount(); i++) {
           jTable1.getColumnModel().getColumn(i).setResizable(false);
       }

       // Crear un renderizador personalizado para la columna de acciones
       jTable1.getColumnModel().getColumn(7).setCellRenderer(new ActionCellRenderer());

       // Crear un editor personalizado para la columna de acciones
       jTable1.getColumnModel().getColumn(7).setCellEditor(new ActionCellEditor(jTable1));
        
    }
    
    // Renderizador de celdas para los botones
    private class ActionCellRenderer extends JPanel implements TableCellRenderer {

        private final JButton agregarButton = new JButton("Agregar");

        public ActionCellRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(agregarButton);
            
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Editor de celdas para los botones
    private class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {

        private final JPanel panel;
        private final JButton agregarButton;
        private final JTable table;

        public ActionCellEditor(JTable table) {
            this.table = table;
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            agregarButton = new JButton("Agregar");
           
            panel.add(agregarButton);
            
            agregarButton.addActionListener(controladorPedido);
            agregarButton.addActionListener(controladorVendedor);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            
            return panel;
        }

        public Object getCellEditorValue() {
            return null; 
        }
    }
    
    public DefaultTableModel getModelo() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        return model;
    }
    
    public JTable getjTable1() {
        return jTable1;
    }
    
    public void ocultarColumna() {
        TableColumnModel columnModel = jTable1.getColumnModel();
        TableColumn column = columnModel.getColumn(6); // Índice de la columna que deseas ocultar (por ejemplo, la columna 0)
        columnModel.removeColumn(column); // Oculta la columna
    }
    
    public void mostrarMensajeExitoso() {
        showMessageDialog(null, "El item se agregó con éxito.", "SE AGREGÓ EL ITEM.", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mostrarMensajeError(String mensaje) {
        showMessageDialog(null, mensaje, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelItemMenu = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Tipo item", "Nombre", "Descripción", "Precio", "ID categoría", "Cantidad", "Acciones"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(30);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setHeaderValue("Nombre");
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setHeaderValue("Precio");
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setHeaderValue("ID categoría");
            jTable1.getColumnModel().getColumn(6).setHeaderValue("Cantidad");
            jTable1.getColumnModel().getColumn(7).setHeaderValue("Acciones");
        }

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLabel1.setBackground(new java.awt.Color(153, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Lista de items menú");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 701, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelItemMenuLayout = new javax.swing.GroupLayout(panelItemMenu);
        panelItemMenu.setLayout(panelItemMenuLayout);
        panelItemMenuLayout.setHorizontalGroup(
            panelItemMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelItemMenuLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelItemMenuLayout.setVerticalGroup(
            panelItemMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelItemMenuLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 713, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelItemMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 427, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelItemMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ItemsMenuPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ItemsMenuPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ItemsMenuPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ItemsMenuPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel panelItemMenu;
    // End of variables declaration//GEN-END:variables
}

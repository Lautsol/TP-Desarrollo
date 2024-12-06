
package isi.deso.desarrollotrabajopractico.Interfaces;

import java.awt.BorderLayout;

public class Principal extends javax.swing.JFrame {

    public Principal() {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        vendedoresBoton = new javax.swing.JButton();
        itemMenusBoton = new javax.swing.JButton();
        clientesBoton = new javax.swing.JButton();
        pedidosBoton = new javax.swing.JButton();
        panelDeAcciones = new javax.swing.JPanel();

        jButton2.setText("jButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        vendedoresBoton.setBackground(new java.awt.Color(102, 102, 102));
        vendedoresBoton.setText("Vendedores");
        vendedoresBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vendedoresBotonActionPerformed(evt);
            }
        });

        itemMenusBoton.setBackground(new java.awt.Color(102, 102, 102));
        itemMenusBoton.setText("Items Menú");
        itemMenusBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenusBotonActionPerformed(evt);
            }
        });

        clientesBoton.setBackground(new java.awt.Color(102, 102, 102));
        clientesBoton.setText("Clientes");
        clientesBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientesBotonActionPerformed(evt);
            }
        });

        pedidosBoton.setBackground(new java.awt.Color(102, 102, 102));
        pedidosBoton.setText("Pedidos");
        pedidosBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pedidosBotonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pedidosBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clientesBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemMenusBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vendedoresBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(vendedoresBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(clientesBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(itemMenusBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(pedidosBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelDeAcciones.setForeground(new java.awt.Color(153, 153, 153));
        panelDeAcciones.setPreferredSize(new java.awt.Dimension(713, 418));

        javax.swing.GroupLayout panelDeAccionesLayout = new javax.swing.GroupLayout(panelDeAcciones);
        panelDeAcciones.setLayout(panelDeAccionesLayout);
        panelDeAccionesLayout.setHorizontalGroup(
            panelDeAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 795, Short.MAX_VALUE)
        );
        panelDeAccionesLayout.setVerticalGroup(
            panelDeAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 420, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelDeAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, 795, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panelDeAcciones, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void vendedoresBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vendedoresBotonActionPerformed
        // TODO add your handling code here:
        ListaDeVendedores panelVendedores = new ListaDeVendedores();
        panelVendedores.setSize(795,420);
        panelVendedores.setLocation(0,0);
        panelDeAcciones.removeAll();
        panelDeAcciones.add(panelVendedores, BorderLayout.CENTER);
        panelDeAcciones.revalidate();
        panelDeAcciones.repaint();
        panelVendedores.getControlador().cargarDatosOriginalesEnTabla();
    }//GEN-LAST:event_vendedoresBotonActionPerformed

    private void clientesBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientesBotonActionPerformed
        // TODO add your handling code here:
        ListaDeClientes panelClientes = new ListaDeClientes();
        panelClientes.setSize(795,420);
        panelClientes.setLocation(0,0);
        panelDeAcciones.removeAll();
        panelDeAcciones.add( panelClientes, BorderLayout.CENTER);
        panelDeAcciones.revalidate();
        panelDeAcciones.repaint();
        panelClientes.getControlador().cargarDatosOriginalesEnTabla();
    }//GEN-LAST:event_clientesBotonActionPerformed

    private void itemMenusBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenusBotonActionPerformed
        // TODO add your handling code here:
        ListaDeItemMenu panelItemMenu = new ListaDeItemMenu();
        panelItemMenu.setSize(795,420);
        panelItemMenu.setLocation(0,0);
        panelDeAcciones.removeAll();
        panelDeAcciones.add( panelItemMenu, BorderLayout.CENTER);
        panelDeAcciones.revalidate();
        panelDeAcciones.repaint();
        panelItemMenu.getControlador().cargarDatosOriginalesEnTabla();
    }//GEN-LAST:event_itemMenusBotonActionPerformed

    private void pedidosBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pedidosBotonActionPerformed
        // TODO add your handling code here:
        ListaDePedidos panelPedidos = new ListaDePedidos();
        panelPedidos.setSize(795,420);
        panelPedidos.setLocation(0,0);
        panelDeAcciones.removeAll();
        panelDeAcciones.add( panelPedidos, BorderLayout.CENTER);
        panelDeAcciones.revalidate();
        panelDeAcciones.repaint();
        panelPedidos.getControlador().cargarDatosOriginalesEnTabla();
    }//GEN-LAST:event_pedidosBotonActionPerformed

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
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clientesBoton;
    private javax.swing.JButton itemMenusBoton;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel panelDeAcciones;
    private javax.swing.JButton pedidosBoton;
    private javax.swing.JButton vendedoresBoton;
    // End of variables declaration//GEN-END:variables
}

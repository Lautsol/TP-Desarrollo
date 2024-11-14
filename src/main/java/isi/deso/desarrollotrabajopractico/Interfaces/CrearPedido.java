
package isi.deso.desarrollotrabajopractico.Interfaces;

import isi.deso.desarrollotrabajopractico.Controladores.PedidoController;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JTextField;

public class CrearPedido extends javax.swing.JFrame {

    private PedidoController controlador;
    
    public CrearPedido() {
        initComponents();
        
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
     public void setControlador(PedidoController controlador) {
        this.controlador = controlador;
        jButton1.addActionListener(controlador);
        jButton2.addActionListener(controlador);
        jButton3.addActionListener(controlador);
    }

    public JTextField getCampoIDpedido() {
        return campoIDpedido;
    } 
     
    public JComboBox<String> getCampoEstado() {
        return CampoEstado;
    }

    public JComboBox<String> getCampoFormaDePago() {
        return CampoFormaDePago;
    }

    public JTextField getCampoIDcliente() {
        return campoIDcliente;
    }

    public JTextField getCampoIDvendedor() {
        return campoIDvendedor;
    }

    public JTextField getCampoTotal() {
        return campoTotal;
    }

     public void setearCamposEnBlanco() {
        campoIDpedido.setText("");
        campoIDcliente.setText("");
        campoTotal.setText("");
        CampoEstado.setSelectedItem("EN ENVIO");
        campoIDvendedor.setText("");
        CampoFormaDePago.setSelectedItem("MERCADOPAGO");
    }
     
    public void mostrarMensajeCamposVacios() {
       showMessageDialog(null, "Hay campos sin rellenar.", "ADVERTENCIA", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mostrarMensajeDatosInvalidos() {
        showMessageDialog(null, "Hay campos inválidos.", "ADVERTENCIA", JOptionPane.INFORMATION_MESSAGE);   
    }
     
    public void mostrarMensajeID() {
        showMessageDialog(null, "Ya existe un pedido con ese identificador.", "ADVERTENCIA", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mostrarMensajeCliente() {
        showMessageDialog(null, "El cliente no existe.", "ADVERTENCIA", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mostrarMensajeVendedor() {
        showMessageDialog(null, "El vendedor no existe.", "ADVERTENCIA", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mostrarMensajeAlias() {
        showMessageDialog(null, "Debe ingresar el alias del cliente para utilizar esa forma de pago.", "ADVERTENCIA", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mostrarMensajeCbu() {
        showMessageDialog(null, "Debe ingresar el CBU del cliente para utilizar esa forma de pago.", "ADVERTENCIA", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mostrarMensajeProductoOtroVendedor() {
        showMessageDialog(null, "El pedido no puede contener productos de distintos vendedores. Vuelva a iniciar.", "ERROR", JOptionPane.ERROR_MESSAGE);
    }
    
    public void mostrarMensajePedidoVacio() {
        showMessageDialog(null, "No puede crear un pedido sin items.", "ERROR", JOptionPane.ERROR_MESSAGE);

    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        campoIDcliente = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        campoTotal = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        campoIDvendedor = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        CampoFormaDePago = new javax.swing.JComboBox<>();
        CampoEstado = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        campoIDpedido = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(713, 423));

        campoIDcliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoIDclienteActionPerformed(evt);
            }
        });
        campoIDcliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDclienteKeyTyped(evt);
            }
        });

        jLabel14.setText("Total:");

        campoTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoTotalKeyTyped(evt);
            }
        });

        jLabel15.setText("ID cliente:");

        jLabel16.setText("Estado:");

        campoIDvendedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDvendedorKeyTyped(evt);
            }
        });

        jLabel17.setText("ID vendedor:");

        jLabel18.setText("Forma de pago:");

        jPanel15.setBackground(new java.awt.Color(153, 153, 153));

        jLabel22.setBackground(new java.awt.Color(153, 255, 255));
        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Crear un nuevo pedido");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jButton2.setBackground(new java.awt.Color(102, 102, 102));
        jButton2.setText("Cancelar");
        jButton2.setActionCommand("CancelarCrear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(102, 102, 102));
        jButton1.setText("Crear");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        CampoFormaDePago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MERCADOPAGO", "TRANSFERENCIA" }));
        CampoFormaDePago.setToolTipText("");
        CampoFormaDePago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CampoFormaDePagoActionPerformed(evt);
            }
        });

        CampoEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "EN_PROCESO", "EN_ENVIO", "RECIBIDO" }));
        CampoEstado.setToolTipText("");
        CampoEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CampoEstadoActionPerformed(evt);
            }
        });

        jLabel19.setText("ID pedido:");

        campoIDpedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoIDpedidoActionPerformed(evt);
            }
        });
        campoIDpedido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoIDpedidoKeyTyped(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(102, 102, 102));
        jButton3.setText("Agregar items");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CampoFormaDePago, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoIDcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoIDpedido, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6)))
                .addGap(83, 83, 83)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel14)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(campoTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel17)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(campoIDvendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CampoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(130, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campoIDpedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel17)
                    .addComponent(campoIDvendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(campoIDcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(campoTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(48, 48, 48)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(CampoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(CampoFormaDePago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 697, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void campoIDclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoIDclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoIDclienteActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void CampoFormaDePagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CampoFormaDePagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CampoFormaDePagoActionPerformed

    private void CampoEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CampoEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CampoEstadoActionPerformed

    private void campoIDpedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoIDpedidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoIDpedidoActionPerformed

    private void campoIDpedidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDpedidoKeyTyped
        if (campoIDpedido.getText().length() >= 6) {
                    evt.consume(); // Evitar que se ingrese más texto
                }
    }//GEN-LAST:event_campoIDpedidoKeyTyped

    private void campoIDvendedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDvendedorKeyTyped
        if (campoIDvendedor.getText().length() >= 6) {
                    evt.consume(); // Evitar que se ingrese más texto
                }
    }//GEN-LAST:event_campoIDvendedorKeyTyped

    private void campoIDclienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIDclienteKeyTyped
        if (campoIDcliente.getText().length() >= 6) {
                    evt.consume(); // Evitar que se ingrese más texto
                }
    }//GEN-LAST:event_campoIDclienteKeyTyped

    private void campoTotalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoTotalKeyTyped
        if (campoTotal.getText().length() >= 6) {
                    evt.consume(); // Evitar que se ingrese más texto
                }
    }//GEN-LAST:event_campoTotalKeyTyped

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(CrearPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CrearPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CrearPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CrearPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CrearPedido CP;
                CP = new CrearPedido();
                CP.setVisible(true);
                CP.setLocationRelativeTo(null);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CampoEstado;
    private javax.swing.JComboBox<String> CampoFormaDePago;
    private javax.swing.JTextField campoIDcliente;
    private javax.swing.JTextField campoIDpedido;
    private javax.swing.JTextField campoIDvendedor;
    private javax.swing.JTextField campoTotal;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel15;
    // End of variables declaration//GEN-END:variables
}

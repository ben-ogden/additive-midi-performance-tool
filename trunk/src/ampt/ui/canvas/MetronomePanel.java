package ampt.ui.canvas;

import javax.swing.SpinnerNumberModel;

/**
 *
 * @author robert
 */
public class MetronomePanel extends javax.swing.JPanel {

    /** Creates new form MetronomePanel */
    public MetronomePanel() {
        initComponents();
        SpinnerNumberModel model = new SpinnerNumberModel();
        model.setMinimum(0);
        tempoSpinner.setModel(model);
        tempoSpinner.setValue(60);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        animatedMetronome1 = new ampt.ui.canvas.AnimatedMetronome();
        tempoSpinner = new javax.swing.JSpinner();
        toggleMetronomeBtn = new javax.swing.JToggleButton();
        setTemoBtn = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(175, 50));

        animatedMetronome1.setMaximumSize(new java.awt.Dimension(28, 90));

        javax.swing.GroupLayout animatedMetronome1Layout = new javax.swing.GroupLayout(animatedMetronome1);
        animatedMetronome1.setLayout(animatedMetronome1Layout);
        animatedMetronome1Layout.setHorizontalGroup(
            animatedMetronome1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(animatedMetronome1Layout.createSequentialGroup()
                .addComponent(tempoSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        animatedMetronome1Layout.setVerticalGroup(
            animatedMetronome1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, animatedMetronome1Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addComponent(tempoSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        toggleMetronomeBtn.setFont(new java.awt.Font("Tahoma", 0, 7)); // NOI18N
        toggleMetronomeBtn.setText("On");
        toggleMetronomeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleMetronomeBtnActionPerformed(evt);
            }
        });

        setTemoBtn.setFont(new java.awt.Font("Tahoma", 0, 7)); // NOI18N
        setTemoBtn.setText("Set Tempo");
        setTemoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setTemoBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(animatedMetronome1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(setTemoBtn, 0, 0, Short.MAX_VALUE)
                    .addComponent(toggleMetronomeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(toggleMetronomeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(setTemoBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                    .addComponent(animatedMetronome1, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void toggleMetronomeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleMetronomeBtnActionPerformed
        if (!toggleMetronomeBtn.isSelected()) {
            animatedMetronome1.stop();
            toggleMetronomeBtn.setText("Start");
        } else {
            animatedMetronome1.start();
            toggleMetronomeBtn.setText("Stop");
        }
    }//GEN-LAST:event_toggleMetronomeBtnActionPerformed

    private void setTemoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setTemoBtnActionPerformed
        animatedMetronome1.setTempo((Integer)tempoSpinner.getValue());
    }//GEN-LAST:event_setTemoBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ampt.ui.canvas.AnimatedMetronome animatedMetronome1;
    private javax.swing.JButton setTemoBtn;
    private javax.swing.JSpinner tempoSpinner;
    private javax.swing.JToggleButton toggleMetronomeBtn;
    // End of variables declaration//GEN-END:variables

}
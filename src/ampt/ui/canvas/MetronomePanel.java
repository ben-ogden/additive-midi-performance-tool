package ampt.ui.canvas;

import ampt.ui.tempo.TempoEvent;
import ampt.ui.tempo.TempoListener;


/**
 *
 * @author robert
 */
public class MetronomePanel extends javax.swing.JPanel implements TempoListener {

	private static final long serialVersionUID = -7840736273508689783L;

	/** Creates new form MetronomePanel */
    public MetronomePanel() {

        initComponents();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        animatedMetronome1 = new ampt.ui.canvas.AnimatedMetronome();
        toggleMetronomeBtn = new javax.swing.JToggleButton();

        setPreferredSize(new java.awt.Dimension(200, 50));

        animatedMetronome1.setMaximumSize(new java.awt.Dimension(28, 90));

        toggleMetronomeBtn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        toggleMetronomeBtn.setText("Start");
        toggleMetronomeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleMetronomeBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout animatedMetronome1Layout = new javax.swing.GroupLayout(animatedMetronome1);
        animatedMetronome1.setLayout(animatedMetronome1Layout);
        animatedMetronome1Layout.setHorizontalGroup(
            animatedMetronome1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, animatedMetronome1Layout.createSequentialGroup()
                .addContainerGap(99, Short.MAX_VALUE)
                .addComponent(toggleMetronomeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        animatedMetronome1Layout.setVerticalGroup(
            animatedMetronome1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toggleMetronomeBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(animatedMetronome1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(animatedMetronome1, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
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




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ampt.ui.canvas.AnimatedMetronome animatedMetronome1;
    private javax.swing.JToggleButton toggleMetronomeBtn;
    // End of variables declaration//GEN-END:variables

    @Override
    public void tempoChanged(TempoEvent event) {
        animatedMetronome1.setTempo(event.getTempo());
    }

}

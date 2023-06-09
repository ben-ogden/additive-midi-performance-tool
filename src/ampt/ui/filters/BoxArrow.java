/*
 * Arrow.java
 *
 * Created on Nov 18, 2009, 9:56:06 AM
 */
package ampt.ui.filters;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JLabel;

/**
 *
 * An arrow for putting in a midiDeviceBox to show that the box has
 * receivers or transmitters.
 *
 * @author Christopher
 */
public class BoxArrow extends JLabel {

	private static final long serialVersionUID = 3040089427783986046L;
	
	private static final int HEIGHT = 7;
    private static final int WIDTH = 7;
    private Color color;

    /** Creates new form BeanForm */
    public BoxArrow() {
        initComponents();
        this.color = Color.BLACK;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setSize(new Dimension(WIDTH, HEIGHT));
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color oldColor = g.getColor();

        g.setColor(this.color);

        g.drawLine(0, HEIGHT / 2, WIDTH, HEIGHT / 2);


        g.drawLine(WIDTH - 3, 0, WIDTH, HEIGHT / 2);
        g.drawLine(WIDTH - 3, HEIGHT - 1, WIDTH, HEIGHT / 2);



        g.setColor(oldColor);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

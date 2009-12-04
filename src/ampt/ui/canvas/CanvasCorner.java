/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ampt.ui.canvas;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 *
 * @author Christopher
 */
public class CanvasCorner extends JComponent{

	private static final long serialVersionUID = 8739390367633480692L;

	protected void paintComponent(Graphics g) {
        // Fill me with dirty brown/orange.
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

}

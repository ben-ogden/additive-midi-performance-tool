/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ampt.ui.canvas;

import ampt.ui.keyboard.KeyboardPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 *
 * @author Christopher
 */
public class KeyboardBox extends JComponent {

    private static final String TEXT = "Keyboard";
    private final KeyboardPanel keyboard;

    public KeyboardBox(KeyboardPanel keyboard){
        this.keyboard = keyboard;
        this.setPreferredSize(new Dimension(51, 51));
    }

    @Override
    protected void paintComponent(Graphics g) {
    	Color color = g.getColor();
	g.setColor(Color.BLACK);

        g.drawRect(0, 0, 50, 50);

	g.drawString(TEXT, 5, 45);

	g.setColor(color);
	}

}

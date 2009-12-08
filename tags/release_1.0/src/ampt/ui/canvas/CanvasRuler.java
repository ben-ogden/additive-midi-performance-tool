/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ampt.ui.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.JComponent;

/**
 *
 * @author Christopher
 */
public class CanvasRuler extends JComponent{

	private static final long serialVersionUID = 1420432483595138018L;

	public enum Orientation {Horizontal, Vertical};

    private static final int INCH = Toolkit.getDefaultToolkit().getScreenResolution();
    public static final int SIZE = 20;


    private Orientation orientation;
    private int units;
    private int increment;

    /**
     * This is a ruler that can be placed in the column and row headings
     * of a ScrolPane, to help the user with a sense of where things are in
     * the console.
     *
     * It is currently implemented in centimeters, but can be converted to inches
     * in the future if we desire to do so.
     * @param orientation
     */
    public CanvasRuler(Orientation orientation){
        super();
        this.orientation = orientation;
        units = (int)((double)INCH / 2.54);
        increment = units;

        // we need to set the preferred size of the ruler.
        if(orientation == Orientation.Horizontal){
            setPreferredSize(new Dimension(3500, SIZE));
        } else {
            setPreferredSize(new Dimension(SIZE, 3500));
        }

    }

        protected void paintComponent(Graphics g) {
        Rectangle drawHere = g.getClipBounds();

        // Fill clipping area with white
        g.setColor(Color.WHITE);
        g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);

        // Do the ruler labels in a small font that's light gray.
        g.setFont(new Font("SansSerif", Font.PLAIN, 8));
        g.setColor(Color.LIGHT_GRAY);

        // Some vars we need.
        int end = 0;
        int start = 0;
        int tickLength = 0;
        String text = null;

        // Use clipping bounds to calculate first and last tick locations.
        if (orientation == Orientation.Horizontal) {
            start = (drawHere.x / increment) * increment;
            end = (((drawHere.x + drawHere.width) / increment) + 1)
                  * increment;
        } else {
            start = (drawHere.y / increment) * increment;
            end = (((drawHere.y + drawHere.height) / increment) + 1)
                  * increment;
        }

        // Make a special case of 0 to display the number
        // within the rule and draw a units label.
        if (start == 0) {
            text = Integer.toString(0) + " cm";
            tickLength = 10;
            if (orientation == Orientation.Horizontal) {
                g.drawLine(0, SIZE-1, 0, SIZE-tickLength-1);
                g.drawString(text, 2, 8);
            } else {
                g.drawLine(SIZE-1, 0, SIZE-tickLength-1, 0);
                g.drawString(text, 2, 10);
            }
            text = null;
            start = increment;
        }

        // ticks and labels
        for (int i = start; i < end; i += increment) {
            if (i % units == 0)  {
                tickLength = 7;
                text = Integer.toString(i/units);
            } else {
                tickLength = 5;
                text = null;
            }

            if (tickLength != 0) {
                if (orientation == Orientation.Horizontal) {
                    g.drawLine(i, SIZE-1, i, SIZE-tickLength-1);
                    if (text != null)
                        g.drawString(text, i-3, 8);
                } else {
                    g.drawLine(SIZE-1, i, SIZE-tickLength-1, i);
                    if (text != null)
                        g.drawString(text, 2, i+3);
                }
            }
        }
    }


}

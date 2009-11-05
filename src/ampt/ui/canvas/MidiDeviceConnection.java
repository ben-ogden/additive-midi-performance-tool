package ampt.ui.canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JComponent;

/**
 * This class represents a connection between two MidiDevices.  It is drawn
 * on the canvas panel as a line.
 *
 * TODO: Add arrows!
 *
 * @author Christopher
 */
public class MidiDeviceConnection extends JComponent {

    MidiDeviceBox from, to;

    public MidiDeviceConnection(MidiDeviceBox from, MidiDeviceBox to) {
        this.from = from;
        this.to = to;
        try {
            from.getTransmitter().setReceiver(to.getReceiver());
        } catch (MidiUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    public void paintOnCanvas(Graphics g) {
        int fromTopX = from.getX() + from.getWidth() / 2;
        int fromTopY = from.getY();
        int fromBottomX = from.getX() + from.getWidth() / 2;
        int fromBottomY = from.getY() + from.getHeight();
        int fromLeftX = from.getX();
        int fromLeftY = from.getY() + from.getHeight() / 2;
        int fromRightX = from.getX() + from.getWidth();
        int fromRightY = from.getY() + from.getHeight() / 2;

        int toTopX = to.getX() + to.getWidth() / 2;
        int toTopY = to.getY();
        int toBottomX = to.getX() + to.getWidth() / 2;
        int toBottomY = to.getY() + to.getHeight();
        int toLeftX = to.getX();
        int toLeftY = to.getY() + to.getHeight() / 2;
        int toRightX = to.getX() + to.getWidth();
        int toRightY = to.getY() + to.getHeight() / 2;

        double fromTopToBottom = Math.sqrt(Math.pow((fromTopX - toBottomX), 2) + Math.pow((fromTopY - toBottomY), 2));
        double fromBottomToTop = Math.sqrt(Math.pow((fromBottomX - toTopX), 2) + Math.pow((fromBottomY - toTopY), 2));
        double fromLeftToRight = Math.sqrt(Math.pow((fromLeftX - toRightX), 2) + Math.pow((fromLeftY - toRightY), 2));
        double fromRightToLeft = Math.sqrt(Math.pow((fromRightX - toLeftX), 2) + Math.pow((fromRightY - toLeftY), 2));
        int fromX, fromY, toX, toY;
        if (fromTopToBottom <= fromBottomToTop && fromTopToBottom <= fromLeftToRight && fromTopToBottom <= fromRightToLeft) {
            fromX = fromTopX;
            fromY = fromTopY;
            toX = toBottomX;
            toY = toBottomY;
//            arrowFromX1 = toX - (int) (7 * Math.sin((Math.asin((toRightX - fromLeftX) / fromLeftToRight)) / 2));
//            arrowFromY1 = toY - (int) (7 * Math.cos((Math.asin((toRightY - fromLeftY) / fromLeftToRight)) / 2));
        } else if (fromBottomToTop <= fromLeftToRight && fromBottomToTop <= fromRightToLeft) {
            fromX = fromBottomX;
            fromY = fromBottomY;
            toX = toTopX;
            toY = toTopY;
//            arrowFromX1 = toX - (int) (7 * Math.sin((Math.asin((toRightX - fromLeftX) / fromLeftToRight)) / 2));
//            arrowFromY1 = toY - (int) (7 * Math.cos((Math.asin((toRightY - fromLeftY) / fromLeftToRight)) / 2));
        } else if (fromLeftToRight <= fromRightToLeft) {
            fromX = fromLeftX;
            fromY = fromLeftY;
            toX = toRightX;
            toY = toRightY;
//            arrowFromX1 = toX - (int) (7 * Math.sin((Math.asin((toRightX - fromLeftX) / fromLeftToRight)) / 3));
//            arrowFromY1 = toY - (int) (7 * Math.cos((Math.asin((toRightY - fromLeftY) / fromLeftToRight)) / 3));
        } else {
            fromX = fromRightX;
            fromY = fromRightY;
            toX = toLeftX;
            toY = toLeftY;
//            arrowFromX1 = toX - (int) (7 * Math.sin((Math.asin((toRightX - fromLeftX) / fromLeftToRight)) / 2));
//            arrowFromY1 = toY - (int) (7 * Math.cos((Math.asin((toRightY - fromLeftY) / fromLeftToRight)) / 2));
        }

        Color color = g.getColor();
        g.setColor(Color.CYAN.BLACK);

        g.drawLine(fromX, fromY, toX, toY);
//        g.drawLine(arrowFromX1, arrowFromY1, toX, toY);
        g.setColor(color);
    }
}

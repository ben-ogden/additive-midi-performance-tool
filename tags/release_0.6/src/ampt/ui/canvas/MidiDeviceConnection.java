package ampt.ui.canvas;

import ampt.ui.filters.MidiDeviceBox;
import java.awt.Color;
import java.awt.Graphics;
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

    MidiDeviceBox _from, _to;

    public MidiDeviceConnection(MidiDeviceBox from, MidiDeviceBox to) throws MidiUnavailableException {
        _from = from;
        _to = to;
        from.connectTo(to);
    }

    public MidiDeviceBox getFrom(){
        return _from;
    }

    public MidiDeviceBox getTo(){
        return _to;
    }

    public void paintOnCanvas(Graphics g) {
        int fromTopX = _from.getX() + _from.getWidth() / 2;
        int fromTopY = _from.getY();
        int fromBottomX = _from.getX() + _from.getWidth() / 2;
        int fromBottomY = _from.getY() + _from.getHeight();
        int fromLeftX = _from.getX();
        int fromLeftY = _from.getY() + _from.getHeight() / 2;
        int fromRightX = _from.getX() + _from.getWidth();
        int fromRightY = _from.getY() + _from.getHeight() / 2;

        int toTopX = _to.getX() + _to.getWidth() / 2;
        int toTopY = _to.getY();
        int toBottomX = _to.getX() + _to.getWidth() / 2;
        int toBottomY = _to.getY() + _to.getHeight();
        int toLeftX = _to.getX();
        int toLeftY = _to.getY() + _to.getHeight() / 2;
        int toRightX = _to.getX() + _to.getWidth();
        int toRightY = _to.getY() + _to.getHeight() / 2;

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
        g.setColor(Color.BLACK);

        g.drawLine(fromX, fromY, toX, toY);
//        g.drawLine(arrowFromX1, arrowFromY1, toX, toY);
        g.setColor(color);
    }
}

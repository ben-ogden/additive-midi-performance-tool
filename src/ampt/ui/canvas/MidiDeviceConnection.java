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
 * @author Christopher
 */
public class MidiDeviceConnection extends JComponent {

	private static final long serialVersionUID = -6917512530683269151L;

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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MidiDeviceConnection){
            MidiDeviceConnection anotherConn = (MidiDeviceConnection) obj;
            if(this._from.equals(anotherConn._from) && this._to.equals(anotherConn._to)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this._from != null ? this._from.hashCode() : 0);
        hash = 79 * hash + (this._to != null ? this._to.hashCode() : 0);
        return hash;
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
        int fromX, fromY, toX, toY, arrowFromX1, arrowFromX2, arrowFromY1, arrowFromY2, arrowToX, arrowToY;
        if (fromTopToBottom <= fromBottomToTop && fromTopToBottom <= fromLeftToRight && fromTopToBottom <= fromRightToLeft) {
            fromX = fromTopX;
            fromY = fromTopY;
            toX = toBottomX;
            toY = toBottomY;
            arrowToX = toX - ((toX - fromX) / 2);
            arrowToY = toY - ((toY - fromY) / 2);
            if(toX < fromX) {
                arrowFromX1 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromTopToBottom)) + Math.PI / 4));
                arrowFromY1 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromTopToBottom)) + Math.PI / 4));
                arrowFromX2 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromTopToBottom)) - Math.PI / 4));
                arrowFromY2 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromTopToBottom)) - Math.PI / 4));
            } else {
                arrowFromX1 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromTopToBottom)) - Math.PI / 4));
                arrowFromY1 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromTopToBottom)) + Math.PI / 4));
                arrowFromX2 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromTopToBottom)) + Math.PI / 4));
                arrowFromY2 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromTopToBottom)) - Math.PI / 4));
            }
        } else if (fromBottomToTop <= fromLeftToRight && fromBottomToTop <= fromRightToLeft) {
            fromX = fromBottomX;
            fromY = fromBottomY;
            toX = toTopX;
            toY = toTopY;
            arrowToX = toX - ((toX - fromX) / 2);
            arrowToY = toY - ((toY - fromY) / 2);
            if(toX > fromX){
                arrowFromX1 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromBottomToTop)) + Math.PI / 4));
                arrowFromY1 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromBottomToTop)) + Math.PI / 4));
                arrowFromX2 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromBottomToTop)) - Math.PI / 4));
                arrowFromY2 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromBottomToTop)) - Math.PI / 4));
            } else {
                arrowFromX1 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromBottomToTop)) - Math.PI / 4));
                arrowFromY1 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromBottomToTop)) + Math.PI / 4));
                arrowFromX2 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromBottomToTop)) + Math.PI / 4));
                arrowFromY2 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromBottomToTop)) - Math.PI / 4));
            }
        } else if (fromLeftToRight <= fromRightToLeft) {
            fromX = fromLeftX;
            fromY = fromLeftY;
            toX = toRightX;
            toY = toRightY;
            arrowToX = toX - ((toX - fromX) / 2);
            arrowToY = toY - ((toY - fromY) / 2);
            if(toY < fromY){
                arrowFromX1 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromLeftToRight)) + Math.PI / 4));
                arrowFromY1 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromLeftToRight)) + Math.PI / 4));
                arrowFromX2 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromLeftToRight)) - Math.PI / 4));
                arrowFromY2 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromLeftToRight)) - Math.PI / 4));
            } else {
                arrowFromX1 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromLeftToRight)) - Math.PI / 4));
                arrowFromY1 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromLeftToRight)) + Math.PI / 4));
                arrowFromX2 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromLeftToRight)) + Math.PI / 4));
                arrowFromY2 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromLeftToRight)) - Math.PI / 4));
            }
        } else {
            fromX = fromRightX;
            fromY = fromRightY;
            toX = toLeftX;
            toY = toLeftY;
            arrowToX = toX - ((toX - fromX) / 2);
            arrowToY = toY - ((toY - fromY) / 2);
            if(toY > fromY){
                arrowFromX1 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromRightToLeft)) + Math.PI / 4));
                arrowFromY1 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromRightToLeft)) + Math.PI / 4));
                arrowFromX2 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromRightToLeft)) - Math.PI / 4));
                arrowFromY2 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromRightToLeft)) - Math.PI / 4));
            }
            else {
                arrowFromX1 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromRightToLeft)) - Math.PI / 4));
                arrowFromY1 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromRightToLeft)) + Math.PI / 4));
                arrowFromX2 = arrowToX - (int) (7 * Math.sin((Math.asin((toX - fromX) / fromRightToLeft)) + Math.PI / 4));
                arrowFromY2 = arrowToY - (int) (7 * Math.cos((Math.acos((toY - fromY) / fromRightToLeft)) - Math.PI / 4));
            }

        }

        Color color = g.getColor();
        g.setColor(Color.BLACK);

        g.drawLine(fromX, fromY, toX, toY);
        g.drawLine(arrowFromX1, arrowFromY1, arrowToX, arrowToY);
        g.drawLine(arrowFromX2, arrowFromY2, arrowToX, arrowToY);
        g.setColor(color);
    }
}

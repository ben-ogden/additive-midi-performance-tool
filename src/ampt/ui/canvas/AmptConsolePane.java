package ampt.ui.canvas;

import ampt.ui.tempo.TempoEvent;
import ampt.ui.tempo.TempoListener;
import java.awt.Color;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * The AMPTConsolePane provides styled logging for AMPT devices.
 *
 * @author Ben
 */
public class AmptConsolePane extends JTextPane implements TempoListener {

	private static final long serialVersionUID = 827314424200554556L;

	// default text color
    private static final Color DEFAULT_COLOR = Color.DARK_GRAY;
    private static final Color TEMPO_COLOR = Color.RED;

    // maximum document size for the console pane (in characters)
    //    32,000 = 400 x 80 character lines
    private static final int DOC_MAX_SIZE = 32000;

    // number of chars to cut as document reaches max size
    //    1600 = 20 x 80 char lines
    private static final int DOC_TRIM_SIZE = 800;

    /**
     * Create a new AmptConsolePane.
     */
    public AmptConsolePane() {

        super();

        // hide the caret
        setCaretColor(getBackground());

        // to disable keyboard input
        this.setFocusable(false);

        /*
         * Lister for the popup menu
         */
        ActionListener popupActionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // clear is the only action, so just clear the pane
                setText("");
            }
        };

        MenuItem menuItem = new MenuItem("Clear");
        menuItem.addActionListener(popupActionListener);

        final PopupMenu popupMenu = new PopupMenu();
        popupMenu.add(menuItem);
        this.add(popupMenu);

        /*
         * Mouse listener for this pane to enable popup menu
         */
        addMouseListener(
                new MouseAdapter() {

                    private void maybeShowPopup(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                            popupMenu.show(e.getComponent(),
                                    e.getX() + 5, e.getY() + 5);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        maybeShowPopup(e);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        maybeShowPopup(e);
                    }
                }
        ); // addMouseListener

    } // default constructor

    /**
     * Append text to the console.
     * 
     * @param msg the message to append.
     */
    public void append(String msg) {
        append(msg, DEFAULT_COLOR);
    }

    /**
     * Append text in the given color to the console.
     *
     * @param msg the message to append.
     * @param color the color of text to display.
     */
    public void append(String msg, Color color) { // better implementation--uses

        StyleContext sc = StyleContext.getDefaultStyleContext();

        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                StyleConstants.Foreground, color);

        Document doc = getDocument();

        // since multiple thread can be writing this console, we need to
        // synchronize on this document
        synchronized(doc) {

            if(doc.getLength() > DOC_MAX_SIZE) {
                try {
                    doc.remove(0, DOC_TRIM_SIZE);
                } catch (BadLocationException ex) {
                    // do nothing
                }
            }

            // move caret to end of document
            setCaretPosition(doc.getLength());

            // set style attribute for new text
            setCharacterAttributes(aset, false);

            // insert text at caret
            replaceSelection(msg);
        }
    }

    /**
     * Get an outputstream to write messages to this AmptConsolePane. The text
     * will be printed in the specified color.
     *
     * @return a PrintStream
     */
    public PrintStream getPrintStream(Color color) {
        // for now each caller has their own printstream
        return new PrintStream(new ConsoleOutputStream(color));
    }

    /**
     * Get an outputstream to write messages to this AmptConsolePane.
     *
     * @return a PrintStream
     */
    public PrintStream getPrintStream() {
        return getPrintStream(DEFAULT_COLOR);
    }

    /**
     * AmptConsolePane is a TempoListener. This method used to catch TempoEvent
     * notifications and display them in the console.
     *
     * @param event a TempoEvent
     */
    @Override
    public void tempoChanged(TempoEvent event) {
        append(String.format("Tempo set to %.1f BPM\n", event.getTempo()),
                TEMPO_COLOR);
    }

    /**
     * ConsoleOutPutStream overrides the write methods of OutputStream to
     * instead call this console's append method.
     */
    private class ConsoleOutputStream extends OutputStream {
        
        private Color _color = DEFAULT_COLOR;

        private ConsoleOutputStream(Color c) {
            _color = c;
        }

        @Override
        public void write(final int b) throws IOException {
          append(String.valueOf((char) b), _color);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
          append(new String(b, off, len), _color);
        }

        @Override
        public void write(byte[] b) throws IOException {
          write(b, 0, b.length);
        }
  }

}

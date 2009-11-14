package examples.logging;

import java.awt.Color;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * An example of a JTextPane that supports appended writes and styled
 * output.
 *
 * @author Ben
 */
public class AmptConsolePane extends JTextPane {

    public AmptConsolePane() {

        super();

        // hide the caret
        setCaretColor(getBackground());


        // only popup option is to clear the console
        ActionListener popupActionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // clear the console pane
                setText("");
            }
        };
        MenuItem menuItem = new MenuItem("Clear");
        menuItem.addActionListener(popupActionListener);

        final PopupMenu popupMenu = new PopupMenu();
        popupMenu.add(menuItem);
        this.add(popupMenu);

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
                });

    }

    public void append(String msg, Color color) { // better implementation--uses

        StyleContext sc = StyleContext.getDefaultStyleContext();

        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                StyleConstants.Foreground, color);

        // move caret to end of document
        setCaretPosition(getDocument().getLength());

        // set style attribute for new text
        setCharacterAttributes(aset, false);

        // insert text at caret
        replaceSelection(msg);
    }

    public static void main(String args[]) throws Exception {

        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        final AmptConsolePane console = new AmptConsolePane();

        // setup gui
        JFrame f = new JFrame("AmptConsolePane Example");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane(new JScrollPane(console));
        f.setSize(400, 200);
        f.setVisible(true);

        // test the pane
        Timer t = new Timer(300, new ActionListener() {

            Random rand = new Random();

            @Override
            public void actionPerformed(ActionEvent evt) {
                String timeString = fmt.format(new Date());

                Color color = new Color(rand.nextInt(255),
                        rand.nextInt(255), rand.nextInt(255));

                console.append("\n" + timeString, color);

            }
            SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
        });
        t.start();

    }
}



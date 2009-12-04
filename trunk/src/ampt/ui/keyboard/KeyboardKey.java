package ampt.ui.keyboard;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * Abstract class which represents a Keyboard Key. This class is not meant to be
 * explicitly initialized, and should only be accessed through it's subclasses.
 * This represents the note that the keyboard key represents, and contains a
 * mouse listener so the color of the button can be changed.
 * 
 * This class subclasses JButton to have the correct behavior.
 * 
 * ********Revisions******** 
 * 08/13/09 [Chris] Added displaying of key binding on each key
 * @author Christopher S. Redding 
 */
public abstract class KeyboardKey extends JComponent implements MouseListener {

    private static final long serialVersionUID = 1L;
    private final int note;
    protected char keyBinding = ' ';
    private Receiver keyboardReceiver;
    protected boolean pressed = false;

    /**
     * Constructor to set the note number that this key represents. Also sets up
     * the mouse listener for changing the color of the key
     *
     * @param note
     *            The note that this key represents
     * @param keyboardReceiver
     *            The receiver to send MIDI messages
     */
    public KeyboardKey(final int note, Receiver keyboardReceiver) {
        super();
        this.note = note;
        this.keyboardReceiver = keyboardReceiver;
        this.addMouseListener(this);
    }

    /**
     * Constructor to set the note number that this key represents. Also sets up
     * the mouse listener for changing the color of the key
     *
     * @param note
     *            The note that this key represents
     * @param keyBinding
     *            The key to bind this button to
     * @param keyboardReceiver
     *            The receiver to send MIDI messages
     */
    public KeyboardKey(final int note, char keyBinding, Receiver keyboardReceiver) {

        this(note, keyboardReceiver);

        this.keyBinding = keyBinding;

        InputMap inputMap = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        String upperKeyBinding = String.valueOf(Character.toUpperCase(keyBinding));
        switch(keyBinding){
            case ';':
                upperKeyBinding = "SEMICOLON";
                break;
            case ',':
                upperKeyBinding = "COMMA";
                break;
            case '.':
                upperKeyBinding = "PERIOD";
                break;
            case '/':
                upperKeyBinding = "SLASH";
                break;
        }
        inputMap.put(KeyStroke.getKeyStroke("pressed " + upperKeyBinding),
                "pressed");
        inputMap.put(KeyStroke.getKeyStroke("released " + upperKeyBinding),
                "released");
        ActionMap actionMap = this.getActionMap();
        actionMap.put("pressed", new AbstractAction() {

			private static final long serialVersionUID = -1951129695473657146L;

			@Override
            public void actionPerformed(ActionEvent arg0) {
                if (!pressed) {
                    pressed = true;
                    repaint();
                    setNoteOn();
                }
            }
        });

        actionMap.put("released", new AbstractAction() {

			private static final long serialVersionUID = -1524625805326778953L;

			@Override
            public void actionPerformed(ActionEvent e) {
                pressed = false;
                repaint();
                setNoteOff();
            }
        });

    }

    /**
     * Returns the note this key represents
     *
     * @return the note value
     */
    public int getNote() {
        return note;
    }

    /**
     * Mouse Listeners that are not used
     */
    @Override
    public void mouseClicked(final MouseEvent event) {
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
    }

    @Override
    public void mouseExited(final MouseEvent event) {
    }

    /**
     * Listener for when the button is pressed to change the color of the button
     */
    @Override
    public void mousePressed(final MouseEvent event) {
        pressed = true;
        this.repaint();
        this.setNoteOn();
    }

    /**
     * Listener for when the button is pressed to change the color of the button
     */
    @Override
    public void mouseReleased(final MouseEvent event) {
        pressed = false;
        this.repaint();
        this.setNoteOff();

    }

    /**
     * Sends a Note On message to the set receiver on the set channel.
     */
    public void setNoteOn() {
        ShortMessage msg = new ShortMessage();
        try {
            msg.setMessage(ShortMessage.NOTE_ON, note, 0);
        } catch (InvalidMidiDataException ex) {
            // this shouldn't happen
            throw new RuntimeException(ex);
        }
        keyboardReceiver.send(msg, -1);
    }

    /**
     * Sends a Note Off message to the set receiver on the set channel.
     */
    public void setNoteOff() {
        ShortMessage msg = new ShortMessage();
        try {
            msg.setMessage(ShortMessage.NOTE_OFF, note, 0);
         } catch (InvalidMidiDataException ex) {
            // this shouldn't happen
            throw new RuntimeException(ex);
        }
        keyboardReceiver.send(msg, -1);
    }
}

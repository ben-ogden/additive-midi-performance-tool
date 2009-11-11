package ampt.ui.keyboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.sound.midi.Receiver;

/**
 * This is a white key on the keyboard, and can be added to a JPanel just like
 * any other button.
 * 
 * @author Christopher S. Redding
 * 
 */
public class WhiteKey extends KeyboardKey {

    /*
     * Enum which represents the key type, left, center, or right, in refrence
     * to how many black keys are adjacent to this key.
     */
    public static enum KeyType {

        Left, Center, Right
    };
    private static final long serialVersionUID = 1L;
    public static final int KEY_WIDTH = 20;
    public static final int KEY_HEIGHT = 100;
    private KeyType keyType;

    /**
     * Constructor which sets the note the key represents, the key type, and the
     * preferred size of the key
     *
     * @param keyType
     *            The KeyType that this key is for. Options are Left, Center, or
     *            Right
     * @param note
     *            The note that this key represents
     * @param channel
     *            The channel to send MIDI messages on
     */
    public WhiteKey(KeyType keyType, int note, Receiver receiver) {
        super(note, receiver);

        this.keyType = keyType;
        this.setPreferredSize(new Dimension(KEY_WIDTH, KEY_HEIGHT));
    }

    /**
     * Constructor which sets the note the key represents, the key type, and the
     * preferred size of the key
     *
     * @param keyType
     *            The KeyType that this key is for. Options are Left, Center, or
     *            Right
     * @param note
     *            The note that this key represents
     * @param keyBinding
     *            The key to associate with this button
     * @param channel
     *            The channel to send MIDI messages on
     */
    public WhiteKey(KeyType keyType, int note, char keyBinding, Receiver receiver) {
        super(note, keyBinding, receiver);

        this.keyType = keyType;
        this.setPreferredSize(new Dimension(KEY_WIDTH, KEY_HEIGHT));
    }

    /**
     * Paints the border of the button, depending on if this is a left, right,
     * or center button.
     */
    @Override
    protected void paintBorder(Graphics g) {

        // Paint the border of the key depending on what type it is.
        switch (keyType) {

            case Left:
                g.drawLine(0, 0, 3 * KEY_WIDTH / 4, 0);
                g.drawLine(3 * KEY_WIDTH / 4, 0, 3 * KEY_WIDTH / 4, KEY_HEIGHT / 2);
                g.drawLine(3 * KEY_WIDTH / 4, KEY_HEIGHT / 2, KEY_WIDTH - 1,
                        KEY_HEIGHT / 2);
                g.drawLine(KEY_WIDTH - 1, KEY_HEIGHT / 2, KEY_WIDTH - 1,
                        KEY_HEIGHT - 1);
                g.drawLine(KEY_WIDTH - 1, KEY_HEIGHT - 1, 0, KEY_HEIGHT - 1);
                g.drawLine(0, KEY_HEIGHT - 1, 0, 0);
                break;
            case Center:
                g.drawLine(KEY_WIDTH / 4, 0, 3 * KEY_WIDTH / 4, 0);
                g.drawLine(3 * KEY_WIDTH / 4, 0, 3 * KEY_WIDTH / 4, KEY_HEIGHT / 2);
                g.drawLine(3 * KEY_WIDTH / 4, KEY_HEIGHT / 2, KEY_WIDTH - 1,
                        KEY_HEIGHT / 2);
                g.drawLine(KEY_WIDTH - 1, KEY_HEIGHT / 2, KEY_WIDTH - 1,
                        KEY_HEIGHT - 1);
                g.drawLine(KEY_WIDTH - 1, KEY_HEIGHT - 1, 0, KEY_HEIGHT - 1);
                g.drawLine(0, KEY_HEIGHT - 1, 0, KEY_HEIGHT / 2);
                g.drawLine(0, KEY_HEIGHT / 2, KEY_WIDTH / 4, KEY_HEIGHT / 2);
                g.drawLine(KEY_WIDTH / 4, KEY_HEIGHT / 2, KEY_WIDTH / 4, 0);
                break;
            case Right:
                g.drawLine(KEY_WIDTH / 4, 0, KEY_WIDTH - 1, 0);
                g.drawLine(KEY_WIDTH - 1, 0, KEY_WIDTH - 1, KEY_HEIGHT - 1);
                g.drawLine(KEY_WIDTH - 1, KEY_HEIGHT - 1, 0, KEY_HEIGHT - 1);
                g.drawLine(0, KEY_HEIGHT - 1, 0, KEY_HEIGHT / 2);
                g.drawLine(0, KEY_HEIGHT / 2, KEY_WIDTH / 4, KEY_HEIGHT / 2);
                g.drawLine(KEY_WIDTH / 4, KEY_HEIGHT / 2, KEY_WIDTH / 4, 0);
                break;
        }

    }

    /**
     * Paints the key on screen, setting the color by if the key has been
     * pressed or not.
     */
    @Override
    public void paintComponent(Graphics g) {
        Color color = g.getColor();
        if (pressed) {
            g.setColor(Color.GRAY);
        } else {
            g.setColor(Color.WHITE);
        }
        g.fillRect(KEY_WIDTH / 4, 0, KEY_WIDTH / 2, KEY_HEIGHT / 2);
        g.fillRect(0, KEY_HEIGHT / 2, KEY_WIDTH, KEY_HEIGHT / 2);

        switch (keyType) {
            case Left:
                g.fillRect(0, 0, KEY_WIDTH / 4, KEY_HEIGHT / 2);
                break;
            case Right:
                g.fillRect(3 * KEY_WIDTH / 4, 0, KEY_WIDTH / 4, KEY_HEIGHT / 2);
                break;
        }

        if (pressed) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.BLACK);
        }

        Font font = g.getFont();

        g.setFont(new Font(Font.DIALOG, Font.PLAIN, 8));

        FontMetrics fontMetrics = g.getFontMetrics();

        int xPos = KEY_WIDTH;
        xPos -= fontMetrics.charWidth(keyBinding);
        xPos /= 2;

        g.drawString(Character.toString(keyBinding), xPos, 3 * KEY_HEIGHT / 4);

        g.setFont(font);
        g.setColor(color);
    }
}

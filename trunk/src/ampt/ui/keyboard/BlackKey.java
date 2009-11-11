package ampt.ui.keyboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.sound.midi.Receiver;

/**
 * This is a black key on a keyboard, and can be added to a JPanel just like any
 * other button.
 * 
 * @author Christopher S. Redding
 * 
 */
public class BlackKey extends KeyboardKey {

    private static final long serialVersionUID = 1L;
    public static final int KEY_WIDTH = 10;
    public static final int KEY_HEIGHT = 50;

    /**
     * Constructor which sets the note of the key, and the preferred size of the
     * key.
     *
     * @param note
     *            The note that this key represents
     * @param channel
     *            The channel to send MIDI messages on
     */
    public BlackKey(int note, Receiver keyboardReceiver) {
        super(note, keyboardReceiver);

        this.setPreferredSize(new Dimension(KEY_WIDTH, KEY_HEIGHT));
    }

    /**
     * Constructor which sets the note of the key, and the preferred size of the
     * key.
     *
     * @param note
     *            The note that this key represents
     * @param keyBinding
     *            The key to bind this button to
     * @param channel
     *            The channel to send MIDI messages on
     */
    public BlackKey(int note, char keyBinding, Receiver keyboardReceiver) {
        super(note, keyBinding, keyboardReceiver);

        this.setPreferredSize(new Dimension(KEY_WIDTH, KEY_HEIGHT));
    }

    /**
     * Paints the border of the black key
     */
    @Override
    protected void paintBorder(final Graphics g) {
        g.drawLine(0, 0, KEY_WIDTH - 1, 0);
        g.drawLine(KEY_WIDTH - 1, 0, KEY_WIDTH - 1, KEY_HEIGHT - 1);
        g.drawLine(KEY_WIDTH - 1, KEY_HEIGHT - 1, 0, KEY_HEIGHT - 1);
        g.drawLine(0, KEY_HEIGHT - 1, 0, 0);
    }

    /**
     * Paints the black key, changing the color if the key is pressed.
     */
    @Override
    public void paintComponent(final Graphics g) {

        final Color color = g.getColor();
        if (pressed) {
            g.setColor(Color.GRAY);
        } else {
            g.setColor(Color.BLACK);
        }
        g.fillRect(0, 0, KEY_WIDTH, KEY_HEIGHT);



        g.setColor(Color.WHITE);

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

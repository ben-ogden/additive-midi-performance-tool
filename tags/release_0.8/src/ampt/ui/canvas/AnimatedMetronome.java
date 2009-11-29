package ampt.ui.canvas;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * this class demonstrates a simple metronome animation.
 *
 * @author robert
 */
public class AnimatedMetronome extends JPanel implements Runnable {

    // number of animation frames
    private static final int NUM_FRAMES = 20;
    private static final int FRAME_INTERVAL = 60 / NUM_FRAMES;

    private final AtomicBoolean started = new AtomicBoolean(false);

    //TODO - tempo needs to be float
    //private final AtomicInteger tempo = new AtomicInteger(60);

    private long tempoInterval = (long) (1000000000 / (100.0F / FRAME_INTERVAL));

    private final AtomicInteger position = new AtomicInteger(1);

    public AnimatedMetronome() {
        this.setPreferredSize(new Dimension(28, 90));
    }
    /*
     * @param tempo the desired tempo
     */
    public void setTempo(float tempo) {
        //this.tempo.set(tempo);
        tempoInterval = (long) (1000000000.0F / (tempo / FRAME_INTERVAL));
    }

    /*
     * @param g Graphics object associated with the JPanel
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(Color.WHITE);
        g2d.fill((new Rectangle2D.Float(2, 2, 60, 25)));

        g2d.setPaint(Color.GREEN);
        for(int i = 0; i < position.get(); i++) {
            g2d.fill((new Rectangle2D.Float(2 + (3 * i), 2, 3, 25)));
        }

        g2d.setPaint(Color.BLACK);
        g2d.draw(new Rectangle2D.Float(1, 1, 61, 26));
        g2d.drawLine(16, 2, 16, 4);
        g2d.drawLine(31, 2, 31, 7);
        g2d.drawLine(46, 2, 46, 4);

        g2d.setPaint(Color.RED);
        if (position.get() == 20) {
            g2d.fill((new Ellipse2D.Float(63, 1, 25, 25)));
        }
    }

    /*
     *
     */
    public void start() {
        if (started.get())
            ;//throw exception
        else {
            (new Thread(this)).start();
            started.set(true);
        }
    }

    /*
     *
     */
    public void stop() {
        if (!started.get())
            ; //throw exception
        else
            started.set(false);
    }

    /*
     *
     */
    @Override
    public void run() {
        // long nextTick = (long) (System.nanoTime() + (1000000000.0F / (tempo / 3.0F)));
        long nextTick = (long) (System.nanoTime() + tempoInterval);
        while (started.get()) {
            try {
                Thread.sleep(0, 1);
            } catch (InterruptedException ie) {
            }
            if(System.nanoTime() >= nextTick) {
                if(position.get() == 20)
                    position.set(1);
                else
                    position.incrementAndGet();
                repaint();
                nextTick += tempoInterval;
            }
        }
    }

}

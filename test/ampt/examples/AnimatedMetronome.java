package ampt.examples;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * this class demonstrates a simple metronome animation.
 *
 * @author robert
 */
public class AnimatedMetronome extends JPanel implements Runnable {

    private final AtomicBoolean started = new AtomicBoolean(false);
    private final AtomicInteger tempo = new AtomicInteger(60);
    private final AtomicInteger position = new AtomicInteger(1);

    /*
     * @param tempo the desired tempo
     */
    public void setTempo(int tempo) {
        (this.tempo).set(tempo);
    }

    /*
     * @param g Graphics object associated with the JPanel
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(Color.WHITE);
        g2d.fill((new Rectangle2D.Float(20, 20, 40, 25)));

        g2d.setPaint(Color.BLACK);
        g2d.draw(new Rectangle2D.Float(19, 19, 41, 26));
        g2d.drawLine(29, 20, 29, 23);
        g2d.drawLine(40, 20, 40, 26);
        g2d.drawLine(51, 20, 51, 23);

        g2d.setPaint(Color.GREEN);
        for(int i = 0; i < position.get(); i++) {
            g2d.fill((new Rectangle2D.Float(20 + (2 * i), (40 - i), 2, 5 + i)));
        }

        g2d.setPaint(Color.RED);
        if (position.get() == 20) {
            g2d.fill((new Ellipse2D.Float(70, 15, 30, 30)));
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
        long nextTick = System.nanoTime() + (1000000000 / (tempo.get() / 3));
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
                nextTick += (1000000000 / (tempo.get() / 3));
            }
        }
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("Metronome Animation");
        frame.setSize(100, 100);
        AnimatedMetronome am = new AnimatedMetronome();
        frame.setContentPane(am);
        frame.setVisible(true);
        am.start();
    }

}

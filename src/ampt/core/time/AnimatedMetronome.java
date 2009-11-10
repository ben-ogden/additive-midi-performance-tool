package ampt.core.time;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * this class demonstrates a simple metronome animation.
 *
 * @author robert
 */
public class AnimatedMetronome extends JPanel implements Runnable {
    //modified by only one thread - does not need synchronization. Volatile
    //guarantees visibility.
    private volatile int position = 1;

    //the next three variable are guarded by "this"
    private long nextTick;
    private int tempo = 60;
    private boolean started = false;

    /*
     * @param tempo the desired tempo
     */
    public synchronized void setTempo(int tempo) {
        this.tempo = tempo;
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
        for(int i = 0; i < position; i++) {
            g2d.fill((new Rectangle2D.Float(20 + (2 * i), (40 - i), 2, 5 + i)));
        }

        g2d.setPaint(Color.RED);
        if (position == 20) {
            g2d.fill((new Ellipse2D.Float(70, 15, 30, 30)));
        }
    }


    public synchronized void start() {
        if (started)
            ;//throw exception
        else {
            nextTick = System.nanoTime() + (1000000000 / (tempo / 3));
            (new Thread(this)).start();
            started = true;
        }
    }

    public synchronized void stop() {
        if (!started)
            ; //throw exception
        else
            started = false;
    }

    @Override
    public synchronized void run() {
        while (started) {
            try {
                Thread.sleep(0, 1);
            } catch (InterruptedException ie) {
            }
            if(System.nanoTime() >= nextTick) {
                if(position == 20)
                    position = 1;
                else
                    position++;
                repaint();
                nextTick += (1000000000 / (tempo / 3));
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

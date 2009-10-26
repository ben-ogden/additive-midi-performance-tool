package ampt.examples.timing;

/**
 * This class implements a metronome. It is designed to work with threads,
 * like a monitor. The internal ticker thread advances the tickCounter value
 * at the appropriate time, according to tempo. One tick represents 1/32 of
 * a beat (or a 128th note) - so 32 ticks is a quarternote in a standard meter.
 * When a thread enters the play method the the timestamp determines at what
 * tick the thread is allowed to continue. The returned tickCount value can
 * be ignored, or used to calculate the timeStamp for a future note. The
 * metronome does not actually play a note. It simply delays a thread until the
 * appropriate tick.
 *
 * <p> This class currently uses System.nanoTime() - I'm thinking we need a
 * Clock interface that, like nanoTime(), returns a long representing a certain
 * number of time units since a fixed point. That way we can use whatever
 * system timer we like. The time() method of interface would replace calls
 * to System.nanoTime() in this class.
 *
 * <p> A potential problem with this and any other method of using Thread
 * methods like wait and sleep to time notes is that at best they can only
 * specify the soonest an action will occur.
 *
 * <p> for some reason the stop() method is broken.
 *
 * @author robert
 */
public class Metronome {
    protected long nextTickTime;
    protected long tickLength;
    protected int tickCount;
    protected Thread ticker;
    protected boolean STOP = false;

    public Metronome(int tempo) {
        float f = (60f/tempo)/32;
        tickLength = (long)(f * 1000000000);
    }

    public void start() {
        tickCount = 1;
        ticker = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if (STOP)
                        return;
                    tick();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        });

        nextTickTime = System.nanoTime() + tickLength;
        ticker.start();
    }

    public void stop() {
        STOP = true;
    }

    public synchronized long getTickCount() {
        return tickCount;
    }

    protected synchronized void tick() {
        long currentTime;
        if ((currentTime = System.nanoTime()) >= nextTickTime) {
            nextTickTime = currentTime + tickLength;
            tickCount++;
            notifyAll();
        }
    }

    public synchronized int play(int timeStamp) {
        while (!(tickCount >= timeStamp)) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        return tickCount;
    }
}
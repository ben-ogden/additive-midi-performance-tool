package ampt.examples.timing;

import ampt.core.time.Clock;

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
 *
 * @author robert
 */
public class Metronome {
    //since only one thread ever modifies tickCount, volatile will work
    private int tickCount = 1;

    protected long nextTickTime;
    protected long tickLength;
    protected Thread ticker;
    protected boolean STOP = false;
    protected Clock clock;

    public Metronome(int tempo) {
        float f = (60f/tempo)/32;
        tickLength = (long)(f * 1000);
        nextTickTime = 1;
        clock = Clock.getInstance(Clock.CLOCK_TYPE_STANDARD);
    }

    public void start() {
        ticker = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if (STOP)
                        return;
                    tick();
                    Thread.yield();
                }
            }
        });

        nextTickTime = clock.getTime() + tickLength;
        ticker.start();
    }

    public void stop() {
        STOP = true;
    }

    public synchronized int getTickCount() {
        return tickCount;
    }

    private synchronized void tick() {
        long currentTime = clock.getTime();
        if (currentTime >= nextTickTime) {
            nextTickTime = currentTime + tickLength;
            tickCount++;
            notifyAll();
        }
    }

    public synchronized void play(int timeStamp) {
        while (!(tickCount >= timeStamp)) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }
}
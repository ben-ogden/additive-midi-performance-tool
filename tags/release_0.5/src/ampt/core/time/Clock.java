package ampt.core.time;

/**
 * Clock is the base class for all Clocks in AMPT. Use getInstance to get an
 * instance of a clock.
 *
 * <p>Note:Currently the high resolution clock is only available for Windows
 * users. Users on other platforms will get the standard clock.
 *
 * <p>Note (by Rob): added an overloaded getInstance() that takes an int
 * parameter in order to select the nanoTime timer.
 *
 * TODO add native clocks for OSX and Linux.
 *
 * @author Ben
 */
public abstract class Clock {

    public static final int UNIT_SECONDS = 1;        // 10^0
    public static final int UNIT_MILLIS = 1000;      // 10^3
    public static final int UNIT_MICROS = 1000000;   // 10^6
    public static final int UNIT_NANOS = 1000000000; // 10^9

    public static final int CLOCK_TYPE_STANDARD = 1;
    public static final int CLOCK_TYPE_NANO = 2;

    /**
     * Get an instance of a clock. getInstance will attempt to load a high
     * resolution clock if possible, otherwise it will return the standard
     * clock.
     * 
     * @return a new Clock
     */
    public static Clock getInstance() {
        Clock clock;
        if (NativeWinClock.isAvailable()) {
            System.out.println("Using native clock");
            clock = new NativeWinClock();
        } else {
            System.out.println("Using standard clock");
            clock = new StandardClock();
        }
        return clock;
    }

    /**
     * Get an instance of a clock of the type specified in the
     * parameter. This method gives you the option of selecting
     * JRE dependent implementations only.
     *
     * @param type the type of clock desired
     * @return a new Clock of the requested type
     */
    public static Clock getInstance(int type) {
        Clock clock;
        if (type == CLOCK_TYPE_NANO) {
            System.out.println("Using nanoTime() clock");
            clock = new JavaNanoClock();
        } else if (type == CLOCK_TYPE_STANDARD) {
            System.out.println("Using standard clock");
            clock = new StandardClock();
        } else {
            clock = getInstance();
        }
        return clock;
    }

    // Stores the last time stampTime was call, like a stopwatch.
    protected long stampTime;

    //TODO - add javadoc

    public abstract long getTime();

    public abstract int getDefaultUnit();

    public abstract void stampTime();

    public abstract long getElapsedTime();

    public abstract long getElapsedTime(int unit);

}

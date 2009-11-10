package ampt.core.time;

/**
 * A high resolution native timer for windows users. NativeWinClock achieves
 * timer resolution higher than the default timer available in Java. This class
 * makes native calls to the windows timer using JNI.
 *
 * <p>Adapted from examples by Andrew Mulholland and Glen Murphy, 2003.
 *
 * @author Ben
 */
public class NativeWinClock extends Clock {

    // indicates availability of native timer
    private static boolean available;

    // The frequency of the native system timer.
    private long frequency;


    /*
     * Attempt to load the WinClock library and record whether or
     * not the library is available. 
     */
    static {
        try {
            System.loadLibrary("AmptWinClock");
            available = true;
        } catch (UnsatisfiedLinkError ule) {
            // do nothing
        } catch (SecurityException se) {
            // do nothing
        }
    }

    /**
     * Check if the windows native timer is available. This means the caller
     * is on a Windows platform and the JNI dependencies such as the required
     * .dll file is present.
     *
     * @return true if the timer is available for use, otherwise false
     */
    public static boolean isAvailable() {
        return available;
    }


    /*
     * JNI call to determine the frequency of the system timer.
     */
    private native long getFrequency();

    /*
     * JNI call to get the current counter of the system timer.
     */
    private native long getCounter();


    /**
     * Create a new instance of NativeWinClock. Callers should use
     * Clock.getInstance().
     */
    protected NativeWinClock() {
        frequency = getFrequency();
    }

    @Override
    public long getTime() {
        return (getCounter() * UNIT_MICROS) / frequency;
    }

    @Override
    public int getDefaultUnit() {
        return UNIT_MICROS;
    }

    @Override
    public void stampTime() {
        stampTime = getCounter();
    }

    @Override
    public long getElapsedTime() {
        return ((getCounter() - stampTime) * UNIT_MICROS) / frequency;
    }

    @Override
    public long getElapsedTime(int unit) {
        return ((getCounter() - stampTime) * unit) / frequency;
    }
}

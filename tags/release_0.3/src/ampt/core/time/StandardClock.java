package ampt.core.time;

/**
 * StandardClock is the default clock based on System.currentTimeMillis. It is
 * not considered to have reliable accuracy for high resolution timing but is
 * adequate for coarser grained timing.
 *
 * @author Ben
 */
public class StandardClock extends Clock {

    @Override
    public long getTime() {
        return System.currentTimeMillis();
    }

    @Override
    public int getDefaultUnit() {
        return UNIT_MILLIS;
    }

    @Override
    public void stampTime() {
        stampTime = System.currentTimeMillis();
    }

    @Override
    public long getElapsedTime() {
        return System.currentTimeMillis() - stampTime;
    }

    @Override
    public long getElapsedTime(int unit) {
        return ((System.currentTimeMillis() - stampTime) * unit) / UNIT_MILLIS;
    }
}

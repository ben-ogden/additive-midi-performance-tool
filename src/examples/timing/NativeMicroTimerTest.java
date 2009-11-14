package examples.timing;

import ampt.core.time.Clock;

/**
 * This test shows the improved accuracy of the native clock. A message is
 * printed as each second passes. The error, in microseconds, is displayed as
 * well. (1 microsecond = 1000 millis)
 *
 * <p>I am seeing accuracy within 1400 microseconds (1.4 millis), often below
 * 1 millisecond. Note that the 1 ms call to Thread.sleep(1) is probably
 * responsible for much of this delay.
 * 
 * @author Ben
 */
public class NativeMicroTimerTest {

    public static void main(String[] args) throws InterruptedException {

        long elapsedTime;
        int counter = 0;
        int secondsCounter = 1;

        Clock clock = Clock.getInstance();
        clock.stampTime();

        while (secondsCounter < 11) // run for 10 seconds
        {
            // get elasped time and stamp the clock
            elapsedTime = clock.getElapsedTime();
            clock.stampTime();

            // increase counter
            counter += elapsedTime;

            if (counter >= Clock.UNIT_MICROS) // if 1 second elapsed
            {
                System.out.println(secondsCounter + " second(s), error: " + (counter-Clock.UNIT_MICROS) + " micros");
                counter = 0;
                secondsCounter++;
            }

            // give other threads a chance
           Thread.sleep(1);
        }
    }
}

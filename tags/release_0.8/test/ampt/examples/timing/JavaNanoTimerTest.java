package ampt.examples.timing;

import ampt.core.time.Clock;

/**
 * This test is designed as a direct comparison to Ben's test of the
 * NativeWinClock, but using the JavaNanoClock. It too displays a message
 * after each second passes, as well as the error in microseconds;
 *
 * <p> Similar to Ben's results, this one has results ranging between less than
 * one millisecond up to around 1.7 - 1.8 milliseconds.
 * 
 * @author robert
 */
public class JavaNanoTimerTest {

    public static void main(String[] args) throws InterruptedException {

        long elapsedTime;
        int counter = 0;
        int secondsCounter = 1;

        Clock clock = Clock.getInstance(Clock.CLOCK_TYPE_NANO);
        clock.stampTime();

        while (secondsCounter < 11) // run for 10 seconds
        {
            // get elasped time and stamp the clock
            elapsedTime = clock.getElapsedTime();
            clock.stampTime();

            // increase counter
            counter += elapsedTime;

            if (counter >= Clock.UNIT_NANOS) // if 1 second elapsed
            {
                System.out.println(secondsCounter + " second(s), error: " + (counter-Clock.UNIT_NANOS) / 1000 + " micros");
                counter = 0;
                secondsCounter++;
            }

            // give other threads a chance
           Thread.sleep(1);
        }
    }
}

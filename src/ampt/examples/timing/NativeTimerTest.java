package ampt.examples.timing;

import ampt.core.time.*;

/**
 * A first example using the NativeWinClock. The program prints the number of
 * milliseconds that have passed according to the high resolution timer, at a
 * resolution of 1ms. The first value display is the high resolution timer, the
 * second value is from System.currentTimeMillis(). Note the improved resolution
 * of the NativeWinClock.
 *
 *
 * @author Ben
 */
public class NativeTimerTest {

    public static void main(String[] args) throws InterruptedException {

        long sysBase = System.currentTimeMillis();
        long natElapsed = 0;
        long last = 0;

        Clock clock = Clock.getInstance();
        // record time starting
        clock.stampTime();

        while(natElapsed < 100) {

            last = natElapsed;
            natElapsed = clock.getElapsedTime(Clock.UNIT_MILLIS);

            if(natElapsed-last > 0) {
                
                System.out.println(natElapsed + "," + (System.currentTimeMillis()-sysBase));
                
                // report any delays of 3ms or larger
                if(natElapsed-last > 2) {
                 System.out.println("*** big delay:" + (natElapsed-last));
                }

                // give other threads a chance to run
                Thread.yield();
            }
        }        
    }
}

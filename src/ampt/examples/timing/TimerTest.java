/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ampt.examples.timing;

import java.util.Timer;
import java.util.TimerTask;

/**
 * I'm not sure this shows us anything definitive. Just that you can't tell
 * if the timer events are firing when you want them.
 *
 * @author Ben
 */
public class TimerTest {

    TimerTask task;

    public TimerTest(int period) {
        task = new HelloTask();
        Timer timer = new Timer();
        // wait 20ms, then execute task every period ms
        timer.scheduleAtFixedRate(task, 20L, period);
    }

    public static void main(String args[]) {

        System.out.println("Starting...");

        // execute every 20 ms
        TimerTest test = new TimerTest(20);

        long base=System.currentTimeMillis();
        for (int r=0; r<20000; ++r)  {
          System.out.print(System.currentTimeMillis()-base);
          System.out.print(' ');
        }
        
        test.task.cancel();

        System.out.println("Finished.");
    }


    /**
     * HelloTask is a TimerTask that will print a message when it is executed.
     */
    class HelloTask extends TimerTask {

        public void run() {
            System.out.println("\n--Go Go TimerTask!--");
        }

    }

}

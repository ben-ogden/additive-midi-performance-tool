/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ampt.examples.timing;

/**
 * Run this test and examin the console output. The point here is that the
 * resolution of the system time is not ideal on some platforms. I am seeing
 * resolution of about 15-16ms.
 *
 * @author Ben Ogden
 */
public class TimingTest {
  public static void main(String[] args)
  {
    long base=System.currentTimeMillis();

    for (int r=0; r<2000; ++r)
    {
      System.out.print(System.currentTimeMillis()-base);
      System.out.print(' ');
    }
  }
}

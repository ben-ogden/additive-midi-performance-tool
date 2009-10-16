/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ampt.examples.jmusic;

import jm.music.rt.RTLine;
import jm.audio.Instrument;
import jm.music.data.Note;
import javax.swing.*;

/**
 *
 * @author Ben
 */
public class BassLine extends RTLine {
    private Note n = new Note(36, 0.5);
    private int pitch = 36;
    private int[] intervals = {0, 0, 0, 4, 7, 10, 12};
    private double panPosition = 0.5;

    /** Constructor */
    public BassLine (Instrument[] instArray) {
        super(instArray);
    }
    /**
    * Generate the next note when requested.
    */
    public synchronized Note getNote() {
        n.setPitch(pitch + intervals[(int)(Math.random() * intervals.length)]);
        n.setDynamic((int)(Math.random()* 80 + 45));
        n.setPan(panPosition);
        n.setRhythmValue((int)(Math.random() * 2 + 1) * 0.25);
        n.setDuration(n.getRhythmValue() * 0.9);
        return n;
    }

    /** Allow other classes to set the notes pan value */
    public void setPanValue(double p) {
        this.panPosition = p;
    }

    // added for control change
    public synchronized void externalAction(Object obj, int actionNumber){
        if(actionNumber == 1){
            JSlider slider = (JSlider)obj;
            double filterCutoff = (double)(slider.getValue() * 100);
            for(int i=0;i<inst.length;i++){
                double[] values = {filterCutoff};
                inst[i].setController(values);
            }
        }
    }
}

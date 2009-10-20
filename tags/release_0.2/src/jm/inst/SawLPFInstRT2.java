package jm.inst;

import jm.audio.Instrument;
import jm.audio.io.*;
import jm.audio.synth.*;
import jm.music.data.Note;
import jm.audio.AudioObject;

/**
 * A monophonic sawtooth waveform instrument implementation
 * which includes a static low pass filter.
 * @author Andrew Brown
 */

public final class SawLPFInstRT2 extends Instrument{
    //----------------------------------------------
    // Instance variables
    //----------------------------------------------
    private int sampleRate;
    private int filterCutoff;
    private int channels;
    private Filter filt;

    //----------------------------------------------
    // Constructor
    //----------------------------------------------
    public SawLPFInstRT2(){
        this(44100);
    }
    /**
     * Basic default constructor to set an initial
     * sampling rate and use a default cutoff.
     * @param sampleRate
     */
    public SawLPFInstRT2(int sampleRate){
        this(sampleRate, 1000);
    }

     /**
    *  Constructor that sets sample rate and the filter cutoff frequency.
     * @param sampleRate The number of samples per second (quality)
     * @param filterCutoff The frequency above which overtones are cut
     */
     public SawLPFInstRT2(int sampleRate, int filterCutoff){
        this(sampleRate, filterCutoff, 1);
    }

    /**
    *  Constructor that sets sample rate, filter cutoff frequency, and channels.
    * @param sampleRate The number of samples per second (quality)
    * @param filterCutoff The frequency above which overtones are cut
    * @param channels 1 = mono, 2 = stereo.
    */
     public SawLPFInstRT2(int sampleRate, int filterCutoff, int channels){
        this.sampleRate = sampleRate;
        this.filterCutoff = filterCutoff;
        this.channels = channels;
    }

    //----------------------------------------------
    // Methods
    //----------------------------------------------
    /**
     * Initialisation method used to build the objects that
     * this instrument will use and specify thier interconnections.
     */
    public void createChain(){
        Oscillator wt = new Oscillator(this,
            Oscillator.SAWTOOTH_WAVE, this.sampleRate, this.channels);
        filt = new Filter(wt, this.filterCutoff, Filter.LOW_PASS);
        Envelope env = new Envelope(filt,
             new double[] {0.0, 0.0, 0.02, 1.0, 0.2, 0.5, 0.8, 0.3, 1.0, 0.0});
        Volume vol = new Volume(env);
        StereoPan pan = new StereoPan(vol);
    }
    
    /** Changes the specified controller when called */
    public void setController(double[] controlValues){
        filt.setCutOff(controlValues[0]);
    }
}

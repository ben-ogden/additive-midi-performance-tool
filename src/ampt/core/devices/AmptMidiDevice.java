package ampt.core.devices;

import java.io.PrintStream;
import javax.sound.midi.MidiDevice;

/**
 * All AmptDevices should implement AmptMidiDevice.
 *
 * @author Ben
 */
public interface AmptMidiDevice extends MidiDevice {

    public boolean isMidiDebugEnabled();

    public void setMidiDebugEnabled(boolean midiDebug) ;
    
    public void setLogger(PrintStream out);

}

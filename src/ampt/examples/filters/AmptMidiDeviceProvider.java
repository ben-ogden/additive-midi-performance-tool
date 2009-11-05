package ampt.examples.filters;

import ampt.ui.keyboard.KeyboardDevice;
import java.util.ArrayList;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.spi.MidiDeviceProvider;

/**
 * This class is an example of how a MidiDeviceProvider could be implemented.
 * We make sure to include the midi devices that we are going to support, 
 * and by the spi, we can integrate them with the MidiSystem.
 *
 * @author Christopher
 */
public class AmptMidiDeviceProvider extends MidiDeviceProvider{

    private ArrayList<Info> infoList;

    /**
     * Constructor which creates the list of supported devices.
     */
    public AmptMidiDeviceProvider(){
        infoList = new ArrayList<Info>();

        // Add an info from each installed filter to the vector
        infoList.add(new ChordFilterDevice().getDeviceInfo());
        infoList.add(new KeyboardDevice().getDeviceInfo());
    }

    /**
     * Returns an array of MidiDevice.Info which contains the Info for each
     * supported device.
     * @return
     */
    @Override
    public Info[] getDeviceInfo() {
        return (Info[]) infoList.toArray(new Info[0]);
    }

    /**
     * returns the MidiDevice that is represented by info.
     * @param info - the info for the desired MidiDevice.
     * @return
     */
    @Override
    public MidiDevice getDevice(Info info) {
        MidiDevice device = new ChordFilterDevice();
        if(device.getDeviceInfo().getName().equals(info.getName())){
            return device;
        }
        device = new KeyboardDevice();
        if(device.getDeviceInfo().getName().equals(info.getName())){
            return device;
        }
        
        throw new IllegalArgumentException("The info object specified does not match the info object for a device supported by this MidiDeviceProvider ");
    }

}

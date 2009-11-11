package ampt.core.devices;

import ampt.ui.keyboard.KeyboardDevice;
import java.util.ArrayList;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.spi.MidiDeviceProvider;

/**
 * This is the MidiDeviceProvider for AMPT. All devices supported by AMPT should
 * be registered here.
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

        //TODO

        // Add an info from each installed filter to the vector
        infoList.add(new ChordFilterDevice().getDeviceInfo());
        infoList.add(new KeyboardDevice().getDeviceInfo());
    }

    /**
     * Returns an array of MidiDevice.Info which contains the Info for each
     * supported device.
     *
     * @return array of Info
     */
    @Override
    public Info[] getDeviceInfo() {
        return (Info[]) infoList.toArray(new Info[0]);
    }

    /**
     * Get the MidiDevice that is represented by info.
     *
     * @param info - the info for the desired MidiDevice.
     * @return a MidiDevice matching the provided Info
     */
    @Override
    public MidiDevice getDevice(Info info) {

        //TODO

        MidiDevice device = new ChordFilterDevice();
        if(device.getDeviceInfo().getName().equals(info.getName())){
            return device;
        }

        device = new KeyboardDevice();
        if(device.getDeviceInfo().getName().equals(info.getName())){
            return device;
        }
        
        throw new IllegalArgumentException("The info object specified does not match the info object for a device supported by this MidiDeviceProvider.");
    }

}

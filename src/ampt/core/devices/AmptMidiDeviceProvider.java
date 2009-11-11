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
     * @param info the info describing the desired MidiDevice.
     * @return a MidiDevice matching the provided Info
     */
    @Override
    public MidiDevice getDevice(Info info) {

        if(ChordFilterDevice.DEVICE_NAME.equals(info.getName())) {
            return new ChordFilterDevice();
        }

        if(KeyboardDevice.DEVICE_NAME.equals(info.getName())){
            return new KeyboardDevice();
        }

        throw new IllegalArgumentException("Device not supported by this MidiDeviceProvider.");
    }

}

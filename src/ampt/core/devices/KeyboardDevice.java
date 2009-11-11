package ampt.core.devices;

import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/**
 *
 * @author Christopher
 */
public class KeyboardDevice extends AmptDevice {

    public static final String DEVICE_NAME = "Software Keyboard";
    private static final String DEVICE_DESCRIPTION = "A software MIDI keyboard";

    private static final int OCTAVE_INTERVAL = 12;

    private int _channel;
    private int _velocity;
    private int _octave;

    public KeyboardDevice(){
        this(1, 93, 5);
    }

    public KeyboardDevice(int channel, int velocity, int octave){

        super(DEVICE_NAME, DEVICE_DESCRIPTION);

        _channel = channel;
        _velocity = velocity;
        _octave = octave;
    }

    public void setVelocity(int velocity){
        if(velocity < 0 || velocity > 127){
            throw new IllegalArgumentException("Invalid Velocity Value");
        }
        this._velocity = velocity;
    }

    public void setChannel(int channel){
        //TODO - does the numbering start at 1 or 0?
        if(channel < 0 || channel > 15){
            throw new IllegalArgumentException("Invalid Channel Number");
        }
        this._channel = channel;
    }

    public void setOctave(int octave){
        if(octave < 0 || octave > 9){
            throw new IllegalArgumentException("Invalid Octave Number");
        }
        this._octave = octave;
    }


    @Override
    protected void initDevice() {
        // nothing to do
    }

    @Override
    protected void closeDevice() {
        // nothing to do
    }

    /**
     * Returns a new Keyboard Receiver that is not yet bound to any
     * transmitters.
     *
     * @return a new ChordFilterReceiver
     */
    @Override
    protected Receiver getAmptReceiver() {
        return new KeyboardReceiver();
    }

    /**
     * Inner class that implements a receiver for a chord filter.  This is
     * where all of the actual filtering takes place.
     */
    public class KeyboardReceiver extends AmptReceiver {

        List<MidiMessage> messages = new ArrayList<MidiMessage>();

        @Override
        protected List<MidiMessage> filter(MidiMessage message, long timeStamp) throws InvalidMidiDataException {

            messages.clear();

            // scale the note value to appropriate octave
            // element 1 in message.getMessage is the same as msgNote.getData1() for a ShortMessage
            int note = _octave * OCTAVE_INTERVAL + (int)(message.getMessage()[1] & 0xFF);

            // use updated note value and user-selected channel and velocities
            ShortMessage msgNote = new ShortMessage();
            msgNote.setMessage(message.getStatus(), _channel, note, _velocity);

            messages.add(msgNote);
            return messages;
        }
    }

}

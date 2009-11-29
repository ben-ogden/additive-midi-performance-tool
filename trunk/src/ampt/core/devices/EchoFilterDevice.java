package ampt.core.devices;

import ampt.midi.note.Decay;
import ampt.midi.note.NoteValue;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/**
 * EchoFilterDevice is a TimedDevice that repeats MIDI messages received at a
 * given interval and decay. Currently, the class will echo all midi messages
 * received. This means that overlapping note off messages may truncate other
 * notes played on the same key. This may change in the future to a user
 * preference.
 *
 * @author Ben
 */
public class EchoFilterDevice extends TimedDevice {

    private static final String DEVICE_NAME = "Echo Filter";
    private static final String DEVICE_DESCRIPTION = "Repeats incoming notes";

    // the number of times to repeat the note
    private int _duration;

    // the distance between notes
    private NoteValue _interval;

    // how the velocity of repeated notes changes over time
    private Decay _decay;

    /**
     * Create a new EchoFilterDevice
     *
     */
    public EchoFilterDevice() {
        super(DEVICE_NAME, DEVICE_DESCRIPTION);

        _interval = NoteValue.HALF_NOTE;
        _decay = Decay.LINEAR;
        _duration = 5;
    }

    /**
     * Get the echo duration, which is the number of times to repeat the note.
     *
     * @return the duration
     */
    public int getDuration() {
        return _duration;
    }

    /**
     * Set the echo duration, which is the number of times to repeat the note.
     *
     * @param duration
     */
    public void setDuration(int duration) {
        if(duration < 0) {
            throw new IllegalArgumentException(
                    "Duration must be greater than zero.");
        }
        this._duration = duration;
    }

    /**
     *  Get the interval between between repeated notes
     *
     * @return the echo interval
     */
    public NoteValue getInterval() {
        return _interval;
    }

    /**
     * Set the interval between between repeated notes
     *
     * @param interval
     */
    public void setInterval(NoteValue interval) {
        this._interval = interval;
    }

    /**
     * Get the decay, which is how the velocity of repeated notes changes over
     * time.
     *
     * @return the decay
     */
    public Decay getDecay() {
        return _decay;
    }

    /**
     * Set the decay, which is how the velocity of repeated notes changes over
     * time.
     *
     * @param decay
     */
    public void setDecay(Decay decay) {
        _decay = decay;
    }

    @Override
    protected void initDevice() throws MidiUnavailableException {
        // nothing to do
    }

    @Override
    protected void closeDevice() {
        // nothing to do
    }

    /**
     * Returns a new Echo Filter Receiver that is not yet bound to any
     * transmitters.
     *
     * @return a new EchoilterReceiver
     */
    @Override
    protected Receiver getAmptReceiver() throws MidiUnavailableException {
        return new EchoFilterReceiver();
    }
    
    /**
     * Inner class that implements a receiver for an echo filter.  This is
     * where all of the actual filtering takes place.
     */
    public class EchoFilterReceiver extends AmptReceiver {

        @Override
        protected void filter(MidiMessage message, long timeStamp) throws InvalidMidiDataException {

            // send all messages received immediately
            sendNow(message);

            // schedule the repeated notes only if duration is greater than zero
            if(_duration < 1) {
                return;
            }

            // only repeat short messages
            if(!(message instanceof ShortMessage)) {
                return;
            }

            ShortMessage msg = (ShortMessage) message;
            int command = msg.getCommand();
            // only support note on/off, etc...
            if(command < 128 || command > 224) {
                return;
            }
            int channel = msg.getChannel();
            int note = msg.getData1();
            int velocity = msg.getData2();

            float milliDelay = (60000 / (_tempo * _interval.getNotesPerBeat()));

            int[] velocities;
            if(ShortMessage.NOTE_ON == command) {
                velocities = calculateDecay(velocity);
            } else {
                velocities = new int[_duration];
            }

            // schedule each note
            for(int i = 0; i < _duration; i++) {
                ShortMessage schedNote = new ShortMessage();
                schedNote.setMessage(command, channel, note, velocities[i]);
                sendLater(schedNote, (long) milliDelay * (i+1));
            }
        }

        /*
         * Build array of note velocities, decaying over time
         *
         * TODO this should be refined/optimized
         */
        private int[] calculateDecay(int velocity) {

            int[] decayedVelocities = new int[_duration];

            if (Decay.NONE.equals(_decay)) {
                for(int i = 0; i < _duration; i++) {
                    decayedVelocities[i] = velocity;
                }
            } else if (Decay.LINEAR.equals(_decay)) {
                int decayAmt = velocity / _duration;
                for(int i = 0; i < _duration; i++) {
                    decayedVelocities[i] = velocity;
                    velocity -= decayAmt;
                }
            }
            else if (Decay.EXPONENTIAL.equals(_decay)) {

                // y = 1 / 2 ^ (rd)

                int r = 1; // decay factor - this value could be another user option
                double increment = 1.0 / _duration;
                double d;
                double v = velocity;
                for(int i = 1; i <= _duration; i++) {
                    d = i * increment;
                    v *= (1 / (Math.pow(2, (r * d))));
                    decayedVelocities[i-1] = (int) v;
                }
            }
            else if (Decay.LOGARITHMIC.equals(_decay)) {

                // y = 0.5 * log(-rd+1)+1
                
                double increment = 1.0 / _duration;
                double d;
                double r = 0.5; // decay factor
                double v = velocity;
                for(int i = 1; i <= _duration; i++) {
                    d = i * increment;
                    v *= (0.5 * Math.log1p(-1 * d) + 1);
                    if(v < 0) {
                        v = 0;
                    }
                    decayedVelocities[i-1] = (int) v;
                }
            }
            return decayedVelocities;
        }

        
    } // EchoFilterReceiver

}

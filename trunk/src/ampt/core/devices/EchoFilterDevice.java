package ampt.core.devices;

import ampt.midi.note.Decay;
import ampt.midi.note.NoteValue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/**
 *
 * @author Ben
 */
public class EchoFilterDevice extends AmptDevice {

    private static final String DEVICE_NAME = "Echo Filter";
    private static final String DEVICE_DESCRIPTION = "Echoes incoming notes";

    // the number of times to repeat the note
    private int _duration;

    // the distance between notes
    private NoteValue _interval;

    // how the velocity of repeated notes changes over time
    private Decay _decay;


    //TODO move this to new class AmptScheduledDevice if this works
    //TODO is this the best pool size?
    private ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(10);

    /**
     * Create a new EchoFilterDevice
     *
     */
    public EchoFilterDevice() {
        super(DEVICE_NAME, DEVICE_DESCRIPTION);

        _interval = NoteValue.EIGHTH_NOTE;
        _duration = 4;
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

            // schedule the repeated notes if duration is greater than zero
            if(_duration < 1) {
                return;
            }

            // TODO - make this global
            float tempo = 120; // BPM
            
            float milliDelay = (60000 / (tempo * _interval.getNotesPerBeat()));

            ShortMessage msg = (ShortMessage) message;
            int command = msg.getCommand();
            int channel = msg.getChannel();
            int note = msg.getData1();
            int velocity = msg.getData2();

            int[] velocities = calculateDecay(velocity);

            // schedule each note
            for(int i = 0; i < _duration; i++) {
                ShortMessage schedNote = new ShortMessage();
                schedNote.setMessage(command, channel, note, velocities[i]);
                executor.schedule(
                    new EchoNoteTask(schedNote),
                    (long) milliDelay * (i+1),
                    TimeUnit.MILLISECONDS);
            }

        }

        /*
         * Build array of note velocities, decaying over time
         *
         * TODO this should be optimized
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

        /**
         * Each note is a scheduled task.
         */
        class EchoNoteTask implements Runnable {

            private MidiMessage _message;
            
            EchoNoteTask(MidiMessage message) {
                _message = message;
            }

            @Override
            public void run() {
                sendNow(_message);
            }
        }
        
    } // EchoFilterReceiver

}

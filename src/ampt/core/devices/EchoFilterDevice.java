package ampt.core.devices;

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

    //TODO - add decay options: linear, logarithmic, exponential

    // the number of times to repeat the note
    private int _duration;

    // the distance between notes
    private NoteValue _interval;


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

    public int getDuration() {
        return _duration;
    }

    public void setDuration(int duration) {
        if(duration < 0) {
            throw new IllegalArgumentException(
                    "Duration must be greater than zero.");
        }
        this._duration = duration;
    }

    public NoteValue getInterval() {
        return _interval;
    }

    public void setInterval(NoteValue interval) {
        this._interval = interval;
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

            // schedule the repeated notes

            // TODO - make this global
            float tempo = 120; // BPM
            
            float milliDelay = (60000 / (tempo * _interval.getNotesPerBeat()));

            ShortMessage msg = (ShortMessage) message;
            int command = msg.getCommand();
            int channel = msg.getChannel();
            int note = msg.getData1();
            int velocity = msg.getData2();

            int decay = velocity / _duration;

            for(int i = 1; i <= _duration; i++) {

                ShortMessage schedNote = new ShortMessage();
                schedNote.setMessage(command, channel, note, velocity);

                velocity -= decay;

                executor.schedule(
                    new EchoNoteTask(schedNote),
                    (long) milliDelay * i,
                    TimeUnit.MILLISECONDS);
            }

        }


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

        
    }

}

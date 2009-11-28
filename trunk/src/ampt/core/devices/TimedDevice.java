package ampt.core.devices;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.sound.midi.MidiMessage;

/**
 * TimedDevice provides additional implementation beyond a standard AmptDevice.
 * Any devices requiring scheduling or tempo can extend TimedDevice to access
 * a global tempo and scheduling. Subclasses are provide the sendNow and
 * sendLater methods for scheduling MIDI message events.
 *
 * @author Ben
 */
public abstract class TimedDevice extends AmptDevice {

    // the default size for thread pool
    private static final int DEFAULT_POOL_SIZE = 10;

    // the scheduler for this timed device
    private ScheduledExecutorService _executor;

    // the tempo, in BPM
    protected float _tempo = 120;

    /**
     * Create a new TimedDevice. The default thread pool size is used.
     *
     * @param name the name of the device
     * @param description the device description
     */
    public TimedDevice(String name, String description) {
        this(name, description, DEFAULT_POOL_SIZE);
    }

    /**
     * Create a new TimedDevice. This constructor allows the caller to specify
     * the desired size of the thread pool.
     *
     * @param name the name of the device
     * @param description the device description
     * @param poolSize the size of the thread pool used for scheduling
     */
    public TimedDevice(String name, String description, int poolSize) {
        super(name, description);
        _executor = new ScheduledThreadPoolExecutor(poolSize);
    }

    /**
     * Get the tempo the device is using.
     *
     * @return the tempo
     */
    public float getTempo() {
        return _tempo;
    }

    /**
     * Set the tempo for the device to use.
     *
     * @param tempo the tempo
     */
    public void setTempo(float _tempo) {
        this._tempo = _tempo;
    }

    /**
     * Schedule a MIDI message to be send millDelay milliseconds in the future.
     *
     * @param message the MIDI message to send
     * @param milliDelay number of milliseconds from now to play the message
     */
    protected void sendLater(MidiMessage message, long milliDelay) {
        _executor.schedule(
                new TimedNoteTask(message),
                milliDelay,
                TimeUnit.MILLISECONDS);
    }

    /**
     * Each note is a scheduled task.
     */
    class TimedNoteTask implements Runnable {

        private MidiMessage _message;

        TimedNoteTask(MidiMessage message) {
            _message = message;
        }

        @Override
        public void run() {
            sendNow(_message);
        }
    }
}

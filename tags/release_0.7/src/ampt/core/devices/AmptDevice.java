package ampt.core.devices;

import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

/**
 * A base class for all AMPT Midi devices.
 *
 * @author Ben Ogden
 */
public abstract class AmptDevice implements AmptMidiDevice {

    // Each device has a AmptDeviceInfo describing that device
    private final AmptDeviceInfo _deviceInfo;

    // true if the AmptDevice is currently in use, otherwise false
    private boolean _isOpen;

    // Each device has a list of receivers and transmitters. Any methods
    // operating on these lists must be synchronized.
    private final List<Receiver> _receivers =
            Collections.synchronizedList(new LinkedList<Receiver>());
    private final List<Transmitter> _transmitters =
            Collections.synchronizedList(new LinkedList<Transmitter>());

    // A file or interactive log to which devices can send debug or error messages.
    private PrintStream _logger;

    // If this device should write debug for incoming MIDI messages
    private boolean _midiDebugEnabled = false;

    /**
     * Create a new AmptDevice. This constructor should be called by classes
     * that extend AmptDevices.
     *
     * @param name
     * @param description
     */
    public AmptDevice(String name, String description) {
        _deviceInfo = new AmptDeviceInfo(name, description, this.getClass());
    }

    /**
     * initDevice is called each time a device is opened. All classes extending
     * AmptDevice should implement this method.
     */
    protected abstract void initDevice() throws MidiUnavailableException;

    /**
     * closeDevice is called each time a device is closed. All classes extending
     * AmptDevice should implement this method.
     */
    protected abstract void closeDevice();

    /**
     * getAmptReceiver is used for implementing classes to provide a their own
     * custom implementation of Receiver to AmptDevice when getReceiver() is
     * called. All classes extending AmptDevice should implement this method.
     *
     * @return a new Receiver
     */
    protected abstract Receiver getAmptReceiver() throws MidiUnavailableException;

    /**
     * An application opening a device explicitly with this call has to close 
     * the device by calling close(). This is necessary to release system 
     * resources and allow applications to exit cleanly.
     *
     * <p>Note: Classes extending AmptDevice should implement initDevice rather
     * than providing an alternate implementation for open().
     *
     * @throws MidiUnavailableException if the device is already open
     */
    @Override
    public void open() throws MidiUnavailableException {

        // can't open a device twice
        if (_isOpen) {
            throw new MidiUnavailableException(_deviceInfo.getName() +
                    " is already in use.");
        }

        // initialize the device
        _isOpen = true;

        // optional implementation provided by subclasses
        initDevice();
    }

    /**
     * Closes the device, indicating that the device should now release any 
     * system resources it is using. All Receiver and Transmitter instances open
     * from this device are closed. This includes instances retrieved via MidiSystem.
     * 
     * <p>Note: Classes extending AmptDevice should implement closeDevice rather
     * than providing an alternate implementation for close().
     * 
     */
    @Override
    public void close() {

        // close any open receivers
        synchronized(_receivers) {
            for (Receiver r : _receivers.toArray(new Receiver[0])) {
                r.close();
            }
            _receivers.clear();
        }

        // close any open transmitters
        synchronized(_transmitters) {
            for (Transmitter t : _transmitters.toArray(new Transmitter[0])) {
                t.close();
            }
            _transmitters.clear();
        }

        // implementation provided by subclasses
        closeDevice();

        _isOpen = false;
    }

    /**
     * Connect a transmitter (output) from this MidiDevice to a receiver (input)
     * of the given MidiDevice.
     *
     * @param device the MidiDevice to connect to
     * @throws MidiUnavailableException if the connection cannot be made due to
     *                    either of the devices not being open or if there are
     *                    no additional transmitters or receivers to make the
     *                    connection
     */
    public void connectTo(MidiDevice anotherDevice) throws MidiUnavailableException {
        this.getTransmitter().setReceiver(anotherDevice.getReceiver());
    }

    /**
     * Reports whether the device is open.
     *
     * @return true if the device is open, otherwise false
     */
    @Override
    public boolean isOpen() {
        return _isOpen;
    }

    /**
     * Obtains the maximum number of MIDI IN connections available on this MIDI
     * device for receiving MIDI data. By default, AMPT devices have an
     * unlimited number of receivers. 
     * 
     * <p>Subclasses with specific limits on the number of receivers should 
     * override this method.
     *
     * @return maximum number of MIDI IN connections, or -1 if an unlimited
     * number of connections is available.
     */
    @Override
    public int getMaxReceivers() {
        return -1;
    }

    /**
     * Obtains the maximum number of MIDI OUT connections available on this
     * MIDI device for transmitting MIDI data. By default, AMPT devices have an
     * unlimited number of transmitters.
     *
     * <p>Subclasses with specific limits on the number of receivers should
     * override this method.
     *
     * @return maximum number of MIDI OUT connections, or -1 if an unlimited
     * number of connections is available.
     */
    @Override
    public int getMaxTransmitters() {
        return -1;
    }

    /**
     * Returns all currently active, non-closed receivers connected with this
     * MidiDevice. A receiver can be removed from the device by closing it.
     *
     * @return an unmodifiable list of the open receivers
     */
    @Override
    public List<Receiver> getReceivers() {
        return Collections.unmodifiableList(_receivers);
    }

    /**
     * Returns all currently active, non-closed transmitters connected with this
     * MidiDevice. A transmitter can be removed from the device by closing it.
     *
     * @return an unmodifiable list of the open transmitters
     */
    @Override
    public List<Transmitter> getTransmitters() {
        return Collections.unmodifiableList(_transmitters);
    }

    /**
     * Obtains a MIDI IN receiver through which the AMPT device may receive
     * MIDI data. The returned receiver must be closed when the application has
     * finished using it. Obtaining a Receiver with this method does not open
     * the device. To be able to use the device, it has to be opened explicitly
     * by calling open(). Also, closing the Receiver does not close the device.
     * It has to be closed explicitly by calling close().
     *
     * @return a MIDI IN receiver for the device
     * @throws MidiUnavailableException thrown if a receiver is not available
     *                due to resource restrictions or if the device is not open
     */
    @Override
    public Receiver getReceiver() throws MidiUnavailableException {

        // device must be open
        if (!_isOpen) {
            throw new MidiUnavailableException("The AMPT device is not open.");
        }

        // device must support additional receivers
        if (_receivers.size() >= getMaxReceivers() && getMaxReceivers() != -1) {
            throw new MidiUnavailableException(
                    "There are no receivers available for this device.");
        }

        // get a new receiver
        Receiver r = getAmptReceiver();
        _receivers.add(r);
        return r;
    }

    /**
     * Obtains a MIDI OUT connection from which the AMPT device will transmit
     * MIDI data. The returned transmitter must be closed when the application
     * has finished using it. Obtaining a Transmitter with this method does not
     * open the device. To be able to use the device, it has to be opened
     * explicitly by calling open(). Also, closing the Transmitter does not
     * close the device. It has to be closed explicitly by calling close().
     *
     * @return a MIDI OUT transmitter for the device.
     * @throws MidiUnavailableException thrown if a transmitter is not available
     *                due to resource restrictions or if the device is not open
     */
    @Override
    public Transmitter getTransmitter() throws MidiUnavailableException {

        // device must be open
        if (!_isOpen) {
            throw new MidiUnavailableException("The AMPT device is not open.");
        }

        // device must support additional transmitters
        if (_transmitters.size() >= getMaxTransmitters() && getMaxTransmitters() != -1) {
            throw new MidiUnavailableException(
                    "There are no more receivers available for this device.");
        }

        // get a new transmitter
        Transmitter t = new AmptTransmitter();
        _transmitters.add(t);
        return t;
    }

    /**
     * Obtains information about the device, including its Java class and
     * Strings containing its name, vendor, and description.
     *
     * @return device info
     */
    @Override
    public Info getDeviceInfo() {

        if (null == _deviceInfo) {
            throw new IllegalStateException("The AMPT device was not properly initialized.");
        }
        return _deviceInfo;
    }

    /**
     * Obtains the current time-stamp of the device, in microseconds. If a
     * device supports time-stamps, it should start counting at 0 when the
     * device is opened and continue incrementing its time-stamp in microseconds
     * until the device is closed. If it does not support time-stamps,
     * it should always return -1.
     *
     * <p>AMPT Devices do not support time-stamps by default.
     */
    @Override
    public long getMicrosecondPosition() {
        return -1;
    }

    /**
     * Set the OutputStream for this AmptDevice.
     *
     * @param out
     */
    public void setLogger(PrintStream out) {
        _logger = out;
    }

    /**
     * @return true if MIDI debugging is enabled for incomming MIDI messages on
     *         this device, otherwise false
     */
    public boolean getMidiDebugEnabled() {
        return _midiDebugEnabled;
    }
    
    /**
     * Enable or disable debugging for incoming MIDI messages on this device.
     * 
     * @param midiDebug true to enable MIDI debugging, otherwise false
     */
    public void setMidiDebugEnabled(boolean midiDebug) {
        _midiDebugEnabled = midiDebug;
    }

    /**
     * Write a message to the logger associated with this device.
     * 
     * @param msgToLog
     */
    protected void log(String msgToLog) {
        if(null != _logger && null != msgToLog) {
            _logger.println(msgToLog);
        }
    }

    /**
     * Sends a given MidiMessage immediately to all Transmitters registered
     * with this AMPT device.
     *
     * @param midiMessage the MidiMessage to send
     */
    protected void sendNow(MidiMessage midiMessage) {

        // nothing to send if there are no transmitters
        if (_transmitters.isEmpty()) {
            return;
        }

        // send message to each registered transmitter
        synchronized(_transmitters) {
            for (Transmitter transmitter : _transmitters) {
                sendNow(midiMessage, transmitter);
            }
        }
    }

    /**
     * Sends a list of MidiMessages immediately to all Transmitters registered
     * with this AMPT device.
     *
     * @param midiMessage the MidiMessage to send
     */
    protected void sendNow(List<MidiMessage> midiMessages) {

        // nothing to send if there are no transmitters
        if (_transmitters.isEmpty()) {
            return;
        }

        // send messages to each registered transmitter
        synchronized(_transmitters) {
            for (Transmitter transmitter : _transmitters) {
                for (MidiMessage midiMessage : midiMessages) {
                    sendNow(midiMessage, transmitter);
                }
            }
        }
    }

    /**
     * Sends a given MidiMessage to a given transmitter. Most devices will want
     * to use sendNow(MidiMessage) but this method will be useful for devices
     * that need to control output to specific transmitters.
     *
     * @param midiMessage the MidiMessage to send
     * @param receiver the Receiver to whic
     */
    protected void sendNow(final MidiMessage midiMessage, Transmitter transmitter) {

        // perform sanity checks
        if (null == transmitter) {
            throw new IllegalArgumentException("Transmitter expected.");
        }

        final Receiver receiver = transmitter.getReceiver();

        if (null == receiver) {
            throw new IllegalStateException("Receiver not initialized.");
        }

        /*
         * This may eventually use another thread if needed
         */
        receiver.send(midiMessage, -1); // timestamp not currently supported

    }

    /**
     * A Receivers accepts MIDI messages. This represents an input port on an
     * AMPT Device. There may be multiple Receivers for a device.
     *
     * <p>Note: Classes extending AmptDevice must also provide implementation for
     *          the abstract methods in AmptReceiver.
     */
    public abstract class AmptReceiver implements Receiver {

        // true if the receiver is closed, false otherwise
        protected boolean _receiverClosed = false;

        /**
         * Processes an incoming MIDI message and time-stamp and generates a
         * list of midi messages to be sent to all transmitters of the 
         * associated AmptDevice. All classes implementing AmptDevice must
         * provide this implementation.
         *
         * @param message the MIDI message to send
         * @param timeStamp the time-stamp for the message, in microseconds
         */
        protected abstract void filter(MidiMessage message, long timeStamp) throws InvalidMidiDataException;

        /**
         * Sends a MIDI message and time-stamp to this receiver. If
         * time-stamping is not supported by this receiver, the time-stamp value
         * should be -1.
         *
         * <p>Note: This method delegates to the abstract method filter(). All
         * classes extending AmptReceiver should provide implementation for the
         * filter() method rather than overriding send().
         *
         * @param msg the MIDI message to send
         * @param timeStamp the time-stamp for the message, in microseconds
         */
        @Override
        public void send(MidiMessage msg, long timeStamp) {

            // can't send if the device is closed
            if (!_isOpen) {
                throw new IllegalStateException("This AmptDevice is closed.");
            }

            // can't send if the receiver is closed
            if (_receiverClosed) {
                throw new IllegalStateException("This AmptReceiver is closed.");
            }

            // debug the midi message if debug is enabled
            if (_midiDebugEnabled) {

                StringBuilder logMsg = new StringBuilder();
                logMsg.append(_deviceInfo.getName());
                logMsg.append(": ");
                byte[] msgBytes = msg.getMessage(); // length of array returned not guaranteed to match the actual message length
                for (int i = 0; i < msg.getLength(); i++) {
                    logMsg.append((int) (msgBytes[i] & 0xFF));
                    logMsg.append(' ');
                }
                logMsg.append('\n');
                _logger.print(logMsg);
                 
            }

            // delegate to the implementing filter
            try {
                filter(msg, timeStamp);
            } catch (InvalidMidiDataException imde) {
                log(imde.getMessage());
            }
        }

        @Override
        public void close() {
            _receiverClosed = true;
            _receivers.remove(this);
        }
    } // class AmptReceiver

    /**
     * A Transmitter sends MidiEvent objects to one or more Receivers. This
     * represents an output port on an AMPT Device. There may be multiple
     * Transmitters for a device.
     */
    public class AmptTransmitter implements Transmitter {

        // where the transmitter will send MIDI messages
        private Receiver _receiver;

        /**
         * Sets the receiver to which this transmitter will deliver
         * MIDI messages. If a receiver is currently set,
         * it is replaced with this one.
         *
         * @param receiver
         */
        @Override
        public void setReceiver(Receiver receiver) {

            //TODO Should this close the existing receiver if replacing?
            // _receiver.close();

            _receiver = receiver;
        }

        /**
         * Obtains the current receiver to which this transmitter will deliver
         * MIDI messages.
         *
         * @return the current receiver. If no receiver is currently set,
         *         returns null
         */
        @Override
        public Receiver getReceiver() {
            return _receiver;
        }

        /**
         * Indicates that the application has finished using the transmitter,
         * and that limited resources it requires may be released or made
         * available.
         */
        @Override
        public void close() {

            //TODO Should this close the receiver?
            // _receiver.close();

            _transmitters.remove(this);
        }
    } // class AmptTransmitter
}

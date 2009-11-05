package ampt.examples.filters;

import java.util.List;
import java.util.Vector;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

/**
 * This is an implementation of a filter that creates a chord.  In this 
 * implementation, the class implements MidiDevice, allowing it to be plugged-in
 * to the MidiSystem.
 * 
 * This class contains inner classes which represent it's receivers and 
 * transmitters.
 *
 * This class also contains an inner class which extends MidiDevice.Info.  This
 * is required since MidiDevice.Info has protected status within it's package,
 * so we can't instantiate it unless we put our stuff in the javax.sound.midi
 * package.
 *
 * @author Christopher
 */
public class ChordFilterDevice implements MidiDevice {

    private Vector<Receiver> receivers;
    private Vector<Transmitter> transmitters;
    private boolean isOpen;
    private static final int thirdInterval = 4;
    private static final int fifthInterval = 7;

    public ChordFilterDevice() {
        this.receivers = new Vector<Receiver>();
        this.transmitters = new Vector<Transmitter>();
        isOpen = false;
    }

    @Override
    public Info getDeviceInfo() {
        return new ChordFilterInfo("Chord Filter", "AMPT Team", "Creates a chord from a single note", "1.0");
    }

    @Override
    public void open() throws MidiUnavailableException {
        isOpen = true;
    }

    @Override
    public void close() {
        isOpen = false;
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public long getMicrosecondPosition() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * return -1 for an infinate number of receivers.
     * @return
     */
    @Override
    public int getMaxReceivers() {
        return -1;
    }

    /**
     * return -1 for an infinate number of transmitters.
     * @return
     */
    @Override
    public int getMaxTransmitters() {
        return -1;
    }

    /**
     * Returns a new Chord Filter Receiver that is not yet bound to any
     * transmitters.
     * @return
     * @throws MidiUnavailableException
     */
    @Override
    public Receiver getReceiver() throws MidiUnavailableException {
        Receiver receiver = new ChordFilterReceiver();
        receivers.add(receiver);
        return receiver;
    }

    /**
     * Returns a list of all current receivers.  The list is a copy of
     * the object's list, so any changes made to the list itself do not carry
     * over into this object.
     * @return
     */
    @Override
    public List<Receiver> getReceivers() {
        return (Vector<Receiver>) receivers.clone();
    }

    /**
     * Returns a new Chord Filter Transmitter that is not yet bound to any
     * transmitters.
     * @return
     * @throws MidiUnavailableException
     */
    @Override
    public Transmitter getTransmitter() throws MidiUnavailableException {
        Transmitter transmitter = new ChordFilterTransmitter();
        transmitters.add(transmitter);
        return transmitter;
    }

    /**
     * Returns a list of all current transmitters.  The list is a copy of
     * the object's list, so any changes made to the list itself do not carry
     * over into this object.
     * @return
     */
    @Override
    public List<Transmitter> getTransmitters() {
        return (Vector<Transmitter>) transmitters.clone();
    }

    /**
     * Inner class that implements a receiver for a chord filter.  This is
     * where all of the actual filtering takes place.
     */
    public class ChordFilterReceiver implements Receiver {

        private boolean receiverClosed = false;

        @Override
        public void send(MidiMessage message, long timeStamp) {

            // Check to make sure we should forward any messages.
            if (isOpen == false || receiverClosed || transmitters.isEmpty()) {
                return;
            }

            ShortMessage third = null;
            ShortMessage fifth = null;

            // If the message is not a short message, we don't know how to deal
            // with it now.
            if (message instanceof ShortMessage) {
                ShortMessage root = (ShortMessage) message;
                // We only care about note on and note off messages.
                if (root.getCommand() == ShortMessage.NOTE_ON || root.getCommand() == ShortMessage.NOTE_OFF) {
                    
                    // Create the note that makes the third of the chord.
                    third = new ShortMessage();
                    // Tor to make the message, if it fails, then the note for
                    // the third is probably not supported.
                    try {
                        third.setMessage(root.getCommand(), root.getChannel(), root.getData1() + thirdInterval, root.getData2());
                    } catch (InvalidMidiDataException ex) {
                        third = null;
                    }
                    // Try to make the message, if it fails, then the note for
                    // the fifth is probably not supported.
                    fifth = new ShortMessage();
                    try {
                        fifth.setMessage(root.getCommand(), root.getChannel(), root.getData1() + fifthInterval, root.getData2());
                    } catch (InvalidMidiDataException ex) {
                        fifth = null;
                    }
                }
            }

            // Make sure we send the message to each of the transmitters
            // registered to the ChordFilterDevice
            for (Transmitter transmitter : transmitters) {
                Receiver receiver = transmitter.getReceiver();

                // Make sure we send each message in a separate thread, since
                // we don't know how long it would take for the method call to
                // return and it could cause a noticable difference.
                if (receiver != null) {
                    new Thread(new MessageSenderRunnable(receiver, message, timeStamp)).start();
                    if (third != null) {
                        new Thread(new MessageSenderRunnable(receiver, third, timeStamp)).start();
                    }
                    if (fifth != null) {
                        new Thread(new MessageSenderRunnable(receiver, fifth, timeStamp)).start();
                    }
                }
            }

        }

        @Override
        public void close() {
            receivers.remove(this);
        }

        /**
         * Inner class which takes care of sending midi messages to the
         * appropriate receivers
         */

        public class MessageSenderRunnable implements Runnable {

            private Receiver receiver;
            private MidiMessage message;
            private long timeStamp;

            /**
             * Constructor that takes the necessary arguments for the message to
             * be sent.
             *
             * @param receiver - The receiver to send the message to.
             * @param message - The message to send.
             * @param timeStamp - The timestamp of the message
             */
            public MessageSenderRunnable(Receiver receiver, MidiMessage message, long timeStamp) {
                this.receiver = receiver;
                this.message = message;
                this.timeStamp = timeStamp;
            }

            // This is called when the thread starts running.
            @Override
            public void run() {
                receiver.send(message, timeStamp);
            }
        }
    }

    /**
     * This class represents a transmitter for a chord filter.
     */
    public class ChordFilterTransmitter implements Transmitter {

        private Receiver receiver;

        @Override
        public void setReceiver(Receiver receiver) {
            this.receiver = receiver;
        }

        @Override
        public Receiver getReceiver() {
            return receiver;
        }

        @Override
        public void close() {
            transmitters.remove(this);
        }
    }

    /**
     * This inner class is so we can create an MidiDevice.Info object correctly
     * for integration with the MidiSystem.
     */
    public class ChordFilterInfo extends Info {

        public ChordFilterInfo(String name, String vendor, String description, String version) {
            super(name, vendor, description, version);
        }
    }
}




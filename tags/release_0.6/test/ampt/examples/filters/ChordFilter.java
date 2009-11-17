package ampt.examples.filters;

import java.util.Vector;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

/**
 * This class intercepts MIDI messages, adding the third and fifth of the major
 * chord to be played as well, thus creating a major chord.
 * 
 * Only the notes with the status message of NOTE_ON and NOTE_OFF are 
 * intercepted.
 * 
 * It is possible in the future to expand this class to offer more than one kind
 * of chord to be generated, for example, a minor chord could be generated 
 * instead of a major one.
 *
 *
 * @author Chris Redding
 */
public class ChordFilter implements Receiver, Transmitter{

//    private Receiver receiver;
    private Vector<Receiver> receivers;
    private static final int thirdInterval = 4;
    private static final int fifthInterval = 7;

    /**
     * Instantiates the class.
     */
    public ChordFilter(){
        receivers = new Vector<Receiver>();
    }

    /**
     * Receives MIDI messages, and uses the NOTE_ON and NOTE_OFF messages to
     * create chords.
     *
     * @param message The MIDI message
     * @param timeStamp
     */
    @Override
    public void send(MidiMessage message, long timeStamp) {

        if(receivers.isEmpty()){
            return;
        }

        if(message instanceof ShortMessage){
            ShortMessage root = (ShortMessage) message;
            if(root.getCommand() == ShortMessage.NOTE_ON || root.getCommand() == ShortMessage.NOTE_OFF){
                try{
                    ShortMessage third = new ShortMessage();
                    third.setMessage(root.getCommand(), root.getChannel(), root.getData1() + thirdInterval, root.getData2());
                    ShortMessage fifth = new ShortMessage();
                    fifth.setMessage(root.getCommand(), root.getChannel(), root.getData1() + fifthInterval, root.getData2());
                    for(Receiver receiver: receivers){
                        receiver.send(root, timeStamp);
                        receiver.send(third, timeStamp);
                        receiver.send(fifth, timeStamp);
                    }
                    return;
                } catch (InvalidMidiDataException ex){
                    // Do Nothing
                }
            }
        }

        for(Receiver receiver: receivers){
            receiver.send(message, timeStamp);
        }






    }

    /**
     * Closes the filter.  Not currently supported
     */
    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * sets the receiver to forward the MIDI messages to
     * @param receiver The receiver to send MIDI messages to
     */
    @Override
    public void setReceiver(Receiver receiver) {
        this.receivers.add(receiver);
    }

    /**
     * Returns the current receiver
     */
    @Override
    public Receiver getReceiver() {
        return this.receivers.firstElement();
    }


}

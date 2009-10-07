package examples;

import java.io.File;
import java.io.PrintStream;
import java.io.IOException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Sequence;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Transmitter;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.InvalidMidiDataException;

/*
 * This class intercepts MIDI messages between a transmitter
 * and a receiver. It will check the content of those messages
 * and for those whose status command is a NOTE_ON or NOTE_OFF,
 * it will change the tone value of the note by the offset.
 *
 * To use the class, create an instance, giving the contructor
 * the destination receiver - then pass the ChangeNoteFilter to
 * the setReceiver(Receiver) method of the Transmitter object.
 * The main() method below demonstrates a sample usage.
 *
 */

public class ChangeNoteFilter implements Receiver, Transmitter {

    private Receiver destination;
    private PrintStream out;
    private int offset;
    
    /*
     * Creates an instance with both a destination receiver and a
     * PrintStream to print out the contents of the messages.
     *
     * @param destination the receiver where the modified messages
     * will be sent
     *
     * @param offset how much to change the note
     
     * @param out the PrintStream that will print out the message
     * contents
     */
    public ChangeNoteFilter(Receiver destination, int offset, PrintStream out) {
        this.destination = destination;
        this.out = out;
        this.offset = offset;
    }
    
    /*
     * Creates an instance with just the destination receiver.
     *
     * @ param offset how much to change the note
     * @param destination the receiver where the modified messages
     * will be sent
     */
    public ChangeNoteFilter(Receiver destination, int offset) {
        this.destination = destination;
        this.offset = offset;
    }
    
    /*
     * Needed to satisfy the Transmitter interface - I'm not sure this
     * class actually gets anything out of implementing this interface.
     *
     * @param destination the destination receiver where MIDI messages
     * are sent
     */
    public void setReceiver(Receiver receiver) {
        destination = receiver;
    }
    
    /*
     * Needed to satisfy the Transmitter interface - I'm not sure this
     * class actually gets anything out of implementing this interface.
     *
     * @return destination the destination receiver where MIDI messages
     * are sent
     */
    public Receiver getReceiver() {
        return destination;
    }
    
    /*
     * Setter method for the offset
     *
     * @param the amount by which notes are to be changed
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    /*
     * Getter method for the offset
     *
     * @return the amount by which notes are changed
     */
    public int getOffset() {
        return offset;
    }
    
    /*
     * Method to handle the incoming MIDI messages
     *
     * @param message a MidiMessage object to be received from an
     * input device's Transmitter object
     *
     * @param timestamp MIDI timestamp for the message
     */
    public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage sMsg = (ShortMessage)message;
            if(out != null)
                out.println("Type: Short Message; Command : " + sMsg.getCommand()
                    + "; Channel: " + sMsg.getChannel() + "; data1: " + sMsg.getData1()
                    + "; data2: " + sMsg.getData2());
            if((sMsg.getCommand() == ShortMessage.NOTE_ON) ||
                (sMsg.getCommand() == ShortMessage.NOTE_OFF)) {
                try {
                    sMsg.setMessage(sMsg.getCommand(), sMsg.getChannel(),
                        sMsg.getData1() + offset, sMsg.getData2());
                } catch (InvalidMidiDataException mde) {
                    mde.printStackTrace();
                }
            }
            destination.send(sMsg, timeStamp);
        }
        else {
            if(out != null)
                out.println("Non ShortMessage: " + message);
            destination.send(message, timeStamp);
        }
    }
    
    /*
     */
    public void close() {
    }
    
    /*
     * Demonstrates usage of the ChangeNoteFilter - plays Back in
     * the USSR transposed up an octave
     */
    public static void main(String args[]) {
    
        Sequencer sqncr;
        Synthesizer synth;
        Receiver rcvr;
        Transmitter trans;
        try {
            sqncr = MidiSystem.getSequencer();
            synth = MidiSystem.getSynthesizer();
            rcvr = synth.getReceiver();
            ChangeNoteFilter filter = new ChangeNoteFilter(rcvr, 12, System.out);
            trans = sqncr.getTransmitter();
            trans.setReceiver(filter);
            sqncr.open();
            synth.open();
            File midiFile = new File("../resources/BackInTheUSSR.mid");
            Sequence seq = MidiSystem.getSequence(midiFile);
            sqncr.setSequence(seq);
            sqncr.start();
        } catch (MidiUnavailableException mue) {
            //die silently
        } catch (InvalidMidiDataException mde) {
            //die silently
        } catch (IOException ioe) {
            //die silently
        }
    }
}
package ampt.core.devices;

import ampt.midi.note.SequenceBuilder;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;
import java.util.HashMap;
import java.util.List;
import javax.sound.midi.Receiver;

/**
 * This device plays an arpeggio based on the note played.
 *
 * TODO - currently the arpeggio is fixed and arbitrary - need to provide options to
 * play proper argeggios
 *
 * @author Robert
 */
public class ArpFilterDevice extends AmptDevice {

    public final static String DEVICE_NAME = "Arpeggiator";
    public final static String DEVICE_DESCRIPTION = "An arpeggiator for use with AMPT";

    private HashMap<String, Sequencer> arpeggios = new HashMap<String, Sequencer>();

    public ArpFilterDevice() {
        super(DEVICE_NAME, DEVICE_DESCRIPTION);
    }

    @Override
    public AmptReceiver getAmptReceiver() {
        return new ArpFilterReceiver();
    }

    @Override
    public void closeDevice() {
        //moving along, nothing to see here
    }

    @Override
    public void initDevice() {
        //moving along, nothing to see here
    }

    private Sequencer playArpeggio(Sequence sequence) {

        Sequencer sqr = null;
        try {
            sqr = MidiSystem.getSequencer();

            //Remove any default connections
            List<Transmitter> transmitters = sqr.getTransmitters();
            for(Transmitter transmitter : transmitters)
                transmitter.close();

            sqr.getTransmitter().setReceiver(new Receiver() {
                @Override
                public void send(MidiMessage message, long timeStamp) {
                    if (message.getStatus() == 145 || message.getStatus() == 129)
                        sendNow(message);
                    }

                    @Override
                    public void close() {
                        //moving along, nothing to see here.
                    }
            });

            //play the arpeggio
            sqr.open();
            sqr.setSequence(sequence);
            sqr.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            sqr.setLoopStartPoint(0);
            sqr.setLoopEndPoint(sequence.getTickLength());
            sqr.start();

        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

        //sequencer is returned so that it can be closed later
        return sqr;
    }
    
    public final class ArpFilterReceiver extends AmptReceiver {

        @Override
        public void filter(MidiMessage message, long timeStamp) {

            if (message instanceof ShortMessage) {
                ShortMessage sMsg = (ShortMessage) message;
                String key = sMsg.getChannel() + ":" + sMsg.getData1();

                //first check for key released
                if ((sMsg.getCommand() == ShortMessage.NOTE_OFF) || (sMsg.getCommand() ==
                        ShortMessage.NOTE_ON && sMsg.getData2() == 0)) {

                    //find the arpeggio and stop it
                    Sequencer sqr = arpeggios.remove(key);
                    sqr.stop();
                    sqr.close();
                }

                //check for key pressed
                else if(sMsg.getCommand() == ShortMessage.NOTE_ON) {
                    int channel = sMsg.getChannel();
                    int tone = sMsg.getData1();
                    int velocity = sMsg.getData2();

                    //build the arpeggio
                    SequenceBuilder sb = new SequenceBuilder(Sequence.PPQ, 480);
                    sb.addEighthNote(channel, tone, velocity);
                    sb.addEighthNote(channel, tone + 4, velocity);
                    sb.addEighthNote(channel, tone + 7, velocity);
                    sb.addEighthNote(channel, tone + 12, velocity);
                    sb.addEighthNote(channel, tone + 7, velocity);
                    sb.addEighthNote(channel, tone + 4, velocity);

                    //start arpeggio playing and put it in hash map
                    arpeggios.put(key, playArpeggio(sb.getSequence()));
                }
            }

            //forward on any messages received
            sendNow(message);
        }
    }
}

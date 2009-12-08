package ampt.core.devices;

import ampt.midi.note.SequenceBuilder;
import ampt.midi.chord.ChordType;
import ampt.midi.note.NoteValue;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

/**
 * This device plays an arpeggio based on the note played.
 *
 * History: 11/22/09   Ben    Synchronized sequencer map. Added null check
 *                            before stopping/closing sequencer since another
 *                            note off event could have already removed it. No
 *                            longer creates a new sequence if one exists for
 *                            a given key.
 *
 *          11/22/09   Chris  Random note generation.  Added checks for note
 *                            fixing note off events, or note on events with
 *                            a velocity of zero, so the correct note would
 *                            be turned off instead of a random one.
 *
 *          11/25/09   Ben    Only forward messages to sendNow if they are not
 *                            going to be part of the arpeggio.
 *
 * @author Robert
 */
public class ArpFilterDevice extends TimedDevice {

    public final static String DEVICE_NAME = "Arp";
    public final static String DEVICE_DESCRIPTION = "An arpeggiator for use with AMPT";

    public final static int ASCEND = 0;
    public final static int DESCEND = 1;
    public final static int ASCEND_DESCEND = 2;
    public final static int DESCEND_ASCEND = 3;
    public final static int RANDOM = 4;
    public final static String[] arpTypes = {"Up", "Down", "Up and Down",
            "Down and Up", "Random"};

    private final Map<String, Sequencer> arpeggios = 
            Collections.synchronizedMap(new HashMap<String, Sequencer>());

    private final ArrayBlockingQueue<Sequencer> sequencerPool =
            new ArrayBlockingQueue<Sequencer>(12);
    private Runnable sequenceGenerator;
    private boolean generateSequencers = true;
    private ChordType chordType = ChordType.MAJOR;
    private NoteValue noteValue = NoteValue.EIGHTH_NOTE;
    private int arpType = ASCEND_DESCEND;
    private int[] randomNotes = new int[7];

    public ArpFilterDevice() {
        super(DEVICE_NAME, DEVICE_DESCRIPTION);
    }

    @Override
    public AmptReceiver getAmptReceiver() {
        return new ArpFilterReceiver();
    }

    @Override
    public void closeDevice() {
        //stop generating sequencers for the pool
        generateSequencers = false;
    }

    public void setNoteValue(NoteValue noteValue) {
        this.noteValue = noteValue;
    }

    public void setChordType(ChordType chordType) {
        this.chordType = chordType;
    }

    public void setArpType(int arpType) {
        this.arpType = arpType;
    }

    @Override
    public void initDevice() {
        //when put in a thread this will continually generate a sequencer that is as
        //initialized as possible for the sequencer pool - it generates them as they are
        //taken out of the pool.
        sequenceGenerator = new Runnable() {
            @Override
            public void run() {
                while(generateSequencers == true) {
                    try {
                        //initialize the sequencer as much as possible
                        Sequencer sequencer = MidiSystem.getSequencer();
                        sequencer.open();
                        sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
                        sequencer.setLoopStartPoint(0);

                        //kill any default connections (the Java Real Time Sequencer is
                        //connected to the Java Synthesizer)
                        List<Transmitter> transmitters = sequencer.getTransmitters();
                        for (final Transmitter transmitter : transmitters)
                            transmitter.close();

                        //this receiver acts as a gatekeeper between the sequencer and
                        //the destination devices - it removes any messages that are
                        //not either a note on or note off
                        sequencer.getTransmitter().setReceiver(new Receiver() {
                            
                            private int previousNoteOn = -1;

                            @Override
                            public void send(MidiMessage message, long timeStamp) {
                                if (message.getStatus() >= 128 &&
                                        message.getStatus() <= 159) {

                                    if (arpType == RANDOM) {
                                        int index = ((int) (Math.random() * 6)) + 1;
                                        ShortMessage sMsg = (ShortMessage)message;
                                        if(sMsg.getCommand() == ShortMessage.NOTE_ON && sMsg.getData2() != 0){
                                            try {
                                                sMsg.setMessage(sMsg.getCommand(), sMsg.getChannel(), randomNotes[index], sMsg.getData2());
                                                previousNoteOn = randomNotes[index];
                                            } catch (InvalidMidiDataException e) {
                                                e.printStackTrace();
                                            }
                                        } else if (sMsg.getCommand() == ShortMessage.NOTE_OFF || (sMsg.getCommand() == ShortMessage.NOTE_ON && sMsg.getData2() == 0)){
                                            if(previousNoteOn != -1){
                                                try {
                                                    sMsg.setMessage(sMsg.getCommand(), sMsg.getChannel(), previousNoteOn, sMsg.getData2());
                                                } catch (InvalidMidiDataException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        message = sMsg;
                                    }
                                    sendNow(message);
                                }
                            }

                            @Override
                            public void close() {
                                //moving along, nothing to see here
                            }
                        });

                        //put the sequence in the pool
                        sequencerPool.put(sequencer);
                        Thread.sleep(0, 1);
                    } catch (MidiUnavailableException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        //start generating sequences
        (new Thread(sequenceGenerator)).start();
    }

    private Sequencer playArpeggio(Sequence sequence) {

        Sequencer sequencer = null;

        try {
            sequencer = sequencerPool.take();
            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(_tempo);
            sequencer.setLoopEndPoint(sequence.getTickLength());
            sequencer.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

        //sequencer is returned so that it can be closed later
        return sequencer;
    }
    
    public final class ArpFilterReceiver extends AmptReceiver {

        @Override
        public void filter(MidiMessage message, long timeStamp) {

            if (message instanceof ShortMessage) {

                ShortMessage sMsg = (ShortMessage) message;
                String key = sMsg.getChannel() + ":" + sMsg.getData1();

                // first check for key released - could be NOTE_OFF or NOTE_ON
                // with velocity == 0.
                if (sMsg.getCommand() == ShortMessage.NOTE_OFF ||
                    (sMsg.getCommand() == ShortMessage.NOTE_ON && sMsg.getData2() == 0)) {

                    //find the arpeggio and stop it, but only if it exists in map
                    Sequencer sqr = arpeggios.remove(key);
                    if(null != sqr) {
                        sqr.stop();
                        sqr.close();
                    } // else fail silently

                }

                // check for key pressed but only create new sequence if one
                // does not already exist for this key
                else if(sMsg.getCommand() == ShortMessage.NOTE_ON &&
                        !arpeggios.containsKey(key)) {
                    int channel = sMsg.getChannel();
                    int tone = sMsg.getData1();
                    int velocity = sMsg.getData2();

                    SequenceBuilder sb = new SequenceBuilder(Sequence.PPQ, 480);
                    if (arpType == ASCEND_DESCEND) {
                        sb.addNote(noteValue, channel, tone, velocity);
                        sb.addNote(noteValue, channel, tone +
                                chordType.getThirdInterval(), velocity);
                        sb.addNote(noteValue, channel, tone +
                                chordType.getFifthInterval(), velocity);
                        sb.addNote(noteValue, channel, tone + 12, velocity);
                        sb.addNote(noteValue, channel, tone +
                                chordType.getFifthInterval(), velocity);
                        sb.addNote(noteValue, channel, tone +
                                chordType.getThirdInterval(), velocity);
                    }
                    if (arpType == DESCEND_ASCEND) {
                        sb.addNote(noteValue, channel, tone, velocity);
                        sb.addNote(noteValue, channel, tone +
                                (chordType.getFifthInterval() - 12), velocity);
                        sb.addNote(noteValue, channel, tone +
                                (chordType.getThirdInterval() - 12), velocity);
                        sb.addNote(noteValue, channel, tone - 12, velocity);
                        sb.addNote(noteValue, channel, tone +
                                (chordType.getThirdInterval() - 12), velocity);
                        sb.addNote(noteValue, channel, tone +
                                (chordType.getFifthInterval() - 12), velocity);
                    }
                    if (arpType == DESCEND) {
                        sb.addNote(noteValue, channel, tone, velocity);
                        sb.addNote(noteValue, channel, tone +
                                (chordType.getFifthInterval() - 12), velocity);
                        sb.addNote(noteValue, channel, tone +
                                (chordType.getThirdInterval() - 12), velocity);
                        sb.addNote(noteValue, channel, tone - 12, velocity);
                    }
                    if (arpType == ASCEND) {
                        sb.addNote(noteValue, channel, tone, velocity);
                        sb.addNote(noteValue, channel, tone +
                                chordType.getThirdInterval(), velocity);
                        sb.addNote(noteValue, channel, tone +
                                chordType.getFifthInterval(), velocity);
                        sb.addNote(noteValue, channel, tone + 12, velocity);
                    }
                    if (arpType == RANDOM) {
                        sb.addNote(noteValue, channel, tone, velocity);
                        randomNotes[0] = tone;
                        randomNotes[1] = tone + chordType.getThirdInterval();
                        randomNotes[2] = tone + chordType.getFifthInterval();
                        randomNotes[3] = tone + chordType.getThirdInterval() - 12;
                        randomNotes[4] = tone + chordType.getFifthInterval() - 12;
                        randomNotes[5] = tone + 12;
                        randomNotes[6] = tone - 12;
                    }
                    //start arpeggio playing and put it in hash map
                    arpeggios.put(key, playArpeggio(sb.getSequence()));
                } else {
                    // forward on any short messages that aren't note on or off
                    sendNow(message);
                }
            } else {
                // forward on any messages received that aren't short message
                sendNow(message);
            }
        }
    }
}

package ampt.examples.filters;

import java.util.HashMap;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

/**
 * Variation on Rob's Flutter Filter. Just playing around with timing. See the
 * run method for changes.
 *
 * @author Ben
 */
public class FlutterFilterVariation implements Receiver, Transmitter {

    private Receiver receiverOut;
    private HashMap<String, Thread> notesMap;

    public FlutterFilterVariation() {
        notesMap = new HashMap<String, Thread>();
    }

    public void send(MidiMessage messageIn, long timeStamp) {
        if (messageIn instanceof ShortMessage) {
            ShortMessage sMsg = (ShortMessage) messageIn;
            int command = sMsg.getCommand();
            int channel = sMsg.getChannel();
            int noteValue = sMsg.getData1();

            //map keys are a combination of channel and note
            String key = channel + "_" + noteValue;

            if (command == ShortMessage.NOTE_ON) {
                //if a note of the same value is already being played
                //on the channel, stop the note.
                if (notesMap.get(key) != null) {
                    Thread thread = notesMap.get(key);
                    thread.interrupt();
                }

                //create a new thread with a new generator
                Thread thread = new Thread(
                        new NoteGenerator(sMsg, receiverOut, timeStamp));

                //put the thread in the map
                notesMap.put(key, thread);

                thread.start();

            } else if (command == ShortMessage.NOTE_OFF) {
                //find the corresponding thread and stop it
                if (notesMap.get(key) != null) {
                    Thread thread = notesMap.get(key);
                    thread.interrupt();
                }

                //send the stop message
                receiverOut.send(sMsg, timeStamp);

            } else {
                receiverOut.send(sMsg, timeStamp);
            }
        } else {
            receiverOut.send(messageIn, timeStamp);
        }
    }

    public void close() {
    }

    public void setReceiver(Receiver receiverOut) {
        this.receiverOut = receiverOut;
    }

    public Receiver getReceiver() {
        return receiverOut;
    }
}

class NoteGenerator implements Runnable {

    private ShortMessage message;
    private Receiver receiverOut;
    private long timeStamp;

    public NoteGenerator(ShortMessage message, Receiver receiverOut, long timeStamp) {

        this.message = message;
        this.receiverOut = receiverOut;
        this.timeStamp = timeStamp;
    }

    public void run() {
        int t = 100;
        int i = 10;
        try {
            while (true) {
                receiverOut.send(message, timeStamp);
                Thread.sleep(t);

                if (t > 100) {
                    t = 100;
                    i *= -1;
                } else if (t < 20) {
                    t = 20;
                    i *= -1;
                } else {
                    t -= i;
                }
            }
        } catch (InterruptedException ie) {
            return;
        }
    }
}

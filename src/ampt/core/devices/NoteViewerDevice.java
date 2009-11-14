package ampt.core.devices;

import ampt.ui.filters.NoteViewerBox;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/**
 *
 * @author Christopher
 */
public class NoteViewerDevice extends AmptDevice {

    public static final String DEVICE_NAME = "Note Viewer";
    private static final String DEVICE_DESCRIPTION = "Displays the notes on a staff";

    private List<NoteViewerBox> listeners;

    public NoteViewerDevice(){
        super(DEVICE_NAME, DEVICE_DESCRIPTION);
        listeners = new ArrayList<NoteViewerBox>();
    }

    public void addNoteViewerBox(NoteViewerBox listener){
        listeners.add(listener);
    }

    public void removeNoteViewerBox(NoteViewerBox listener){
        listeners.remove(listener);
    }

    @Override
    protected void initDevice() throws MidiUnavailableException {
        //nothing to do
    }

    @Override
    protected void closeDevice() {
        //nothing to do
    }

    @Override
    protected Receiver getAmptReceiver() throws MidiUnavailableException {
        return new NoteViewerReceiver();
    }

    public class NoteViewerReceiver extends AmptReceiver {

        List<MidiMessage> messages = new ArrayList<MidiMessage>();

        @Override
        protected List<MidiMessage> filter(MidiMessage message, long timeStamp) throws InvalidMidiDataException {
            messages.clear();
            messages.add(message);
            if(message instanceof ShortMessage){
                final ShortMessage sMsg = (ShortMessage) message;
                for(final NoteViewerBox box: listeners){
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            if(sMsg.getCommand() == ShortMessage.NOTE_ON){
                                box.noteOn(sMsg.getData1());
                            }
                            else if(sMsg.getCommand() == ShortMessage.NOTE_OFF){
                                box.noteOff(sMsg.getData1());
                            }
                        }
                    }).start();
                    
                }
            }
            return messages;
        }

    }

}

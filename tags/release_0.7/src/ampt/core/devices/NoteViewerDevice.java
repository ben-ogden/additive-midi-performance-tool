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

    private static final String DEVICE_NAME = "Note Viewer";
    private static final String DEVICE_DESCRIPTION = "Displays the notes on a staff";
    private List<NoteViewerBox> listeners;

    public NoteViewerDevice() {
        super(DEVICE_NAME, DEVICE_DESCRIPTION);
        listeners = new ArrayList<NoteViewerBox>();
    }

    public void addNoteViewerBox(NoteViewerBox listener) {
        listeners.add(listener);
    }

    public void removeNoteViewerBox(NoteViewerBox listener) {
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

        @Override
        protected void filter(MidiMessage message, long timeStamp) throws InvalidMidiDataException {

            if (message instanceof ShortMessage) {
                final ShortMessage sMsg = (ShortMessage) message;
                for (final NoteViewerBox box : listeners) {

                    // check for note off - some keyboards transmit note off as
                    // note one with velocity of zero
                    if (sMsg.getCommand() == ShortMessage.NOTE_OFF ||
                                (sMsg.getCommand() == ShortMessage.NOTE_ON &&
                                 sMsg.getData2() == 0x0)) {
                        box.noteOff(sMsg.getChannel(), sMsg.getData1());
                    }

                    // check for note on
                    else if (sMsg.getCommand() == ShortMessage.NOTE_ON) {
                        box.noteOn(sMsg.getChannel(), sMsg.getData1());
                    } 

                }
            }

            // NoteViewerDevice will pass on any notes it receives to connected devices
            sendNow(message);

        }
    }
}

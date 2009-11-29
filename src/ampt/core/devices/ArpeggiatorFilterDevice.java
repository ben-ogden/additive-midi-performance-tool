package ampt.core.devices;

import ampt.midi.note.Arpeggio;
import ampt.midi.note.NoteValue;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author Robert
 */
public class ArpeggiatorFilterDevice extends TimedDevice {

    public final static String DEVICE_NAME = "Arpeggiator";
    public final static String DEVICE_DESCRIPTION = "An arpeggiator for use with AMPT";

    

    private final ConcurrentHashMap<String, Boolean> _arpeggios
            = new ConcurrentHashMap<String, Boolean>();


    private int _motion = Arpeggio.ASCEND_DESCEND;
    private int _chordType = Arpeggio.MAJOR;
    private NoteValue _noteValue = NoteValue.EIGHTH_NOTE;
    private Arpeggio _arp;

    public ArpeggiatorFilterDevice() {
        super(DEVICE_NAME, DEVICE_DESCRIPTION);
    }

    @Override
    public void initDevice() {
        _arpeggios.clear();

        synchronized(this) {
            _arp = Arpeggio.newArpeggio(_motion, _chordType, _noteValue);
        }
    }

    public void setMotion(int motion) {
        Collection<Boolean> arpsView = _arpeggios.values();

        synchronized(this) {
            for (Boolean b : arpsView)
                b = false;
            _motion = motion;
            _arp = Arpeggio.newArpeggio(_motion, _chordType, _noteValue);
        }
    }

    public void setNoteValue(NoteValue noteValue) {
        Collection<Boolean> arpsView = _arpeggios.values();

        synchronized(this) {
            for (Boolean b : arpsView)
                b = false;
            _noteValue = noteValue;
            _arp = Arpeggio.newArpeggio(_motion, _chordType, _noteValue);
        }
    }

    public void setChordType(int chordType) {
        Collection<Boolean> arpsView = _arpeggios.values();

        synchronized(this) {
            for (Boolean b : arpsView)
                b = false;
            _chordType = chordType;
            _arp = Arpeggio.newArpeggio(_motion, _chordType, _noteValue);
        }
    }

    @Override
    public AmptReceiver getAmptReceiver() {
        return (new AmptReceiver() {

            @Override
            public void filter(MidiMessage message, long timeStamp) {

                //if it is a ShortMessage (a note)
                if (message instanceof ShortMessage) {

                    //cast to ShortMessage
                    ShortMessage sMsg = (ShortMessage) message;

                    int command = sMsg.getCommand();
                    int channel = sMsg.getChannel();
                    int tone = sMsg.getData1();
                    int velocity = sMsg.getData2();

                    //the key for the HashMap is the channel combined with
                    //tone
                    String key = channel + ":" + tone;

                    //check for a keyboard key release via either a NOTE_OFF or
                    //NOTE_ON with velocity == 0.
                    if (command == ShortMessage.NOTE_OFF ||
                       (command == ShortMessage.NOTE_ON && velocity == 0)) {

                        //if _key is mapped to a flag, then set that flag to
                        //false.
                        _arpeggios.remove(key);

                    } else {

                        //check for a keyboard key pressed via NOTE_ON
                        if (command == ShortMessage.NOTE_ON) {
                            float noteFactor;
                            int[] intervals;
                            synchronized(ArpeggiatorFilterDevice.this) {
                                noteFactor = 60000 / _noteValue.getNotesPerBeat();
                                intervals = _arp.getIntervals();
                            }
                            
                            ArpeggioTask arpTask = new ArpeggioTask(key, intervals, channel, velocity, tone, noteFactor);
                            _arpeggios.putIfAbsent(key, true);
                            execute(arpTask);

                            //send the original note
                            sendNow(sMsg);
                        }
                    }
                } else
                    //send non-note messages
                    sendNow(message);
            }
        });
    }

    @Override
    public void close() {
        
    }

    @Override
    public void closeDevice() {
        
    }

    class ArpeggioTask implements Runnable {

        String _key;
        int[] _intervals;
        int _channel;
        int _velocity;
        int _tone;
        float _noteFactor;

        public ArpeggioTask(String key, int[] intervals, int channel, int velocity, int tone, float noteFactor) {
            _key = key;
            _intervals = intervals;
            _channel = channel;
            _velocity = velocity;
            _tone = tone;
            _noteFactor = noteFactor;
        }

        @Override
        public void run() {
            ShortMessage sMsg = new ShortMessage();
            try {
                   sMsg.setMessage(ShortMessage.NOTE_ON,
                                   _channel,
                                   _tone + _intervals[0],
                                   _velocity);
                   sendNow(sMsg);
               } catch (InvalidMidiDataException e) {
                   e.printStackTrace();
               }

            int noteCount = 0;
            long nextNote = System.currentTimeMillis() + (long) (_noteFactor / _tempo);
            while (_arpeggios.get(_key) == true) {
                if(System.currentTimeMillis() >= nextNote) {
                    try {
                        sMsg.setMessage(ShortMessage.NOTE_OFF,
                                        _channel,
                                        _tone + _intervals[noteCount % _intervals.length],
                                        _velocity);
                        sendNow(sMsg);
                        noteCount++;

                        sMsg.setMessage(ShortMessage.NOTE_ON,
                                        _channel,
                                        _tone + _intervals[noteCount % _intervals.length],
                                        _velocity);
                        sendNow(sMsg);

                    } catch (InvalidMidiDataException e) {
                        e.printStackTrace();
                    }
                    
                    nextNote = nextNote + (long) (_noteFactor / _tempo);
                }

                try {
                   sMsg.setMessage(ShortMessage.NOTE_OFF,
                                   _channel,
                                   _tone + _intervals[noteCount % _intervals.length],
                                   _velocity);
                   sendNow(sMsg);
               } catch (InvalidMidiDataException e) {
                   e.printStackTrace();
               }

               try {
                   Thread.sleep(0, 1);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
            }
        }
    }
}

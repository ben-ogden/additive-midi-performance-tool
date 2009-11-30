package ampt.core.devices;

import ampt.midi.note.Arpeggio;
import ampt.midi.note.NoteValue;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import java.util.HashMap;

/**
 * 
 * @author Robert
 */
public class ArpeggiatorFilterDevice extends TimedDevice {

    public final static String DEVICE_NAME = "Arpeggiator";
    public final static String DEVICE_DESCRIPTION = "An arpeggiator for use with AMPT";

    

    private final HashMap<String, Boolean> _arpeggios = new HashMap<String, Boolean>();


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
        synchronized(this) {
            _motion = motion;
            _arp = Arpeggio.newArpeggio(_motion, _chordType, _noteValue);
        }
    }

    public void setNoteValue(NoteValue noteValue) {
        synchronized(this) {
            _noteValue = noteValue;
            _arp = Arpeggio.newArpeggio(_motion, _chordType, _noteValue);
        }
    }

    public void setChordType(int chordType) {
        synchronized(this) {
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
                        synchronized(ArpeggiatorFilterDevice.this) {
                            _arpeggios.put(key, false);
                        }

                    } else {

                        //check for a keyboard key pressed via NOTE_ON
                        if (command == ShortMessage.NOTE_ON) {
                            float noteFactor;
                            int[] intervals;
                            ArpeggioTask arpTask;
                            synchronized(ArpeggiatorFilterDevice.this) {

                                //if key is not mapped
                                if (!_arpeggios.containsKey(key)) {
                                    _arpeggios.put(key, true);
                                    noteFactor = 60000 / _noteValue.getNotesPerBeat();
                                    intervals = _arp.getIntervals();
                                    arpTask = new ArpeggioTask(key, intervals, channel, velocity, tone, noteFactor, _motion == Arpeggio.RANDOM);

                                    execute(arpTask);

                                //if key is mapped to a flag that is false, set
                                //flag to true
                                } else if (!_arpeggios.get(key)) {
                                    _arpeggios.put(key, true);
                                    noteFactor = 60000 / _noteValue.getNotesPerBeat();
                                    intervals = _arp.getIntervals();
                                    arpTask = new ArpeggioTask(key, intervals, channel, velocity, tone, noteFactor, _motion == Arpeggio.RANDOM);

                                    execute(arpTask);
                                }
                            }
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
        boolean _random;

        public ArpeggioTask(String key, int[] intervals, int channel, int velocity, int tone, float noteFactor, boolean random) {
            _key = key;
            _intervals = intervals;
            _channel = channel;
            _velocity = velocity;
            _tone = tone;
            _noteFactor = noteFactor;
            _random = random;
        }

        @Override
        public void run() {
            ShortMessage sMsg = new ShortMessage();

            //noteCount is the current position in the interval array
            int noteCount;

            if (_random)
                noteCount = ((int) (Math.random() * (_intervals.length - 1))) + 1;
            else
                noteCount = 0;

            //Play the first Note
            try {
                sMsg.setMessage(ShortMessage.NOTE_ON,
                                _channel,
                                _tone + _intervals[noteCount],
                                _velocity);
                sendNow(sMsg);

            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }

            long nextNote = System.currentTimeMillis() + (long) (_noteFactor / _tempo);
            while (_arpeggios.get(_key)) {
                if(System.currentTimeMillis() >= nextNote) {
                    try {

                        //turn off previous note
                        sMsg.setMessage(ShortMessage.NOTE_OFF,
                                        _channel,
                                        _tone + _intervals[noteCount],
                                        _velocity);
                        sendNow(sMsg);

                        //get next note
                        if (_random)
                            noteCount = ((int) (Math.random() * (_intervals.length - 1))) + 1;
                        else if (++noteCount == _intervals.length)
                            noteCount = 0;

                        //turn on next note
                        sMsg.setMessage(ShortMessage.NOTE_ON,
                                        _channel,
                                        _tone + _intervals[noteCount],
                                        _velocity);
                        sendNow(sMsg);

                    } catch (InvalidMidiDataException e) {
                        e.printStackTrace();
                    }

                    nextNote = nextNote + (long) (_noteFactor / _tempo);
                }

               try {
                   Thread.sleep(0, 1);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
            }

            //turn off the last note
            try {
                sMsg.setMessage(ShortMessage.NOTE_OFF,
                                _channel,
                                _tone + _intervals[noteCount],
                                _velocity);
                sendNow(sMsg);
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
        }
    }
}

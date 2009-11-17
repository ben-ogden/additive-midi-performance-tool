/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ampt.ui.keyboard;

import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

/**
 *
 * @author Christopher
 */
public class KeyboardDevice implements MidiDevice{

    private Vector<Transmitter> transmitters;
    private boolean isOpen = false;
    private int channel;
    private int velocity;

    public KeyboardDevice(){
        this(1, 93);
    }

    public KeyboardDevice(int channel, int velocity){
        transmitters = new Vector<Transmitter>();
        this.channel = channel;
        this.velocity = velocity;
    }

    @Override
    public Info getDeviceInfo() {
        return new KeyboardDeviceInfo("Software Keyboard", "AMPT Team", "A software MIDI keyboard", "1.0");
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

    @Override
    public int getMaxReceivers() {
        return 0;
    }

    @Override
    public int getMaxTransmitters() {
        return -1;
    }

    @Override
    public Receiver getReceiver() throws MidiUnavailableException {
        throw new MidiUnavailableException("No Receivers Available");
    }

    @Override
    public List<Receiver> getReceivers() {
        return new Vector<Receiver>();
    }

    @Override
    public Transmitter getTransmitter() throws MidiUnavailableException {
        Transmitter transmitter = new KeyboardDeviceTransmitter();
        transmitters.add(transmitter);
        return transmitter;
    }

    @Override
    public List<Transmitter> getTransmitters() {
        return (Vector<Transmitter>) transmitters.clone();
    }

    public void sendMessage(int messageType, int note){
        if(transmitters.isEmpty()){
            return;
        }

        final ShortMessage msgNote = new ShortMessage();
        try {
            msgNote.setMessage(messageType, channel, note, velocity);
            for(Transmitter transmitter: transmitters){
                final Receiver receiver = transmitter.getReceiver();
                if(receiver != null){
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            receiver.send(msgNote, -1);
                        }
                    }).start();
                }
            }

        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(KeyboardDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class KeyboardDeviceTransmitter implements Transmitter {

        private Receiver receiver;

        @Override
        public void setReceiver(Receiver receiver) {
            this.receiver = receiver;
        }

        @Override
        public Receiver getReceiver() {
            return this.receiver;
        }

        @Override
        public void close() {
            transmitters.remove(this);
        }

    }

    public class KeyboardDeviceInfo extends Info {

        public KeyboardDeviceInfo(String name, String vendor, String description, String version){
            super(name, vendor, description, version);
        }
    }

}
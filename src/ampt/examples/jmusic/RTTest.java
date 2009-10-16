package ampt.examples.jmusic;

import jm.audio.RTMixer;
import jm.audio.Instrument;
import jm.music.rt.RTLine;
import jm.JMC;

import jm.inst.RTPluckInst;

public class RTTest implements JMC{
	public static void main(String[] args){
		int sampleRate = 44100;
		Instrument inst = new RTPluckInst(sampleRate);
		Instrument[] insts = new Instrument[] {inst};
		RTLine[] rtlines = {new RealtimeMelody(insts)};
		final RTMixer rtm = new RTMixer(rtlines);
		rtm.begin();
	}
}
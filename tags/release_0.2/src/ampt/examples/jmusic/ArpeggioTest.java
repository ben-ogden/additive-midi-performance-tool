package ampt.examples.jmusic;

import jm.JMC;
import jm.music.data.*;
import jm.music.tools.*;
import jm.util.*;

/**
 * This is an example from the jMusic author, Andrew Brown. This class turns a
 * series of pitches into a repeating arpeggio and writes the result to a midi
 * file. (You should find the MIDI file a few directories up in the project)
 *
 */
public class ArpeggioTest implements JMC {

    public static void main(String[] args) {
        new ArpeggioTest();
    }

    public ArpeggioTest() {
        int[] pitches = {C4, F4, BF4};
        // turn pitches into a phrase
        Phrase arpPhrase = new Phrase();
        for (int i = 0; i < pitches.length; i++) {
            Note n = new Note(pitches[i], SEMI_QUAVER);
            arpPhrase.addNote(n);
        }

        // repeat the arpeggio a few times
        Mod.repeat(arpPhrase, 3);

        // save it as a file
        Write.midi(arpPhrase, "Arpeggio1.mid");

        // display the result to the screen
        View.notate(arpPhrase);

    }
}
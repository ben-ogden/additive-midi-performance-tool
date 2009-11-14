package examples.jmusic;

import jm.music.data.*;
import jm.JMC;
import jm.util.*;

/**
 * This test shows different ways jMusic can display notation. Each window that
 * opens has capabilities to play or save to MIDI. This will open 2 windows
 * and write data to the console, so refer to all three areas. Note the ability
 * to add new notes and reconstruct MIDI files at runtime.
 *
 * @author Ben
 */
public class NotationTest implements JMC {

    public static void main(String[] args) {

        // generate some notes
        Phrase phr = new Phrase();
        for(int i = 0; i < 16; i++) {
            Note n = new Note(C4+i, JMC.EIGHTH_NOTE);
            phr.addNote(n);
        }

        // display CPN score
        View.notate(phr);

        // display phrase in a piano roll view
        View.show(phr);

        // print phrase on standard out
        View.print(phr);


    }
}

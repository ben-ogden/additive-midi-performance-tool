package examples.jmusic;

import jm.music.data.Phrase;
import jm.util.View;

/**
 * This test creates an empty phrase and opens up the sketch view. The
 * user can proceed to "sketch" the musical part and play it from the
 * window.
 *
 * @author Ben Ogden
 */
public class SketchTest {
        
    public static void main(String args[]) {

        Phrase phr = new Phrase();   
        View.sketch(phr);

    }
}

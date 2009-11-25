/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ampt.midi.note;

/**
 *
 * @author Ben
 */
public enum Decay {

    NONE("None"),
    LINEAR("Linear"),
    LOGARITHMIC("Logarithmic"),
    EXPONENTIAL("Exponential");

    private String _name;

    Decay(String name) {
        _name = name;
    }

    @Override
    public String toString() {
        return _name;
    }


}

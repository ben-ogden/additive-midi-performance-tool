package ampt.ui.tempo;

import java.awt.AWTEvent;

/**
 * A Tempo Event for AMPT swing components.
 * 
 * @author Ben
 */
public class TempoEvent extends AWTEvent {

    /*
     * The event id's follow the AWTEvent and Event approach. All custom
     * event id's, must be greater than AWTEvent.RESERVED_ID_MAX (which is
     * currently 1999)
     */

    /* Tempo events */
    private static final int TEMP0_EVENT = 4000;

    /* Tempo changed event id */
    public static final int TEMPO_CHANGE = TEMP0_EVENT + 1;

    // the tempo
    private float _tempo;

    /**
     * Create a new TempoEvent.
     *
     * @param source the source of the event
     * @param id the event id
     * @param tempo the tempo to associate with this event
     */
    public TempoEvent(Object source, int id, float tempo) {
        super(source, id);
        _tempo = tempo;
    }

    /**
     * Get the tempo associated with this TempoEvent.
     *
     * @return the tempo
     */
    public float getTempo() {
        return _tempo;
    }


}

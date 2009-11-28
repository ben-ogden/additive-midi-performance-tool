package ampt.ui.tempo;

import java.util.EventListener;

/**
 * A TempoListener can receive tempo-related event notification.
 * 
 * @author Ben
 */
public interface TempoListener extends EventListener {

    /**
     * Notify the listener of a change in tempo.
     *
     * @param event a TempoEvent
     */
    public void tempoChanged(TempoEvent event);

}

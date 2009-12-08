package ampt.examples.canvas;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This class is a mouse adapter that listens for a user clicking on a filter
 * after choosing to create a new filter connection.  This class is used to
 * determine what the second filter in the connection is.
 *
 * @author Christopher
 */
public class FilterConnectionMouseAdapter implements MouseListener{

	private SampleFilter from;
	private DrawingPanel drawingPanel;

        /**
         * Constructor.  Keeps the drawing panel and the source filter for the
         * connection.
         * @param from - the source filter for the connection.
         */
	public FilterConnectionMouseAdapter(SampleFilter from){
		super();
		this.from = from;
		this.drawingPanel = (DrawingPanel) from.getParent();
	}

        /**
         * This method is called when the user clicks on a sample filter after
         * choosing to create a filter connection.
         * @param event
         */
	@Override
	public void mouseClicked(MouseEvent event) {
		drawingPanel.removeFilterConnectionMouseAdapter(this);
		SampleFilter to = (SampleFilter) event.getSource();
		if(to.equals(from)){
			return;
		}
		drawingPanel.addFilterConnection(new FilterConnection(from, to));

	}

        /**
         * None of these methods are used, but are required when implementing
         * a mouse listener.
         */
	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}



}

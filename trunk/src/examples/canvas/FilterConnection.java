package examples.canvas;

import java.awt.PopupMenu;
import javax.swing.JComponent;

/**
 * This class represents a connection between two filters.
 *
 * @author Christopher
 */
public class FilterConnection extends JComponent{


	private SampleFilter from, to;
	private PopupMenu popupMenu;

	public FilterConnection(SampleFilter from, SampleFilter to){
		super();
		this.from = from;
		this.to = to;
	}

	public SampleFilter getFrom(){
		return from;
	}

	public SampleFilter getTo(){
		return to;
	}

}

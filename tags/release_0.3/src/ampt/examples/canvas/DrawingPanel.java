package ampt.examples.canvas;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JPanel;

/**
 * This class is the actual canvas that can be drawn on.  The user can
 * right-click on this panel to create a sample filter.  This class is
 * responsible for drawing the filter connections.
 * 
 * @author Christopher
 */
public class DrawingPanel extends JPanel implements ActionListener {

	private PopupMenu popupMenu;
	private Vector<SampleFilter> filters;
	private Vector<FilterConnection> filterConnections;

	public DrawingPanel(){
		super();
		filters = new Vector<SampleFilter>();
		filterConnections = new Vector<FilterConnection>();
		this.setPreferredSize(new Dimension(400, 400));
		this.setLayout(null);
		popupMenu = new PopupMenu();
		MenuItem menuItem = new MenuItem("Add Sample Filter");
		menuItem.addActionListener(this);
		popupMenu.add(menuItem);
		this.add(popupMenu);
		MouseListener popupListener = new MouseAdapter(){
		    public void mousePressed(MouseEvent e) {
		        maybeShowPopup(e);
		    }

		    public void mouseReleased(MouseEvent e) {
		        maybeShowPopup(e);
		    }

		    private void maybeShowPopup(MouseEvent e) {
		        if (e.isPopupTrigger()) {
		            popupMenu.show(e.getComponent(),
		                       e.getX(), e.getY());
		        }
		    }

		};
		this.addMouseListener(popupListener);
	}

	public void moveComponent(SampleFilter filter, Point locationOnScreen){

		Point topLeft = this.getLocationOnScreen();


		int xPos = (int) (locationOnScreen.getX() - topLeft.getX());
		xPos = xPos < 0 ? 0 : (xPos > this.getWidth() - filter.getWidth() ? this.getWidth() - filter.getWidth() : xPos);

		int yPos = (int) (locationOnScreen.getY() - topLeft.getY());
		yPos = yPos < 0 ? 0 : yPos;
		yPos = yPos < 0 ? 0 : (yPos > this.getHeight() - filter.getHeight() ? this.getHeight() - filter.getHeight() : yPos);

		filter.setLocation(xPos, yPos);

		this.repaint();
	}

	public void addFilterConnectionMouseAdapter(FilterConnectionMouseAdapter adapter){
		for(SampleFilter filter: filters){
			filter.addMouseListener(adapter);
		}
	}

	public void removeFilterConnectionMouseAdapter(FilterConnectionMouseAdapter adapter){
		for(SampleFilter filter: filters){
			filter.removeMouseListener(adapter);
		}
	}

	public void addFilterConnection(FilterConnection connection){
		this.add(connection);
		filterConnections.add(connection);
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() instanceof MenuItem){
			SampleFilter filter = new SampleFilter("Sample", this);


			this.add(filter);
			filters.add(filter);

			filter.setSize(filter.getPreferredSize());

			Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
			moveComponent(filter, mouseLocation);


		}

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Color color = g.getColor();
		g.setColor(Color.BLACK);
		for(FilterConnection connection: filterConnections){
			SampleFilter from = connection.getFrom();
			Insets fromInsets = from.getInsets();
			SampleFilter to = connection.getTo();
			Insets toInsets = to.getInsets();

			int fromX = from.getX() + from.getWidth() + fromInsets.left;
			int fromY = from.getY() + from.getHeight()/2;
			int toX = to.getX();
			int toY = to.getY() + to.getHeight()/2;

			g.drawLine(fromX, fromY, toX, toY);
		}

		g.setColor(color);

	}



}

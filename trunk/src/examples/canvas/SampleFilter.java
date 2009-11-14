package examples.canvas;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

/**
 * This class represents a sample filter.  It is used for drawing the filter
 * on the screen, as well as for handling the filter being moved, and being
 * right-clicked.
 * 
 * @author Christopher
 */
public class SampleFilter extends JComponent implements MouseMotionListener, ActionListener{

	private String text;
	private DrawingPanel dPanel;
	private PopupMenu popupMenu;

	public SampleFilter(String text, DrawingPanel dPanel){
		this.text = text;
		this.dPanel = dPanel;
		this.setPreferredSize(new Dimension(51, 51));
		this.addMouseMotionListener(this);

		popupMenu = new PopupMenu();
		MenuItem menuItem = new MenuItem("Add Filter Connection");
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

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() instanceof MenuItem){

			dPanel.addFilterConnectionMouseAdapter(new FilterConnectionMouseAdapter(this));

		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		Color color = g.getColor();
		g.setColor(Color.BLACK);

		g.drawRect(0, 0, 50, 50);

		g.drawString(text, 5, 45);

		g.setColor(color);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point point = e.getLocationOnScreen();
		dPanel.moveComponent(this, point);
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}



}

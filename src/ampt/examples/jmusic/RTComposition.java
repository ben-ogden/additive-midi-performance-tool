package ampt.examples.jmusic;

import jm.JMC;
import jm.audio.RTMixer;
import jm.audio.Instrument;
import jm.music.rt.RTLine;

import jm.inst.SawLPFInstRT2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;

public class RTComposition implements JMC, ChangeListener, ActionListener {
	private JSlider cutoff, panner;
	private RTMixer mixer;
    	private JLabel val;
    	private BassLine bass;
	private JButton goBtn;
	private RTLine[] lineArray = new RTLine[1];

	public static void main(String[] args) {
		new RTComposition();
	}

	public RTComposition() {
            int sampleRate = 44100;
            int channels = 2;

            Instrument[] instArray = new Instrument[1];
            for(int i=0;i<instArray.length;i++){
                instArray[i] = new SawLPFInstRT2(sampleRate, 1000, channels);
            }

            bass = new BassLine(instArray);
            bass.setTempo(104);
            lineArray[0] = bass;

            // show slider panel
            makeGUI();
        }

	private void makeGUI() {
		JFrame f = new JFrame("Title");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(100, 250);
		JPanel panel = new JPanel(new BorderLayout());
		f.getContentPane().add(panel);
        	// pan
        	panner = new JSlider(1, 1, 100, 50);
		panner.setEnabled(false);
		panner.addChangeListener(this);
		panel.add(panner, "West");
        	// filter
		cutoff = new JSlider(1, 1, 100, 10);
		cutoff.setEnabled(false);
		cutoff.addChangeListener(this);
		panel.add(cutoff, "Center");
        	// filter value display
        	val = new JLabel("1000");
        	panel.add(val, "East");
		// start stop
		goBtn = new JButton("Start");
		goBtn.addActionListener(this);
		panel.add(goBtn, "North");

		f.pack();
		f.setVisible(true);
	}

	public void stateChanged(ChangeEvent e){
        	// panner slider moved
        	if (e.getSource() == panner) {
            		bass.setPanValue((double)panner.getValue() / 100.0);
        	}
        	// filter slider moved
		if (e.getSource() == cutoff) {
            		val.setText("" + (cutoff.getValue() * 100));
			mixer.actionLines(cutoff, 1);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == goBtn) {
			mixer = new RTMixer(lineArray);
			mixer.begin();
			cutoff.setEnabled(true);
			panner.setEnabled(true);
			goBtn.setEnabled(false);
		}
	}
}

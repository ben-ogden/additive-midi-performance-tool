package ampt.ui.filters;

import ampt.core.devices.NoteViewerDevice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Christopher
 */
public class NoteViewerBox extends MidiDeviceBox {

    /**
     * Use a sorted map which maps the channel number to the set of notes that
     * are on for that channel.
     * 
     * NOTE: The map should be synchronized so we can have concurrent access.
     */
    SortedMap<Integer, SortedSet<Integer>> notes;
//    SortedSet<Integer> notes;
    private static final int staffX1 = 20;
    private static final int staffX2 = 80;
    private static final int ledgerX1 = 54;
    private static final int ledgerX2 = 76;
    private static final int lineSpacing = 6;
    private static final int staffLineCount = 5;
    private static final int middleCLedgerY = 125;
    private static final int middleCNoteNum = 60;
    private static final int noteHeight = 6;
    private static final int noteWidth = 8;
    private static final int lineNoteX = 56;
    private static final int spaceNoteX = 64;
    private static final int middleCNoteY = 122;
    private static final int noteSpacing = 3;
    private static final int sharpHorX1NoteMod = -18;
    private static final int sharpHorX2NoteMod = -11;
    private static final int sharpVerX1NoteMod = -15;
    private static final int sharpVerX2NoteMod = -18;
    private static final int sharpVerX3NoteMod = -11;
    private static final int sharpVerX4NoteMod = -14;
    private static final int sharpHorY1NoteMod = 2;
    private static final int sharpHorY2NoteMod = 5;
    private static final int sharpVerY1NoteMod = 0;
    private static final int sharpVerY2NoteMod = 7;
    private static final int flatOvalXNoteMod = -14;
    private static final int flatOvalYNoteMod = 1;
    private static final int flatOvalWidth = 4;
    private static final int flatOvalHeight = 4;
    private static final int flatLineX1NoteMod = -11;
    private static final int flatLineX2NoteMod = -14;
    private static final int flatLineY1NoteMod = -3;
    private static final int flatLineY2NoteMod = 3;
    private static final int bassClefX = 20;
    private static final int bassClefY = 129;
    private static final int bassClefWidth = 20;
    private static final int bassClefHeight = 22;
    private static final int trebleClefX = 15;
    private static final int trebleClefY = 90;
    private static final int trebleClefWidth = 25;
    private static final int trebleClefHeight = 40;
    private Image bassClefImage;
    private Image trebleClefImage;

    public NoteViewerBox(NoteViewerDevice device) throws MidiUnavailableException {
        super(device);

        device.addNoteViewerBox(this);

        // use a syncrhonized sorted map so we can access the map concurrently.
        notes = Collections.synchronizedSortedMap(new TreeMap<Integer, SortedSet<Integer>>());

        // put in the set of notes for each channel
        for(int channel = 0; channel < 16; channel++){
            notes.put(channel, Collections.synchronizedSortedSet(new TreeSet<Integer>()));
        }

        overridePaintComponent = false;

        this.setPreferredSize(new Dimension(100, 250));
        this.setBackground(Color.WHITE);
        this.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Viewer", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.BELOW_TOP));
        this.setLayout(null);



        try {
            InputStream bassInStream = this.getClass().getClassLoader().getResourceAsStream("images/bassclef.jpg");
            bassClefImage = ImageIO.read(bassInStream);
        } catch (IOException ex) {
            //TODO log error or throw exception?
        }
        try {
            InputStream trebleInStream = this.getClass().getClassLoader().getResourceAsStream("images/trebleclef.jpg");
            trebleClefImage = ImageIO.read(trebleInStream);
        } catch (IOException ex) {
            //TODO log error or throw exception?
        }


    }

    public void noteOn(int channel, int note) {
        notes.get(channel).add(note);
        this.repaint();
    }

    public void noteOff(int channel, int note) {
        notes.get(channel).remove(note);
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color oldColor = g.getColor();

        g.setColor(Color.BLACK);

        //Draw the bass clef image
        if (bassClefImage != null) {
            g.drawImage(bassClefImage, bassClefX, bassClefY, bassClefWidth, bassClefHeight, this);
        }

        // Draw the Treble Clef Image
        if (trebleClefImage != null) {
            g.drawImage(trebleClefImage, trebleClefX, trebleClefY, trebleClefWidth, trebleClefHeight, this);
        }


        // Draw the treble staff
        int lineCount = 0;
        int trebleStaffY = middleCLedgerY - lineSpacing;
        while (lineCount < staffLineCount) {
            g.drawLine(staffX1, trebleStaffY, staffX2, trebleStaffY);
            lineCount++;
            trebleStaffY -= lineSpacing;
        }

        // Draw the bass staff
        lineCount = 0;
        int bassStaffY = middleCLedgerY + lineSpacing;
        while (lineCount < staffLineCount) {
            g.drawLine(staffX1, bassStaffY, staffX2, bassStaffY);
            lineCount++;
            bassStaffY += lineSpacing;
        }







        int topNote = middleCNoteNum;
        int bottomNote = middleCNoteNum;
        boolean hasMiddleC = false;

        // Draw the notes
        synchronized (notes) {
            for(SortedSet<Integer> channelNotes: notes.values()){
                for(int note : channelNotes){

                    if(note > topNote){
                        topNote = note;
                    }
                    if(note < bottomNote){
                        bottomNote = note;
                    }

                    if(note == middleCNoteNum){
                        hasMiddleC = true;
                    }

                    int relativeNote = getNotePositionRelativeToMiddleC(note);

                    int noteY = middleCNoteY - (relativeNote * noteSpacing);
                    int noteX = spaceNoteX;
                    if (relativeNote % 2 == 0) {
                        noteX = lineNoteX;
                    }
                    g.drawOval(noteX, noteY, noteWidth, noteHeight);
                    g.fillOval(noteX, noteY, noteWidth, noteHeight);

                    if (getIsNoteFlat(note)) {
                        g.drawOval(flatOvalXNoteMod + noteX, flatOvalYNoteMod + noteY, flatOvalWidth, flatOvalHeight);
                        g.drawLine(flatLineX1NoteMod + noteX, flatLineY1NoteMod + noteY, flatLineX2NoteMod + noteX, flatLineY2NoteMod + noteY);
                    }
                    if (getIsNoteSharp(note)) {
                        g.drawLine(sharpHorX1NoteMod + noteX, sharpHorY1NoteMod + noteY, sharpHorX2NoteMod + noteX, sharpHorY1NoteMod + noteY);
                        g.drawLine(sharpHorX1NoteMod + noteX, sharpHorY2NoteMod + noteY, sharpHorX2NoteMod + noteX, sharpHorY2NoteMod + noteY);
                        g.drawLine(sharpVerX1NoteMod + noteX, sharpVerY1NoteMod + noteY, sharpVerX2NoteMod + noteX, sharpVerY2NoteMod + noteY);
                        g.drawLine(sharpVerX3NoteMod + noteX, sharpVerY1NoteMod + noteY, sharpVerX4NoteMod + noteX, sharpVerY2NoteMod + noteY);
                    }
                }
            }
        }

        // Draw Ledger Lines above treble staff
        if (notes.isEmpty() == false) {
            int relativeTopNote = getNotePositionRelativeToMiddleC(topNote);
            int numLedgerLines = relativeTopNote / 2 - staffLineCount;
            lineCount = 0;

            while (numLedgerLines > 0 && numLedgerLines > lineCount) {
                g.drawLine(ledgerX1, trebleStaffY, ledgerX2, trebleStaffY);
                lineCount++;
                trebleStaffY -= lineSpacing;
            }
        }
        
        // Draw Ledger Lines Below Bass Staff
        if (notes.isEmpty() == false) {
            int relativeBottomNote = getNotePositionRelativeToMiddleC(bottomNote);
            int numLedgerLines = (-relativeBottomNote) / 2 - staffLineCount;
            lineCount = 0;

            while (numLedgerLines > 0 && numLedgerLines > lineCount) {
                g.drawLine(ledgerX1, bassStaffY, ledgerX2, bassStaffY);
                lineCount++;
                bassStaffY += lineSpacing;
            }
        }

        // Draw Middle C ledger line if needed
        if (hasMiddleC) {
            g.drawLine(ledgerX1, middleCLedgerY, ledgerX2, middleCLedgerY);
        }


        //Draw arrows for connections
        int yPos = this.getHeight() / 2;
        int xMax = this.getWidth() - 3;
        if (hasTransmitter()) {
            g.drawLine(xMax - 7, yPos, xMax, yPos);
            g.drawLine(xMax - 3, yPos - 3, xMax, yPos);
            g.drawLine(xMax - 3, yPos + 3, xMax, yPos);
        }
        if (hasReceiver()) {
            g.drawLine(3, yPos, 10, yPos);
            g.drawLine(7, yPos - 3, 10, yPos);
            g.drawLine(7, yPos + 3, 10, yPos);
        }

        g.setColor(oldColor);
    }

    private boolean getIsNoteFlat(int note) {
        note -= middleCNoteNum;
        int noteMod = note % 12;
        if (noteMod == -11 || noteMod == -9 || noteMod == -6 || noteMod == -4 || noteMod == -2) {
            return true;
        }
        return false;
    }

    private boolean getIsNoteSharp(int note) {
        note -= middleCNoteNum;
        int noteMod = note % 12;
        if (noteMod == 1 || noteMod == 3 || noteMod == 6 || noteMod == 8 || noteMod == 10) {
            return true;
        }
        return false;
    }

    private int getNotePositionRelativeToMiddleC(int note) {
        note -= middleCNoteNum;

        int noteMod = note % 12;
        note -= 5 * (note / 12);
        switch (noteMod) {
            case 11:
            case -11:
                if (note < 0) {
                    note++;
                }
            case 10:
            case -10:
                if (note > 0) {
                    note--;
                }
            case 9:
            case -9:
                if (note < 0) {
                    note++;
                }
            case 8:
            case -8:
                if (note > 0) {
                    note--;
                }
            case 7:
            case -7:
            case 6:
            case -6:
                if (note > 0) {
                    note--;
                }
                if (note < 0) {
                    note++;
                }
            case 5:
            case -5:
            case 4:
            case -4:
                if (note < 0) {
                    note++;
                }
            case 3:
            case -3:
                if (note > 0) {
                    note--;
                }
            case 2:
            case -2:
                if (note < 0) {
                    note++;
                }
            case 1:
            case -1:
                if (note > 0) {
                    note--;
                }
            case 0:
                break;
        }
        return note;
    }
}

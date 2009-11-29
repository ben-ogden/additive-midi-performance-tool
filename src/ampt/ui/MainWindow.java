/*
 * MainWindow.java
 *
 * Created on Oct 21, 2009, 10:46:35 AM
 */
package ampt.ui;

import ampt.core.devices.AmptMidiDevice;
import ampt.core.devices.ChordFilterDevice;
import ampt.core.devices.KeyboardDevice;
import ampt.core.devices.NoteViewerDevice;
import ampt.core.devices.ArpFilterDevice;
import ampt.core.devices.ArpeggiatorFilterDevice;
import ampt.core.devices.EchoFilterDevice;
import ampt.core.devices.TimedDevice;
import ampt.ui.canvas.CanvasCorner;
import ampt.ui.canvas.CanvasRuler;
import ampt.ui.canvas.CanvasRuler.Orientation;
import ampt.ui.canvas.MidiDeviceButton;
import ampt.ui.filters.AmptMidiDeviceBox;
import ampt.ui.filters.ChordFilterBox;
import ampt.ui.filters.KeyboardBox;
import ampt.ui.filters.MidiDeviceBox;
import ampt.ui.filters.NoteViewerBox;
import ampt.ui.filters.ArpFilterBox;
import ampt.ui.filters.ArpeggiatorFilterBox;
import ampt.ui.filters.EchoFilterBox;
import ampt.ui.filters.SynthesizerBox;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.JOptionPane;
import javax.swing.ScrollPaneConstants;

/**
 * This is the main window for the GUI.  It contains a split pane, in which the
 * top contains any tabs that are desired, such as the canvas and the software
 * keyboard.  The bottom contains a console for any desired output.
 *
 * @author Christopher Redding
 */
public class MainWindow extends JFrame {

    /*
     * To exclude a MidiDevice from the toolbar, add the device to the list
     * below.
     */
    private List<String> excludedDevices = Arrays.asList("Microsoft MIDI Mapper",
            "Real Time Sequencer");
    
    /*
     * This is used for listening for the user clicking on the canvas panel so
     * the user can place a filter after choosing the filter from the Toolbar.
     *
     * NOTE: For some reason, Netbeans places all of the class variables at the
     * bottom of the file, so look there for any other variables you want to
     * see.
     */
    private MouseAdapter canvasButtonMouseAdapter = null;

    /** Creates new form MainWindow */
    public MainWindow() {
        initComponents();

        // register the console pane as a tempo listener
        tempoPanel.addTempoListener(consolePane);
        // register the metronome pane as a tempo listener
        tempoPanel.addTempoListener(metronomePanel);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     *
     * I have modified this method to insert all of the devices obtained from
     * calling MidiSystem.getDeviceInfo() as a button in the toolbar.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane = new javax.swing.JSplitPane();
        topPane = new javax.swing.JPanel();
        toolbarPane = new ampt.ui.canvas.CanvasToolbar();
        canvasPanel = new javax.swing.JPanel();
        canvasScrollPane = new javax.swing.JScrollPane();
        theActualCanvasPanel = new ampt.ui.canvas.CanvasPanel();
        propertiesPanel = new javax.swing.JPanel();
        metronomePanel = new ampt.ui.canvas.MetronomePanel();
        filterPropertiesPanel = new ampt.ui.canvas.FilterPropertiesPanel();
        tempoPanel = new ampt.ui.canvas.TempoPanel();
        bottomPane = new javax.swing.JPanel();
        midiConsoleLabel = new javax.swing.JLabel();
        consoleScrollPane = new javax.swing.JScrollPane();
        consolePane = new ampt.ui.canvas.AmptConsolePane();
        amptMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        viewMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Additive MIDI Performance Tool");

        splitPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        splitPane.setDividerLocation(450);
        splitPane.setDividerSize(12);
        splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setToolTipText("");
        splitPane.setOneTouchExpandable(true);
        splitPane.setPreferredSize(new java.awt.Dimension(800, 600));

        topPane.setMinimumSize(new java.awt.Dimension(200, 200));
        topPane.setPreferredSize(new java.awt.Dimension(800, 450));

        toolbarPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Canvas Toolbar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        toolbarPane.setFloatable(false);
        toolbarPane.setOrientation(1);
        toolbarPane.setRollover(true);

        Info[] deviceInfos = MidiSystem.getMidiDeviceInfo();
        for(Info deviceInfo: deviceInfos){

            // don't list excluded devices
            if(excludedDevices.contains(deviceInfo.getName())) {
                continue;
            }

            MidiDeviceButton button = new MidiDeviceButton(deviceInfo);
            if(deviceInfo.getName().equals(new KeyboardDevice().getDeviceInfo().getName())){
                MidiDeviceButton extendedButton = new MidiDeviceButton(deviceInfo);
                extendedButton.setText("Extended " + extendedButton.getText());
                extendedButton.setToolTipText("Extened " + extendedButton.getToolTipText());
                toolbarPane.add(extendedButton);
                extendedButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt){
                        buttonHandler(evt);
                    }
                });
            }

            toolbarPane.add(button);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    buttonHandler(evt);
                }
            });

        }

        canvasPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 3, 0));

        canvasScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        canvasScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        canvasScrollPane.setAutoscrolls(true);
        canvasScrollPane.setPreferredSize(new java.awt.Dimension(2002, 2002));
        canvasScrollPane.setColumnHeaderView(new CanvasRuler(Orientation.Horizontal));
        canvasScrollPane.setRowHeaderView(new CanvasRuler(Orientation.Vertical));
        canvasScrollPane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, new CanvasCorner());
        canvasScrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, new CanvasCorner());
        canvasScrollPane.setCorner(ScrollPaneConstants.LOWER_LEFT_CORNER, new CanvasCorner());

        theActualCanvasPanel.setBackground(new java.awt.Color(255, 255, 255));
        theActualCanvasPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        theActualCanvasPanel.setMaximumSize(new java.awt.Dimension(3200, 3200));
        theActualCanvasPanel.setMinimumSize(new java.awt.Dimension(3200, 3200));

        javax.swing.GroupLayout theActualCanvasPanelLayout = new javax.swing.GroupLayout(theActualCanvasPanel);
        theActualCanvasPanel.setLayout(theActualCanvasPanelLayout);
        theActualCanvasPanelLayout.setHorizontalGroup(
            theActualCanvasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3196, Short.MAX_VALUE)
        );
        theActualCanvasPanelLayout.setVerticalGroup(
            theActualCanvasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3196, Short.MAX_VALUE)
        );

        canvasScrollPane.setViewportView(theActualCanvasPanel);

        javax.swing.GroupLayout canvasPanelLayout = new javax.swing.GroupLayout(canvasPanel);
        canvasPanel.setLayout(canvasPanelLayout);
        canvasPanelLayout.setHorizontalGroup(
            canvasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(canvasScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
        );
        canvasPanelLayout.setVerticalGroup(
            canvasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(canvasScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
        );

        metronomePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Metronome", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        metronomePanel.setMinimumSize(new java.awt.Dimension(0, 0));
        metronomePanel.setPreferredSize(new java.awt.Dimension(200, 80));

        filterPropertiesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filter Properties", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        filterPropertiesPanel.setPreferredSize(new java.awt.Dimension(200, 250));

        javax.swing.GroupLayout filterPropertiesPanelLayout = new javax.swing.GroupLayout(filterPropertiesPanel);
        filterPropertiesPanel.setLayout(filterPropertiesPanelLayout);
        filterPropertiesPanelLayout.setHorizontalGroup(
            filterPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 195, Short.MAX_VALUE)
        );
        filterPropertiesPanelLayout.setVerticalGroup(
            filterPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 253, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout propertiesPanelLayout = new javax.swing.GroupLayout(propertiesPanel);
        propertiesPanel.setLayout(propertiesPanelLayout);
        propertiesPanelLayout.setHorizontalGroup(
            propertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, propertiesPanelLayout.createSequentialGroup()
                .addGroup(propertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(filterPropertiesPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                    .addComponent(tempoPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                    .addComponent(metronomePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE))
                .addContainerGap())
        );
        propertiesPanelLayout.setVerticalGroup(
            propertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(propertiesPanelLayout.createSequentialGroup()
                .addComponent(tempoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(metronomePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterPropertiesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout topPaneLayout = new javax.swing.GroupLayout(topPane);
        topPane.setLayout(topPaneLayout);
        topPaneLayout.setHorizontalGroup(
            topPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPaneLayout.createSequentialGroup()
                .addComponent(toolbarPane, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(canvasPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(propertiesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        topPaneLayout.setVerticalGroup(
            topPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolbarPane, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
            .addComponent(propertiesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(canvasPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        splitPane.setTopComponent(topPane);

        bottomPane.setPreferredSize(new java.awt.Dimension(800, 150));

        midiConsoleLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
        midiConsoleLabel.setText("MIDI Console");
        midiConsoleLabel.setToolTipText("The AMPT MIDI Console displays MIDI events and other messages from AMPT filters.");

        consoleScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        consoleScrollPane.setToolTipText("The AMPT MIDI Console displays MIDI events and other messages from AMPT filters.");
        consoleScrollPane.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());
        consoleScrollPane.setFocusable(false);

        consolePane.setBorder(null);
        consolePane.setFont(new java.awt.Font("Monospaced", 0, 12));
        consolePane.setForeground(new java.awt.Color(51, 51, 51));
        consolePane.setToolTipText("The AMPT MIDI Console displays MIDI events and other messages from AMPT filters.");
        consolePane.setMargin(new java.awt.Insets(10, 10, 10, 10));
        consoleScrollPane.setViewportView(consolePane);

        javax.swing.GroupLayout bottomPaneLayout = new javax.swing.GroupLayout(bottomPane);
        bottomPane.setLayout(bottomPaneLayout);
        bottomPaneLayout.setHorizontalGroup(
            bottomPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bottomPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(consoleScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                    .addComponent(midiConsoleLabel))
                .addContainerGap())
        );
        bottomPaneLayout.setVerticalGroup(
            bottomPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(midiConsoleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(consoleScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addContainerGap())
        );

        splitPane.setBottomComponent(bottomPane);

        amptMenuBar.setPreferredSize(new java.awt.Dimension(800, 21));

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        amptMenuBar.add(fileMenu);

        editMenu.setMnemonic('e');
        editMenu.setText("Edit");
        amptMenuBar.add(editMenu);

        viewMenu.setMnemonic('v');
        viewMenu.setText("View");

        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            JMenuItem item = new JMenuItem(info.getName());

            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    viewMenuHandler(evt);
                }
            });

            viewMenu.add(item);
        }

        amptMenuBar.add(viewMenu);

        setJMenuBar(amptMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed

        // clean up
        theActualCanvasPanel.closeAllDevices();

        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    private void viewMenuHandler(ActionEvent event){
        JMenuItem source = (JMenuItem) event.getSource();
        String lookAndFeelName = source.getText();
        for(LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()){
            if(lookAndFeelName.equals(info.getName())){
                try{
                    UIManager.setLookAndFeel(info.getClassName());
                    SwingUtilities.updateComponentTreeUI(this);
                    this.pack();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * A button handler for all of the buttons on the toolbar.  This method
     * registers a mouse adapter on the canvas so the box that represents a
     * MidiDevice or a filter can be placed on the canvas.
     *
     * @param evt
     */
    private void buttonHandler(ActionEvent evt) {


        final Object source = evt.getSource();
        if (canvasButtonMouseAdapter != null) {
            theActualCanvasPanel.removeMouseListener(canvasButtonMouseAdapter);
        }

        canvasButtonMouseAdapter = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                
                theActualCanvasPanel.removeMouseListener(this);
                Point point = e.getPoint();
                if (source instanceof MidiDeviceButton) {
                    try {

                        MidiDeviceButton deviceButton = (MidiDeviceButton) source;
                        MidiDevice device = MidiSystem.getMidiDevice(deviceButton.getDeviceInfo());

                        MidiDeviceBox box = null;

                        if (device instanceof AmptMidiDevice) {
                            box = addAmptMidiDeviceBox((AmptMidiDevice) device,
                                    deviceButton);
                        } else if (device instanceof Synthesizer) {
                            box = new SynthesizerBox((Synthesizer) device);
                        } else {
                            box = new MidiDeviceBox(device);
                        }

                        theActualCanvasPanel.add(box);
                        box.setLocation(point);
                        box.setSize(box.getPreferredSize());
                        box.validate();
                        theActualCanvasPanel.repaint();

                    } catch (MidiUnavailableException ex) {
                        // send message to console pane and show alert
                        final String msg = "Unable to add device to canvas. ";
                        consolePane.append(msg + ex.getMessage() + "\n");
                        JOptionPane.showMessageDialog(null,
                                "Unable to add device  to canvas.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        theActualCanvasPanel.addMouseListener(canvasButtonMouseAdapter);
    }

    private MidiDeviceBox addAmptMidiDeviceBox(AmptMidiDevice device,
            MidiDeviceButton deviceButton) throws MidiUnavailableException {

        AmptMidiDeviceBox box = null;

        device.setMidiDebugEnabled(true);

        if (device instanceof KeyboardDevice) {
            KeyboardDevice keyboard = (KeyboardDevice) device;
            keyboard.setLogger(consolePane.getPrintStream(Color.CYAN));
            if (deviceButton.getText().matches(".*[eE]xtended.*")) {
                box = new KeyboardBox(keyboard, true);
            } else {
                box = new KeyboardBox(keyboard, false);
            }
        } else if (device instanceof ChordFilterDevice) {
            ChordFilterDevice chordDevice = (ChordFilterDevice) device;
            chordDevice.setLogger(consolePane.getPrintStream(Color.BLUE));
            box = new ChordFilterBox(chordDevice);
        } else if (device instanceof NoteViewerDevice) {
            NoteViewerDevice noteViewerDevice = (NoteViewerDevice) device;
            noteViewerDevice.setLogger(consolePane.getPrintStream(Color.RED));
            box = new NoteViewerBox(noteViewerDevice, consolePane.getPrintStream());
        } else if (device instanceof ArpFilterDevice) {
            ArpFilterDevice arpFilterDevice = (ArpFilterDevice) device;
            arpFilterDevice.setLogger(consolePane.getPrintStream(Color.GREEN));
            box = new ArpFilterBox(arpFilterDevice);
        } else if (device instanceof ArpeggiatorFilterDevice) {
            ArpeggiatorFilterDevice arpeggiatorDevice = (ArpeggiatorFilterDevice) device;
            arpeggiatorDevice.setLogger(consolePane.getPrintStream(Color.ORANGE));
            arpeggiatorDevice.setMidiDebugEnabled(true);
            box = new ArpeggiatorFilterBox(arpeggiatorDevice);
        } else if (device instanceof EchoFilterDevice) {
            EchoFilterDevice echoDevice = (EchoFilterDevice) device;
            echoDevice.setLogger(consolePane.getPrintStream(Color.MAGENTA));
            box = new EchoFilterBox(echoDevice, consolePane.getPrintStream());
        }

        // register timed devices as tempo listeners
        if (device instanceof TimedDevice) {
            tempoPanel.addTempoListener(box);
        }

        return box;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar amptMenuBar;
    private javax.swing.JPanel bottomPane;
    private javax.swing.JPanel canvasPanel;
    private javax.swing.JScrollPane canvasScrollPane;
    private ampt.ui.canvas.AmptConsolePane consolePane;
    private javax.swing.JScrollPane consoleScrollPane;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private ampt.ui.canvas.FilterPropertiesPanel filterPropertiesPanel;
    private ampt.ui.canvas.MetronomePanel metronomePanel;
    private javax.swing.JLabel midiConsoleLabel;
    private javax.swing.JPanel propertiesPanel;
    private javax.swing.JSplitPane splitPane;
    private ampt.ui.canvas.TempoPanel tempoPanel;
    private ampt.ui.canvas.CanvasPanel theActualCanvasPanel;
    private ampt.ui.canvas.CanvasToolbar toolbarPane;
    private javax.swing.JPanel topPane;
    private javax.swing.JMenu viewMenu;
    // End of variables declaration//GEN-END:variables
}

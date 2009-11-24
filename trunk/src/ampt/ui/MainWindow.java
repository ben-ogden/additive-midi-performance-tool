/*
 * MainWindow.java
 *
 * Created on Oct 21, 2009, 10:46:35 AM
 */
package ampt.ui;

import ampt.core.devices.ChordFilterDevice;
import ampt.core.devices.KeyboardDevice;
import ampt.core.devices.NoteViewerDevice;
import ampt.core.devices.ArpFilterDevice;
import ampt.ui.canvas.MidiDeviceButton;
import ampt.ui.filters.ChordFilterBox;
import ampt.ui.filters.KeyboardBox;
import ampt.ui.filters.MidiDeviceBox;
import ampt.ui.filters.NoteViewerBox;
import ampt.ui.filters.ArpFilterBox;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.JOptionPane;

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
    private List<String> excludedDevices = Arrays.asList("Microsoft MIDI Mapper");
    
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
        //TODO - is the best way to start maximized?
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
        metronomePanel1 = new ampt.ui.canvas.MetronomePanel();
        filterPropertiesPanel1 = new ampt.ui.canvas.FilterPropertiesPanel();
        bottomPane = new javax.swing.JPanel();
        midiConsoleLabel = new javax.swing.JLabel();
        consoleScrollPane = new javax.swing.JScrollPane();
        consolePane = new ampt.ui.canvas.AmptConsolePane();
        amptMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
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

        toolbarPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Canvas Toolbar"));
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
            toolbarPane.add(button);
            button.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonHandler(evt);
                }
            });

        }

        canvasPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Canvas"));

        canvasScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        canvasScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        canvasScrollPane.setAutoscrolls(true);
        canvasScrollPane.setPreferredSize(new java.awt.Dimension(2002, 2002));

        theActualCanvasPanel.setBackground(new java.awt.Color(255, 255, 255));
        theActualCanvasPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        theActualCanvasPanel.setPreferredSize(new java.awt.Dimension(1000, 1000));

        javax.swing.GroupLayout theActualCanvasPanelLayout = new javax.swing.GroupLayout(theActualCanvasPanel);
        theActualCanvasPanel.setLayout(theActualCanvasPanelLayout);
        theActualCanvasPanelLayout.setHorizontalGroup(
            theActualCanvasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 996, Short.MAX_VALUE)
        );
        theActualCanvasPanelLayout.setVerticalGroup(
            theActualCanvasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 996, Short.MAX_VALUE)
        );

        canvasScrollPane.setViewportView(theActualCanvasPanel);

        javax.swing.GroupLayout canvasPanelLayout = new javax.swing.GroupLayout(canvasPanel);
        canvasPanel.setLayout(canvasPanelLayout);
        canvasPanelLayout.setHorizontalGroup(
            canvasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(canvasScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
        );
        canvasPanelLayout.setVerticalGroup(
            canvasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(canvasScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );

        metronomePanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Metronome"));

        filterPropertiesPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Filter Properties"));

        javax.swing.GroupLayout filterPropertiesPanel1Layout = new javax.swing.GroupLayout(filterPropertiesPanel1);
        filterPropertiesPanel1.setLayout(filterPropertiesPanel1Layout);
        filterPropertiesPanel1Layout.setHorizontalGroup(
            filterPropertiesPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 175, Short.MAX_VALUE)
        );
        filterPropertiesPanel1Layout.setVerticalGroup(
            filterPropertiesPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 334, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout propertiesPanelLayout = new javax.swing.GroupLayout(propertiesPanel);
        propertiesPanel.setLayout(propertiesPanelLayout);
        propertiesPanelLayout.setHorizontalGroup(
            propertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(metronomePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
            .addComponent(filterPropertiesPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        propertiesPanelLayout.setVerticalGroup(
            propertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(propertiesPanelLayout.createSequentialGroup()
                .addComponent(metronomePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterPropertiesPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        midiConsoleLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
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
                .addComponent(consoleScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        splitPane.setBottomComponent(bottomPane);

        amptMenuBar.setPreferredSize(new java.awt.Dimension(800, 21));

        fileMenu.setText("File");

        jMenuItem1.setText("Exit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        amptMenuBar.add(fileMenu);

        editMenu.setText("Edit");
        amptMenuBar.add(editMenu);

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

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

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
                        if (device instanceof KeyboardDevice) {
                            KeyboardDevice keyboard = (KeyboardDevice) device;
                            keyboard.setLogger(consolePane.getPrintStream(Color.CYAN));
                            keyboard.setMidiDebugEnabled(true);
                            box = new KeyboardBox(keyboard);
                        } else if (device instanceof ChordFilterDevice) {
                            ChordFilterDevice chordDevice = (ChordFilterDevice) device;
                            chordDevice.setLogger(consolePane.getPrintStream(Color.BLUE));
                            chordDevice.setMidiDebugEnabled(true);
                            box = new ChordFilterBox(chordDevice);
                        } else if (device instanceof NoteViewerDevice) {
                            NoteViewerDevice noteViewerDevice = (NoteViewerDevice) device;
                            noteViewerDevice.setLogger(consolePane.getPrintStream(Color.RED));
                            noteViewerDevice.setMidiDebugEnabled(true);
                            box = new NoteViewerBox(noteViewerDevice);
                        } else if (device instanceof ArpFilterDevice){
                            ArpFilterDevice arpFilterDevice = (ArpFilterDevice) device;
                            arpFilterDevice.setLogger(consolePane.getPrintStream(Color.GREEN));
                            arpFilterDevice.setMidiDebugEnabled(true);
                            box = new ArpFilterBox(arpFilterDevice);
                        }else {
                            box = new MidiDeviceBox(device);
                        }
                        theActualCanvasPanel.add(box);
                        box.setLocation(point);
                        box.setSize(box.getPreferredSize());
                        box.validate();
                        theActualCanvasPanel.repaint();
                    } catch (MidiUnavailableException ex) {

                        JOptionPane.showMessageDialog(null,
                                "Unable to add device  to canvas. " + ex.getLocalizedMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        theActualCanvasPanel.addMouseListener(canvasButtonMouseAdapter);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar amptMenuBar;
    private javax.swing.JPanel bottomPane;
    private javax.swing.JPanel canvasPanel;
    private javax.swing.JScrollPane canvasScrollPane;
    private ampt.ui.canvas.AmptConsolePane consolePane;
    private javax.swing.JScrollPane consoleScrollPane;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private ampt.ui.canvas.FilterPropertiesPanel filterPropertiesPanel1;
    private javax.swing.JMenuItem jMenuItem1;
    private ampt.ui.canvas.MetronomePanel metronomePanel1;
    private javax.swing.JLabel midiConsoleLabel;
    private javax.swing.JPanel propertiesPanel;
    private javax.swing.JSplitPane splitPane;
    private ampt.ui.canvas.CanvasPanel theActualCanvasPanel;
    private ampt.ui.canvas.CanvasToolbar toolbarPane;
    private javax.swing.JPanel topPane;
    private javax.swing.JMenu viewMenu;
    // End of variables declaration//GEN-END:variables
}

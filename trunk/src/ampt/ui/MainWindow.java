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
import ampt.core.devices.ArpeggiatorFilterDevice;
import ampt.core.devices.EchoFilterDevice;
import ampt.core.devices.ExtendedKeyboardDevice;
import ampt.core.devices.PanoramaDevice;
import ampt.core.devices.TimedDevice;
import ampt.ui.canvas.CanvasCorner;
import ampt.ui.canvas.CanvasRuler;
import ampt.ui.canvas.CanvasRuler.Orientation;
import ampt.ui.filters.AmptMidiDeviceBox;
import ampt.ui.filters.ChordFilterBox;
import ampt.ui.filters.KeyboardBox;
import ampt.ui.filters.MidiDeviceBox;
import ampt.ui.filters.NoteViewerBox;
import ampt.ui.filters.ArpeggiatorFilterBox;
import ampt.ui.filters.EchoFilterBox;
import ampt.ui.filters.PanoramaDeviceBox;
import ampt.ui.filters.SynthesizerBox;
import ampt.ui.help.AboutDialog;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 * This is the main window for the GUI.  It contains a split pane, in which the
 * top contains any tabs that are desired, such as the canvas and the software
 * keyboard.  The bottom contains a console for any desired output.
 *
 * @author Christopher Redding
 */
public class MainWindow extends JFrame {

    private static final long serialVersionUID = -8513780403274592269L;

    /*
     * To exclude a MidiDevice from the toolbar, add the device to the list
     * below.
     */
    private List<String> excludedDevices = Arrays.asList("Microsoft MIDI Mapper",
            "Real Time Sequencer");
    
    private AboutDialog aboutDialog = new AboutDialog(this);;

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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        canvasPanel = new javax.swing.JPanel();
        viewMenu = new javax.swing.JMenu();
        splitPane = new javax.swing.JSplitPane();
        topPane = new javax.swing.JPanel();
        topSplitPane = new javax.swing.JSplitPane();
        topLeftPanel = new javax.swing.JPanel();
        treeScrollPane = new javax.swing.JScrollPane();
        midiDeviceTree = new javax.swing.JTree();
        tempoPanel = new ampt.ui.canvas.TempoPanel();
        metronomePanel = new ampt.ui.canvas.MetronomePanel();
        topRightPanel = new javax.swing.JPanel();
        canvasScrollPane = new javax.swing.JScrollPane();
        theActualCanvasPanel = new ampt.ui.canvas.CanvasPanel();
        bottomPane = new javax.swing.JPanel();
        midiConsoleLabel = new javax.swing.JLabel();
        consoleScrollPane = new javax.swing.JScrollPane();
        consolePane = new ampt.ui.canvas.AmptConsolePane();
        amptMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        guideMenuItem = new javax.swing.JMenuItem();
        helpSeparator = new javax.swing.JSeparator();
        aboutMenuItem = new javax.swing.JMenuItem();

        javax.swing.GroupLayout canvasPanelLayout = new javax.swing.GroupLayout(canvasPanel);
        canvasPanel.setLayout(canvasPanelLayout);
        canvasPanelLayout.setHorizontalGroup(
            canvasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        );
        canvasPanelLayout.setVerticalGroup(
            canvasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        );

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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Additive MIDI Performance Tool");

        splitPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        splitPane.setDividerLocation(450);
        splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setToolTipText("");
        splitPane.setOneTouchExpandable(true);
        splitPane.setPreferredSize(new java.awt.Dimension(800, 600));

        topPane.setMinimumSize(new java.awt.Dimension(200, 200));
        topPane.setPreferredSize(new java.awt.Dimension(800, 450));

        topSplitPane.setDividerLocation(200);
        topSplitPane.setOneTouchExpandable(true);

        topLeftPanel.setPreferredSize(new java.awt.Dimension(200, 446));

        treeScrollPane.setPreferredSize(new java.awt.Dimension(200, 300));

        midiDeviceTree.setModel(buildMidiDeviceTreeModel());
        midiDeviceTree.setToolTipText("Select a device then click on the canvas area to add.");
        midiDeviceTree.setFocusable(false);
        midiDeviceTree.setPreferredSize(new java.awt.Dimension(200, 300));
        midiDeviceTree.setRequestFocusEnabled(false);
        midiDeviceTree.setRootVisible(false);
        midiDeviceTree.setRowHeight(18);
        treeScrollPane.setViewportView(midiDeviceTree);
        midiDeviceTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                buttonHandler(e);
            }
        });

        midiDeviceTree.setRootVisible(false);

        // disable keyboard input to prevent conflicts with ampt devices
        midiDeviceTree.getInputMap().setParent(null);
        midiDeviceTree.setInputMap(JTree.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, null);
        midiDeviceTree.setInputMap(JTree.WHEN_FOCUSED, null);
        midiDeviceTree.setInputMap(JTree.WHEN_IN_FOCUSED_WINDOW, null);

        metronomePanel.setMinimumSize(new java.awt.Dimension(0, 0));

        javax.swing.GroupLayout topLeftPanelLayout = new javax.swing.GroupLayout(topLeftPanel);
        topLeftPanel.setLayout(topLeftPanelLayout);
        topLeftPanelLayout.setHorizontalGroup(
            topLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(treeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
            .addComponent(metronomePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
            .addComponent(tempoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
        );
        topLeftPanelLayout.setVerticalGroup(
            topLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topLeftPanelLayout.createSequentialGroup()
                .addComponent(tempoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(metronomePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(treeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE))
        );

        topSplitPane.setLeftComponent(topLeftPanel);

        canvasScrollPane.setBorder(null);
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

        javax.swing.GroupLayout topRightPanelLayout = new javax.swing.GroupLayout(topRightPanel);
        topRightPanel.setLayout(topRightPanelLayout);
        topRightPanelLayout.setHorizontalGroup(
            topRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(canvasScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
        );
        topRightPanelLayout.setVerticalGroup(
            topRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(canvasScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
        );

        topSplitPane.setRightComponent(topRightPanel);

        javax.swing.GroupLayout topPaneLayout = new javax.swing.GroupLayout(topPane);
        topPane.setLayout(topPaneLayout);
        topPaneLayout.setHorizontalGroup(
            topPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(topSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
        );
        topPaneLayout.setVerticalGroup(
            topPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(topSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
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

        consolePane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 4, 4, 4));
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
                .addComponent(midiConsoleLabel)
                .addContainerGap(715, Short.MAX_VALUE))
            .addComponent(consoleScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
        );
        bottomPaneLayout.setVerticalGroup(
            bottomPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomPaneLayout.createSequentialGroup()
                .addComponent(midiConsoleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(consoleScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
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

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        guideMenuItem.setMnemonic('g');
        guideMenuItem.setText("User's Guide");
        guideMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userGuideHandler(evt);
            }
        });
        helpMenu.add(guideMenuItem);
        helpMenu.add(helpSeparator);

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        amptMenuBar.add(helpMenu);

        setJMenuBar(amptMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed

        // clean up
        theActualCanvasPanel.closeAllDevices();

        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void userGuideHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userGuideHandler
        try {
            URI userGuideUri = new URI("http", "ampt.sourceforge.net", "/guide", null);
            Desktop.getDesktop().browse(userGuideUri);
        } catch (URISyntaxException ex){
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_userGuideHandler

    /*
     * Show the About dialog.
     */
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed

        int x = this.getX() + this.getWidth() / 2 - aboutDialog.getWidth() / 2;
        int y = this.getY() + this.getHeight() / 2 - aboutDialog.getHeight() / 2;

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        // make sure the dialog ends up on the screen if app center if off screen
        if(x < 0) {
            x = 0;
        } else if (screenSize.getWidth() < (x + aboutDialog.getWidth())) {
            x = (int) (screenSize.getWidth() - aboutDialog.getWidth());
        }
        if(y < 0) {
            y = 0;
        } else if (screenSize.getHeight() < (y + aboutDialog.getHeight())) {
             y = (int) (screenSize.getHeight() - aboutDialog.getHeight());
        }

        aboutDialog.setLocation(x, y);
        aboutDialog.setVisible(true);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                new MainWindow().setVisible(true);
            }
        });
    }

    private void viewMenuHandler(ActionEvent event){

        int windowState = this.getExtendedState();

        JMenuItem source = (JMenuItem) event.getSource();
        String lookAndFeelName = source.getText();
        for(LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()){
            if(lookAndFeelName.equals(info.getName())){
                try{
                    UIManager.setLookAndFeel(info.getClassName());                    
                    SwingUtilities.updateComponentTreeUI(this);
                    SwingUtilities.updateComponentTreeUI(aboutDialog);
                    aboutDialog.pack();
                    this.pack();

                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        setExtendedState(windowState);
    }

    /*
     * Add buttons to the toolbar
     */
    private TreeModel buildMidiDeviceTreeModel() {

        Info[] deviceInfos = MidiSystem.getMidiDeviceInfo();

        List<Info> inList = new LinkedList<Info>();
        List<Info> thruList = new LinkedList<Info>();
        List<Info> outList = new LinkedList<Info>();

        MidiDevice device;

        for (Info deviceInfo : deviceInfos) {

            // don't list excluded devices
            if (excludedDevices.contains(deviceInfo.getName())) {
                continue;
            }

            try {
                device = MidiSystem.getMidiDevice(deviceInfo);
            } catch (MidiUnavailableException ex) {
                consolePane.append("Can access device. " + ex.getMessage() + "\n");
                continue;
            }
            
            int maxReceivers = device.getMaxReceivers();
            int maxTransmitters = device.getMaxTransmitters();
            if(maxReceivers != 0 && maxTransmitters == 0) {
                outList.add(deviceInfo);
            } else if(maxReceivers == 0 && maxTransmitters != 0) {
                inList.add(deviceInfo);
            } else {
                // everything else except keyboard should be treated as filter
                if(!(device instanceof KeyboardDevice)) {
                    thruList.add(deviceInfo);
                } else {
                    inList.add(deviceInfo); // add keyboard to controller list
                }
            }
        }

        // create tree
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("AMPT");

        rootNode.add(buildDeviceTree("Controllers (IN)", inList));
        rootNode.add(buildDeviceTree("Filters (THRU)", thruList));
        rootNode.add(buildDeviceTree("Devices (OUT)", outList));

        return new DefaultTreeModel(rootNode);
    }



    private DefaultMutableTreeNode buildDeviceTree(String label, List<Info> infoList) {

        DefaultMutableTreeNode node = new DefaultMutableTreeNode(label, true);

        for(Info info : infoList) {
            node.add(new DefaultMutableTreeNode(info, false));
        }
        return node;
    }

    /*
     * A button handler for all of the buttons on the toolbar.  This method
     * registers a mouse adapter on the canvas so the box that represents a
     * MidiDevice or a filter can be placed on the canvas.
     */
    private void buttonHandler(TreeSelectionEvent evt) {

        final Object source = evt.getSource();
        if (canvasButtonMouseAdapter != null) {
            theActualCanvasPanel.removeMouseListener(canvasButtonMouseAdapter);
        }

        canvasButtonMouseAdapter = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                
                theActualCanvasPanel.removeMouseListener(this);
                Point point = e.getPoint();
                if (source instanceof JTree) {
                    try {

                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                                midiDeviceTree.getLastSelectedPathComponent();
                        if (node == null || !node.isLeaf()) {
                            return;
                        }
                        
                        Info nodeInfo = (Info)node.getUserObject();

                        MidiDevice device = MidiSystem.getMidiDevice(nodeInfo);

                        MidiDeviceBox box = null;

                        if (device instanceof AmptMidiDevice) {
                            box = addAmptMidiDeviceBox((AmptMidiDevice) device);
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

    private MidiDeviceBox addAmptMidiDeviceBox(AmptMidiDevice device)
            throws MidiUnavailableException {

        AmptMidiDeviceBox box = null;

        device.setMidiDebugEnabled(true);

        if (device instanceof KeyboardDevice) {
            KeyboardDevice keyboard = (KeyboardDevice) device;
            keyboard.setLogger(consolePane.getPrintStream(Color.CYAN));
            //if (deviceButton.getText().matches(".*[eE]xtended.*")) {
            if (device instanceof ExtendedKeyboardDevice) {
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
        } else if (device instanceof ArpeggiatorFilterDevice) {
            ArpeggiatorFilterDevice arpeggiatorDevice = (ArpeggiatorFilterDevice) device;
            arpeggiatorDevice.setLogger(consolePane.getPrintStream(Color.ORANGE));
            arpeggiatorDevice.setMidiDebugEnabled(true);
            box = new ArpeggiatorFilterBox(arpeggiatorDevice);
        } else if (device instanceof EchoFilterDevice) {
            EchoFilterDevice echoDevice = (EchoFilterDevice) device;
            echoDevice.setLogger(consolePane.getPrintStream(Color.MAGENTA));
            box = new EchoFilterBox(echoDevice, consolePane.getPrintStream());
        } else if (device instanceof PanoramaDevice) {
            PanoramaDevice panDevice = (PanoramaDevice) device;
            panDevice.setLogger(consolePane.getPrintStream(Color.YELLOW));
            box = new PanoramaDeviceBox(panDevice);
        } else {
            consolePane.append(String.format("Unable to load device! Class:%s\n",
                    device.getClass().getName()));
        }

        // register timed devices as tempo listeners
        if (device instanceof TimedDevice) {
            tempoPanel.addTempoListener(box);
        }

        return box;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuBar amptMenuBar;
    private javax.swing.JPanel bottomPane;
    private javax.swing.JPanel canvasPanel;
    private javax.swing.JScrollPane canvasScrollPane;
    private ampt.ui.canvas.AmptConsolePane consolePane;
    private javax.swing.JScrollPane consoleScrollPane;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem guideMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JSeparator helpSeparator;
    private ampt.ui.canvas.MetronomePanel metronomePanel;
    private javax.swing.JLabel midiConsoleLabel;
    private javax.swing.JTree midiDeviceTree;
    private javax.swing.JSplitPane splitPane;
    private ampt.ui.canvas.TempoPanel tempoPanel;
    private ampt.ui.canvas.CanvasPanel theActualCanvasPanel;
    private javax.swing.JPanel topLeftPanel;
    private javax.swing.JPanel topPane;
    private javax.swing.JPanel topRightPanel;
    private javax.swing.JSplitPane topSplitPane;
    private javax.swing.JScrollPane treeScrollPane;
    private javax.swing.JMenu viewMenu;
    // End of variables declaration//GEN-END:variables
}

package ampt.ui.canvas;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.JButton;

/**
 * This is a button that is placed on the CanvasToolbar.  It represents a
 * MidiDevice that can be placed on the canvas.
 *
 * @author Christopher
 */
public class MidiDeviceButton extends JButton {

    private Info deviceInfo;

    public MidiDeviceButton(Info deviceInfo) {
        this.deviceInfo = deviceInfo;
        this.setText(deviceInfo.getName());
        this.setToolTipText("Name: " + deviceInfo.getName() + " Description: "
                + deviceInfo.getDescription() + " Vendor: "
                + deviceInfo.getVendor() + " Version: "
                + deviceInfo.getVersion());
    }

    public Info getDeviceInfo(){
        return deviceInfo;
    }
}

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

        // use description for tooltip, otherwise device name
        String description = deviceInfo.getDescription();
        if(null != description && description.length() > 0) {
            this.setToolTipText(description);
        } else {
            this.setToolTipText(deviceInfo.getName());
        }
    }

    public Info getDeviceInfo(){
        return deviceInfo;
    }
}

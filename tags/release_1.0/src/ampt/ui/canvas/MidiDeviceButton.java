package ampt.ui.canvas;

import java.awt.Font;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.JButton;

/**
 * This is a button that is placed on the CanvasToolbar.  It represents a
 * MidiDevice that can be placed on the canvas.
 *
 * @author Christopher
 */
public class MidiDeviceButton extends JButton {

	private static final long serialVersionUID = 4596058985580742449L;

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

        this.setFont(new Font("Tahoma", Font.PLAIN, 11));

    }

    public Info getDeviceInfo(){
        return deviceInfo;
    }
}

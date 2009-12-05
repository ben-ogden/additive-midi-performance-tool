package ampt.core.devices;

/**
 * ExtendedKeyboardDevice is a KeyBoardDevice that will support enhanced
 * features, such as a possibly a larger playing area.
 *
 * @author Ben
 */
public class ExtendedKeyboardDevice extends KeyboardDevice {

    private static final String DEVICE_NAME = "Extended Keyboard";
    private static final String DEVICE_DESCRIPTION = "A software MIDI keyboard";

    public ExtendedKeyboardDevice() {
        super(1, 93, 5, DEVICE_NAME, DEVICE_DESCRIPTION);
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ampt.ui.filters;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox.KeySelectionManager;

/**
 * This is a empty key selection manager to be used for a JComboBox where
 * you do not want any keys to correspond to any choices from the combo box.
 *
 *
 * @author Christopher
 */
public class EmptyKeySelectionManager implements KeySelectionManager{

    @Override
    public int selectionForKey(char aKey, ComboBoxModel aModel) {
        return -1;
    }

}

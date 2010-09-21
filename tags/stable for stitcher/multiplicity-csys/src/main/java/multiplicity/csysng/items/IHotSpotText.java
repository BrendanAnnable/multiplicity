package multiplicity.csysng.items;

import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.keyboard.model.KeyboardDefinition;

public interface IHotSpotText extends IEditableText, IHotSpotFrame {

    public void initializeGeometry();
    
    public boolean isKeyboardVisible();

    public IFrame getKeyboard();

    public void setKeyboardVisible(boolean isKeyboardShown);

    public int tap();

    public void resetTaps();

    public void toggle();
    
    public void createKeyboard(Class<? extends KeyboardDefinition> keyboardDef);
    
    public void createText(String text);


}

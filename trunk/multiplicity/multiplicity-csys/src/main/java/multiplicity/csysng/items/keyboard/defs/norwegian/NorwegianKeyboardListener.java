package multiplicity.csysng.items.keyboard.defs.norwegian;

import java.awt.event.KeyEvent;

import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.IEditableText;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.keyboard.IKeyboard;
import multiplicity.csysng.items.keyboard.behaviour.IMultiTouchKeyboardListener;
import multiplicity.csysng.items.keyboard.model.KeyModifiers;
import multiplicity.csysng.items.keyboard.model.KeyboardKey;

public class NorwegianKeyboardListener implements IMultiTouchKeyboardListener {

    
    private IEditableText editItem;
    private IKeyboard keyboard;
    
    public NorwegianKeyboardListener(IEditableText editItem, IKeyboard keyboard) {
        this.editItem = editItem;
        this.keyboard = keyboard;
    }

    @Override
    public void keyPressed(KeyboardKey k, boolean shiftDown, boolean altDown,
            boolean ctlDown) {
        if(k.getKeyCode() == 222) {
            if(!shiftDown)
                editItem.appendString("æ");
            else
                editItem.appendString("Æ");
        } else if(k.getKeyCode() == 59) {
            if(!shiftDown)
                editItem.appendString("ø");
            else
                editItem.appendString("Ø");
        } else if(k.getKeyCode() == 91) {
             if(!shiftDown)
                 editItem.appendString("å");
             else
                 editItem.appendString("Å");
         } else if(k.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            editItem.removeChar();
            
            
        }else if(k.getKeyCode() == KeyEvent.VK_ENTER) {
            // ignore
        }else if(k.getModifiers() == KeyModifiers.NONE) {               
            if(shiftDown) {
                String txt = KeyEvent.getKeyText(k.getKeyCode()).toUpperCase();
                editItem.appendChar(txt.charAt(0));
            }else{
                String txt = KeyEvent.getKeyText(k.getKeyCode()).toLowerCase();
                editItem.appendChar(txt.charAt(0));
            }
        }
        
        if( editItem.getParentItem() instanceof IFrame ) {
            IFrame frame = (IFrame) editItem.getParentItem();
            frame.setSize(editItem.getWidth(), frame.getSize().y);
        }
        
        keyboard.reDrawKeyboard(shiftDown, altDown, ctlDown);
    }

    @Override
    public void keyReleased(KeyboardKey k, boolean shiftDown, boolean altDown,
            boolean ctlDown) {
        // TODO Auto-generated method stub
        
    }

}

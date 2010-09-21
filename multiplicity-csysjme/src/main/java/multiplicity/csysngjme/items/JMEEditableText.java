package multiplicity.csysngjme.items;

import java.util.UUID;

import multiplicity.csysng.items.IEditableText;

public class JMEEditableText extends JMELabel implements IEditableText {
	private static final long serialVersionUID = 6532845831297630376L;
	private int cursorIndex;

	public JMEEditableText(String name, UUID uuid) {
		super(name, uuid);
	}
	
	@Override
	public void setText(String newText) {
		super.setText(newText);
		this.cursorIndex = newText.length();
	}

	@Override
	public void setCursorAt(int index) {
		//if(index >= 0 && index <= text.length()-1) {
			cursorIndex = index;
		//}
	}

	@Override
	public void setCursorDisplay(boolean onOrOff) {

	}

	@Override
	public void insertChar(char c) {
		this.text = this.text.substring(0, cursorIndex) + c + this.text.substring(cursorIndex);
		cursorIndex++;
		update();
	}
	
	@Override
	public void removeChar() {
		if(this.text.length() > 0) {
			this.text = this.text.substring(0, cursorIndex) + this.text.substring(cursorIndex+1);
			cursorIndex--;
			update();
		}
	}
	
	@Override
	public void appendChar(char c) {
		this.text = this.text + c;
		cursorIndex = this.text.length()-1;
		update();		
	}

    @Override
    public void appendString(String charSet) {
        this.text = this.text + charSet;
        cursorIndex = this.text.length()-1;
        update();
    }
}

package multiplicity3.csys.items.mutablelabel;

import java.util.UUID;

import com.jme3.math.Vector2f;

import multiplicity3.csys.items.item.ItemImpl;

public class MutableLabelImpl extends ItemImpl implements IMutableLabel {

	protected IMutableLabelDelegate labelDelegate;
	private String text = "";
	private boolean boxSizeIsSet = false;

	public MutableLabelImpl(String name, UUID uuid) {
		super(name, uuid);
	}
	
	public void setDelegate(IMutableLabelDelegate delegate) {
		super.setDelegate(delegate);
		this.labelDelegate = delegate;
	}

	@Override
	public void setText(String text) {
		this.text  = text;
		labelDelegate.setText(text);		
	}

	@Override
	public void setFont(String resourcePath) {
		labelDelegate.setFont(resourcePath);		
	}

	@Override
	public void removeChar() {
		if(text.length() > 0) {
			text = text.substring(0, text.length() - 1);
		}
		labelDelegate.setText(text);
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void appendChar(char theChar) {
		text = text + theChar;
		labelDelegate.setText(text);
	}

	@Override
	public Vector2f getTextSize() {
		//TODO this breaks the 'never get stuff from the delegate rule...'
		return labelDelegate.getTextSize();
	}

	@Override
	public void setBoxSize(float width, float height) {
		this.boxSizeIsSet = true;
		labelDelegate.setBoxSize(width, height);
	}

	@Override
	public boolean isBoxSizeSet() {
		return boxSizeIsSet;
	}


}

package multiplicity.csysng.items.mutablelabel;

import java.util.UUID;

import multiplicity.csysng.items.item.ItemImpl;

public class MutableLabelImpl extends ItemImpl implements IMutableLabel {

	protected IMutableLabelDelegate labelDelegate;
	private String text = "";

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


}

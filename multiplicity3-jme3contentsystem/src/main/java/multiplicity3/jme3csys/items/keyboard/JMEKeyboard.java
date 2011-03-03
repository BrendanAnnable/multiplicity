package multiplicity3.jme3csys.items.keyboard;

import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity.csysng.annotations.ImplementsContentItem;
import multiplicity.csysng.items.keyboard.IKeyboard;
import multiplicity.csysng.items.keyboard.KeyboardImpl;
import multiplicity3.jme3csys.items.IInitable;

@ImplementsContentItem(target = IKeyboard.class)
public class JMEKeyboard extends KeyboardImpl implements IInitable {

	public JMEKeyboard(String name, UUID uuid) {
		super(name, uuid);
		setDelegate(new JMEKeyboardDelegate(this));
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		((JMEKeyboardDelegate) getDelegate()).initializeGeometry(assetManager);		
	}

}

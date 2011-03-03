package multiplicity.csysjme3.items.keyboard;

import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity.csysjme3.items.IInitable;
import multiplicity.csysng.annotations.ImplementsContentItem;
import multiplicity.csysng.items.keyboard.IKeyboard;
import multiplicity.csysng.items.keyboard.KeyboardImpl;

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

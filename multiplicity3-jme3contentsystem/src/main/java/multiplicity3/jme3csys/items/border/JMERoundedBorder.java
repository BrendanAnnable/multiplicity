package multiplicity3.jme3csys.items.border;

import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity.csysng.annotations.ImplementsContentItem;
import multiplicity.csysng.items.border.IRoundedBorder;
import multiplicity.csysng.items.border.RoundedBorderImpl;
import multiplicity3.jme3csys.items.IInitable;

@ImplementsContentItem(target = IRoundedBorder.class)
public class JMERoundedBorder extends RoundedBorderImpl implements IInitable {

	public JMERoundedBorder(String name, UUID uuid) {
		super(name, uuid);
		JMERoundedBorderDelegate borderDelegate = new JMERoundedBorderDelegate(this);
		setDelegate(borderDelegate);
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		((JMERoundedBorderDelegate) getDelegate()).initializeGeometry(assetManager);		
	}

}

package multiplicity3.jme3csys.items.mutablelabel;

import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity.csysng.annotations.ImplementsContentItem;
import multiplicity.csysng.items.mutablelabel.IMutableLabel;
import multiplicity.csysng.items.mutablelabel.MutableLabelImpl;
import multiplicity3.jme3csys.items.IInitable;

@ImplementsContentItem(target = IMutableLabel.class)
public class JMEMutableLabel extends MutableLabelImpl implements IInitable {

	public JMEMutableLabel(String name, UUID uuid) {
		super(name, uuid);
		setDelegate(new JMEMutableLabelDelegate(this));
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		((JMEMutableLabelDelegate) getDelegate()).initializeGeometry(assetManager);
	}
}

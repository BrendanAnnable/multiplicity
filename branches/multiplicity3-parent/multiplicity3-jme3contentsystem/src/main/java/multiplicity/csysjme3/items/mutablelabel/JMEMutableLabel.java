package multiplicity.csysjme3.items.mutablelabel;

import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity.csysjme3.items.IInitable;
import multiplicity.csysng.annotations.ImplementsContentItem;
import multiplicity.csysng.items.mutablelabel.IMutableLabel;
import multiplicity.csysng.items.mutablelabel.MutableLabelImpl;

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

package multiplicity3.jme3csys.items.line;

import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.items.line.LineImpl;
import multiplicity3.jme3csys.items.IInitable;

@ImplementsContentItem(target = ILine.class)
public class JMELine extends LineImpl implements IInitable {

	public JMELine(String name, UUID uuid) {
		super(name, uuid);
		setDelegate(new JMELineDelegate(this));
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		((JMELineDelegate) getDelegate()).initializeGeometry(assetManager);		
	}

}

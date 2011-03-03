package multiplicity3.jme3csys.items.stage;

import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity3.csys.ContentSystem;
import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.csys.stage.StageImpl;
import multiplicity3.jme3csys.items.IInitable;

@ImplementsContentItem(target = IStage.class)
public class JMEStage extends StageImpl implements IInitable {
		
	private ContentSystem csys;

	public JMEStage(String name, UUID uuid) {
		this(name, uuid, null);		
	}

	public JMEStage(String name, UUID uuid, ContentSystem csys) {
		super(name, uuid);
		this.csys = csys;
	}
	
	@Override
	public ContentSystem getContentSystem() {
		return csys;
	}

	@Override
	public boolean isLocal() {	
		return true;
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		((JMEStageDelegate)getDelegate()).initializeGeometry(assetManager);		
	}

}

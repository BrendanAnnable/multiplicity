package multiplicity.csysjme3.items.stage;

import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity.csysjme3.items.IInitable;
import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.annotations.ImplementsContentItem;
import multiplicity.csysng.stage.IStage;
import multiplicity.csysng.stage.StageImpl;

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

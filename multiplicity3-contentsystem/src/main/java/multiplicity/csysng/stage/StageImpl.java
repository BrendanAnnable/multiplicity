package multiplicity.csysng.stage;

import java.util.UUID;


import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.items.item.ItemImpl;

public abstract class StageImpl extends ItemImpl implements IStage {
	
	public StageImpl(String name, UUID uuid) {
		super(name, uuid);
	}
	
	public void setDelegate(IStageDelegate delegate) {
		super.setDelegate(delegate);
	}
	
	@Override
	public abstract ContentSystem getContentSystem();

}

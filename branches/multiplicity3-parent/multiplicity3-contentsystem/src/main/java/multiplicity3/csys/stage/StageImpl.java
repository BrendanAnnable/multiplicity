package multiplicity3.csys.stage;

import java.util.UUID;


import multiplicity3.csys.ContentSystem;
import multiplicity3.csys.items.item.ItemImpl;

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

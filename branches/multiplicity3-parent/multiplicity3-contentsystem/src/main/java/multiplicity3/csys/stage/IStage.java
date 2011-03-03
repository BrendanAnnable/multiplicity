package multiplicity3.csys.stage;

import multiplicity3.csys.ContentSystem;
import multiplicity3.csys.items.container.IContainer;

public interface IStage extends IContainer {
	public ContentSystem getContentSystem();
	public boolean isLocal();
}

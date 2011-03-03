package multiplicity.csysng.stage;

import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.items.container.IContainer;

public interface IStage extends IContainer {
	public ContentSystem getContentSystem();
	public boolean isLocal();
}

package multiplicity.csysng.threedee;

import java.util.UUID;

import multiplicity.csysng.items.events.MultiTouchEventDispatcher;

import com.jme.scene.Spatial;

public interface IThreeDeeContent {
	public void setSpatial(Spatial s);
	public Spatial getSpatial();
	public UUID getUUID();
	public MultiTouchEventDispatcher getMultiTouchDispatcher();
}

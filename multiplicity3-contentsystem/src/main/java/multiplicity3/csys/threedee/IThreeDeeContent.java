package multiplicity3.csys.threedee;

import java.util.UUID;

import multiplicity3.csys.items.events.MultiTouchEventDispatcher;

import com.jme3.scene.Spatial;

public interface IThreeDeeContent {
	public void setSpatial(Spatial s);
	public Spatial getSpatial();
	public UUID getUUID();
	public MultiTouchEventDispatcher getMultiTouchDispatcher();
}

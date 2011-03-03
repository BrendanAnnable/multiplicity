package multiplicity.csysjme3.threed;

import java.util.UUID;

import com.jme3.scene.Spatial;

import multiplicity.csysjme3.picking.ItemMap;
import multiplicity.csysng.items.events.MultiTouchEventDispatcher;
import multiplicity.csysng.threedee.IThreeDeeContent;

public class JMEThreeDeeContent implements IThreeDeeContent {

	public static final String KEY_JMETHREEDEEITEMDATA = "KEY_JMETHREEDEEITEMDATA";
	protected MultiTouchEventDispatcher dispatcher = new MultiTouchEventDispatcher();
	private String name;
	private UUID uuid;
	private Spatial spatial;
	

	public JMEThreeDeeContent(String name, UUID uuid) {
		this.setName(name);
		this.setUUID(uuid);		
	}

	@Override
	public Spatial getSpatial() {		
		return spatial;
	}

	@Override
	public void setSpatial(Spatial s) {
		if(this.spatial != null) {
			this.spatial.setUserData(KEY_JMETHREEDEEITEMDATA, null);
		}
		this.spatial = s;		
		this.spatial.setUserData(KEY_JMETHREEDEEITEMDATA, uuid.toString());
		ItemMap.register(this.spatial, this);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public MultiTouchEventDispatcher getMultiTouchDispatcher() {
		return dispatcher;
	}

}

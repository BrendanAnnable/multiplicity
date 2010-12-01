package multiplicity.csysngjme.items.hotspots;

import java.util.UUID;

import multiplicity.csysng.items.hotspot.IHotSpotRepo;
import multiplicity.csysngjme.items.JMEFrame;

public class HotSpotRepo extends JMEFrame implements IHotSpotRepo {
	private static final long serialVersionUID = 4851796682623871047L;

	public HotSpotRepo(String name, UUID uuid, float width, float height) {
        super(name, uuid, width, height);
    }

    @Override
    public void initializeGeometry() {
        super.initializeGeometry();
    }
}

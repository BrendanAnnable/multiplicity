package multiplicity.csysngjme.items.hotspots;

import java.util.UUID;

import multiplicity.csysng.items.hotspot.IHotSpotRepo;
import multiplicity.csysngjme.items.JMEFrame;

public class HotSpotRepo extends JMEFrame implements IHotSpotRepo {

    public HotSpotRepo(String name, UUID uuid, float width, float height) {
        super(name, uuid, width, height);
    }

    @Override
    public void initializeGeometry() {
        super.initializeGeometry();
    }
}

package multiplicity.csysng.factory;

import java.util.UUID;

import multiplicity.csysng.items.IFrame;

public interface IHotSpotContentFactory {
    
    public IFrame createHotSpotFrame(String name, UUID uuid, int width, int height);

}

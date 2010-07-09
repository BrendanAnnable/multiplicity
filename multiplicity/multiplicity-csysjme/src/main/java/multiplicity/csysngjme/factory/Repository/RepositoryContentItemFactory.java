package multiplicity.csysngjme.factory.Repository;

import java.util.UUID;

import multiplicity.csysng.items.repository.IRepositoryContentItemFactory;
import multiplicity.csysng.items.repository.IRepositoryFrame;
import multiplicity.csysngjme.factory.ContentItemFactoryUtil;
import multiplicity.csysngjme.items.repository.RepositoryFrame;

import com.jme.math.Vector2f;

public class RepositoryContentItemFactory implements IRepositoryContentItemFactory {

    @Override
    public IRepositoryFrame createRepositoryFrame(String name, UUID uuid, int width, int height) {
        IRepositoryFrame frame = new RepositoryFrame(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid), width, height);
        frame.initializeGeometry();
        return frame;
    }
    
    @Override
    public IRepositoryFrame createRepositoryFrame(String name, UUID uuid, int width, int height, Vector2f openLocation, Vector2f closeLocation) {
       IRepositoryFrame frame = new RepositoryFrame(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid), width, height, openLocation, closeLocation);
       frame.initializeGeometry();
       return frame;
    }
    
}

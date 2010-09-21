package multiplicity.csysngjme.items.repository;

import java.util.UUID;

import multiplicity.csysng.items.repository.IImageRepositoryFrame;

import com.jme.math.Vector2f;

public class ImageRepository extends RepositoryFrame implements IImageRepositoryFrame{

    public ImageRepository(String name, UUID uuid, int width, int height) {
        super(name, uuid, width, height);
    }
    
    public ImageRepository(String name, UUID uuid, int width, int height, Vector2f openLocation, Vector2f closeLocation) {
        super(name, uuid, width, height, openLocation, closeLocation);
    }

    @Override
    public void initializeGeometry() {
        super.initializeGeometry();
    }
}

package multiplicity.csysng.items.repository;

import java.util.UUID;

import com.jme.math.Vector2f;

public interface IRepositoryContentItemFactory {

    public IRepositoryFrame createRepositoryFrame(String name, UUID uuid,
            int width, int height, Vector2f openLocation, Vector2f closeLocation);

    public IRepositoryFrame createRepositoryFrame(String name, UUID uuid,
            int width, int height);

}

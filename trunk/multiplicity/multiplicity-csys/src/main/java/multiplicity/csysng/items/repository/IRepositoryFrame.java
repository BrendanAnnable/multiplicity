package multiplicity.csysng.items.repository;

import com.jme.math.Vector2f;

public interface IRepositoryFrame {

    public abstract boolean isOpen();

    public abstract Vector2f getCloseLocation();

    public abstract void setCloseLocation(Vector2f closeLocation);

    public abstract Vector2f getOpenLocation();

    public abstract void setOpenLocation(Vector2f openLocation);

    public abstract void open();

    public abstract void close();

}

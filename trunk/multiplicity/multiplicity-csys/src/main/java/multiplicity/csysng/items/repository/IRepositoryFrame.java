package multiplicity.csysng.items.repository;

import com.jme.math.Vector2f;

public interface IRepositoryFrame {

    public boolean isOpen();

    public Vector2f getCloseLocation();

    public void setCloseLocation(Vector2f closeLocation);

    public Vector2f getOpenLocation();

    public void setOpenLocation(Vector2f openLocation);

    public void open();

    public void close();
    
    public int tap();

    public void resetTaps();
    
}

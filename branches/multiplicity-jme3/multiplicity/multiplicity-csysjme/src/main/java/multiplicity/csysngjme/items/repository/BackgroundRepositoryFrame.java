package multiplicity.csysngjme.items.repository;

import java.util.UUID;

import com.jme.math.Vector2f;

import multiplicity.csysng.items.repository.IBackgroundRepositoryFrame;

public class BackgroundRepositoryFrame extends RepositoryFrame implements IBackgroundRepositoryFrame {
	private static final long serialVersionUID = 8174178337709496305L;

	public BackgroundRepositoryFrame(String name, UUID uuid, int width,
            int height) {
        super(name, uuid, width, height);
    }
    public BackgroundRepositoryFrame(String name, UUID uuid, int width, int height, Vector2f openLocation, Vector2f closeLocation) {
        super(name, uuid, width, height, openLocation, closeLocation);
    }
    
    @Override
    public void initializeGeometry() {
        // TODO Auto-generated method stub
        super.initializeGeometry();
    }
    
}

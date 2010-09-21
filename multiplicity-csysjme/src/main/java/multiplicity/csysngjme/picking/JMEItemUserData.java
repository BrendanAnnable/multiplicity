package multiplicity.csysngjme.picking;

import java.io.IOException;
import java.util.UUID;

import com.jme.scene.TriMesh;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.Savable;

public class JMEItemUserData implements Savable {
	public UUID uuid;
	private TriMesh maskGeometry;
	
	public JMEItemUserData(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUUID() {
		return uuid;
	}

	@Override
	public Class<JMEItemUserData> getClassTag() {
		return JMEItemUserData.class;
	}

	@Override
	public void read(JMEImporter im) throws IOException {}

	@Override
	public void write(JMEExporter ex) throws IOException {}

	public void setMaskGeometry(TriMesh maskGeometry) {
		this.maskGeometry = maskGeometry;		
	}
	
	public TriMesh getMaskGeometry() {
		return maskGeometry;
	}
}

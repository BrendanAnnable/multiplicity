package multiplicity.app.utils;

import com.jme.renderer.Camera;
import com.jme.system.DisplaySystem;

public class CameraUtility {
	public static void cameraPerspective(Camera cam) {
		cam.setFrustumPerspective( 45.0f, (float) DisplaySystem.getDisplaySystem().getWidth() / (float) DisplaySystem.getDisplaySystem().getHeight(), 1, 1000 );
		cam.setParallelProjection( false );
		cam.update();
	}

	public static void cameraParallel(Camera cam) {
		cam.setParallelProjection( true );
		float aspect = (float) DisplaySystem.getDisplaySystem().getWidth() / DisplaySystem.getDisplaySystem().getHeight();
		cam.setFrustum( -100, 1000, -50 * aspect, 50 * aspect, -50, 50 );
		cam.update();
	}
}

package multiplicity.app.utils;

import java.util.logging.Logger;

import multiplicity.app.AbstractSurfaceSystem;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.system.DisplaySystem;
import com.jme.util.Debug;

public class KeyboardInputUtility {
	private static final Logger log = Logger.getLogger(AbstractSurfaceSystem.class.getName());
	private AbstractSurfaceSystem app;

	public KeyboardInputUtility(AbstractSurfaceSystem testGfxSuper) {
		this.app = testGfxSuper;
	}

	public void setupBindings() {
		KeyBindingManager.getKeyBindingManager().set( "toggle_pause", KeyInput.KEY_P );
		KeyBindingManager.getKeyBindingManager().set( "step", KeyInput.KEY_ADD );
		KeyBindingManager.getKeyBindingManager().set( "toggle_wire", KeyInput.KEY_T );
		KeyBindingManager.getKeyBindingManager().set( "toggle_lights", KeyInput.KEY_L );
		KeyBindingManager.getKeyBindingManager().set( "toggle_bounds", KeyInput.KEY_B );
		KeyBindingManager.getKeyBindingManager().set( "toggle_normals",	KeyInput.KEY_N );
		KeyBindingManager.getKeyBindingManager().set( "camera_out", KeyInput.KEY_C );
		KeyBindingManager.getKeyBindingManager().set( "mem_report", KeyInput.KEY_R);
		KeyBindingManager.getKeyBindingManager().set( "exit", KeyInput.KEY_ESCAPE );
		KeyBindingManager.getKeyBindingManager().set( "screen_shot", KeyInput.KEY_F1 );
		KeyBindingManager.getKeyBindingManager().set( "parallel_projection", KeyInput.KEY_F2 );
		KeyBindingManager.getKeyBindingManager().set( "toggle_depth", KeyInput.KEY_F3 );
		KeyBindingManager.getKeyBindingManager().set( "toggle_stats", KeyInput.KEY_F4 );
	}

	public void processKeyboard(float tpf) {
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_pause", false ) ) {			
			app.setPaused(!app.isPaused());
		}
		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("step", true ) ) {
			app.simpleUpdate();
			app.getRootNode().updateGeometricState(tpf, true);
		}

		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_wire", false ) ) {
			app.setDrawingWireframe(!app.isDrawingWireframe());
		}

		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_lights", false ) ) {
			app.setLightingEnabled(!app.isLightingEnabled());
			app.getRootNode().updateRenderState();
		}

		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_bounds", false ) ) {
			app.setDrawingBounding(!app.isDrawingBounding());
		}

		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_depth", false ) ) {
			app.setShowDepthBuffer(!app.isShowingDepthBuffer());
		}

		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_normals", false ) ) {
			app.setDrawingNormals(!app.isDrawingNormals());
		}

		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("camera_out", false ) ) {
			log.info("Camera at: " + DisplaySystem.getDisplaySystem().getRenderer().getCamera().getLocation() );
		}

		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("screen_shot", false ) ) {
			DisplaySystem.getDisplaySystem().getRenderer().takeScreenShot( "SimpleGameScreenShot" );
		}


		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_stats", false ) && Debug.stats ) {							
			app.setDrawingStats(!app.isDrawingStats());
		}


		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("parallel_projection", false ) ) {
			if ( app.getCamera().isParallelProjection() ) {
				CameraUtility.cameraPerspective(app.getCamera());
			} else {
				CameraUtility.cameraParallel(app.getCamera());
			}
		}

		if ( KeyBindingManager.getKeyBindingManager().isValidCommand("mem_report", false ) ) {
			long totMem = Runtime.getRuntime().totalMemory();
			long freeMem = Runtime.getRuntime().freeMemory();
			long maxMem = Runtime.getRuntime().maxMemory();

			log.info("|*|*|  Memory Stats  |*|*|");
			log.info("Total memory: "+(totMem>>10)+" kb");
			log.info("Free memory: "+(freeMem>>10)+" kb");
			log.info("Max memory: "+(maxMem>>10)+" kb");
		}

		if ( KeyBindingManager.getKeyBindingManager().isValidCommand( "exit", false ) ) {
			app.finish();
		}
	}
}

package multiplicity.appgallery.stereo3d;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Torus;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;

import multiplicity.app.AbstractSurfaceSystem;
import multiplicity.app.singleappsystem.AbstractStandaloneApp;
import multiplicity.app.singleappsystem.SingleAppTableSystem;
import multiplicity.appgallery.gallery.GalleryApp;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.ILabel;
import multiplicity.csysng.threedee.IThreeDeeContent;
import multiplicity.csysng.threedee.interaction.RotateInteraction;
import multiplicity.input.IMultiTouchEventProducer;

public class Stereo3DDemoApp extends AbstractStandaloneApp {

	public Stereo3DDemoApp(AbstractSurfaceSystem ass, IMultiTouchEventProducer producer) {
		super(ass, producer);
	}

	@Override
	public void onAppStart() {
		Torus t = new Torus("torus", 64, 32, 5, 15);
		t.setSolidColor(ColorRGBA.red);
		t.setLocalTranslation(0, 0, -50f);
		t.setModelBound(new BoundingSphere());
		t.updateModelBound();
		MaterialState ms=DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
		ms.setAmbient(ColorRGBA.blue);
		t.setRenderState(ms);
		t.updateRenderState();
		
		IThreeDeeContent threeD = getContentFactory().createThreeDeeContent("threedee", UUID.randomUUID());
		threeD.setSpatial(t);
		addThreeDeeItem(threeD);
		
		threeD.getMultiTouchDispatcher().addListener(new RotateInteraction(t));
		
		ILabel label2 = getContentFactory().createLabel("label", UUID.randomUUID());
		label2.setText("Stereo 3D Demonstration");
		label2.setFont(new Font("Helvetica", Font.BOLD, 24));
		label2.setTextColour(Color.white);
		label2.setRelativeLocation(new Vector2f(-300, 300));
		add(label2);
		
		IImage img = getContentFactory().createImage("photo", UUID.randomUUID());
		img.setImage(GalleryApp.class.getResource("aotn.jpg"));
			
		IFrame frame = getContentFactory().createFrame("frame", UUID.randomUUID(), img.getWidth(), img.getHeight());		
		frame.setBorder(getContentFactory().createRoundedRectangleBorder("frameborder", UUID.randomUUID(), 20f, 8));		
		frame.maintainBorderSizeDuringScale();
		frame.addItem(img);
		frame.setRelativeScale(0.25f);
		frame.setRelativeLocation(new Vector2f(320, -270));
		BehaviourMaker.addBehaviour(frame, RotateTranslateScaleBehaviour.class);
		
		add(frame);
		
	}
	
	public static void main(String[] args) throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		SingleAppTableSystem.startSystem(Stereo3DDemoApp.class);
	}

}

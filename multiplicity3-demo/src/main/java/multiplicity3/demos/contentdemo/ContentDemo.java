package multiplicity3.demos.contentdemo;

import java.util.UUID;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

import multiplicity3.appsystem.IMultiplicityApp;
import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.ContentSystem;
import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.gfx.Gradient;
import multiplicity3.csys.gfx.Gradient.GradientDirection;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.keyboard.IKeyboard;
import multiplicity3.csys.items.keyboard.behaviour.IMultiTouchKeyboardListener;
import multiplicity3.csys.items.keyboard.behaviour.KeyboardBehaviour;
import multiplicity3.csys.items.keyboard.defs.simple.SimpleAlphaKeyboardDefinition;
import multiplicity3.csys.items.keyboard.defs.simple.SimpleAlphaKeyboardRenderer;
import multiplicity3.csys.items.keyboard.model.KeyboardKey;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchInputComponent;

public class ContentDemo implements IMultiplicityApp {

	public static void main(String[] args) {
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		ContentDemo app = new ContentDemo();
		client.setCurrentApp(app);
	}

	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo) {
		final IStage stage = MultiplicityEnvironment.get().getLocalStages().get(0); // get first local stage
		ContentSystem csys = stage.getContentSystem();
		IContentFactory cf = csys.getContentFactory();
		
		stage.getZOrderManager().setAutoBringToTop(true); // it is true by default, but just showing its existence!
		
		// solid and gradient rectangles
		try {
			IContainer keyboardWithFrame = cf.create(IContainer.class, "keywf", UUID.randomUUID());			
			
			IKeyboard kb = cf.create(IKeyboard.class, "kb", UUID.randomUUID());
			kb.setKeyboardDefinition(new SimpleAlphaKeyboardDefinition());
			kb.setKeyboardRenderer(new SimpleAlphaKeyboardRenderer(kb.getKeyboardDefinition()));
			keyboardWithFrame.addItem(kb);
			kb.reDraw();
			KeyboardBehaviour kbb = csys.getBehaviourMaker().addBehaviour(kb, KeyboardBehaviour.class);
			kbb.addListener(new IMultiTouchKeyboardListener() {
				
				@Override
				public void keyReleased(KeyboardKey k, boolean shiftDown, boolean altDown,
						boolean ctlDown) {
					System.out.println(k.getKeyStringRepresentation());
				}
				
				@Override
				public void keyPressed(KeyboardKey k, boolean shiftDown, boolean altDown,
						boolean ctlDown) {
					// TODO Auto-generated method stub
					
				}
			});			
			
			// support different sizes...
			IRoundedBorder border = cf.create(IRoundedBorder.class, "border", UUID.randomUUID());
			keyboardWithFrame.addItem(border);
			border.setSize((float)kb.getKeyboardDefinition().getBounds().getWidth(), (float)kb.getKeyboardDefinition().getBounds().getHeight());
			border.setColor(new ColorRGBA(1, 1, 1, 0.5f));
			RotateTranslateScaleBehaviour rts = csys.getBehaviourMaker().addBehaviour(border, RotateTranslateScaleBehaviour.class);
			rts.setItemActingOn(keyboardWithFrame);
			keyboardWithFrame.setRelativeLocation(new Vector2f(50, -200));
			stage.addItem(keyboardWithFrame);
			
			
			IColourRectangle rect = cf.create(IColourRectangle.class, "solidbox", UUID.randomUUID());
			rect.setRelativeLocation(new Vector2f(0, 0));
			rect.setSolidBackgroundColour(ColorRGBA.Blue);
			rect.setRelativeLocation(new Vector2f(200, 300));
			csys.getBehaviourMaker().addBehaviour(rect, RotateTranslateScaleBehaviour.class);
			stage.addItem(rect);
			rect.setSize(200, 100);
			
			IColourRectangle rect2 = cf.create(IColourRectangle.class, "gradientbox", UUID.randomUUID());
			rect2.setRelativeLocation(new Vector2f(400, 190));
			Gradient g = new Gradient(ColorRGBA.White, ColorRGBA.Gray, GradientDirection.VERTICAL);
			rect2.setGradientBackground(g);
			csys.getBehaviourMaker().addBehaviour(rect2, RotateTranslateScaleBehaviour.class);
			stage.addItem(rect2);
			rect2.setSize(200, 100);
			
			ILine line = cf.create(ILine.class, "line", UUID.randomUUID());
			line.setSourceItem(rect);
			line.setDestinationItem(rect2);
			line.setLineWidth(3f);
			stage.addItem(line);
			
			IMutableLabel label = cf.create(IMutableLabel.class, "lbl", UUID.randomUUID());
			label.setFont("multiplicity3/demos/contentdemo/arial32_white.fnt");
			label.setText("multiplicity");
			label.setRelativeLocation(new Vector2f(-200, 250));
			label.setRelativeScale(0.5f);
			csys.getBehaviourMaker().addBehaviour(label, RotateTranslateScaleBehaviour.class);
			stage.addItem(label);
			
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}
		
		
		
		
	}

	@Override
	public void shouldStop() {
		
		
	}

	@Override
	public String getFriendlyAppName() {
		return "Content Demo";
	}


}
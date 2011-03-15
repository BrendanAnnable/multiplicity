package multiplicity3.demos.coordinates;

import java.util.UUID;

import com.jme3.math.Vector2f;

import multiplicity3.appsystem.IMultiplicityApp;
import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.ContentSystem;
import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.items.events.ItemListenerAdapter;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchInputComponent;

public class CoordinateDemo implements IMultiplicityApp {

	public static void main(String[] args) {
		new MultiplicityClient(new CoordinateDemo()).start();
	}

	@Override
	public void init(MultiTouchInputComponent input, IQueueOwner iqo) {
		final IStage stage = MultiplicityEnvironment.get().getLocalStages().get(0); // get first local stage
		ContentSystem csys = stage.getContentSystem();
		IContentFactory cf = csys.getContentFactory();

		try {
			IMutableLabel label_0_0 = cf.create(IMutableLabel.class, "lbl", UUID.randomUUID());
			label_0_0.setFont("multiplicity3/demos/contentdemo/arial32_white.fnt");
			label_0_0.setText("(0,0)");
			label_0_0.setWorldLocation(new Vector2f(0, 0));
			label_0_0.setRelativeScale(0.5f);
			stage.addItem(label_0_0);
			
			IMutableLabel label_100_100 = cf.create(IMutableLabel.class, "lbl", UUID.randomUUID());
			label_100_100.setFont("multiplicity3/demos/contentdemo/arial32_white.fnt");
			label_100_100.setText("(100,100)");
			label_100_100.setWorldLocation(new Vector2f(100, 100));
			label_100_100.setRelativeScale(0.5f);
			stage.addItem(label_100_100);
			
			final IMutableLabel cursorLabel = cf.create(IMutableLabel.class, "lbl", UUID.randomUUID());
			cursorLabel.setFont("multiplicity3/demos/contentdemo/arial32_white.fnt");			
			cursorLabel.setWorldLocation(new Vector2f(-100, -100));
			cursorLabel.setRelativeScale(0.25f);
			stage.addItem(cursorLabel);
			cursorLabel.setText("Move me!");
			csys.getBehaviourMaker().addBehaviour(cursorLabel, RotateTranslateScaleBehaviour.class).setScaleEnabled(false);
			
			cursorLabel.addItemListener(new ItemListenerAdapter() {
				@Override
				public void itemMoved(IItem item) {
					cursorLabel.setText("World: " + item.getWorldLocation() + "\nRelative: " + item.getRelativeLocation());
				}
			});
			
			
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}
	}
}

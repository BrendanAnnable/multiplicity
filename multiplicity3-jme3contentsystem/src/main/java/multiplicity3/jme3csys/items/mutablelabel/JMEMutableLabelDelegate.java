package multiplicity3.jme3csys.items.mutablelabel;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import multiplicity3.csys.items.mutablelabel.IMutableLabelDelegate;
import multiplicity3.jme3csys.items.item.JMEItemDelegate;
import multiplicity3.jme3csys.picking.ItemMap;

public class JMEMutableLabelDelegate extends JMEItemDelegate implements
		IMutableLabelDelegate {

	private BitmapFont fnt;
	private BitmapText txt;
	private String currentText = "text";
	private JMEMutableLabel item;
	private AssetManager assetManager;
	private float lineWidth;
	private float lineHeight;

	public JMEMutableLabelDelegate(JMEMutableLabel jmeMutableLabel) {
		this.item = jmeMutableLabel;
	}


	@Override
	public void initializeGeometry(AssetManager assetManager) {
		this.assetManager = assetManager;
		fnt = FontCache.get().getFont("Interface/Fonts/Default.fnt", assetManager);
        txt = new BitmapText(fnt, false);
        doUpdate();
        for(Spatial c : txt.getChildren()) {
        	ItemMap.register(c, item);
        }
        attachChild(txt);
	}
	
	@Override
	public Vector2f getTextSize() {
		return new Vector2f(lineWidth, lineHeight);
	}

	
	@Override
	public Spatial getManipulableSpatial() {
		return txt;
	}

	@Override
	public void setText(String text) {
		currentText = text;
		doUpdate();
	}


	@Override
	public void setFont(String resourcePath) {
		fnt = FontCache.get().getFont(resourcePath, assetManager);
				
		
		detachChild(txt);
		
		for(Spatial c : txt.getChildren()) {
        	ItemMap.unregister(c, item);
        }
		txt = new BitmapText(fnt, false);


		doUpdate();
        attachChild(txt);
        ItemMap.register(txt, item);
	}
	
//	@Override
//	public void setZOrder(int zOrder) {
//		super.setZOrder(zOrder);
//		Vector3f newZOrder = getLocalTranslation().clone();
//		txt.setLocalTranslation(newZOrder.x, newZOrder.y, zOrder);
//	}

	
	private void doUpdate() {
		for(Spatial c : txt.getChildren()) {
        	ItemMap.unregister(c, item);
        }
        txt.setSize(fnt.getPreferredSize() * 2f);
        txt.setText(currentText);
        for(Spatial c : txt.getChildren()) {
        	ItemMap.register(c, item);
        }
        
        lineWidth = fnt.getLineWidth(currentText) * 2.01f; // hmmm... not sure what all this *2 stuff is about
        lineHeight = txt.getLineHeight();
		//txt.setBox(new Rectangle(0, 0, lineWidth, txt.getLineHeight()));
		txt.setLocalTranslation(-lineWidth/2f, lineHeight/2f, txt.getLocalTranslation().z);		
	}

}

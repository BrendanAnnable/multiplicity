package multiplicity3.jme3csys.items.mutablelabel;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.font.Rectangle;
import com.jme3.font.BitmapFont.Align;
import com.jme3.math.Vector2f;
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
	private Rectangle boundingBox;
	private float fontScale = 1;

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
				
		System.out.println(resourcePath + " -> " + fnt);
		
		detachChild(txt);
		
		for(Spatial c : txt.getChildren()) {
        	ItemMap.unregister(c, item);
        }
		txt = new BitmapText(fnt, false);

		doUpdate();
        attachChild(txt);
        ItemMap.register(txt, item);
	}
	
	private void doUpdate() {
		for(Spatial c : txt.getChildren()) {
        	ItemMap.unregister(c, item);
        }
		
		if(boundingBox != null) {
			txt.setBox(boundingBox);
			txt.setAlignment(Align.Center);
			txt.setVerticalAlignment(VAlign.Center);
			txt.setLineWrapMode(LineWrapMode.Word);
		}
		txt.setSize(32 * fontScale);
        txt.setText(currentText);
        for(Spatial c : txt.getChildren()) {
        	ItemMap.register(c, item);
        }

        System.out.println("\n----[" + this.currentText + "]-----");
        System.out.println("fnt.linewidth(currenttext): " + fnt.getLineWidth(currentText));
        System.out.println("fnt.prefferredsize: " + fnt.getPreferredSize());
        System.out.println("txt.linewidth: " + txt.getLineWidth());
        System.out.println("txt.lineheight: " + txt.getLineHeight());
        System.out.println("txt.height: " + txt.getHeight());
        System.out.println("txt.linecount: " + txt.getLineCount());
        
        if(boundingBox != null) {
        	txt.setLocalTranslation(-txt.getLineWidth()/2f, txt.getHeight()/2f, 0);
        }else{
        	txt.setLocalTranslation(-fnt.getLineWidth(currentText)/2f, txt.getHeight()/2f, 0);
        }
	}

	@Override
	public void setBoxSize(float width, float height) {
		this.boundingBox = new Rectangle(0, 0, width, height);
		doUpdate();
	}


	@Override
	public void setFontScale(float scale) {
		this.fontScale = scale;
		doUpdate();
	}

}

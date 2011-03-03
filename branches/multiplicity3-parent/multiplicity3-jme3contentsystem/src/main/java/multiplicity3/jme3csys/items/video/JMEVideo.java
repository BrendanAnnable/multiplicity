package multiplicity3.jme3csys.items.video;

import java.util.UUID;

import com.jme3.asset.AssetManager;

import multiplicity3.csys.IUpdateable;
import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.items.video.IVideo;
import multiplicity3.csys.items.video.VideoImpl;
import multiplicity3.jme3csys.annotations.RequiresUpdate;
import multiplicity3.jme3csys.items.IInitable;

@ImplementsContentItem(target = IVideo.class)
@RequiresUpdate
public class JMEVideo extends VideoImpl implements IInitable, IUpdateable {

	public JMEVideo(String name, UUID uuid) {
		super(name, uuid);
		JMEVideoDelegate delegate = new JMEVideoDelegate(this);
		setDelegate(delegate);
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		((JMEVideoDelegate) getDelegate()).initializeGeometry(assetManager);
	}

	@Override
	public void update(float tpf) {
		((JMEVideoDelegate) getDelegate()).simpleUpdate(tpf);		
	}
}

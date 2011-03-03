package multiplicity.csysng.items.video;

import java.util.UUID;

import multiplicity.csysng.items.item.ItemImpl;
import multiplicity.csysng.items.video.exceptions.CouldNotPlayVideoException;

public class VideoImpl extends ItemImpl implements IVideo {

	private IVideoDelegate videoDelegate;
	private boolean playing = false;

	public VideoImpl(String name, UUID uuid) {
		super(name, uuid);
	}
	
	public void setDelegate(IVideoDelegate delegate) {
		super.setDelegate(delegate);
		this.videoDelegate = delegate;
	}

	@Override
	public void setSize(float width, float height) {
		videoDelegate.setSize(width, height);		
	}

	@Override
	public void setResource(String resource) {
		videoDelegate.setResource(resource);		
	}

	@Override
	public void startPlaying() throws CouldNotPlayVideoException {
		if(!isPlaying()) {
			videoDelegate.startPlaying();
			playing = true;
		}
	}

	@Override
	public void stopPlaying() {
		if(isPlaying()) {
			videoDelegate.stopPlaying();
			playing = false;
		}
	}
	
	@Override
	public boolean isPlaying() {
		return playing;
	}

}

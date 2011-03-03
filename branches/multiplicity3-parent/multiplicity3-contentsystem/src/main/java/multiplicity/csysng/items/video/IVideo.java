package multiplicity.csysng.items.video;

import multiplicity.csysng.items.item.IItem;
import multiplicity.csysng.items.video.exceptions.CouldNotPlayVideoException;

public interface IVideo extends IItem {
	public void setSize(float width, float height);
	public void setResource(String resource);
	public void startPlaying() throws CouldNotPlayVideoException;
	public void stopPlaying();
	boolean isPlaying();
}

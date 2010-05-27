package no.uio.intermedia.snomobile.restful;

import no.uio.intermedia.snomobile.interfaces.ITag;


/**
 * @author Jeremy Toussaint
 *
 */
public class RestFulTag implements ITag {

	private String name = null;
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.ITag#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.ITag#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

}

package no.uio.intermedia.snomobile.restful;

import no.uio.intermedia.snomobile.interfaces.ISpace;


/**
 * @author Jeremy Toussaint
 *
 */
public class RestFulSpace implements ISpace {
	
	private String id = null;
	private String wiki = null;
	private String name = null;
	private String home = null;
	private String xwikiRelativeUrl = null;
	private String xwikiAbsoluteUrl = null;
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.ISpace#getId()
	 */
	@Override
	public String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.ISpace#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.ISpace#getWiki()
	 */
	@Override
	public String getWiki() {
		return wiki;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.ISpace#setWiki(java.lang.String)
	 */
	@Override
	public void setWiki(String wiki) {
		this.wiki = wiki;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.ISpace#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.ISpace#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.ISpace#getHome()
	 */
	@Override
	public String getHome() {
		return home;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.ISpace#setHome(java.lang.String)
	 */
	@Override
	public void setHome(String home) {
		this.home = home;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.ISpace#getRelativeUrl()
	 */
	@Override
	public String getRelativeUrl() {
		return xwikiRelativeUrl;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.ISpace#setRelativeUrl(java.lang.String)
	 */
	@Override
	public void setRelativeUrl(String xwikiRelativeUrl) {
		this.xwikiRelativeUrl = xwikiRelativeUrl;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.ISpace#getAbsoluteUrl()
	 */
	@Override
	public String getAbsoluteUrl() {
		return xwikiAbsoluteUrl;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.ISpace#setAbsoluteUrl(java.lang.String)
	 */
	@Override
	public void setAbsoluteUrl(String xwikiAbsoluteUrl) {
		this.xwikiAbsoluteUrl = xwikiAbsoluteUrl;
	}
}

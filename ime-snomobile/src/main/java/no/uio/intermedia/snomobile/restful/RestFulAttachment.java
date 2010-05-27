package no.uio.intermedia.snomobile.restful;

import java.util.Date;

import no.uio.intermedia.snomobile.interfaces.IAttachment;


/**
 * @author Jeremy Toussaint
 *
 */
public class RestFulAttachment implements IAttachment {

	public String id = null;
	public String name = null;
	public String size = null;
	public String version = null;
	public String pageId = null;
	public String pageVersion = null;
	public String mimeType = null;
	public String author = null;
	public Date date = null;
	public String xwikiRelativeUrl = null;
	public String xwikiAbsoluteUrl = null;
	public Object resourceObject = null;
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#getId()
	 */
	@Override
	public String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#getSize()
	 */
	@Override
	public String getSize() {
		return size;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#setSize(java.lang.String)
	 */
	@Override
	public void setSize(String size) {
		this.size = size;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#getVersion()
	 */
	@Override
	public String getVersion() {
		return version;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#setVersion(java.lang.String)
	 */
	@Override
	public void setVersion(String version) {
		this.version = version;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#getPageId()
	 */
	@Override
	public String getPageId() {
		return pageId;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#setPageId(java.lang.String)
	 */
	@Override
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#getPageVersion()
	 */
	@Override
	public String getPageVersion() {
		return pageVersion;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#setPageVersion(java.lang.String)
	 */
	@Override
	public void setPageVersion(String pageVersion) {
		this.pageVersion = pageVersion;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#getMimeType()
	 */
	@Override
	public String getMimeType() {
		return mimeType;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#setMimeType(java.lang.String)
	 */
	@Override
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#getAuthor()
	 */
	@Override
	public String getAuthor() {
		return author;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#setAuthor(java.lang.String)
	 */
	@Override
	public void setAuthor(String author) {
		this.author = author;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#getDate()
	 */
	@Override
	public Date getDate() {
		return date;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#setDate(java.lang.String)
	 */
	@Override
	public void setDate(Date date) {
		this.date = date;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#getRelativeUrl()
	 */
	@Override
	public String getRelativeUrl() {
		return xwikiRelativeUrl;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#setRelativeUrl(java.lang.String)
	 */
	@Override
	public void setRelativeUrl(String xwikiRelativeUrl) {
		this.xwikiRelativeUrl = xwikiRelativeUrl;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#getAbsoluteUrl()
	 */
	@Override
	public String getAbsoluteUrl() {
		return xwikiAbsoluteUrl;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#setAbsoluteUrl(java.lang.String)
	 */
	@Override
	public void setAbsoluteUrl(String xwikiAbsoluteUrl) {
		this.xwikiAbsoluteUrl = xwikiAbsoluteUrl;
	}

	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#getResource()
	 */
	@Override
	public Object getResource() {
		return resourceObject;
	}

	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IAttachment#setResource(java.lang.Object)
	 */
	@Override
	public void setResource(Object resource) {
		this.resourceObject = resource;
	}
}

package no.uio.intermedia.snomobile.restful;

import java.util.Vector;

import no.uio.intermedia.snomobile.interfaces.IAttachment;
import no.uio.intermedia.snomobile.interfaces.IComment;
import no.uio.intermedia.snomobile.interfaces.IPage;
import no.uio.intermedia.snomobile.interfaces.ITag;


/**
 * @author Jeremy Toussaint
 *
 */
public class RestFulPage implements IPage {
		

	private String language = null;
	private String version = null;
	private String majorVersion = null;
	private String minorVersion = null;
	private String created = null;
	private String creator = null;
	private String modified = null;
	private String modifier = null;
	private String content = null;
	private String pageName = null;
	private Vector<IComment> comments = null;
	private Vector<IAttachment> attachments = null;
	private Vector<ITag> tags = null;

	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#getContent()
	 */
	@Override
	public String getContent() {
		return content;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#setContent(java.lang.String)
	 */
	@Override
	public void setContent(String content) {
		this.content = content;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#getLanguage()
	 */
	@Override
	public String getLanguage() {
		return language;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#setLanguage(java.lang.String)
	 */
	@Override
	public void setLanguage(String language) {
		this.language = language;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#getVersion()
	 */
	@Override
	public String getVersion() {
		return version;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#setVersion(java.lang.String)
	 */
	@Override
	public void setVersion(String version) {
		this.version = version;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#getMajorVersion()
	 */
	@Override
	public String getMajorVersion() {
		return majorVersion;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#setMajorVersion(java.lang.String)
	 */
	@Override
	public void setMajorVersion(String majorVersion) {
		this.majorVersion = majorVersion;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#getMinorVersion()
	 */
	@Override
	public String getMinorVersion() {
		return minorVersion;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#setMinorVersion(java.lang.String)
	 */
	@Override
	public void setMinorVersion(String minorVersion) {
		this.minorVersion = minorVersion;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#getCreated()
	 */
	@Override
	public String getCreated() {
		return created;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#setCreated(java.lang.String)
	 */
	@Override
	public void setCreated(String created) {
		this.created = created;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#getCreator()
	 */
	@Override
	public String getCreator() {
		return creator;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#setCreator(java.lang.String)
	 */
	@Override
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#getModified()
	 */
	@Override
	public String getModified() {
		return modified;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#setModified(java.lang.String)
	 */
	@Override
	public void setModified(String modified) {
		this.modified = modified;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#getModifier()
	 */
	@Override
	public String getModifier() {
		return modifier;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#setModifier(java.lang.String)
	 */
	@Override
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#setPageName(java.lang.String)
	 */
	@Override
	public void setPageName(String pageName) {
		this.pageName = pageName;		
	}

	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#getPageName()
	 */
	@Override
	public String getPageName() {
		return pageName;
	}

	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#getComments()
	 */
	@Override
	public Vector<IComment> getComments() {
		return comments;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#setComments(java.util.Vector)
	 */
	@Override
	public void setComments(Vector<IComment> comments) {
		this.comments = comments;
	}

	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#getAttachments()
	 */
	@Override
	public Vector<IAttachment> getAttachments() {
		return attachments;
	}

	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#setAttachments(java.util.Vector)
	 */
	@Override
	public void setAttachments(Vector<IAttachment> attachments) {
		this.attachments = attachments;
		
	}

	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#getTags()
	 */
	@Override
	public Vector<ITag> getTags() {
		return tags;
	}

	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IPage#setTags(java.util.Vector)
	 */
	@Override
	public void setTags(Vector<ITag> tags) {
		this.tags = tags;
	}
}

package no.uio.intermedia.snomobile.restful;

import java.util.Date;

import no.uio.intermedia.snomobile.interfaces.IComment;


/**
 * @author Jeremy Toussaint
 *
 */
public class RestFulComment implements IComment{

	private String id = null;
	private String pageId = null;
	private String author = null;
	private Date date = null;
	private String highlight = null;
	private String text = null;
	private String replyTo = null;
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IComment#getId()
	 */
	@Override
	public String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IComment#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IComment#getPageId()
	 */
	@Override
	public String getPageId() {
		return pageId;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IComment#setPageId(java.lang.String)
	 */
	@Override
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IComment#getAuthor()
	 */
	@Override
	public String getAuthor() {
		return author;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IComment#setAuthor(java.lang.String)
	 */
	@Override
	public void setAuthor(String author) {
		this.author = author;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IComment#getDate()
	 */
	@Override
	public Date getDate() {
		return date;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IComment#setDate(java.lang.String)
	 */
	@Override
	public void setDate(Date date) {
		this.date = date;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IComment#getHighlight()
	 */
	@Override
	public String getHighlight() {
		return highlight;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IComment#setHighlight(java.lang.String)
	 */
	@Override
	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IComment#getText()
	 */
	@Override
	public String getText() {
		return text;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IComment#setText(java.lang.String)
	 */
	@Override
	public void setText(String text) {
		this.text = text;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IComment#getReplyTo()
	 */
	@Override
	public String getReplyTo() {
		return replyTo;
	}
	
	/* (non-Javadoc)
	 * @see no.uio.intermedia.snomobile.interfaces.IComment#setReplyTo(java.lang.String)
	 */
	@Override
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
}

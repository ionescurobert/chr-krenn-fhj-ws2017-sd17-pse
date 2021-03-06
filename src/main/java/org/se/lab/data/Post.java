package org.se.lab.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

@Entity
@Table(name = "post")
public class Post implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Transient
	private Logger LOG = Logger.getLogger(Post.class);

	// Constants
	public static final int MAX_TEXT_LENGTH = 1024;
	private static final String TOSTRING_MSG = "Post: {id: %d, text: %s, created: %s, by: %s, in: %s, replyto: %s}";

	// Exception messages
	private static final String ID_INVALID_ERROR = "The given id is less than 1";
	private static final String POST_NULL_ERROR = "The given post must not be null";
	private static final String USER_NULL_ERROR = "The given user must not be null";
	private static final String TEXT_NULL_ERROR = "The given text must not be null";
	private static final String TEXT_INVALID_ERROR = "The given text is to long and exceeds " 
			+ MAX_TEXT_LENGTH
			+ " characters";
	private static final String CREATED_NULL_ERROR = "The given created timestamp must not be null";
	private static final String SELF_REFERENTIAL_ERROR = "The given parent post must not be the same as this post";
	private static final String LIKE_NULL_ERROR = "The given Like (EnumerationItem) must not be null";	
	
	
	/*
	 * Constructor
	 */

	protected Post() {
	};

	public Post(Post parentpost, Community community, User user, String text, Date created) {
		LOG.debug("New Post");
		LOG.trace(
				String.format("\t{\n\tparentpost: %s,\n\tcommunity: %s\n\tuser: %s\n\ttext: %s\n\tcreated: %s",
				parentpost,
				community,
				user,
				text,
				created ));
		setParentpost(parentpost);
		setCommunity(community);
		setUser(user);
		setText(text);
		setCreated(created);
	}

	/*
	 * Columns: id, parent_post_id, fk_community_id, fk_user_id, text, created
	 */

	// id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * Getter for id field of post
	 * 
	 * @return: (int) id
	 */
	public int getId() {
		LOG.debug("getID -> " + id);
		return id;
	}

	/**
	 * Setter for id field of post Should not be negative or zero
	 * 
	 * @param id
	 * 
	 * @throws IllegalArgumentException.class if given id less than 1
	 */
	public void setId(int id) {
		LOG.debug("setId(" + id + ")");
		if (id < 1)
			throw new IllegalArgumentException(ID_INVALID_ERROR);
		this.id = id;
	}

	// parent_post_id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_post_id")
	private Post parentpost;

	/**
	 * Getter to get parent post of post
	 * 
	 * @return (Post) parentpost
	 */
	public Post getParentpost() {
		LOG.debug("getParentPost -> " + parentpost);
		return parentpost;
	}

	/**
	 * Setter for parent post of post
	 * 
	 * @param parentpost
	 * 
	 * @throws IllegalArgumentException.class if Post is reply to itself
	 */
	public void setParentpost(Post parentpost) {
		LOG.debug("setParentpost(" + parentpost + ")");
		// Parent post can be null
		if (parentpost != null && this.id != 0 && parentpost.getId() == this.id)
			throw new IllegalArgumentException(SELF_REFERENTIAL_ERROR);
		this.parentpost = parentpost;
		if (parentpost != null && !parentpost.getChildPosts().contains(this))
			parentpost.addChildPost(this);
	}

	// unmapped child_post
	@OneToMany(mappedBy = "parentpost", fetch = FetchType.EAGER)
	private List<Post> children = new ArrayList<Post>();

	/**
	 * Getter for childposts to this post
	 * 
	 * @return (List<Post>) children
	 */
	public List<Post> getChildPosts() {
		LOG.debug("getChildPosts -> " + children);
		return children;
	}

	/**
	 * Add given post as child to this post and sets parent of given post to this
	 * post
	 * 
	 * @param post
	 * 
	 * @throws IllegalArgumentException.class if given post is null
	 */
	public void addChildPost(Post post) {
		LOG.debug("addChildPost(" + post + ")");
		if (post == null)
			throw new IllegalArgumentException(POST_NULL_ERROR);
		if (children.contains(post))
			return;
		children.add(post);
		post.setParentpost(this);
	}

	// fk_community_id 
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_community_id")
	private Community community;

	/**
	 * Getter for the community this post was posted in
	 * 
	 * @return (Community) community
	 */
	public Community getCommunity() {
		LOG.debug("getCommunity -> " + community);
		return community;
	}

	/**
	 * Setter for the Community this post was posted in
	 * 
	 * @param community
	 * 
	 * @throws IllegalArgumentException.class if given community is null
	 */
	public void setCommunity(Community community) {
		LOG.debug("setCommunity(" + community + ")");
		this.community = community;
	}

	// fk_user_id 
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_user_id")
	private User user;

	/**
	 * Getter for the user that posted this post
	 * 
	 * @return (User) user
	 */
	public User getUser() {
		LOG.debug("getUser -> " + user);
		return user;
	}

	/**
	 * Setter for the user that posted this post
	 * 
	 * @param user
	 * 
	 * @throws IllegalArgumentException.class if given user is null
	 */
	public void setUser(User user) {
		LOG.debug("setUser(" + user + ")");
		if (user == null)
			throw new IllegalArgumentException(USER_NULL_ERROR);
		this.user = user;
	}

	// Likes
	@ManyToMany(mappedBy = "liked")
	private List<Enumeration> likes = new ArrayList<Enumeration>();

	/**
	 * Gets Likes as EnumeratioItem for post
	 * 
	 * @return (EnumerationItem) likes
	 */
	public List<Enumeration> getLikes() {
		LOG.debug("getLikes -> " + likes);
		return likes;
	}

	/**
	 * Add a Like to a Post This EnumertionItem must not be null Takes
	 * EnumeratioItem that allows for e.g. Dislikes etc.
	 * 
	 * @param like
	 * 
	 * @throws IllegalArgumentException.class if given Enumeration is null
	 */
	public void addLike(Enumeration like) {
		LOG.debug("addLikeToPost(" + like + ")");
		if (like == null)
			throw new IllegalArgumentException(LIKE_NULL_ERROR);
		if (!like.getLikedPosts().contains(this))
			like.addLikedPost(this);
		if (!this.likes.contains(like))
			this.likes.add(like);
	}
	

	// text
	@Column(name = "text", length=MAX_TEXT_LENGTH)
	private String text;

	/**
	 * Getter for the text message of this post
	 * 
	 * @return (String) text
	 */
	public String getText() {
		LOG.debug("getText -> " + text);
		return text;
	}

	/**
	 * Setter for the text message of this post
	 * 
	 * @param text
	 * 
	 * @throws IllegalArgumentException.class if given text is null or exceeds the
	 * character limit of 1024 characters
	 */
	public void setText(String text) {
		LOG.debug("setText(" + text + ")");
		if (text == null)
			throw new IllegalArgumentException(TEXT_NULL_ERROR);
		if (text.length() > MAX_TEXT_LENGTH)
			throw new IllegalArgumentException(TEXT_INVALID_ERROR);
		this.text = text;
	}
	
	// created (Timestamp)
	@Column(name = "created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	/**
	 * Getter for "created" timestamp of this post
	 * 
	 * @return (Date) created
	 */
	public Date getCreated() {
		LOG.debug("getCreated -> " + created);
		return created;
	}

	/**
	 * Setter for the "created" timestamp of this post
	 * 
	 * @param created
	 * 
	 * @throws IllegalArgumentException.class if given created is null
	 */
	public void setCreated(Date created) {
		LOG.debug("setCreated(" + created + ")");
		if (created == null)
			throw new IllegalArgumentException(CREATED_NULL_ERROR);
		this.created = created;
	}

	/*
	 * Overrides
	 */

	@Override
	public int hashCode() {
		LOG.debug("hashCode -> " + id);
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		LOG.debug(this + ".equals(" + obj + ")");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		if (this.id != ((Post) obj).getId())
			return false;
		return true;
	}

	@Override
	public String toString() {
		LOG.trace("toString");
		return String.format(TOSTRING_MSG, this.id, this.text, this.created, this.user, this.community,
				this.parentpost);
	}

	// end
}

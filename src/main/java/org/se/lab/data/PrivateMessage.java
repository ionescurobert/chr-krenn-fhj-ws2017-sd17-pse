package org.se.lab.data;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;


@Entity
@Table(name="private_message")
public class PrivateMessage implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Transient
	private Logger LOG = Logger.getLogger(Post.class);
	
	/**
	 * Private Message Constants
	 */
	
		public static final int MAX_TEXT_LENGTH = 1024;
		private static final String TOSTRING_MSG = "PrivateMessage: {id: %d, text: %s, FK_User_Sender: %s, FK_User_Receiver: %s}";

		// Exception messages
		private static final String ID_INVALID_ERROR = "The given id is less than 1";
		private static final String USERSENDER_NULL_ERROR = "The given user must not be null";
		private static final String USERRECEIVER_NULL_ERROR = "The given post must not be null";
		private static final String TEXT_NULL_ERROR = "The given text must not be null";
		private static final String TEXT_INVALID_ERROR = "The given text is to long and exceeds " 
				+ MAX_TEXT_LENGTH
				+ " characters";
		private static final String TEXT_WHITESPACE_ERROR = "The given text must have charakters not only whitespaces";
	
	public PrivateMessage(String text, User sender, User receiver )
	{
		LOG.debug("New Private Message");
		LOG.trace(
				String.format("\t{\n\ttext: %s,\n\tsender: %s\n\treceiver:",
				text,
				sender,
				receiver));
		setText(text);
		setUserSender(sender);
		setUserReceiver(receiver);
	}
	
	protected PrivateMessage()
	{
		
	}
	
	
	/**
	 * Getter for id field of PrivateMessage
	 * 
	 * @return: (int) id
	 */
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ID;
	public int getID() {
		return ID;
	}

	/**
	 * Setter for id field of PrivateMessage Should not be negative or zero
	 * 
	 * @param id
	 * 
	 * @throws IllegalArgumentException.class if given id less than 1
	 */
	public void setID(int id) {
		if (id <= 0)
			throw new IllegalArgumentException(ID_INVALID_ERROR);
		ID = id;
	}
	
	/**
	 * Getter to get Text of PrivateMessage
	 * 
	 * @return (String) text
	 */
	@Column(name="text")
	private String text;
	public String getText() {
		LOG.debug("getText -> " + text);
		return text;
	}

	public void setText(String text) {
		LOG.debug("setText(" + text + ")");
		if (text == null)
			throw new IllegalArgumentException(TEXT_NULL_ERROR);
		if (text.length() > MAX_TEXT_LENGTH)
			throw new IllegalArgumentException(TEXT_INVALID_ERROR);
		
		if (text.trim().length() == 0)
		{
			throw new IllegalArgumentException(TEXT_WHITESPACE_ERROR);
		}
		this.text = text;
	}
	
	/**
	 * Setter for UserSender field of PrivateMessage Should not be null
	 * 
	 * @param usersender
	 * 
	 * @throws IllegalArgumentException.class if given user object is null
	 */
	@ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="fk_user_id_sender")
    private User usersender;

    public void setUserSender(User usersender) {
        if(usersender == null)
            throw new IllegalArgumentException(USERSENDER_NULL_ERROR);
        this.usersender = usersender;
        usersender.addPrivateMessageSender(this);
    }

    /**
	 * Getter to get UserSender of PrivateMessage
	 * 
	 * @return (User) usersender
	 */
    public User getUserSender(){
        return usersender;
    }
    
    
    /**
	 * Setter for UserReceiver field of PrivateMessage Should not be null
	 * 
	 * @param userreceiver
	 * 
	 * @throws IllegalArgumentException.class if given user object is null
	 */
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="fk_user_id_receiver")
    private User userreceiver;

    public void setUserReceiver(User userreceiver) {
        if(userreceiver == null)
            throw new IllegalArgumentException(USERRECEIVER_NULL_ERROR);
        this.userreceiver = userreceiver;
        userreceiver.addPrivateMessageReceiver(this);
    }

    /**
	 * Getter to get UserReceiver of PrivateMessage
	 * 
	 * @return (User) userreceiver
	 */
    public User getUserReceiver(){
        return userreceiver;
    }
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userreceiver != null) ? userreceiver.getId() : 0);
		result = prime * result + ((usersender != null) ? usersender.getId() : 0);
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrivateMessage other = (PrivateMessage) obj;
		if (userreceiver.getId() != other.getUserReceiver().getId())
			return false;
		if (usersender.getId() != other.getUserSender().getId())
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format(TOSTRING_MSG, this.ID, this.text, this.usersender, this.userreceiver);
	}

}

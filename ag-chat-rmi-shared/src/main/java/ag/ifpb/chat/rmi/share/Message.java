package ag.ifpb.chat.rmi.share;

/**
 * Representa a mensagem.
 * 
 * 
 * @author arigarcia
 *
 */
public class Message {
	
	/**
	 * Identifica uma mensagem como sendo única
	 */
	private long ID;
	
	/**
	 * Identificação (email) do usuário que enviou a mensagem
	 */
	private String user;
	
	/**
	 * O conteúdo da mensagem.
	 */
	private String content;

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Message [ID=" + ID + ", user=" + user + ", content=" + content + "]";
	}
	
	
	
	
	
	
	
}

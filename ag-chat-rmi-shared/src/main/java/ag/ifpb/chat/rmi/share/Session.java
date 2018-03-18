package ag.ifpb.chat.rmi.share;

/**
 * Objeto de transferência de dados
 * que representa uma sessão de usuário.
 * 
 * @author arigarcia
 *
 */
public class Session {
	
	/**
	 * A identificação da sessão
	 */
	private String token;
	
	/**
	 * O email do usuário
	 */
	private String user;
	
	
	public String getToken() {
		return token;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
}

package ag.ifpb.chat.rmi.share;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIChatService extends Remote {
	
	/**
	 * Faz o login no server e retorna o token da sessão
	 * 
	 * @param email
	 * @return
	 */
	String login(String email) throws RemoteException;
	
	
	/**
	 * Enviar um token para identificar a sessão do usuário
	 * e a própria mensagem
	 * 
	 * @param token
	 * @param text
	 */
	void sendMessage(String token, String text) throws RemoteException;
	
	/**
	 * Receber uma mensagem a partir a identificação da sessão do usuário
	 * 
	 * @param token
	 * @return um vetor contendo o email do usuário e o texto da mensagem, 
	 * nesta sequência
	 */
	String[] receiveMessage(String token) throws RemoteException;
	
}

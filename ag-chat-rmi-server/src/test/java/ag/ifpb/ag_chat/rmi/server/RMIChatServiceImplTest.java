package ag.ifpb.ag_chat.rmi.server;

import java.rmi.RemoteException;
import ag.ifpb.chat.rmi.share.ChatServer;
import ag.ifpb.chat.rmi.share.RMIChatService;
import junit.framework.TestCase;

public class RMIChatServiceImplTest extends TestCase {
	ChatServer server = new ChatServerImpl();
	RMIChatService service = new RMIChatServiceImpl(server);

	private String login(String email) throws RemoteException{
		String token = service.login(email);
		assertNotNull(token);
		return token;
	}
	
	private void sendMsg(String token, String message) throws RemoteException {
		service.sendMessage(token, message);
	}
	
	public void test() throws RemoteException{
		//login
		String tk1 = login("aristofanio@hotmail.com");
		String tk2 = login("mariabonita@hotmail.com");
		//enviar uma mensagem
		sendMsg(tk1, "Hello");
	}
	
	
	
}

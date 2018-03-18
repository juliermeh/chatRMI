package ag.ifpb.ag_chat.rmi.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import ag.ifpb.chat.rmi.share.ChatServer;
import ag.ifpb.chat.rmi.share.Message;
import ag.ifpb.chat.rmi.share.RMIChatService;
import ag.ifpb.chat.rmi.share.Session;

@SuppressWarnings(value = "serial")
public class RMIChatServiceImpl implements RMIChatService {
	private final ChatServer chatServer;
	private final List<Session> sessions = new ArrayList<Session>();

	private void saveSession(Session s){
		sessions.add(s);
	}
	
	protected RMIChatServiceImpl(ChatServer server) {
		this.chatServer = server;
	}

	public String login(String email) throws RemoteException{
		Session session = chatServer.login(email);
		//
		saveSession(session);//TODO: criar a sessão no banco de dados
		//
		if (session != null){
			return session.getToken();
		} else {
			return null;
		}
	}

	public void sendMessage(String token, String text) throws RemoteException{
		//
		String user = null;
		for (Session session : sessions) {
			if (session.getToken().equalsIgnoreCase(token)){
				user = session.getUser();
			}
		}
		//
		if (user == null){throw new RemoteException("Sessão Inválida");}
                //
                Message m = new Message();
		m.setID(System.currentTimeMillis());
		m.setUser(user);
		m.setContent(text);
		//
		chatServer.persistAndforwardToAll(m);
	}

	public String[] receiveMessage(String token) throws RemoteException{
                Session current = null;
                for(Session session : sessions){
                    if(session.getToken().equals(token)){
                        current = session;
                    }
                }
                if(current != null){
                    List<Message> msgs = chatServer.listMessagesAll().get(current.getUser());
                    if(msgs == null){ return null; }
                    Message m = msgs.get(0);
                    String[] result = new String[]{m.getUser(), m.getContent()};
                    chatServer.removeNotification(m.getID(),current.getUser());
                    return result;
                }
                return null;
	}

}

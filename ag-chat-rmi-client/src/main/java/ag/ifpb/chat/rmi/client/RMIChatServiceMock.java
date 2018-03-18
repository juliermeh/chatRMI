package ag.ifpb.chat.rmi.client;

import ag.ifpb.chat.rmi.share.RMIChatService;

public class RMIChatServiceMock implements RMIChatService{
	private String currentMessage;
	private String currentUser;
	private Object lock = new Object();
	
	public String login(String email) {
		synchronized (lock) {
			currentUser = email;
		}
		return String.valueOf(System.currentTimeMillis());
	}

	public void sendMessage(String token, String text) {
		synchronized (lock) {
			currentMessage = text;
		}
	}

	public String[] receiveMessage(String token) {
		synchronized (lock) {
			String[] msg = new String[]{currentUser, currentMessage};
			currentMessage = null;
			return msg;
		}
	}

}

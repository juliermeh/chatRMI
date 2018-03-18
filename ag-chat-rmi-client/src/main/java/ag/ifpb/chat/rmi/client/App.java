package ag.ifpb.chat.rmi.client;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;

import ag.ifpb.chat.rmi.share.RMIChatService;

public class App {
	private static Object consoleLock = new Object();
	
	private static class Sender implements Runnable{
		private final RMIChatService service;
		private final Scanner scanner;
		private final String token;
		//
		public Sender(RMIChatService service, Scanner scanner, String token) {
			this.service = service;
			this.scanner = scanner;
			this.token = token;
		}
		//envio
		public void run() {
			while(true){
				System.out.print("[eu]: ");
				String textOut = scanner.nextLine();
				try {
					service.sendMessage(token, textOut);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static class Receiver implements Runnable{
		private final RMIChatService service;
		private final String token;
		
		public Receiver(RMIChatService service, String token) {
			this.service = service;
			this.token = token;
		}

		public void run() {
			while(true){
				//recebimento
				try {
					String[] textsIn = service.receiveMessage(token);
					if (textsIn[0] != null && textsIn[1] != null){
						System.out.println(String.format("[%s]: %s", textsIn[0], textsIn[1]));
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		//declarações de variáveis
		Scanner scanner = new Scanner(System.in);
		//solicitar o usuário que digite o seu email
		System.out.println("Por favor, digite um email:");
		String email = scanner.nextLine();
		Runtime.getRuntime().exec("reset");Runtime.getRuntime().exec("clear");
		//fazer login no server
		RMIChatService service = new RMIChatServiceMock();
		String token = service.login(email);
		//paralelizar
		Sender sender = new Sender(service, scanner, token);
		Receiver receiver = new Receiver(service, token);
		//inicializar as threads
		Thread t0 = new Thread(sender);
		t0.start();
		Thread t1 = new Thread(receiver);
		t1.start();
	}
}

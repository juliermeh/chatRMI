package ag.ifpb.chat.rmi.share;

import java.util.List;
import java.util.Map;

/*

DROP TABLE users;
DROP TABLE messages;
DROP TABLE messages_users;
DROP TABLE users_connected;

CREATE TABLE users(
	uemail VARCHAR(100) NOT NULL PRIMARY KEY
);
 
CREATE TABLE messages(
	id bigint PRIMARY KEY,
	ufrom varchar(100) NOT NULL,
	text varchar(256) NOT NULL,
	is_removed boolean NOT NULL
);

CREATE TABLE users_connected(
	token varchar(100) PRIMARY KEY
	uemail varchar(100) NOT NULL,
	is_removed boolean NOT NULL
 );
 
CREATE TABLE messages_users(
	message_id bigint NOT NULL,
	uto varchar(100) NOT NULL,
	ufrom varchar(100) NOT NULL,
  	is_sended  boolean NOT NULL
);


 */
public interface ChatServer {
	
	/**
	 * Verifica a existência do usuário a partir
	 * do email e, caso não exista, cria o usuário.
	 * Após isto, ou caso já exista, adiciona-se o usuário
	 * em repositório de "usuários existentes" e o adiciona no
	 * repositório de "usuários conectados".
	 * 
	 * Database:
	 *  -- usuários existentes
	 * 	users (
	 *    email varchar(100)
	 *  )
	 *  
	 *  -- usuários conectados
	 *  users_connected (
	 *    email varchar(100),
	 *    is_removed boolean
	 *  )
	 * 
	 * [adicional]:
	 * - caso o usuário já esteja conectado,
	 * remover a sua conectar.
	 * 
	 * 
	 * @param email
	 * @return
	 */
	Session login(String email);
	
	/**
	 * Mantém a mensagem até saber que todos receberam
	 * e encaminha as mensagens para todos os usuários
	 * que estão no repositório de "usuários existentes".
	 * 
	 * Database:
	 *  message (
	 *    id long,
	 *    from varchar(100),
	 *    text varchar(256),
	 *    is_removed boolean
	 *  )
	 *  
	 *  -- percorrer todos os usuários existentes
	 *  -- e a partir de uma mensagem criar uma lista
	 *  -- de "message_user" com a mensagem+usuário (por tupla).
	 *  -- isto resultará em 1 mensagem enviada para N usuários existentes,
	 *  -- logo terá como resultado N "message_user"
	 *  message_user(
	 *    message_id long,
	 *    to varchar(100), --email do destinatário
	 *    from varchar(100), --email da origem
	 *    is_sended boolean
	 *  )
	 * 
	 * @param msg
	 */
	void persistAndforwardToAll(Message msg);
	
	/**
	 * Remove a mensagem depois de saber que todos
	 * a receberam.
	 * 
	 * [adicional]:
	 * - alterar o campo "is_removed" em "message" para "true"
	 * 
	 * @param msg
	 */
	void remove(Message msg);
        
        /* Listar mensagens */
        Map<String,List<Message>> listMessagesAll();
        
        /* remover as notificações atualizando valor de is_romoved para TRUE */
        void removeNotification(long message_id, String uto);
	
}

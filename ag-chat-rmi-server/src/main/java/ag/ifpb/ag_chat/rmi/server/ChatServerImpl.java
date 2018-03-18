package ag.ifpb.ag_chat.rmi.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ag.ifpb.chat.rmi.share.ChatServer;
import ag.ifpb.chat.rmi.share.Message;
import ag.ifpb.chat.rmi.share.Session;
import java.util.HashMap;
import java.util.Map;

public class ChatServerImpl implements ChatServer {

	private void deleteUser(Connection conn, String email) throws SQLException {
		String sql = "UPDATE users_connected SET is_removed=true WHERE uemail=?";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, email);
		statement.executeUpdate();
	}

	private void insertUser(Connection conn, String token, String email) throws SQLException {
		String sql = "INSERT INTO users_connected (token, uemail, is_removed) VALUES(?, ?,false)";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, token);
		statement.setString(2, email);
		statement.executeUpdate();
	}
	
	private List<String> listAllUsers(Connection conn) throws SQLException{
		String sql = "SELECT uemail,is_removed from users_connected WHERE is_removed=false";
		PreparedStatement statement = conn.prepareStatement(sql);
		ResultSet rs = statement.executeQuery();
		//
		List<String> result = new ArrayList<String>();
		while(rs.next()){
			result.add(rs.getString("uemail"));
		}
		//
		return result;
	}

	public Session login(String email) {
		Connection conn = Connector.init();
		Session session = null;
		try {
			//
			String token = String.valueOf(System.currentTimeMillis());
			//
			deleteUser(conn, email);
			insertUser(conn, token, email);
			//
			session = new Session();
			session.setToken(token);
			session.setUser(email);
			//
		} catch (SQLException ex) {
			Logger.getLogger(ChatServerImpl.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				Connector.close();
			} catch (SQLException ex) {
				Logger.getLogger(ChatServerImpl.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return session;
	}

	public void persistAndforwardToAll(Message msg) {
		String sql = "INSERT INTO messages_users(message_id, "
				+ "ufrom, uto, is_sended) VALUES (?, ?, ?, false);";
		//
		Connection conn = Connector.init();
		try {
			List<String> users = listAllUsers(conn);
			PreparedStatement ps = conn.prepareStatement(sql);
			//
			for (String uto : users) {
				ps.setLong(1, msg.getID());
                                ps.setString(2, msg.getUser());
				ps.setString(3, uto);//
				ps.executeUpdate();
			}
		} catch (SQLException ex) {
			Logger.getLogger(ChatServerImpl.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				Connector.close();
			} catch (SQLException ex) {
				Logger.getLogger(ChatServerImpl.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void remove(Message msg) {
		Connection conn = Connector.init();
		try {
			String sql = "UPDATE FROM messages WHERE id = ?;";
			//
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setLong(1, msg.getID());
			statement.executeUpdate();
			//
		} catch (SQLException ex) {
			Logger.getLogger(ChatServerImpl.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				Connector.close();
			} catch (SQLException ex) {
				Logger.getLogger(ChatServerImpl.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

    public Map<String,List<Message>> listMessagesAll() {
        String sql = "SELECT a.id, a.ufrom,a.text,b.uto FROM Messages a"
                + "JOIN messages_users b ON a.id = b.message_id WHERE a.is_removed = FALSE"
                + "AND b.is_sended = FALSE";
        
        Map<String,List<Message>> results = new HashMap<String,List<Message>>();
        
        Connection conn = Connector.init();
        
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    // mostrar os resultados //
                    long id = rs.getLong(1);
                    String ufrom = rs.getString(2);
                    String text = rs.getString(3);
                    String uto = rs.getString(4);
                    // mostrar a mensagem //
                    Message m = new Message();
                    m.setID(id);
                    m.setUser(ufrom);
                    m.setContent(text);
                    // montar a lista de mensagens //
                    if(results.get(uto) == null){
                        results.put(uto, new ArrayList<Message>());
                    }
                    results.get(uto).add(m);
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(ChatServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        return results;
    }

    public void removeNotification(long message_id, String uto) {
        String sql = "UPDATE messages SET is_removed=true WHERE id = " + message_id + " AND uto = " + uto;
        Connection conn = Connector.init();    
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ChatServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

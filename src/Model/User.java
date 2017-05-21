package model;
import java.io.Serializable;

/**
 * @author Xoapit
 *
 */
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String myIP;
	private String serverIP;
	private String time;
	
	public User(String name, String myIP, String serverIP, String time){
		this.name= name;
		this.myIP= myIP;
		this.serverIP=serverIP;
		this.time= time;		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public String getMyIP() {
		return myIP;
	}

	public void setMyIP(String myIP) {
		this.myIP = myIP;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	
	
}

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class ComToTcpDirector implements Runnable {
	ComConnection com;
	Socket outputSocket;
	Socket inputSocket;
	
	public ComToTcpDirector(ComConnection com,Socket outputSocket,Socket inputSocket) {
		this.com = com;
		this.outputSocket = outputSocket;
		this.inputSocket = inputSocket;
	}
	
	@Override
	public void run() {
		try {
			DataOutputStream outToServer = new DataOutputStream(outputSocket.getOutputStream());
			
			while(true){
				byte [] data = com.receive();
				if(data.length>0)
					outToServer.write(data);
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} 
		

		
	}
	
	
}

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class TcpToComDirector extends ComToTcpDirector {

	public TcpToComDirector(ComConnection com, Socket outputSocket,
			Socket inputSocket) {
		super(com, outputSocket, inputSocket);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void run() {
		try {
			byte[] buffer = new byte[1024];
			DataInputStream inFromServer =  new DataInputStream(inputSocket.getInputStream());
			int len;
			while((len = inFromServer.read(buffer))!= -1){
				byte[] result = new byte[len];
				
				for(int i=0;i<len;i++)
					result[i] = buffer[i];
				com.send(result);
				
			}
			System.out.println(" tcp to com abourt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

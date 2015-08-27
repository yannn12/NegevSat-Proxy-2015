import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;


public class ComConnection implements Connection<byte []> {
	
	String portName;
	SerialPort serialPort;
	DataOutputStream outStream;
	DataInputStream inStream;
	
	public ComConnection(String port) {
		this.portName = port;
	}
	
	@Override
	public boolean connect() throws Exception{
        CommPortIdentifier portIdentifier = null;

		portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
            return false;
        }
        CommPort commPort = null;
		commPort = portIdentifier.open(this.getClass().getName(),2000);

        if ( !(commPort instanceof SerialPort) )
        {
        	System.out.println("Error: Port is currently in use");
            return false;
        }
        serialPort = (SerialPort) commPort;

		serialPort.setSerialPortParams(19200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

        serialPort.setFlowControlMode(serialPort.FLOWCONTROL_RTSCTS_OUT|serialPort.FLOWCONTROL_RTSCTS_IN);

        outStream = new DataOutputStream(serialPort.getOutputStream());
        inStream = new DataInputStream(serialPort.getInputStream()); 
        return true;
	}

	@Override
	public boolean send(byte [] msg) {
		try {
			outStream.write(msg);
	
	        this.outStream.flush();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	private byte[] removeDelimiter(byte[] msg)
	{
		Vector<Byte> resultVec = new Vector<Byte>();
		for(int i=0;i<msg.length;i++){
			switch(msg[i]){
				case 10:
					resultVec.add((byte) 11);
					resultVec.add((byte) 12);
					break;
				case 11:
					resultVec.add((byte) 11);
					resultVec.add((byte) 11);
				default:
					resultVec.add((byte) msg[i]);
					break;
			}
		}
		byte[] result = new byte[resultVec.size()];
		for(int i=0;i<result.length;i++){
			result[i] = resultVec.get(i);
		}
		return result;
	}

	@Override
	public byte [] receive() {
		byte [] buffer = new byte[1024];
		try{
			int lenght = inStream.read(buffer);
			byte[] result = new byte[lenght];
			for(int i=0;i<lenght;i++){
				result[i] = buffer[i];
			}
			return result;
		}
		catch(Exception exp){
			return null;
		}
		

	}

}

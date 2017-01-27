// Student ID : 1001215289
// Name : Aastha Gupta
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;

public class Client {
	private static Socket socket;
	final static String CRLF = "\r\n";
	private static String fileName;

	public static void main(String args[]) throws Exception{
		long timeStart, timeEnd, timeUsed;
		timeStart = System.nanoTime();
		//Setting connection with the server
		String server_url = "http://" + args[0] + ":" + args[1] + "/" + args[2];
		URL url = new URL(server_url);
		socket = new Socket("localhost",Integer.parseInt(args[1]));
		System.out.println("connection established with the server");
		fileName = args[2];
		//requesting file from the server
		PrintStream printstream = new PrintStream(socket.getOutputStream());
		printstream.println("GET" +" /"+ fileName + " "+ "HTTP/1.1"+ CRLF);
		System.out.println("Connection received from : " + socket.getInetAddress().getHostName());
		System.out.println("Port : " + socket.getPort());
		System.out.println("Protocol :"+ url.getProtocol());
		System.out.println("TCP No Delay : " + socket.getTcpNoDelay());
		System.out.println("Timeout : " + socket.getSoTimeout());
		System.out.println("response from server");
		// printing server response
		try{
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			StringBuilder stringbuilder = new StringBuilder();
			String get_String;
			while ((get_String = bufferedreader.readLine()) != null) {
			    stringbuilder.append(get_String + "\n");
			}

			bufferedreader.close();
			System.out.println(stringbuilder.toString());
			socket.close();
			timeEnd = System.nanoTime();
			timeUsed = (timeEnd - timeStart) / 1000000;
			System.out.println("RTT=" +  (double)Math.round(timeUsed * 100)/100+" ms");

		} catch (IOException ex) {
			System.out.println("error");
		}
	}
}








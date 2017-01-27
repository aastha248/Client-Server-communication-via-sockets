// Student ID : 1001215289
// Name : Aastha Gupta
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.StringTokenizer;

public class Server {

	private static ServerSocket serverSocket;
	public static void main(String args[]) throws Exception
	{
		//set up the port number
		int port = Integer.parseInt(args[0]);
		
		try{
			// Establish the listen socket
			serverSocket = new ServerSocket(port);
			System.out.println("Server socket is up and listening for connections");
		}
		catch(Exception e){
			System.out.println("error");	
		}
		// Client server communication requires "always on" server 
		while (true){
			//Listen for a TCP connection request
			Socket socket = serverSocket.accept();
			//process the HTTP request message.
			HttpRequest request = new HttpRequest(socket);
			// Multi-threaded to process the multiple client requests.
			Thread thread = new Thread(request);
			// Start the thread.
			thread.start();
			  
		}
	}
}

final class HttpRequest implements Runnable
{
	final static String CRLF = "\r\n";
	Socket socket;
	public HttpRequest(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket=socket;
	}
	public void run()
	{
		try {
			processRequest();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	private void processRequest() throws Exception {
		// TODO Auto-generated method stub
		
		String server_url = "http://localhost:" + socket.getPort() + "/";
		URL prot = new URL(server_url);
		//Get a reference to the socket's input and output streams.
		OutputStream outputstream = socket.getOutputStream();
		DataOutputStream dataoutputstream = new DataOutputStream(outputstream);
		// Set up input stream readers.
		BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		// Get the request line of the HTTP request message.
		String requestLine = bufferedreader.readLine();
		// Display the request line.
		System.out.println();
		System.out.println(requestLine);
		System.out.println("Connection received from " + socket.getInetAddress().getHostName());
		System.out.println("Port : " + socket.getPort());
		System.out.println("Protocol : "+ prot.getProtocol());
		System.out.println("TCP No Delay : " + socket.getTcpNoDelay());
		System.out.println("Timeout : " + socket.getSoTimeout());
		//Display Header
		String headerLine = null;
		System.out.println("header : ");
		while ((headerLine = bufferedreader.readLine()).length() != 0) {
			System.out.println(headerLine);
		}

		// Extracting filename from the request line.
		StringTokenizer tokens = new StringTokenizer(requestLine);
		tokens.nextToken(); // skip over the method, which should be "GET"
		String fileName = tokens.nextToken();
		fileName =  "C:\\Users\\Aastha\\workspace\\ClientServerCommunication\\bin\\" + fileName;

		// Opening the requested file.
		FileInputStream fileinputstream = null;
		boolean fileExists = true;
		try {
			fileinputstream = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			fileExists = false;
		}
		// Construct the response message.
		String statusLine = null, contentTypeLine = null, entityBody = null;
		if (fileExists) {
			statusLine = "HTTP/1.1 200 OK";
			contentTypeLine = "Content Type: " +"  " + contentType( fileName ) + CRLF;
		} else {
			statusLine = "HTTP/1.1 404 Not Found";
			contentTypeLine = "no contents to display\n";
			entityBody = "<HTML><HEAD><TITLE>Not Found</TITLE></HEAD><BODY>Not Found</BODY></HTML>";
		}
		// Send response.
		dataoutputstream.writeBytes(CRLF);
		dataoutputstream.writeBytes(statusLine);
		dataoutputstream.writeBytes(CRLF);
		dataoutputstream.writeBytes(contentTypeLine);
		dataoutputstream.writeBytes(CRLF);
		if (fileExists) {
			dataoutputstream.writeBytes(CRLF);
			sendBytes(fileinputstream, outputstream);
			fileinputstream.close();
			dataoutputstream.writeBytes(CRLF);
			dataoutputstream.writeBytes(statusLine);
			dataoutputstream.writeBytes(CRLF);
			dataoutputstream.writeBytes(contentTypeLine);
		} else {
			dataoutputstream.writeBytes(CRLF);
			dataoutputstream.writeBytes(entityBody);
			dataoutputstream.writeBytes(CRLF);
			dataoutputstream.writeBytes(statusLine);
			dataoutputstream.writeBytes(CRLF);
			dataoutputstream.writeBytes(contentTypeLine);
		}
		dataoutputstream.flush();
		dataoutputstream.close();
		socket.close();
	}
	private static void sendBytes(FileInputStream fileinputstream, OutputStream outputstream)
			throws Exception
	{
		byte[] buffer = new byte[1024];
		int bytes = 0;
		while((bytes = fileinputstream.read(buffer)) != -1) {
			outputstream.write(buffer, 0, bytes);
		}
	}
	private String contentType(String fileName) {
		// TODO Auto-generated method stub
		if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
			return "text/html";
		}
		if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")){
			return "image/jpeg";
		}
		if(fileName.endsWith(".gif")) {
			return "image/gif";
		}
		return "application/octet-stream";

	}

}
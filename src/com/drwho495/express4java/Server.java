import java.io.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.net.*;
import java.util.*;

public class Server {

  static HashMap<String, HttpEvent.EventHandler> getFunctions = new HashMap<String, HttpEvent.EventHandler>();
	public static class res {
		static PrintWriter pr2 = null;

		res(PrintWriter pr) {
			res.pr2 = pr;
		}

		public void send(String s) {
			res.pr2.println("HTTP/1.1 200 OK\r\n\n" + s);
			res.pr2.flush();
		}

		public void sendFile(String file) throws IOException {
			StringBuilder htmlBuilder = new StringBuilder();

			Files.lines(new File(file).toPath())
			   .map(s -> s.trim())
				 .filter(s -> !s.isEmpty())
				 .forEach(htmlBuilder::append);

			res.pr2.println("HTTP/1.1 200 OK\r\n\n" + htmlBuilder.toString());
			res.pr2.flush();
		}
	}

	public static class request {
		String body = null;
		String route = null;
		String method = null;

		request(String body, String route, String method) {
			this.body = body;
			this.route = route;
			this.method = method;
		}

		public String getBody() {
			return this.body;
		}
	}

  public static void start() throws IOException {
    @SuppressWarnings("resource")
	ServerSocket servSoc = new ServerSocket(3000);
    	    	
		while(true) {
			Socket soc = servSoc.accept();
			
			System.out.println("New Connection");
      
			BufferedReader br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			
			String request = br.readLine();

			String route = request.split(" ")[1];

			System.out.println("Route: " + route);
			System.out.println(request);

			PrintWriter out = new PrintWriter(soc.getOutputStream());
			
			boolean bodyMode = false;
			String body = "";

			if(request.startsWith("POST")) {
						String line = null;

						out.println("HTTP/1.1 200 OK\r\nConnection: close");
						out.flush();

						while((line = br.readLine()) != null) {
							if(line.equals("")) {
								bodyMode = true;
							}

							if(bodyMode == true) {
								body = body + "\n" + line;
							}
						}
			}

			if(route != null) {
				if(getFunctions.get(route) != null) {
					try {
						getFunctions.get(route).run(new res(out), new request(body.toString(), route, request.split(" ")[0]));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			out.close();
			soc.close();
		}
  }

	public void route(String reqRoute, HttpEvent.EventHandler method) {
		try {
			getFunctions.put(reqRoute, method);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
}

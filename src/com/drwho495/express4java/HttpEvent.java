public class HttpEvent {
	public interface EventHandler {
		void run(Server.res a, Server.request req);
	}
}

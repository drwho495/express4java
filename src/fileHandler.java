import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class fileHandler {
	  String render(String filename) {
		StringBuilder htmlBuilder = new StringBuilder();  
		
		try {
			Files.lines(new File("C:\\Users\\drwho\\eclipse-workspace\\my_web_server\\src\\my_web_server\\" + filename).toPath())
			     .map(s -> s.trim())
				 .filter(s -> !s.isEmpty())
				 .forEach(htmlBuilder::append);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return htmlBuilder.toString();
	  }


	  
}

import javax.swing.JFrame;

public class ClientTest {
	public static void main(String[] main) {
		Client object = new Client("127.0.0.1");
		object.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		object.startRunning();
		
	}

}

import javax.swing.JFrame;

public class ServerTest {
	public static void main(String[] args) {
		Server object = new Server();
		object.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		object.startRunning();
	}

}

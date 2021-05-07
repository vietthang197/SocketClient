import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author thanglv on 5/7/2021
 * @project SocketClient
 */
public class TestMain {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        serverSocket.setReuseAddress(true);
        ExecutorService executor = Executors.newCachedThreadPool();

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                if (socket.isConnected()) {
                    executor.execute(new WorkerWriter(socket));
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


}

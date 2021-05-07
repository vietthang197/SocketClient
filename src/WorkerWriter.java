import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author thanglv on 5/7/2021
 * @project SocketClient
 */
public class WorkerWriter implements Runnable {

    private Socket socketClient;

    private InputStreamReader inputStreamReader;

    private BufferedReader bufferedReader;

    public WorkerWriter(Socket socketClient) {
        this.socketClient = socketClient;
    }

    @Override
    public void run() {
        try {
            if (socketClient.isConnected()) {
                if (inputStreamReader == null) {
                    inputStreamReader = new InputStreamReader(this.socketClient.getInputStream(), StandardCharsets.UTF_8);
                    bufferedReader = new BufferedReader(inputStreamReader);
                }
                String line = null;
                Path filePath = Paths.get("test.txt");
                if (!Files.exists(filePath)) {
                    Files.createFile(filePath);
                }
                AsynchronousFileChannel asyncChannel = AsynchronousFileChannel.open(filePath, StandardOpenOption.WRITE);
                while ((line = bufferedReader.readLine()) != null) {
                    line = line + "\n";
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    byteBuffer.put(line.getBytes());
                    byteBuffer.flip();
                    // ghi ra file async
                    asyncChannel.write(byteBuffer, asyncChannel.size());
                    byteBuffer.clear();
                }

                asyncChannel.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.inputStreamReader.close();
                this.bufferedReader.close();
                this.socketClient.close();
            } catch (IOException e) {
                System.out.println( e.getMessage());
            }
        }
    }
}

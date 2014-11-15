import java.io.*;
import java.net.*;

public class MyClient {
    public static void main(String[] args) throws IOException {

        String hostName = "127.0.0.1";
        int portNumber = 8888;

        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out =
                    new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in =
                    new BufferedReader(
                new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn =
                    new BufferedReader(
                new InputStreamReader(System.in))
            ) {
            String userInput;
            while (true) {
                userInput = stdIn.readLine();
                out.println(userInput);
                System.out.println("echo: " + in.readLine());
            }
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                               hostName);
            System.exit(1);
        }
    }
}
import java.io.*;
import java.net.*;
import java.util.*;

public class MyClient {
    public static void main(String[] args) throws IOException, InterruptedException {

        String hostName = "127.0.0.1";
        int portNumber = 8888;

        try {
            Socket MySocket = new Socket(hostName, portNumber);
            PrintWriter out =
                new PrintWriter(MySocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(MySocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader( new InputStreamReader(System.in));

            String userInput = "";
            Thread.sleep(400);
            if (in.ready()) {
                while (in.ready()) {
                    System.out.print(Character.toChars(in.read()));[]
                }
            }

            while (true) {

                userInput = stdIn.readLine();
                out.println(userInput);
                Thread.sleep(400);
                if (in.ready()) {
                    while (in.ready()) {
                        System.out.print(Character.toChars(in.read()));

                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                               hostName);
            System.exit(1);
        }
    }
}
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.text.*;

public class MyServer implements Runnable {

    String[] MyProblemList = new String[10];
    String[] MyAnswerList = new String[10];

    String[] StudentAnswerList = new String[10];

    final Socket socket;

    public MyServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        System.out.printf("connection received from %s%n", socket);
        try {
            // socket open: make PrinterWriter and Scanner from it...
            String studentName = "";
            int studentScore = 0;
            int studentInput = 0;

            parsefile("questions");
            parseans("answers");

            boolean keepGoing = true;
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            BufferedReader in
                = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // read from input, and echo output...
            String line;
            pw.printf("Please enter your name%n");
            pw.flush();
            System.out.printf("Welcome: %s%n", studentName);
            studentName = in.readLine();

            while (keepGoing) {
                //System.out.printf("%s says: %s%n", socket, line);
                //pw.printf("echo: %s%n", line);
                //pw.flush();
                for (int i = 0; i < 10; i++) {
                    pw.printf(MyProblemList[i]);
                    pw.flush();
                    line = in.readLine();
                    if (line.equals(MyAnswerList[i])) {
                        studentScore++;
                    }
                    if (i == 9) {
                        keepGoing = false;
                    }
                }
            }


            pw.printf("Thank you!");
            pw.flush();
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            PrintWriter writer = new PrintWriter(studentName + " " + timeStamp + ".studentrecord", "UTF-8");
            writer.println("Name: " + studentName);
            writer.println("Score: " + studentScore);
            writer.println("Time: " + timeStamp);
            writer.close();
            System.out.println(studentName + " is done");

            // input done, close connections...
            pw.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parsefile(String crackle) {
        String filename = null;
        try {
            filename = (crackle + ".txt");
            Scanner fileparser = new Scanner(new File(filename));
            for (int i = 0; i < MyProblemList.length; i++) {
                MyProblemList[i] = fileparser.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException while parsing: " + filename + "]");
        }
    }

    public void parseans(String crackle) {
        String filename = null;
        try {
            filename = (crackle + ".txt");
            Scanner fileparser = new Scanner(new File(filename));
            for (int i = 0; i < MyAnswerList.length; i++) {
                MyAnswerList[i] = fileparser.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException while parsing: " + filename + "]");
        }
    }

    public static void main(String[] args) throws IOException {
        // allocate server socket at default port...
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.printf("socket open, waiting for connections on %s%n",
                          serverSocket);

        // infinite server loop:
        // accept connection, spawn thread to handle...
        while (true) {
            Socket socket = serverSocket.accept();
            MyServer server = new MyServer(socket);
            new Thread(server).start();
        }
    }
}
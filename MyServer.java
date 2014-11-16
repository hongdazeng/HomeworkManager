import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.text.*;

public class MyServer implements Runnable {

    static ArrayList<String> classGrades = new ArrayList<String>();
    static int totalProblemNumber = 0;
    static File file;

    static String questionfiles = "";
    static String answerfiles = "";

    String[] MyProblemList = new String[totalProblemNumber];
    String[] MyAnswerList = new String[totalProblemNumber];

    String[] StudentAnswerList = new String[totalProblemNumber];

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

            parsefile("questions");
            parseans("answers");

            boolean keepGoing = true;
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            BufferedReader in
                = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw2 = new PrintWriter(bw);

            String line;
            pw.printf("Please enter your name ");
            pw.flush();
            studentName = in.readLine();
            pw.printf("Welcome " + studentName + " %n");
            System.out.printf(studentName + " have connected%n");

            if (studentName.equals("Teacher")) {
                keepGoing = false;
                Iterator it = classGrades.iterator( );
                while (it.hasNext()) {
                    System.out.println(it.next());
                }
            }



            while (keepGoing) {
                //System.out.printf("%s says: %s%n", socket, line);
                //pw.printf("echo: %s%n", line);
                //pw.flush();
                for (int i = 0; i < totalProblemNumber; i++) {
                    pw.printf(MyProblemList[i]);
                    pw.flush();
                    line = in.readLine();
                    if (line.equals(MyAnswerList[i])) {
                        studentScore++;
                    }
                    if (i == (totalProblemNumber - 1)) {
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
            if (!studentName.equals("Teacher")) {
                String tobeadd = ("Time: " + timeStamp + " Name: " + studentName + " Score: " + studentScore);
                System.out.println(tobeadd);
                classGrades.add(tobeadd);
                pw2.println(tobeadd);
            }
            writer.close();
            System.out.println(studentName + " is done");
            pw.println(" Your score is " + studentScore);

            // input done, close connections...
            pw.close();
            pw2.close();
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
        //
        file = new File("Records.txt");
        if (!file.exists()) {
            file.createNewFile();
        }

        Scanner serverinputreader = new Scanner(System.in);
        System.out.println("Please enter the expected number of questions ");
        totalProblemNumber = serverinputreader.nextInt();
        serverinputreader.nextLine();
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
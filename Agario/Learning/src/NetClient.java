import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by bh on 03/08/2016.
 */
public class NetClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("172.25.12.92", 7576);
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        Scanner scanner = new Scanner(System.in);
        Scanner inputScanner = new Scanner(inputStream);
        PrintWriter printWriter = new PrintWriter(outputStream);
        while (true) {

            printWriter.println(scanner.nextLine());
            System.out.println("sent");
            System.out.println("payam said:" + inputScanner.next());

        }


    }
}

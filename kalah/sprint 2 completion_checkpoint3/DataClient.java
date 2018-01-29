

    import java.util.*;
    import java.net.Socket;
    import java.io.IOException;
    import java.io.PrintWriter;
    import java.io.BufferedReader;
    import java.io.InputStreamReader;
    import java.io.File;
    //client
    public class DataClient {
        public static void main(String[] args) throws IOException {
          //  String serverAddress = "127.0.0.1";
            Socket s = new Socket("build.tamu.edu", 7896);
            BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String answer = input.readLine();
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            System.out.println(answer);
            s.close();
            System.exit(0);
        }
    }








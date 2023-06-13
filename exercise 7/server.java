import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private List<TranslationRequest> translationLog;
    private int requestCounter;

    public Server() {
        translationLog = new ArrayList<>();
        requestCounter = 0;
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started. Waiting for connections...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            new Thread(new ClientHandler(clientSocket)).start();
        }
    }

    private void translateText(String text, String targetLanguage, PrintWriter out) {
        String translatedText = ""; // Implement your translation logic here
        out.println(translatedText);
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String text = in.readLine();
                String targetLanguage = in.readLine();
                translateText(text, targetLanguage, out);

                TranslationRequest request = new TranslationRequest(requestCounter, text, targetLanguage);
                translationLog.add(request);
                requestCounter++;

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(9999);
    }
}

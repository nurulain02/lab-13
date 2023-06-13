import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private JFrame frame;
    private JTextField textInput;
    private JComboBox<String> languageDropdown;
    private JTextArea translationArea;
    private JLabel requestCounterLabel;
    private int requestCounter;

    public Client() {
        requestCounter = 0;
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Translation Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel textLabel = new JLabel("Text:");
        textInput = new JTextField(20);
        inputPanel.add(textLabel);
        inputPanel.add(textInput);

        JLabel languageLabel = new JLabel("Target Language:");
        String[] languages = {"Bahasa Malaysia", "Arabic", "Korean"};
        languageDropdown = new JComboBox<>(languages);
        inputPanel.add(languageLabel);
        inputPanel.add(languageDropdown);

        JButton translateButton = new JButton("Translate");
        translateButton.addActionListener(new TranslateButtonListener());
        inputPanel.add(translateButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        translationArea = new JTextArea();
        translationArea.setEditable(false);
        frame.add(new JScrollPane(translationArea), BorderLayout.CENTER);

        requestCounterLabel = new JLabel("Total Requests: 0");
        frame.add(requestCounterLabel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void connect(String serverAddress, int serverPort) throws IOException {
        socket = new Socket(serverAddress, serverPort);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void sendRequest(String text, String targetLanguage) {
        out.println(text);
        out.println(targetLanguage);
    }

    private void displayResponse(String response) {
        translationArea.setText(response);
    }

    private void displayStatistics() {
        requestCounterLabel.setText("Total Requests: " + requestCounter);
    }

    private void closeConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    private class TranslateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String text = textInput.getText();
            String targetLanguage = (String) languageDropdown.getSelectedItem();

            try {
                connect("localhost", 9999);
                sendRequest(text, targetLanguage);
                String response = in.readLine();
                displayResponse(response);
                closeConnection();

                requestCounter++;
                displayStatistics();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Client();
            }
        });
    }
}

import java.net.*;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.TrayIcon.MessageType;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;

public class Client extends JFrame {
    BufferedReader br;
    PrintWriter out;
    Socket soc;

    // declare components
    private JLabel heading = new JLabel("HeyClient");
    private JTextArea MessageArea = new JTextArea();
    private JTextField MessageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Client() {
        try {

            System.out.println("Sending request to server...");
            Socket soc = new Socket("localhost",7777);
           // soc = new Socket("127.0.0.1", 7777);
            System.out.println("Connected...");
            br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            out = new PrintWriter(soc.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();

        }
        createGUI();
        handelEvents();
        startReading();
        // startWriting();

    }

    // Utility method to load and resize an image
    protected static ImageIcon createResizedImageIcon(String path, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(new File("chat.png"));
            Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void createGUI() {
        this.setTitle("Client MAessager[END]");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        // coding for components
        heading.setFont(font);
        ImageIcon resizedIcon = createResizedImageIcon("chat.png", 50, 50);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setIcon(resizedIcon);
        MessageArea.setFont(font);
        MessageInput.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        MessageArea.setEditable(false);
        // for setting layout
        this.setLayout(new BorderLayout());
        // adding components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane js = new JScrollPane(MessageArea);
        this.add(js, BorderLayout.CENTER);
        this.add(MessageInput, BorderLayout.SOUTH);

        // MessageInput pr kaam for putting at center

        MessageInput.setHorizontalAlignment(SwingConstants.CENTER);

    }

    private void handelEvents() {
        MessageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("key released "+e.getKeyCode());
                if (e.getKeyCode() == 10) {
                    // System.out.println("You Pressed Enter !");
                    String contentToSend = MessageInput.getText();
                    MessageArea.append("Me: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    MessageInput.setText("");
                    MessageInput.requestFocus();
                }

                // throw new UnsupportedOperationException("Unimplemented method
                // 'keyReleased'");
            }

        });
    }

    void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader Started");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("server terminated chat...");
                        JOptionPane.showMessageDialog(this, "Server Terminated the Chat");
                        MessageInput.setEnabled(false);
                        soc.close();
                        break;
                    }
                    // System.out.println("Server Says " + msg);
                    MessageArea.append("Server Says: " + msg + "\n");

                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection is closed");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting() {
        System.out.println("Writer started");
        Runnable r2 = () -> {
            try {
                while (!soc.isClosed()) {
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br2.readLine();

                    out.println(content);
                    out.flush();
                    if (content.equals("exit")) {
                        soc.close();
                        break;
                    }
                }

            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("connection is closed");
            }
        };

        new Thread(r2).start();

    }

    public static void main(String[] args) {
        Client C = new Client();
    }
}

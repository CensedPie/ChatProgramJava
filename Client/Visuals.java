package chatmain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import message.Message;

public class Visuals extends JPanel implements Runnable {
    private final Thread thread;
    private final int WIDTH, HEIGHT;
    private final Color background, topBar;
    public static Font newFont = new Font("Monospaced Bold", 1, 30);
    private final JFrame frame;
    private final JButton back, send, register;
    private JTextField textfield, textfieldUser, textfieldPassword;
    private final JLabel userLabel, passwordLabel;
    private byte menuIndex;
    private final ImageIcon backIcon, backIconPressed, sendIcon, sendIconPressed, registerIcon, registerIconPressed;
    private JTextArea textArea;
    private final JScrollPane scrollPane;
    private Client c;
    private File fSS, fSR;
    private FileWriter fWriter, fWriter1;
    private PrintWriter pWriter, pWriter1;
    private Encryption encrypt;
    private boolean sendCheck;
    
    public Visuals() {
        sendCheck = false;
        try {
            fSS = new File("fSS.dat");
            fSR = new File("fSR.dat");
            fSS.createNewFile();
            fSR.createNewFile();
            fWriter = new FileWriter(fSS, true);
            fWriter1 = new FileWriter(fSR, true);
            pWriter = new PrintWriter(fWriter);
            pWriter1 = new PrintWriter(fWriter1);
        }
        catch(IOException e) {
            
        }
        encrypt = new Encryption();
        frame = new JFrame();
        this.setLayout(null);
        WIDTH = 375;
        HEIGHT = 667; //(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 200;
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setContentPane(this);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setFocusable(true);
        background = new Color(186, 218, 238);
        topBar = new Color(163, 182, 144);
        backIcon = new ImageIcon("back.png");
        backIconPressed = new ImageIcon("backPressed.png");
        sendIcon = new ImageIcon("send.png");
        sendIconPressed = new ImageIcon("sendPressed.png");
        registerIcon = new ImageIcon("register.png");
        registerIconPressed = new ImageIcon("registerPressed.png");
        back = new JButton();
        send = new JButton();
        register = new JButton();
        textfield = new JTextField(100){
            @Override
            protected void paintComponent(Graphics g) {
                //g2d.setPaint(p);
                setOpaque( false );
                g.setColor(Color.WHITE);
                g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 50, 50);
                super.paintComponent( g );
                //setOpaque( true );
            }
            @Override
            protected void paintBorder(Graphics g) {
                
            }
        };
        textfield.setBounds(WIDTH/100, HEIGHT - 11* HEIGHT/80, 6* (WIDTH/10), HEIGHT/12);
        textfield.setFont(newFont);
        textfield.setSelectedTextColor(Color.GREEN);
        textfieldUser = new JTextField(15){
            @Override
            protected void paintComponent(Graphics g) {
                //g2d.setPaint(p);
                setOpaque( false );
                g.setColor(Color.WHITE);
                g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 50, 50);
                super.paintComponent( g );
                //setOpaque( true );
            }
            @Override
            protected void paintBorder(Graphics g) {
                
            }
        };
        textfieldUser.setBounds(WIDTH/8, (3 * HEIGHT)/8 - 48, (6 * WIDTH)/8, HEIGHT/10);
        textfieldUser.setFont(newFont);
        textfieldUser.setSelectedTextColor(Color.GREEN);
        textfieldPassword = new JTextField(15){
            @Override
            protected void paintComponent(Graphics g) {
                //g2d.setPaint(p);
                setOpaque( false );
                g.setColor(Color.WHITE);
                g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 50, 50);
                super.paintComponent( g );
                //setOpaque( true );
            }
            @Override
            protected void paintBorder(Graphics g) {
                
            }
        };
        textfieldPassword.setBounds(WIDTH/8, (4 * HEIGHT)/8, (6 * WIDTH)/8, HEIGHT/10);
        textfieldPassword.setFont(newFont);
        textfieldPassword.setSelectedTextColor(Color.GREEN);
        userLabel = new JLabel("User:");
        passwordLabel = new JLabel("Password:");
        userLabel.setFont(newFont);
        userLabel.setBounds(WIDTH/2 - userLabel.getPreferredSize().width/2,(3 * HEIGHT)/8 - 100, userLabel.getPreferredSize().width, userLabel.getPreferredSize().height);
        passwordLabel.setFont(newFont);
        passwordLabel.setBounds(WIDTH/2 - 70,(4 * HEIGHT)/8 - 50, 160, userLabel.getPreferredSize().height);
        back.setIcon(new ImageIcon(backIcon.getImage().getScaledInstance(HEIGHT/10, -1, Image.SCALE_SMOOTH)));
        back.setPressedIcon(new ImageIcon(backIconPressed.getImage().getScaledInstance(HEIGHT/10, -1, Image.SCALE_SMOOTH)));
        back.setBorder(null);
        back.setBounds(0, 0, HEIGHT/10, HEIGHT/10);
        back.setMargin(new Insets(0, 0, 0, 0));
        back.addActionListener((ActionEvent e) -> {
            if(menuIndex == 0) {
                System.exit(0);
            }
            else {
                c.disconnect();
                menuIndex = 0;
            }
        });
        send.setIcon(new ImageIcon(sendIcon.getImage().getScaledInstance( 3*(WIDTH/10), HEIGHT/10, Image.SCALE_SMOOTH)));
        send.setPressedIcon(new ImageIcon(sendIconPressed.getImage().getScaledInstance( 3*(WIDTH/10), HEIGHT/12, Image.SCALE_SMOOTH)));
        send.setBorder(null);
        send.setBounds(WIDTH - 3*(WIDTH/10) - WIDTH/30, HEIGHT - 11* HEIGHT/80, 3*(WIDTH/10), HEIGHT/12);
        send.setMargin(new Insets(0, 0, 0, 0));
        send.addActionListener((ActionEvent e) -> {
            if(!textfield.getText().equals("")) {
                sendCheck = true;
                c.sendMessage(new Message(textfield.getText()));
                String textEncrypt = encrypt.encrypt(textfield.getText());
                System.out.println(textEncrypt);
                pWriter.println(textEncrypt);
                pWriter.flush();
                textfield.setText("");
            }
        });
        register.setMargin(new Insets(0, 0, 0, 0));
        register.setIcon(new ImageIcon(registerIcon.getImage().getScaledInstance( WIDTH/3, HEIGHT/10, Image.SCALE_SMOOTH)));
        register.setPressedIcon(new ImageIcon(registerIconPressed.getImage().getScaledInstance( WIDTH/3, HEIGHT/10, Image.SCALE_SMOOTH)));
        register.setBorder(null);
        register.setBounds(4 * (WIDTH/ 8) - WIDTH/6, 5*(HEIGHT/8) + HEIGHT/16, WIDTH/3, HEIGHT/10);
        register.addActionListener((ActionEvent e) -> {
            if(menuIndex == 0) {
                if((!textfieldUser.getText().equals("")) && (!textfieldPassword.getText().equals(""))) {
                    c = new Client(textfieldUser.getText(), textfieldPassword.getText());
                    if(!c.start()) {
                        textfieldUser.setText("");
                        textfieldPassword.setText("");
                        return;
                    }
                    menuIndex = 1;
                }
            }
        });
        textfield.setEditable(true);
        textfieldUser.setEditable(true);
        thread = new Thread(this);
        textArea = new JTextArea(5, 5);
        textArea.setFont(newFont);
        textArea.setBackground(background);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0, HEIGHT/10, WIDTH - 5, HEIGHT - 5* HEIGHT/20);
        frame.add(back);
        frame.add(send);
        frame.add(textfield);
        frame.add(textfieldUser);        
        frame.add(textfieldPassword);
        frame.add(register);
        frame.add(userLabel);
        frame.add(passwordLabel);
        frame.add(scrollPane);
        menuIndex = 0;
        menu();
        thread.start();
    }
    
    public void menu() {
        if(menuIndex == 0) {
            send.setVisible(false);
            textfield.setText("");
            textfield.setVisible(false);
            scrollPane.setVisible(false);
            textfieldUser.setVisible(true);        
            textfieldPassword.setVisible(true);
            register.setVisible(true);
            userLabel.setVisible(true);
            passwordLabel.setVisible(true);
        }
        else {
            send.setVisible(true);
            textfield.setVisible(true);
            scrollPane.setVisible(true);
            textfieldUser.setVisible(false);        
            textfieldPassword.setVisible(false);
            textfieldUser.setText("");
            textfieldPassword.setText("");
            register.setVisible(false);
            userLabel.setVisible(false);
            passwordLabel.setVisible(false);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(background);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(topBar);
        g.fillRect(0, 0, WIDTH, HEIGHT/10);
        if(!(menuIndex == 0)) {
            g.setColor(new Color(143,156,167));
            g.fillRect(0, HEIGHT - 3* HEIGHT/20, WIDTH, HEIGHT);
        } 
    }
    
    @Override
    public void run() {
        while(frame != null) {
            try {
                //System.out.println(menuIndex);
                menu();
                if(c != null && !c.isQueueEmpty()) {
                    String textToAppend = c.getFromQueue();
                    appendText(textToAppend);
                    if(!sendCheck) {
                        String textToSplit = textToAppend.replaceFirst(".*:", "");
                        String textEncrypt = encrypt.encrypt(textToSplit);
                        pWriter1.println(textEncrypt);
                        pWriter1.flush();
                        sendCheck = false;
                    }
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                }
                repaint();
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.println("Error in Visual Thread");
            }
        }
    }
    
    public void appendText(String aText) {
        textArea.setText(textArea.getText() + "\n" + aText);
    }
}

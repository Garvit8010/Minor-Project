import java.net.*;
import java.text.Format;

import javax.security.auth.PrivateCredentialPermission;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.FontUIResource;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client  extends JFrame{ 
    Socket socket;

    BufferedReader br;
    PrintWriter out;
    

    private JLabel heading=new JLabel("Client Area");  //heading
    private JTextArea messageArea=new JTextArea(); //show message area
    private JTextField messageInput= new JTextField();// message writing area
    private Font font=new Font("Roboto",Font.PLAIN,20);


    public Client()
    
    {
        try {

            System.out.println("sending request to server");
            socket=new Socket("192.168.165.117",7788);
            System.out.println("connection done.. ");


            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out=new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
    
            
        } catch (Exception e) {
            
        }
    }


    private void handleEvents() {

        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) 
            {
                if(e.getKeyChar()==10)
                {
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();

                }

            }

        });

    }


    private void createGUI() 

    {
        this.setTitle("Client Message");
        this.setSize(500,500);
        this.setLocationRelativeTo(null); 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        heading.setFont(font); 
        messageArea.setFont(font); 
        messageInput.setFont(font); 

        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM); 

        heading.setHorizontalAlignment(SwingConstants.CENTER);;
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        this.setLayout(new BorderLayout());

        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea); 
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
    }
    public void startReading()

    {
        
        Runnable r1=()->{
            System.out.println("reader starts....");
            try
            {

            while(true)
            {
                    String msg = br.readLine();
                     if (msg.equals("exit"))
                    {
                        System.out.println("Chat is ended by Server");
                        JOptionPane.showMessageDialog(this, "chat is ended by the server");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }

                    messageArea.append("server:"+msg+"\n");
                }
                
            } catch(Exception e){
            System.out.println("Connection Closed");
            }
        };

        new Thread(r1).start();
    }

    public void startWriting()
    {
        Runnable r2  = () -> 
        {
            System.out.println("writing started.. ");
            try{

            while(!socket.isClosed()){

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("exit")){
                        socket.close();
                        break;
                    }
                    
                }

                System.out.println("Connection is closed");

            }catch(Exception e){
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {

        System.out.println("this is client....");
        new Client();
    }
}
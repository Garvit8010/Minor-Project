import java.net.*;  
import java.io.*;
class Server
{

    ServerSocket server;
    Socket socket;
    
    BufferedReader br; 
    PrintWriter out; 



    public Server()
    {

        try{
        server = new ServerSocket(7788);
        System.out.println("server is ready for connection");
        System.out.println("waiting ......");
        socket=server.accept();

        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out=new PrintWriter(socket.getOutputStream());

        startReading();
        startWriting();


    }
    catch(Exception e){
        e.printStackTrace();
    }

    
    }


    public void startReading()

    {
        Runnable r1=()->{
            System.out.println("reader starts....");

            try {
                

            while(true)
            {
               
                    String msg = br.readLine();
                    if (msg.equals("exit"))
                    {
                        System.out.println("Chat is ended by Client");

                        socket.close(); 
                        break;
                    }

                    System.out.println("Client:"+msg);
                
            }
            
        } catch (Exception e) 
        {
            System.out.println("COnnection Closed");
        }

        };

        new Thread(r1).start();
    }

    public void startWriting()
    {

        Runnable r2  = () -> {
            System.out.println("writing started.. ");

            try {  
                
            
            while(!socket.isClosed()){
        

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    out.println(content);
                    out.flush();

                    // to close the socket we use another if condition
                    if (content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }
              
            }

            
        } catch (Exception e) {

           System.out.println("Connection is Closed");
        }
        };
        new Thread(r2).start();
    }



    public static void main (String[] args)
    {
        System.out.println("this is server ... and going to start");
        new Server();
    }
}
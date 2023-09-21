import java.net.*;
import java.io.*;
public class Server {
    BufferedReader br;
    PrintWriter out;
    ServerSocket soc;
  
    //Server constructor
    public Server(){
        try{
            soc = new ServerSocket(7777, 0, InetAddress.getByName("192.168.9.160"));
            System.out.println("Server Waiting to Connect....");
              Socket socket =soc.accept();
            br =new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out =new PrintWriter(socket.getOutputStream());  
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        startReading();
             startWriting();
       
    }
    public void startReading() {
        //Here we will create a Thread, that will take client input and show it to user...
//--------------------------------------------------------------------------------------------------------
                  // The lambda expression () -> System.out.println("Hello, World!") represents 
                  //an implementation of the run() method of the Runnable interface. It is similar
                  // to creating a separate class that implements the Runnable interface and providing 
                  //the implementation for the run() method.
                  // The lambda expression is essentially a shorthand way of defining a single-method 
                  //implementation for functional interfaces.
//--------------------------------------------------------------------------------------------------------
      Runnable r1=()->{
System.out.println("Reader Started");
try{ 
while(true){
    String msg = br.readLine();
    if(msg.equals("exit"))
    {
        System.out.println("Client Terminated chat...");
      soc.close();
        break;
    }
System.out.println("Client Says: "+msg);
}  

}catch(Exception e){
   System.out.println("connection is closed");
    // e.printStackTrace();
}
      };
    new Thread(r1).start();
    

    }


    public void startWriting(){
       
        //Here we will create a Thread, that will take user input send to client...
//--------------------------------------------------------------------------------------------------------
                // The lambda expression () -> System.out.println("Hello, World!") represents 
                //an implementation of the run() method of the Runnable interface. It is similar
                // to creating a separate class that implements the Runnable interface and providing 
                //the implementation for the run() method.
                // The lambda expression is essentially a shorthand way of defining a single-method 
                //implementation for functional interfaces.
//--------------------------------------------------------------------------------------------------------
        Runnable r2=()->{

         System.out.println("writer started");
         try
         { 
            while(!soc.isClosed())
            {  
               BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
           String content =br1.readLine();
           out.println(content);
           out.flush();
           if(content.equals("exit")){
            soc.close();        
            break;
           }
        }
    }catch(Exception e)
{//e.printStackTrace();
System.out.println("Connection is closed");}

        };
new Thread(r2).start();
    
    }

    public static void main(String[] args) {
        Server s =new Server();
    }
}
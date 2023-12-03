package message;

import java.net.*;
import java.util.ArrayList;


public class ConnectionAcceptor extends Thread{
    private ServerSocket request;
    private ArrayList<Partner> users;

    public ConnectionAcceptor(String port, ArrayList<Partner> users) throws Exception{
        if (port==null) throw new Exception ("Porta ausente");
        try{
            this.request = new ServerSocket(Integer.parseInt(port));
        }catch(Exception e){
            throw new Exception("Porta Invalida");
        }

        if(users == null) throw new Exception("Usuarios ausentes");

        this.users = users;
    }

    public void run ()
    {
        for(;;){
            Socket connection=null;
            try{
                connection = this.request.accept();
            }catch (Exception e){
                continue;
            }

            ConnectionSupervisor connectionSupervisor=null;
            try{
                connectionSupervisor = new ConnectionSupervisor(connection, users);
            }catch(Exception error){}
            connectionSupervisor.start();
        }
    }


}

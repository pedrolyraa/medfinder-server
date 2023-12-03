package message;

import java.io.*;
import java.net.*;
import java.util.*;

public class ConnectionSupervisor extends Thread{
    private double remainingDays=0;
    private Partner user;
    private Socket connection;
    private ArrayList<Partner> users;

    public ConnectionSupervisor(Socket connection, ArrayList<Partner> users) throws Exception{
        if (connection==null) throw new Exception ("Conexao ausente");
        if (users==null) throw new Exception ("Usuarios ausentes");

        this.connection  = connection;
        this.users = users;
    }

    public void run (){

        ObjectOutputStream transmissor = null;

        try{
            transmissor = new ObjectOutputStream(this.connection.getOutputStream());
        }catch(Exception e){
            return;
        }

        ObjectInputStream receptor=null;
        try{
            receptor = new ObjectInputStream(this.connection.getInputStream());
        }catch(Exception e){
            try{
                transmissor.close();
            }catch(Exception error){} // so tentando fechar antes de acabar a thread

            return;
        }

        try{
            this.user = new Partner(this.connection, receptor, transmissor);
        }catch(Exception error){} // sei que passei os parametros corretos

        try{
            synchronized (this.users){
                this.users.add(this.user);
            }

            for(;;){
                Message message = null;
                try{
                    message = this.user.receive();
                }catch(Exception e){}


                if(message==null) return;
                else if (message instanceof RemainingRequest){
                    RemainingRequest remainingRequest = (RemainingRequest) message;
                    Integer dailyUse = remainingRequest.getDailyUse();
                    Integer totalQuantity = remainingRequest.getTotalQuantity();


                    this.remainingDays = (double) totalQuantity /dailyUse;

                }
                else if (message instanceof RemainingResponse){
                    this.user.send(new Result(this.remainingDays));
                }
                else if (message instanceof RequestToLeave){
                    synchronized(this.users){
                        this.users.remove(this.user);
                    }
                    this.user.goodBye();
                }
            }
        }catch (Exception erro){
            try{
                transmissor.close ();
                receptor.close ();
            }
            catch (Exception falha) {} // so tentando fechar antes de acabar a thread
            return;
        }
    }
}
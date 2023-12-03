package message;
import java.io.*;
import java.net.*;
import java.util.concurrent.Semaphore;

public class Partner {
    private Socket connection;
    private ObjectInputStream receptor;
    private ObjectOutputStream transmissor;

    private Message nextMessage=null;

    private Semaphore mutEx = new Semaphore(1, true);

    public Partner(Socket connection, ObjectInputStream receptor, ObjectOutputStream transmissor) throws Exception{
        if(connection==null) throw new Exception("Conexao ausente");
        if(receptor==null) throw new Exception ("Receptor ausente");
        if(transmissor==null) throw new Exception ("Transmissor ausente");

        this.connection = connection;
        this.receptor = receptor;
        this.transmissor = transmissor;
    }

    public void send (Message x) throws Exception{
        try{
            this.transmissor.writeObject(x);
            this.transmissor.flush();
        }catch(Exception e){
            throw new Exception("Erro de transmissao");
        }
    }

    public Message peek() throws Exception{
        try{
            this.mutEx.acquireUninterruptibly();
            if(this.nextMessage==null) this.nextMessage = (Message)this.receptor.readObject();
            this.mutEx.release();
            return this.nextMessage;
        }catch(Exception e){
            throw new Exception("Erro de recepcao");
        }
    }

    public Message receive() throws Exception{
        try{
            if(this.nextMessage==null) this.nextMessage = (Message)this.receptor.readObject();
            Message ret = this.nextMessage;
            this.nextMessage=null;
            return ret;
        }catch(Exception e){
            throw new Exception("Erro de recepcao");
        }
    }

    public void goodBye() throws Exception{
        try{
            this.transmissor.close();
            this.receptor.close();
            this.connection.close();
        }catch(Exception e){
            throw new Exception("Erro de desconexao");
        }
    }
}
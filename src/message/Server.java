package message;

import java.util.ArrayList;

public class Server {

    public static String DEFAULT_PORT = "3000";
    public static void main(String[] args){
        if (args.length>1){
            System.err.println ("Uso esperado: java Servidor [PORTA]\n");
            return;
        }

        String port=Server.DEFAULT_PORT;

        if (args.length==1)
            port = args[0];

        ArrayList<Partner> users =
                new ArrayList<Partner> ();

        ConnectionAcceptor connectionAcceptor=null;
        try{
            connectionAcceptor = new ConnectionAcceptor(port, users);
            connectionAcceptor.start();
        } catch (Exception e) {
            System.err.println ("Escolha uma porta apropriada e liberada para uso!\n");
            return;
        }

        for(;;){
            System.out.println ("O servidor esta ativo! Para desativa-lo,");
            System.out.println ("use o comando \"desativar\"\n");
            System.out.print   ("> ");

            String command=null;
            try{
                command = Teclado.getUmString();
            }catch(Exception e){}

            if(command.toLowerCase().equals("desativar")){
                synchronized(users){
                    DisconnectionMessage disconnectionMessage = new DisconnectionMessage();
                    for (Partner user:users){
                        try{
                            user.send(disconnectionMessage);
                            user.goodBye();
                        } catch (Exception e) {}
                    }
                }
                System.out.println ("O servidor foi desativado!\n");
                System.exit(0);
            }
            else
                System.err.println ("Comando invalido!\n");
        }
    }
}
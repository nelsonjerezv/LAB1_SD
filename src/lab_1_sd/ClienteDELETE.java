
package lab_1_sd;

import java.io.*;
import java.net.*;

public class ClienteDELETE {
    
    public static void main(String args[]) throws Exception{
        //Variables
        String fromServer;
      
        //Buffer para recibir desde consola
        System.out.println("Eliminar: ");
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        String sentence = inFromUser.readLine();
        
        // estructura improvisada
        sentence = "DELETE /consulta/" + sentence;
        String[] requests = {sentence};
        
        for (String request : requests) {
            //Socket para el cliente (host, puerto)
            Socket clientSocket = new Socket("localhost", 1234);

            //Buffer para enviar el dato al server
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            //Buffer para recibir dato del servidor
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //Leemos del cliente y lo mandamos al servidor
            outToServer.writeBytes(request + '\n');

            //Recibimos del servidor
            fromServer = inFromServer.readLine();
            System.out.println("Server response: " + fromServer);

            //Cerramos el socket
            clientSocket.close();
        }
    }    
}

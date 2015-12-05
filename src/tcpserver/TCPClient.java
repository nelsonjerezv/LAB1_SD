/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

import java.io.*;
import java.net.*;

/**
 *
 * @author luis
 */
public class TCPClient {
    
    public static void main(String args[]) throws Exception{
        //Variables
        String fromServer;
        
        /*String[] requests = {
            "GET /querytype1/hola", // <p>hola mundo</>
            "GET /querytype1",
            "GET /querytype1/1234",
            "GET /querytype1/55556",
            "ABC /querytype1/1234",
            "POST /users username=gbenussi&password=contrasena", // adasdas
            "POST /querytype1/hola body=<p>hola mundo</>",
            "POST /querytype1/hola body=<asdasdasdasdasd",
            "PUT /querytype1/hola title=hola+mundo", // aasdsadasdsa
            "PUT /querytype1/1234 username=giovanni", // aasdsadasdsa
            "DELETE /querytype1/1234",};*/
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        String sentence = inFromUser.readLine();
        sentence = "PUT /consulta/" + sentence;
        String[] requests = {sentence};
        
        for (String request : requests) {
            
                //Buffer para recibir desde el usuario
                //BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                
                //Socket para el cliente (host, puerto)
                Socket clientSocket = new Socket("localhost", 1234);
                
                //Buffer para enviar el dato al server
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                
                //Buffer para recibir dato del servidor
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                
                //Leemos del cliente y lo mandamos al servidor
                //sentence = inFromUser.readLine();
                outToServer.writeBytes(request + '\n');
                
                //Recibimos del servidor
                fromServer = inFromServer.readLine();
                System.out.println("Server response: " + fromServer);
                
                //Cerramos el socket
                clientSocket.close();
            }
        }
    
}

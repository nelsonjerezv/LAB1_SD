/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tcpserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiThreadServer implements Runnable {
   Socket csocket;
    private String fromClient;
   MultiThreadServer(Socket csocket) {
      this.csocket = csocket;
              String fromClient;

   }

   public static void main(String args[]) 
   throws Exception {
      ServerSocket ssock = new ServerSocket(1234);
      System.out.println("Listening");
      while (true) {
         Socket sock = ssock.accept();
         System.out.println("Connected");
         new Thread(new MultiThreadServer(sock)).start();
          System.out.println(Thread.activeCount());
      }
   }
   public void run() {
       try {     
           
           BufferedReader inFromClient = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
           //Buffer para enviar al cliente
           DataOutputStream outToClient = new DataOutputStream(csocket.getOutputStream());
           
           //Recibimos el dato del cliente y lo mostramos en el server
           fromClient =inFromClient.readLine();
           System.out.println("Received: " + fromClient);
           
           //Se procesa el dato recibido
           //processedData = fromClient.toUpperCase() + '\n';
           String reverse = new StringBuffer(fromClient).reverse().toString() + '\n';
           
           
           //Se le envia al cliente
           outToClient.writeBytes(reverse);
           
           //Thread.interrupted();
           
       } catch (IOException ex) {
           Logger.getLogger(MultiThreadServer.class.getName()).log(Level.SEVERE, null, ex);
       }
       
       
       
       
   }
}

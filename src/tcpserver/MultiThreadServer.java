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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiThreadServer implements Runnable {
   Socket csocket;
    private String fromClient;
   MultiThreadServer(Socket csocket) {
      this.csocket = csocket;
              //String fromClient;

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
   @Override
   public void run() {
       try {     
           
           BufferedReader inFromClient = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
           //Buffer para enviar al cliente
           DataOutputStream outToClient = new DataOutputStream(csocket.getOutputStream());
           
           //Recibimos el dato del cliente y lo mostramos en el server
           fromClient =inFromClient.readLine();
                                    // System.out.println("Received: " + fromClient);

                                     //Se procesa el dato recibido
                                     //processedData = fromClient.toUpperCase() + '\n';
                                     //String reverse = new StringBuffer(fromClient).reverse().toString() + '\n';

                                     //Se le envia al cliente
                                     //outToClient.writeBytes(reverse);
           System.out.println("===== ===== ===== ===== =====");

            //String request = requests[i];

            //String[] tokens = request.split(" ");
            String[] tokens = fromClient.split(" ");
            String parametros = tokens[1];

            String http_method = tokens[0];

            String[] tokens_parametros = parametros.split("/");

            String resource = tokens_parametros.length > 1 ? tokens_parametros[1] : "";
            String id = tokens_parametros.length > 2 ? tokens_parametros[2] : "";

            String meta_data = tokens.length > 2 ? tokens[2] : "";

            System.out.println("Consulta: " + fromClient);
            System.out.println("HTTP METHOD: " + http_method);
            System.out.println("Resource: " + resource);
            System.out.println("ID:          " + id);
            System.out.println("META DATA:    " + meta_data);
            switch (http_method) {
                case "GET":
                    if (id == "") {
                        System.out.println("Buscando en la base de datos los ultimos 10 registros de tipo '" + resource + "'");
                        // buscar en el cache
                        // hit o miss
                    } else {
                        System.out.println("Buscando en el cache de '" + resource + "' el registro con id " + id);
                    }
                    break;
                case "POST":
                    System.out.println("Creando un usuario con los siguientes datos: (" + meta_data + ")");
                    for (String params : meta_data.split("&")) {
                        String[] parametros_meta = params.split("=");
                        System.out.println("\t* " + parametros_meta[0] + " -> " + parametros_meta[1]);
                    }
                    break;
                case "PUT":
                    System.out.println("Actualizando el usuario con id " + id + " con los siguientes datos (" + meta_data + ")");
                    for (String params : meta_data.split("&")) {
                        String[] parametros_meta = params.split("=");
                        System.out.println("\t* " + parametros_meta[0] + " -> " + parametros_meta[1]);
                    }
                    break;
                case "DELETE":
                    System.out.println("Borrando el recurso de tipo '" + resource + "' con id " + id);
                    break;
                default:
                    System.out.println("Not a valid HTTP Request");
                    break;
            }
            //Se le envia al cliente
            outToClient.writeBytes("Consulta Procesada\n");
           
       } catch (IOException ex) {
           Logger.getLogger(MultiThreadServer.class.getName()).log(Level.SEVERE, null, ex);
       }
       
       
       
       
   }
}

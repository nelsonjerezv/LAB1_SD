/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Nelson
 */
public class CacheStart {
    public static void main(String args[]) throws Exception {
        
      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      
      System.out.print("Tamaño total cache: ");
      int tamano      = Integer.parseInt( inFromUser.readLine() ); 
      
      System.out.print("Cantidad particiones: ");
      int particiones = Integer.parseInt( inFromUser.readLine() );
      
      int TamanoParticion = tamano/particiones;
      
      ServerSocket ssock = new ServerSocket(1234);
      ArrayList<LRUCache> Particiones = new ArrayList();
      
      
      for (int i = 0; i < particiones; i++) {
          LRUCache lru_cache = new LRUCache(TamanoParticion);
          Particiones.add(lru_cache);
      }
      
      
      LRUCache lru_cache = new LRUCache(tamano);
      System.out.println("Listening");
      
      while (true) {
         Socket sock = ssock.accept();
         System.out.println("Connected");
         new Thread(new MultiThreadServer(sock, lru_cache)).start();
      }
   }
}

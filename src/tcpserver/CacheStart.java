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
        int diferencia = tamano - TamanoParticion*particiones;
        int bandera = 0;
        ServerSocket ssock = new ServerSocket(1234);
        ArrayList<LRUCache> Particiones = new ArrayList();
        
        if(diferencia!=0)
            System.out.println("Division no entera, se ajusta internamente.");

        for (int i = 0; i < particiones; i++) {
            if(diferencia > bandera){
                LRUCache lru_cache = new LRUCache(TamanoParticion+1);
                Particiones.add(lru_cache);
                bandera=bandera+1;
            }else{
            LRUCache lru_cache = new LRUCache(TamanoParticion);
            Particiones.add(lru_cache);
            }
        }

        LRUCache lru_cache = new LRUCache(tamano);
        System.out.println("Listening");

        while (true) {
           Socket sock = ssock.accept();
           System.out.println("Connected");
           new Thread(new MultiThreadServer(sock, Particiones)).start();
        }
    }  
}

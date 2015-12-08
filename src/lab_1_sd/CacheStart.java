package lab_1_sd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CacheStart {
    public static void main(String args[]) throws Exception {
        
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Tamaño total cache: ");
        int tamano = Integer.parseInt( inFromUser.readLine() );
        
        System.out.print("Tamaño de la parte estática del cache: ");
        int tamEstatica = Integer.parseInt( inFromUser.readLine() ); 

        System.out.print("Cantidad particiones: ");
        int particiones = Integer.parseInt( inFromUser.readLine() );
        
        int tamP = tamano - tamEstatica;
        
        int TamanoParticion = tamP/particiones;
        int diferencia = tamP - TamanoParticion*particiones;
        int bandera = 0;
        ServerSocket ssock = new ServerSocket(1234);
        
        ArrayList<LRUCache> Particiones = new ArrayList();
        //Se agrega la parte estatica del cache
        LRUCache parteEstatica = new LRUCache(tamEstatica);
        IniciarCacheEstatico(parteEstatica, tamEstatica);
        
        if(diferencia!=0)
            System.out.println("Particiones quedan con diferentes tamaños de cache.");
        
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
        Particiones.add(parteEstatica);
        for (int i = 0; i < Particiones.size(); i++) {
            System.out.println("Cache: "+i+" tamaño: "+Particiones.get(i).size);
        }
        System.out.println("Listening");
        
        // Creamos una hebra cada vez que un cliente se conecta, 
        // a ese cliente se le entrega el socket y la lista de 
        // particiones para que pueda saber a cual acceder
        while (true) {
           Socket sock = ssock.accept();
           System.out.println("Connected");
           new Thread(new MultiThreadServer(sock, Particiones)).start();
        }
    }
    public static void IniciarCacheEstatico(LRUCache Estatico, int tamanoEstatico) {
        for (int i = 0; i < tamanoEstatico; i++) {
            String query = "query"+i;
            Estatico.addEntryToCache(query,query.toUpperCase());
        }
    }
}

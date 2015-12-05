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
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiThreadServer implements Runnable {
   
    Socket csocket;
    ArrayList<LRUCache> particiones;
    private String fromClient;

    MultiThreadServer(Socket csocket, ArrayList<LRUCache> Particiones) {
        this.csocket = csocket;  
        this.particiones = Particiones;
    }
   
    @Override
    public void run() {
        try {     

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
            //Buffer para enviar al cliente
            DataOutputStream outToClient = new DataOutputStream(csocket.getOutputStream());

            //Recibimos el dato del cliente y lo mostramos en el server
            fromClient =inFromClient.readLine();
            System.out.println("===== ===== ===== ===== =====");

            String[] tokens = fromClient.split(" ");
            String parametros = tokens[1];

            String http_method = tokens[0];

            String[] tokens_parametros = parametros.split("/");

            String resource = tokens_parametros.length > 1 ? tokens_parametros[1] : "";
            String id = tokens_parametros.length > 2 ? tokens_parametros[2] : "";

            //String meta_data = tokens.length > 2 ? tokens[2] : "";
            String meta_data = "";
            if(tokens.length > 2){
                for (int i = 2; i < tokens.length; i++) {
                    id = id + " " +tokens[i];
                }
            }

            System.out.println("Consulta: " + fromClient);
            System.out.println("HTTP METHOD: " + http_method);
            System.out.println("Resource: " + resource);
            System.out.println("ID:          " + id);
            System.out.println("META DATA:    " + meta_data);            

            //**************************************
            //    AGREGAR FUNCION HASH SOBRE
            //
            int ParticionDestino = hash(id, particiones.size());
            System.out.println("pd:"+ParticionDestino);
            //
            //**************************************

            switch (http_method) {
                case "GET":
                    if ("".equals(id)) {
                        System.out.println("Buscando en la base de datos los ultimos 10 registros de tipo '" + resource + "'");
                        // buscar en el cache nivel general para un resource                      
                    } else {
                        System.out.println("Buscando en el cache de '" + resource + "' el registro con id " + id);
                        // buscar en el cache
                        String result;
                        result = particiones.get(ParticionDestino).getEntryFromCache(id);
                        if (result == null) { // MISS
                            System.out.println("MISS :(");
                            outToClient.writeBytes("MISS\n");
                            //particiones.get(ParticionDestino).addEntryToCache(id, id.toUpperCase());
                        }else{
                            System.out.println("HIT !");
                            // enviar respuesta                            
                            outToClient.writeBytes(result+"\n");
                            //total_hits++;
                        }
                        // Mostrar el cache
                        particiones.get(ParticionDestino).print(); System.out.println("");
                    particiones.get(ParticionDestino).printAns(); System.out.println("");
                    }
                    break;
                case "POST":
                    System.out.println("Mensaje desde IndexService");
                    System.out.println("Agregamos:\n    query : " + id + "\n    answer: " + id.toUpperCase());
                    
                    syncPost(ParticionDestino, id);
                    
                    outToClient.writeBytes("Agregado.\n");
                    // Mostrar el cache
                    particiones.get(ParticionDestino).print(); System.out.println("");
                    particiones.get(ParticionDestino).printAns(); System.out.println("");
                    break;
                case "PUT":
                   
                    System.out.println("Mensaje desde IndexService");
                    System.out.println("Actualizamos:\n    query : " + id + "\n    answer: " + id.toLowerCase());
                    
                    syncPut(ParticionDestino, id);
                    
                    outToClient.writeBytes("Actualizado.\n");
                    // Mostrar el cache
                    particiones.get(ParticionDestino).print(); System.out.println("");
                    particiones.get(ParticionDestino).printAns(); System.out.println("");
                    break;
//                case "DELETE":
//                    System.out.println("Borrando el recurso de tipo '" + resource + "' con id " + id);
//                    break;
                default:
                    System.out.println("Not a valid HTTP Request");
                    break;
            }
            //Se le envia al cliente
            //outToClient.writeBytes("Consulta Procesada\n");

        } catch (IOException ex) {
            Logger.getLogger(MultiThreadServer.class.getName()).log(Level.SEVERE, null, ex);
        }




    }

    private int hash(String id, int size) {
        int hash = 13;
        for (int i = 0; i < id.length(); i++) {
            hash = hash*31 + id.charAt(i);
        }
        hash = hash*hash;
        hash = (int) Math.sqrt(hash);
        hash = hash%size;
        
        return hash;
    }

    private synchronized void syncPost(int ParticionDestino, String id) {
        particiones.get(ParticionDestino).addEntryToCache(id, id.toUpperCase());
    }
    
    private synchronized void syncPut(int ParticionDestino, String id) {
        particiones.get(ParticionDestino).updateAnswerFromCache(id, id.toLowerCase());
    }
    
    
}

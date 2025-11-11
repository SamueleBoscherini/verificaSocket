package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public synchronized static void main(String[] args) throws IOException {
        System.out.println("Sto aspettando un client...");
        ListaThread lista = new ListaThread();
        
        try(ServerSocket ss = new ServerSocket(3000);){
            do {
                Socket sc = ss.accept();
                System.out.println("Client connesso");
                lista.add(new ClientThread(sc));
                lista.avvio();
            } while (true);
        }
    }
}
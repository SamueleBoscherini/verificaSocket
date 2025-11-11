package server;
import java.util.ArrayList;

public class ListaThread {
    int num = 1;
    ArrayList<ClientThread> lista;

    public ListaThread(){
        lista = new ArrayList<>();
    }

    public void add(ClientThread t){
        lista.add(t);
    }

    public void avvio(){
        if(lista.get(lista.size()-1) instanceof ClientThread){
            lista.get(lista.size()-1).start();
        }
    }

    public void broadcast(String nome){
        for (ClientThread t : lista) {
            t.getOut().println(nome + " sono finiti!");
        }
    }

}

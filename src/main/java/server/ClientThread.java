package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread  extends Thread {
    Socket sc;
    BufferedReader in;
    PrintWriter out;
    String message = "";
    static ArrayList<String> username = new ArrayList<>();
    static ArrayList<Integer> disponibilita;
    ListaThread lista;
    
    public ClientThread(Socket sc, ListaThread lista){
        this.sc = sc;
        this.lista = lista;
        fullTicket();
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            out = new PrintWriter(sc.getOutputStream(), true);
            out.println("WELCOME");

            do {
                message = in.readLine();
                String[] div = message.split(" ",2);

                if(div[0].equals("LOGIN")){
                    if(!exist(div[1])){
                        setName(div[1]);
                        out.println("OK");
                        break;
                    } else {
                        out.println("ERR USERINUSE");
                    }
                } else {
                    out.println("ERR LOGINREQUIRED");
                }
            } while (true);

            do {
                message = in.readLine();
                switch (message) {
                    case "BUY":
                    message = in.readLine();
                    String[] div = message.split(" ", 2);
                    out.println(controlloTickets(div));
                        break;

                    case "N":
                        out.println(showTickets());
                        break;
                    case "QUIT":
                        out.println("BYE"); 
                        removeUsername(getName());
                        break;
                
                    default:
                        out.println("ERR UNKNOWNCMD");
                        break;
                }
            } while (!message.equals("QUIT"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean exist(String s){
        if(!username.isEmpty()){
            for (String string : username) {
                if(string.equals(s)){
                    return true;
                }
            }
        };


        username.add(s);
        return false;

    }

    public void fullTicket(){
        disponibilita = new ArrayList<>();
        disponibilita.add(10);
        disponibilita.add(30); 
        disponibilita.add(60); 
    }

    public String showTickets(){
        return "AVAIL Gold:" + disponibilita.get(0) + 
        " Pit:"+disponibilita.get(1) +
        " Parterre:" + disponibilita.get(2);
 
    }

    public String controlloTickets(String[] div){
        if(div[0].equals(null) || div[1].equals(null))
            return "ERR SYNTAX";
        
        try{
            int numb = Integer.parseInt(div[1]);
            
            switch (div[0]) {
                case "Gold":
                    if(disponibilita.get(0)-numb < 0)
                        return "ko";
                    if(disponibilita.get(0)-numb == 0)
                        lista.broadcast("Gold");
                    disponibilita.set(0, disponibilita.get(0)-numb);
                    break;
                case "Pit":
                    if(disponibilita.get(1)-numb < 0) 
                        return "ko";
                    if(disponibilita.get(0)-numb == 0)
                        lista.broadcast("Pit");
                    disponibilita.set(1, disponibilita.get(1)-numb);
                    break;
                case "Parterre":
                    if(disponibilita.get(2)-numb < 0) 
                        return "ko";
                    if(disponibilita.get(0)-numb == 0)
                        lista.broadcast("Parterre");
                    disponibilita.set(2, disponibilita.get(2)-numb);
                    break;

                default:
                return "ERR UNKNOWNTYPE";
            }
        } catch (NumberFormatException  e) {
            return "ERR SYNTAX";
        }


        return "OK";

    }

    public void removeUsername(String s){
        username.remove(s);
    }

    public PrintWriter getOut(){
        return out;
    }

}

package sample;

import javafx.scene.paint.Color;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class Sender extends Thread{


    private SimpleEmail txt;
    private WriteController writer;


    public Sender(SimpleEmail txt,WriteController writer){
        this.writer=writer;
        this.txt=txt;

    }

    @Override
    public void run(){
        ObjectOutputStream oo=null;
        ObjectInputStream oi=null;
        BufferedReader br=null;
     try {
        System.out.println("Apertura connessione…");
        Socket s1 = new Socket ("127.0.0.1", 7777);


        oo = new ObjectOutputStream(s1.getOutputStream());
        oi = new ObjectInputStream(s1.getInputStream());

        InputStream is = s1.getInputStream();
        br = new BufferedReader( new InputStreamReader(is));

        String risposta="";


        oo.writeChars("s");
        oo.writeObject(writer.model.getUser());
        oo.writeObject(txt);

        try{
         risposta=(String)oi.readObject();
        }catch(ClassNotFoundException e){}
        System.out.println(risposta);


        if(risposta.equals("Messaggi inviati correttamente")){
            writer.clearAll();
            writer.getNotifica().setText(risposta);
            writer.getNotifica().setTextFill(Color.GREEN);
        }else{
            writer.getNotifica().setText(risposta);
            writer.getNotifica().setTextFill(Color.RED);
        }

        System.out.println("Chiusura connessione effettuata");
    } catch (ConnectException connExc) {
         writer.getNotifica().setText("Errore: il server non è attivo");
         System.err.println("Errore nella connessione ");
         writer.getNotifica().setTextFill(Color.RED);

    } catch (IOException ex) {
        ex.printStackTrace();
    }
     finally {
         try{
             if(br!=null)
                 br.close();
             if(oo!=null)
              oo.close();
             if(oi!=null)
               oi.close();
         }catch (IOException e){System.out.println(e);}
     }
    }
}

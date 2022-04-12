package sample;


import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Receiver extends Thread {


    private DataModel dt;
    private Circle stato;
    private ObjectOutputStream oo=null;
    private ObjectInputStream oi=null;
    private BufferedWriter bw=null;
    private String msg="";
    private Media m = new Media(Paths.get("src/sample/notify.wav").toUri().toString());
    private MediaPlayer player = new MediaPlayer(m);

    public Receiver(DataModel dt,Circle stato){ this.dt=dt;this.stato=stato;}


    public void notifica(){
        player.play();
        player.seek(player.getStartTime());
    }

    @Override
    public void run(){


        LettoreRecord reader=new LettoreRecord();

        while(true){
            try {

                System.out.println("Apertura connessione…");
                Socket s1 = new Socket ("127.0.0.1", 7777);

                oo = new ObjectOutputStream(s1.getOutputStream());
                oi = new ObjectInputStream(s1.getInputStream());
                bw = new BufferedWriter(new OutputStreamWriter(s1.getOutputStream()));

                oo.writeChars("r");
                oo.writeInt(dt.getNumEmail());
                oo.flush();

                bw.write(dt.getUser()+"\n");
                bw.flush();


                Platform.runLater(new Runnable() {
                    ArrayList<SimpleEmail> ris=null;

                    @Override
                    public void run() {
                        FileOutputStream fw=null;
                        ObjectOutputStream oo=null;

                        try{
                            ris=(ArrayList<SimpleEmail>) oi.readObject();

                            if(ris.size()!=0) {
                                notifica();
                                String filename = dt.getUser()+".txt";
                                synchronized (dt) {
                                    ArrayList<SimpleEmail> tmp = reader.leggi(filename);


                                fw = new FileOutputStream(filename);
                                oo= new ObjectOutputStream(fw);


                                for (int i = 0; i < ris.size(); i++) {
                                    dt.addEmail(reader.convertStoE(ris.get(i)));
                                    tmp.add(0,ris.get(i));
                                }
                                dt.setNumEmail(dt.getNumEmail()+ris.size());


                                oo.writeObject(tmp);
                                oo.flush();
                                }

                                msg ="Nuovo messaggio";
                                if(ris.size()>1)
                                    msg=ris.size()+" nuovi messaggi";



                                 Alert alert = new Alert(null, msg , ButtonType.OK);
                                 alert.setTitle("Nuovo messaggio");
                                 alert.showAndWait();


                            }
                        }catch(ClassNotFoundException e){}
                        catch(IOException e){}
                        finally{
                            try{
                                if(fw!=null)
                                     fw.close();
                                if(oo!=null)
                                 oo.close();
                            }catch(IOException e){}
                        }

                    }
                });

                stato.setFill(Color.LIGHTGREEN);
                System.out.println("Chiusura connessione effettuata");
            } catch (ConnectException connExc) {
                System.err.println("Errore nella connessione ");
                stato.setFill(Color.RED);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            try{
            Thread.sleep(5000);
            }catch(InterruptedException e){System.out.println(e);}

        }
    }


}

package sample;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ParallelServer implements Runnable {

    private Socket client=null;
    private BufferTextArea log;
    private DataModel caselle;

    public ParallelServer (Socket client,BufferTextArea log,DataModel caselle) {
        this.client = client;
        this.log=log;
        this.caselle=caselle;
    }

    public void run() {
        ObjectInputStream oi = null;
        ObjectOutputStream oo = null;
        BufferedReader br= null;
        try {

            oi =new ObjectInputStream(client.getInputStream());
            oo =new ObjectOutputStream(client.getOutputStream());
            br= new BufferedReader(new InputStreamReader(client.getInputStream()));

            SimpleEmail ris=null;

            char msg;
            String utente="--";

            msg=(char)oi.readChar();


            if(msg=='s'){
                utente= (String) oi.readObject();
                log.add("[" + CurrentData.getNow() + "] Connessione aperta con:" + utente + "\n");

                ris=(SimpleEmail) oi.readObject();

                log.add("["+CurrentData.getNow()+"] Messaggio ricevuto da "+utente+"\n");

                String wrongName="";
                String name="";
                StringTokenizer st = new StringTokenizer(ris.getDestinatario(),",; ");


                while (st.hasMoreTokens()){
                    name=st.nextToken();
                    if(!caselle.getMap().containsKey(name)){
                       ris.setDestinatario(ris.getDestinatario().replace(name,""));
                       wrongName+=name+" ";
                        log.add("["+CurrentData.getNow()+"] -- "+utente+": Impossibile inviare messaggio indirizzo non trovato: "+name+"\n");
                    }
                }

                name="";
                st = new StringTokenizer(ris.getDestinatario(),",; ");
                ris.setDestinatario(ris.getDestinatario().replace(","," "));
                ris.setDestinatario(ris.getDestinatario().replace(";"," "));

                while (st.hasMoreTokens()){
                    name=st.nextToken();
                    if(caselle.getMap().containsKey(name) && !name.equals("")){
                        ArrayList<SimpleEmail> dump=caselle.leggiEmail(name);
                        dump.add(0,ris);
                        caselle.setMail(name,caselle.getMap().get(name)+1);
                        caselle.scriviEmail(dump,name);

                        log.add("["+CurrentData.getNow()+"] ++ "+utente+": Invia messaggio a "+name+"\n");
                    }else
                        wrongName+=name+" ";

                }

                if(wrongName.equals(""))
                    oo.writeObject("Messaggi inviati correttamente");
                else
                   oo.writeObject("Destinatario inesistente: "+wrongName);

               // client.close();

            }

            else if(msg=='r'){
                int numEmail=0;
                ArrayList<SimpleEmail> tmp=null;
                utente= br.readLine();
                numEmail=oi.readInt();

                log.add("["+CurrentData.getNow()+"] Connessione aperta con:"+utente+"\n");

                tmp=caselle.getMail(utente,caselle.getMap().get(utente)-numEmail);
                oo.writeObject(tmp);
                oo.flush();
                if(tmp.size()!=0){
                    log.add("["+CurrentData.getNow()+"] ++ "+utente+" Ha ricevuto nuovi messaggi\n");
                }
                else{
                    log.add("["+CurrentData.getNow()+"] -- "+utente+" Non ha ricevuto nessun nuovo messaggio\n");
                }

            }
            else{

                utente = (String) oi.readObject();
                String id=(String) oi.readObject();
                caselle.eliminaEmail(id,utente);
                caselle.setMail(utente,caselle.getMap().get(utente)-1);
                log.add("["+CurrentData.getNow()+"] -- "+utente+" ha eliminato una mail\n");

            }
            log.add("["+CurrentData.getNow()+"] Chiusura connessione con: "+utente+"\n");

        } catch (IOException ex) {
            ex.printStackTrace();

            log.add("["+CurrentData.getNow()+"] ## Errore nel flusso dei messaggi\n");

        } catch (Exception e) {
            e.printStackTrace();
            log.add("["+CurrentData.getNow()+"] ## Errore nel flusso dei messaggi\n");
        }finally{
            try{
                if(oi!=null)
                 oi.close();
                if(oo!=null)
                  oo.close();
                if(br!=null)
                  br.close();
                client.close();
            }catch (IOException e){System.out.println(e);}
        }
    }
}
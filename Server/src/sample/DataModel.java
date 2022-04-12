package sample;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class DataModel {

    int count=0;
    private LettoreRecord l;
    private Semaphore semaphore;
    public Semaphore writer;
    private LettoreRecord reader=new LettoreRecord();

    private HashMap<String, Integer> caselle;


    public DataModel(){
        this.semaphore=new Semaphore(1);
        this.writer=new Semaphore(1);
        this.caselle=new HashMap<String, Integer>();
        this.l=new LettoreRecord();
        FileReader f=null;
        BufferedReader b=null;
       try {
           f=new FileReader("utenti.txt");
           b=new BufferedReader(f);
           String s;
           while(true) {
               s=b.readLine();
               if(s==null)
                   break;
               this.caselle.put(s,reader.leggiFile(s+".txt").size());
           }
       }catch (IOException e){System.out.println(e.getMessage());}
       finally {
           try{
               f.close();
               b.close();
           }catch (IOException e){System.out.println(e.getMessage());}
       }

    }

    public void log(String str, TextArea log){
        try{
        writer.acquire();
        log.appendText(str);
        writer.release();
        }catch (InterruptedException e){}
        count++;
    }


    public void setMail(String utente,Integer email){
        try{
        semaphore.acquire();
        }catch(InterruptedException e){System.out.println(e);}
        this.caselle.put(utente,email);
        semaphore.release();
    }


    public ArrayList<SimpleEmail> getMail(String utente,int email){
        try{
            semaphore.acquire();
        }catch(InterruptedException e){System.out.println(e);}
        ArrayList<SimpleEmail> tmp=new ArrayList<SimpleEmail>(10);
        ArrayList<SimpleEmail> el=reader.leggiFile(utente+".txt");

        int i=0;
        while(i<email){
            tmp.add(0,el.get(i));
            i++;
        }
        semaphore.release();
        return tmp;
    }



    public HashMap<String,Integer> getMap(){
        return this.caselle;
    }


    public void eliminaEmail(String id,String utente){
        try{
            semaphore.acquire();
        }catch(InterruptedException e){System.out.println(e);}
        l.elimina(id,utente+".txt");
        semaphore.release();
    }

    public void scriviEmail(ArrayList<SimpleEmail> dump,String name){
        try{
            semaphore.acquire();
        }catch(InterruptedException e){System.out.println(e);}
        l.scriviFile(dump,name+".txt");
        semaphore.release();
    }

    public ArrayList<SimpleEmail> leggiEmail(String name){
        try{
            semaphore.acquire();
        }catch(InterruptedException e){System.out.println(e);}
        ArrayList<SimpleEmail> tmp=l.leggiFile(name+".txt");
        semaphore.release();
        return tmp;
    }

}

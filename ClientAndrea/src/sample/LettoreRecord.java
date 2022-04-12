package sample;

import java.util.*;
import java.io.*;
import java.io.IOException;

public class LettoreRecord{



    public static Email convertStoE(SimpleEmail simple){
        return new Email(simple.getId(),simple.getMittente(),simple.getDestinatario(),simple.getArgomento(),simple.getTesto(),simple.getData());
    }

    public static SimpleEmail convertEtoS(Email simple){
        return new SimpleEmail(simple.getId(),simple.getMittente(),simple.getDestinatario(),simple.getArgomento(),simple.getTesto(),simple.getData());
    }


    public static ArrayList<SimpleEmail> leggi(String file) {

        ArrayList<SimpleEmail> result=new ArrayList<SimpleEmail>(1);
        FileInputStream in=null;
        ObjectInputStream s=null;

        try{
            in  = new FileInputStream(file);
            s= new ObjectInputStream(in);
            result=(ArrayList<SimpleEmail>)s.readObject();
            in.close();
            s.close();

        }
        catch(EOFException e){
            try{
                System.out.println(e);
                if(in!=null && s!=null){
                   in.close();
                     s.close();
                }
                return result;
            }catch(IOException d){}
        }
        catch(IOException e){System.out.println(e);}
        catch(ClassNotFoundException e){System.out.println(e);}

        return result;
    }


    public static void elimina(String id,String file) {
        FileInputStream in=null;
        ObjectInputStream s=null;
        ArrayList<SimpleEmail> email=null;
        System.out.println(file);
        try{
            in  = new FileInputStream(file);
            s= new ObjectInputStream(in);
            email=(ArrayList<SimpleEmail>) s.readObject();
            for(int i=0;i<email.size();i++){
                if(email.get(i).getId().equals(id))
                    email.remove(i);
            }
        }
        catch(IOException et){System.out.println(et);}
        catch(ClassNotFoundException e){System.out.println(e);}
        finally{
            try{
             in.close();
             s.close();
            }catch(IOException e){}
        }

       scrivi(email,file);
        System.out.println("Eliminato email "+id);

    }


    public static void scrivi(ArrayList<SimpleEmail> email,String file){
        FileOutputStream in=null;
        ObjectOutputStream s=null;
        try{
            in  = new FileOutputStream(file);
            s = new ObjectOutputStream(in);
            s.writeObject(email);

            s.flush();
            s.close();
            in.close();
        }catch(IOException e){System.out.println(e);}
    }
}

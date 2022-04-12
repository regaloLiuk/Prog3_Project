package sample;

import javafx.scene.control.TextArea;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class BufferTextArea extends Thread {

    private Semaphore semaphore=new Semaphore(1);

    public ArrayList<String> buffer=new ArrayList<String>(100);
    public TextArea log;

    public BufferTextArea(TextArea log){
        this.log=log;
    }

    public void add(String str){
        try{
        semaphore.acquire();
        }catch(InterruptedException e){
            System.out.println(e);
        }
        buffer.add(str);
        semaphore.release();
    }

    public boolean isEmpty(){
        return buffer.isEmpty();
    }

    @Override
    public void run(){
        while(true){
        try{
            semaphore.acquire();
            if(!buffer.isEmpty()) {
                log.appendText(buffer.get(0));
                buffer.remove(0);
            }
            semaphore.release();

            Thread.sleep(500);
        }catch (InterruptedException e){System.out.println(e);
        }
        }
    }
}

package sample;

import javafx.scene.control.TextArea;

public class RunnerServer implements Runnable {

    private TextArea log;

    public RunnerServer(TextArea log){
        this.log=log;
    }

    @Override
    public void run(){
        Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BufferTextArea buffer=new BufferTextArea(log);
                        buffer.setDaemon(true);
                        buffer.start();

                        Server ss = new Server(7777);
                        ss.runServer(buffer);

                    }
                });

        t.setDaemon(true);
        t.start();

    }




}

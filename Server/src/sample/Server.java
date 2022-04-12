package sample;

import javafx.scene.control.TextArea;

import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Server {
    private DataModel caselle;
    private ExecutorService exec;
    private int port;
    private ServerSocket server;
    private Socket client;

    public Server (int port) {
        this.exec= Executors.newFixedThreadPool(10,new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                return t;
            }
        });

        this.port = port;
        if(!startServer())
            System.err.println("Errore durate la creazione del Server");

        this.caselle=new DataModel();
    }

    private boolean startServer() {
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public void runServer(BufferTextArea log) {
        while (true) {
            try {
                client = server.accept();
                exec.execute(new ParallelServer(client,log,caselle));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


}
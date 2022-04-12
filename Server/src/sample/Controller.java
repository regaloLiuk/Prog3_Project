package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Controller {

    @FXML
    private Button start;

    @FXML
    private TextArea log;

    private Stage savedStage;

    public void start(){
        start.setVisible(false);
        log.appendText("["+CurrentData.getNow()+"] start server\n");

        Thread t=new Thread(new RunnerServer(log));
        Platform.runLater(t);

    }

    public void download() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.setInitialFileName("Log.txt");
        File savedFile = fileChooser.showSaveDialog(savedStage);

        if (savedFile != null) {

            try {
                FileWriter in = null;
                BufferedWriter s = null;
                try {
                    in = new FileWriter(savedFile);
                    s = new BufferedWriter(in);

                    s.write(log.getText());

                    s.flush();
                    in.close();
                    s.close();
                }
                catch (IOException e) {
                    System.out.println(e);
                }
            }
            catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Salvato");

        }
    }
}

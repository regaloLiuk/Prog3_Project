package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class OptionController {

    @FXML
    private MenuItem save;

    private Scene root;
    private DataModel model ;
    private Stage savedStage;

   

    public void initModel(DataModel model, Scene root) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model ;
        this.root=root;
    }

    public void download() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.setInitialFileName("Email.txt");
        File savedFile = fileChooser.showSaveDialog(savedStage);

        if (savedFile != null) {

            try {

                LettoreRecord reader = new LettoreRecord();
                ObservableList<Email> modelEmail = model.getEmailList();
                ArrayList<SimpleEmail> email = new ArrayList<SimpleEmail>(10);

                for (int i = modelEmail.size() - 1; i > -1; i--)
                    email.add(0, reader.convertEtoS(modelEmail.get(i)));

                FileWriter in = null;
                BufferedWriter s = null;
                SimpleEmail tmpEmail;
                try {
                    in = new FileWriter(savedFile);
                    s = new BufferedWriter(in);

                    s.write("-----------------------------Email--------------------------\n");
                    for (int i = 0; i < email.size(); i++) {
                        tmpEmail = email.get(i);
                        s.write("- [Mittente]: " + tmpEmail.getMittente() + "\n"
                                + "- [Argomento]: " + tmpEmail.getArgomento() + "\n"
                                + "- [Data]: " + tmpEmail.getData() + "\n"
                                + "- [Testo]: \t" + tmpEmail.getTesto().replace("\n", "\n\t\t\t") + "\n");
                        s.write("-----------------------\n");
                        s.write("-----------------------\n");
                    }

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

    public void close(){
        Platform.exit();
        System.exit(0);
    }

    public void mistSilver(){
        String ms= getClass().getResource("MistSilver.css").toExternalForm();

        root.getStylesheets().remove(getClass().getResource("MistSilver.css").toExternalForm());
        root.getStylesheets().remove(getClass().getResource("bootstrap3.css").toExternalForm());
        root.getStylesheets().remove(getClass().getResource("Flatter.css").toExternalForm());

        root.setUserAgentStylesheet(null);

        root.getStylesheets().add(ms);
    }

    public void bootstrap(){
        String b= getClass().getResource("bootstrap3.css").toExternalForm();

        root.getStylesheets().remove(getClass().getResource("MistSilver.css").toExternalForm());
        root.getStylesheets().remove(getClass().getResource("bootstrap3.css").toExternalForm());
        root.getStylesheets().remove(getClass().getResource("Flatter.css").toExternalForm());

        root.setUserAgentStylesheet(null);

        root.getStylesheets().add(b);
    }

    public void flatter(){
        String b= getClass().getResource("Flatter.css").toExternalForm();

        root.getStylesheets().remove(getClass().getResource("MistSilver.css").toExternalForm());
        root.getStylesheets().remove(getClass().getResource("bootstrap3.css").toExternalForm());
        root.getStylesheets().remove(getClass().getResource("Flatter.css").toExternalForm());

        root.setUserAgentStylesheet(null);

        root.getStylesheets().add(b);
    }

    public void normal(){
        root.getStylesheets().remove(getClass().getResource("MistSilver.css").toExternalForm());
        root.getStylesheets().remove(getClass().getResource("bootstrap3.css").toExternalForm());
        root.getStylesheets().remove(getClass().getResource("Flatter.css").toExternalForm());

        root.setUserAgentStylesheet(null);
    }
}
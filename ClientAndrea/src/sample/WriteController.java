package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.Random;
import java.util.StringTokenizer;


public class WriteController {

    @FXML
    private AnchorPane writer;

    @FXML
    private TextField argomento;

    @FXML
    private TextField destinatari;

    @FXML
    private TextArea messaggio;

    @FXML
    private Label notifica;

    DataModel model;


    public void initModel(DataModel model) {

        this.model=model;

        writer.visibleProperty().bind(model.statoWriterProperty());

        messaggio.textProperty().bindBidirectional(model.testoProperty());
        argomento.textProperty().bindBidirectional(model.argomentoProperty());
        destinatari.textProperty().bindBidirectional(model.destinatariProperty());

    }

    private void sendMsg(SimpleEmail txt,WriteController notifica){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma");
            alert.setHeaderText("Attenzione");
            alert.setContentText("Inviare questa email?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Sender s=new Sender(txt,notifica);
                Thread t=new Thread(s);
                Platform.runLater(t);
            }
    }

    public void clearAll(){
        argomento.setText("");
        destinatari.setText("");
        messaggio.setText("");
        notifica.setText("");
    }


    public void leggi() {
        model.setStato("leggi");
        if(model.getEmailList().size()!=0)
            model.setCurrentEmail(model.getPrec());
        else
            model.setCurrentEmail(null);
    }

    public Label getNotifica(){
        return this.notifica;
    }

    private static String checkWhichEmail(String dest){
        String name="";
        StringTokenizer st = new StringTokenizer(dest,",; ");
        while (st.hasMoreTokens()) {
            name=st.nextToken();
            if(!name.matches(".+@hermes.it$"))
                return name;
        }
        return "";
    }

    public static String idGenerator(){
        String id="";
        Calendar calendar=Calendar.getInstance();
        LocalDateTime currentTime = LocalDateTime.now();

        int year = currentTime.getYear();
        int month = currentTime.getMonthValue();
        int day = currentTime.getDayOfMonth();

        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        int seconds = currentTime.getSecond();
        int nanoseconds = currentTime.getNano();


        return year+""+month+""+day+""+hour+""+minute+""+seconds+""+(nanoseconds/1000000);
    }

    public void invia(){
        Random random=new Random();
        if(argomento.getText().equals("") || destinatari.getText().equals("") || messaggio.getText().equals("")){
            notifica.setTextFill(Color.ORANGE);
            notifica.setText("Attenzione: qualche campo è vuoto");
        }
        else{
            if(checkWhichEmail(destinatari.getText()).equals("")){
                String msg="Destinatari: "+destinatari.getText()+ " Argomento: "+ argomento.getText()+" Messaggio: "+messaggio.getText();
                System.out.println(msg);
                sendMsg(new SimpleEmail(idGenerator()+model.getUser(),model.getUser(),destinatari.getText(),argomento.getText(),messaggio.getText(), CurrentData.getNow() ),this);
            }else{
                notifica.setTextFill(Color.ORANGE);
                notifica.setText("Attenzione: qualche indirizzo Email sbagliato");
            }
         }

    }
}

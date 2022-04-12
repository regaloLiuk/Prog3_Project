package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Optional;


public class ContentController {


    @FXML
    private TextArea cont;

    @FXML
    private TextField dest;

    @FXML
    private TextField arg;

    @FXML
    private TextField mit;

    @FXML
    private TextField data;

    @FXML
    private BorderPane content;

    @FXML
    private Button risp;

    @FXML
    private Button del;

    @FXML
    private Button risp_all;

    @FXML
    private Button inoltra;

    @FXML
    private Label dat;

    @FXML
    private Label da;

    @FXML
    private Label a;

    @FXML
    private Label obj;



    private DataModel model;


    private String argomento="";
    private String mittenti="";
    private String destinatari="";
    private String testo="";


    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model ;
        content.visibleProperty().bind(model.statoContentProperty());

        cont.visibleProperty().bind(model.visibleContentProperty());
        dest.visibleProperty().bind(model.visibleContentProperty());
        arg.visibleProperty().bind(model.visibleContentProperty());
        mit.visibleProperty().bind(model.visibleContentProperty());
        data.visibleProperty().bind(model.visibleContentProperty());

        risp.visibleProperty().bind(model.visibleContentProperty());
        risp_all.visibleProperty().bind(model.visibleContentProperty());
        del.visibleProperty().bind(model.visibleContentProperty());
        inoltra.visibleProperty().bind(model.visibleContentProperty());

        da.visibleProperty().bind(model.visibleContentProperty());
        a.visibleProperty().bind(model.visibleContentProperty());
        dat.visibleProperty().bind(model.visibleContentProperty());
        obj.visibleProperty().bind(model.visibleContentProperty());



        model.currentEmailProperty().addListener((obs, oldEmail, newEmail) -> {
            if (oldEmail != null) {
                cont.textProperty().unbindBidirectional(oldEmail.testoProperty());
                dest.textProperty().unbindBidirectional(oldEmail.destinatarioProperty());
                arg.textProperty().unbindBidirectional(oldEmail.argomentoProperty());
                data.textProperty().unbindBidirectional(oldEmail.dataProperty());
                mit.textProperty().unbindBidirectional(oldEmail.mittenteProperty());
                destinatari="";
                argomento="";
                model.setStato("leggi");

            }
            if (newEmail == null) {
                cont.setText("");
                dest.setText("");
                arg.setText("");
                mit.setText("");
                data.setText("");
                model.setStato("leggi");
                model.setVisibleContent(false);

            } else {
                cont.textProperty().bindBidirectional(newEmail.testoProperty());
                dest.textProperty().bindBidirectional(newEmail.destinatarioProperty());
                arg.textProperty().bindBidirectional(newEmail.argomentoProperty());
                data.textProperty().bindBidirectional(newEmail.dataProperty());
                mit.textProperty().bindBidirectional(newEmail.mittenteProperty());

                testo=newEmail.getTesto();
                destinatari=newEmail.getMittente();
                mittenti=newEmail.getDestinatario();
                argomento=newEmail.getArgomento();
                model.setStato("leggi");
                model.setVisibleContent(true);
                model.setPrec(newEmail);
            }
        });
    }
    private void clearAll(){
        model.destinatariProperty().set("");
        model.testoProperty().set("");
        model.argomentoProperty().set("");
    }

    public void delete() {
        synchronized (model) {
        if(model.getCurrentEmail()!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma");
            alert.setHeaderText("Attenzione");
            alert.setContentText("Sei sicuro di voler eliminare questa email?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                ObjectOutputStream oo=null;
                ObjectInputStream oi=null;
                try {
                    System.out.println("Apertura connessione…");
                    Socket s1 = new Socket ("127.0.0.1", 7777);

                    oo=new ObjectOutputStream(s1.getOutputStream());
                    oi =new ObjectInputStream(s1.getInputStream());

                    oo.writeChars("d");
                    oo.writeObject(model.getUser());
                    oo.writeObject(model.getCurrentEmail().getId());
                    oo.flush();

                    LettoreRecord reader=new LettoreRecord();
                    reader.elimina(model.getCurrentEmail().getId(),model.getUser()+".txt");
                    model.removeCurrentEmail(model.getCurrentEmail());
                    model.setNumEmail(model.getNumEmail()-1);

                }catch (ConnectException connExc) {
                    System.err.println("Errore nella connessione ");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }finally{
                    try {
                        if(oo!=null && oi!=null){
                            oo.close();
                            oi.close();
                        }
                    }catch (IOException e){}

                }

            }
        }
        }
    }

    public void rispondi() {
        clearAll();
        model.destinatariProperty().set(this.destinatari);
        model.setStato("scrivi");
    }

    public void rispondi_all() {
        clearAll();
        model.destinatariProperty().set((this.destinatari+" "+this.mittenti).replace(model.getUser(),""));
        model.setStato("scrivi");
    }


    public void inoltra() {
        clearAll();
        model.testoProperty().set(this.testo);
        model.setStato("scrivi");
    }

    public void scrivi() {
        model.setStato("scrivi");
    }

}
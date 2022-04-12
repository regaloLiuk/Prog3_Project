package sample;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class DataModel {

    private String user;
    private int numEmail;
    private Email prec=null;
    private Semaphore semaphore;

    public DataModel(String user){
        this.user=user;
        this.semaphore=new Semaphore(1);
    }

    public String getUser(){return this.user;}

    public int getNumEmail(){
        return this.numEmail;
    }

    public Email getPrec(){
        return this.prec;
    }
    public void setPrec(Email e){
        this.prec=e;
    }

    public void setNumEmail(int newValue){
        try{
            semaphore.acquire();
        }catch(InterruptedException e){}
        this.numEmail=newValue;
        semaphore.release();
    }



    private final BooleanProperty visibleContent = new SimpleBooleanProperty(false);

    public final BooleanProperty visibleContentProperty() {
        return this.visibleContent;
    }

    public final void setVisibleContent(boolean set){
        this.visibleContent.set(set);
    }

    //Properties per lo stato dell'applicazione ---------------
    private final BooleanProperty statoContent = new SimpleBooleanProperty(true);

    public final BooleanProperty statoContentProperty() {
        return this.statoContent;
    }

    private final BooleanProperty statoWriter = new SimpleBooleanProperty(false);

    public final BooleanProperty statoWriterProperty() {
        return this.statoWriter;
    }
    //--------------------------------------------------

    //Properties per il WriterController ---------------
    private final StringProperty testo = new SimpleStringProperty("");

    public final StringProperty testoProperty() {return this.testo;}

    private final StringProperty argomento = new SimpleStringProperty("");

    public final StringProperty argomentoProperty() {
        return this.argomento;
    }

    private final StringProperty destinatari = new SimpleStringProperty("");

    public final StringProperty destinatariProperty() {
        return this.destinatari;
    }

    //---------------------------------------------------

    public void setStato(String i){
        if(i.equals("scrivi")){
            setCurrentEmail(null);
        }
        this.statoContent.set(i.equals("leggi"));
        this.statoWriter.set(i.equals("scrivi"));
    }


    private final ObservableList<Email> emailList = FXCollections.observableArrayList(email ->
            new Observable[] {email.argomentoProperty(),email.dataProperty(),email.mittenteProperty()});

    private final ObjectProperty<Email> currentEmail = new SimpleObjectProperty<>(null);

    public ObjectProperty<Email> currentEmailProperty() {
        return currentEmail ;
    }

    public final Email getCurrentEmail() {
        return currentEmailProperty().get();
    }

    public final void setCurrentEmail(Email email) {
        currentEmailProperty().set(email);
    }

    public final void removeCurrentEmail(Email email) {
        emailList.remove(email);
    }

    public ObservableList<Email> getEmailList() {
        return emailList ;
    }

    public void addEmail(Email email){
        this.getEmailList().add(0,email);
    }

    public void loadData(String file) {

        LettoreRecord reader=new LettoreRecord();

        ArrayList<SimpleEmail> ris=reader.leggi(file);
       for(int i=0;i<ris.size();i++)
            emailList.add(LettoreRecord.convertStoE(ris.get(i)));
       numEmail=ris.size();
    }
}

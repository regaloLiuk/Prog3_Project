package sample;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;



public class Email{
    private String id;

    public String getId(){
        return this.id;
    }

    //          MITTENTE
    private final StringProperty mittente = new SimpleStringProperty();

    public final StringProperty mittenteProperty() {
        return this.mittente;
    }

    public final String getMittente() {
        return this.mittenteProperty().get();
    }

    public final void setMittente(final String mittente) {
        this.mittenteProperty().set(mittente);
    }




    //          DESTINATARIO
    private final StringProperty destinatario = new SimpleStringProperty();

    public final StringProperty destinatarioProperty() {
        return this.destinatario;
    }

    public final String getDestinatario() {
        return this.destinatarioProperty().get();
    }

    public final void setDestinatario(final String destinatario) {
        this.destinatarioProperty().set(destinatario);
    }




    //          ARGOMENTO
    private final StringProperty argomento = new SimpleStringProperty();

    public final StringProperty argomentoProperty() {
        return this.argomento;
    }

    public final String getArgomento() {
        return this.argomentoProperty().get();
    }

    public final void setArgomento(final String argomento) {
        this.argomentoProperty().set(argomento);
    }



    //          TESTO
    private final StringProperty testo = new SimpleStringProperty();

    public final StringProperty testoProperty() {
        return this.testo;
    }

    public final String getTesto() {
        return this.testoProperty().get();
    }

    public final void setTesto(final String testo) {
        this.testoProperty().set(testo);
    }


    //          DATA
    private final StringProperty data = new SimpleStringProperty();

    public final StringProperty dataProperty() {
        return this.data;
    }

    public final String getData() {
        return this.dataProperty().get();
    }

    public final void setData(final String data) {
        this.dataProperty().set(data);
    }



    public Email(String id, String mittente,String destinatario,String argomento,String testo,String data) {
        super();
        this.id=id;
        setMittente(mittente);
        setDestinatario(destinatario);
        setArgomento(argomento);
        setTesto(testo);
        setData(data);
    }

    @Override
    public String toString(){
        return id+" "+argomento.getValue()+" "+data.getValue()+" "+destinatario.getValue()+" "+mittente.getValue()+" "+testo.getValue();
    }
}

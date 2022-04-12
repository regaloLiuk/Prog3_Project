package sample;

import java.io.Serializable;

public class SimpleEmail implements Serializable {
    private String id;
    private String mittente;
    private String destinatario;
    private String argomento;
    private String testo;
    private String data;

    public SimpleEmail(String id, String mittente,String destinatario,String argomento,String testo,String data) {
        this.id=id;
        this.mittente=mittente;
        this.destinatario=destinatario;
        this.argomento=argomento;
        this.testo=testo;
        this.data=data;
    }

    public void setDestinatario(String newDest){
        this.destinatario=newDest;
    }

    public String getId(){
        return this.id;
    }

    public String getMittente(){
        return this.mittente;
    }

    public String getDestinatario(){
        return this.destinatario;
    }

    public String getArgomento(){
        return this.argomento;
    }

    public String getTesto(){
        return this.testo;
    }

    public String getData(){
        return this.data;
    }

    @Override
    public String toString(){
        return id+" "+argomento+" "+data+" "+destinatario+" "+mittente+" "+testo;
    }

}

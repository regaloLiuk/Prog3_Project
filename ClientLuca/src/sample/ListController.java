package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.shape.Circle;


public class ListController {

    @FXML
    private Label user;

    @FXML
    private Circle stato;

    @FXML
    private ListView<Email> listView;

    private DataModel model ;

    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }

        this.model = model ;
        user.setText(model.getUser());
        model.loadData(model.getUser()+".txt");

        listView.setItems(model.getEmailList());

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
                model.setCurrentEmail(newSelection));

        model.currentEmailProperty().addListener((obs, oldEmail, newEmail) -> {
            if (newEmail == null) {
                listView.getSelectionModel().clearSelection();
            } else {
                listView.getSelectionModel().select(newEmail);
            }
        });

        listView.setCellFactory(lv -> new ListCell<Email>() {
            @Override
            public void updateItem(Email email, boolean empty) {
                super.updateItem(email, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText("Oggetto: "+email.getArgomento() +"\nMittente: "+email.getMittente()+"\nData: "+email.getData());
                }
            }
        });


        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                Receiver ss = new Receiver(model,stato);
                ss.start();
            }
        });
        t1.setDaemon(true);
        t1.start();


        }


}
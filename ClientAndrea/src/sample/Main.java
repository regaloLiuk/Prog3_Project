package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;



public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = new BorderPane();
        StackPane stack=new StackPane();
        Scene scene = new Scene(root, 900, 600);



        primaryStage.getIcons().add(new Image(this.getClass().getResource("lettere.jpg").toString()));


        FXMLLoader listLoader = new FXMLLoader(getClass().getResource("List.fxml"));
        root.setLeft(listLoader.load());
        ListController listController = listLoader.getController();


        FXMLLoader optionLoader = new FXMLLoader(getClass().getResource("Option.fxml"));
        root.setTop(optionLoader.load());
        OptionController optionController =  optionLoader.getController();

        FXMLLoader writeLoader = new FXMLLoader(getClass().getResource("Write.fxml"));
        stack.getChildren().add(writeLoader.load());
        WriteController writeController = writeLoader.getController();

        FXMLLoader contentLoader = new FXMLLoader(getClass().getResource("Content.fxml"));
        stack.getChildren().add(contentLoader.load());
        ContentController contentController = contentLoader.getController();

        root.setCenter(stack);

        DataModel model = new DataModel("andrea@hermes.it");

        listController.initModel(model);
        optionController.initModel(model,scene);
        contentController.initModel(model);
        writeController.initModel(model);




        primaryStage.setTitle("Hermes Mail");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

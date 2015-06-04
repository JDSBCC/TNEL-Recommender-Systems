package ginterface;

import agents.ClientAgent;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Rating_menu extends Application{
	
	private Scene scene;
	private Button classify, historic, newRec;
	
	public Rating_menu(){
	}

    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Recommender System");
        init(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    public void init(final Stage primaryStage){

        FlowPane flow =new FlowPane();
        flow.setPadding(new Insets(50,0,0,0));
        flow.setAlignment(Pos.CENTER);
        Text title = new Text("Recommender System");
        title.setId("title");
        flow.getChildren().add(title);

        FlowPane flow2 =new FlowPane();
        flow2.setPadding(new Insets(50,0,0,0));
        flow2.setAlignment(Pos.CENTER);
        Text text = new Text("Trust based recommender system using ant colony for "
        		+ "trust computation\nA trust-semantic fusion-based recommendation "
        		+ "approach for e-business applications");
        text.setId("text");
        flow2.getChildren().add(text);

        FlowPane flow3 =new FlowPane();
        flow3.setPadding(new Insets(50,0,0,0));
        flow3.setAlignment(Pos.CENTER);
        classify = new Button("Classify the movies");
        classify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Classify ml = new Classify(primaryStage);
                primaryStage.setScene(ml.getScene());
            }
        });
        historic = new Button("Show movies historic");
        historic.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Historic h = new Historic(primaryStage);
                primaryStage.setScene(h.getScene());
            }
        });
        newRec = new Button("See algorithms recommendations");
        newRec.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NewClassification nc = new NewClassification(primaryStage);
                primaryStage.setScene(nc.getScene());
            }
        });
        flow3.getChildren().addAll(classify, historic, newRec);

        BorderPane root = new BorderPane();
        root.setId("scene");
        root.setTop(flow);
        root.setCenter(flow2);
        root.setBottom(flow3);

        scene = new Scene(root, 600, 600);
        scene.getStylesheets().add("css/mainmenu.css");
    }

    public Scene getScene(){
        return scene;
    }

}
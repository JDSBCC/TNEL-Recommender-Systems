package ginterface;

import java.text.DecimalFormat;

import agents.ClientAgent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import data.Item;
import recommendersystem.RecommenderSystem;

/**
 * Created by João on 02/03/2015.
 */
public class NewClassification {

    private Scene scene;
    private Stage primaryStage;

    public NewClassification(final Stage primaryStage){
    	
    	this.primaryStage=primaryStage;
    	
    	//RecommenderSystem.items;

        primaryStage.setTitle("Recommender System");

        BorderPane root = new BorderPane();
        root.setTop(header());
        root.setCenter(moviesList());
        root.setId("root");
        scene = new Scene(root, 600, 600);
        scene.getStylesheets().add("css/movieslist.css");
    }

    public Scene getScene(){
        return scene;
    }

    public HBox header(){
    	HBox hbox = new HBox();
    	hbox.setId("header");
        //hbox.setPadding(new Insets(15, 12, 15, 12));
        //hbox.setSpacing(10);
        //hbox.setStyle("-fx-background-color: #336699;");

        Label title = new Label("Recommendations");
        title.setId("title");
        
        Button back=new Button("Return");
        back.setId("back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Rating_menu rm = new Rating_menu();
                rm.init(primaryStage);
                primaryStage.setScene(rm.getScene());
            }
        });
        
        hbox.getChildren().addAll(title, back);
        return hbox;
    }
    
    public ScrollPane moviesList(){
    	
        VBox vb = new VBox();
        vb.setPadding(new Insets(15, 15, 15, 15));
        vb.setSpacing(10);
        vb.setMaxWidth(585);
        vb.setMinWidth(585);
        vb.setId("list");
        for (int i = 0; i < ClientAgent.recommendation.getRecommendedItems().size(); i++) {
        	vb.getChildren().add(getMovie(ClientAgent.recommendation.getRecommendedItems().get(i).getFirst(), ClientAgent.recommendation.getRecommendedItems().get(i).getSecond()));
        }

    	ScrollPane scroll = new ScrollPane();
    	scroll.setPrefSize(600, 520);
    	scroll.setContent(vb);
    	scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        return scroll;
    }
    
    public HBox getMovie(Item item, Double rate){
    	HBox hbox = new HBox();
    	hbox.setId("hbox");
    	hbox.setSpacing(10);
    	
    	//title, date, imdbURL
    	VBox vb = new VBox();
        vb.setSpacing(10);
        Label title = new Label(item.getTitle());
        title.setId("titleMovie");
        Label date = new Label("("+item.getVideoReleaseDate()+")");
        date.setId("date");
        Label genres = new Label(item.getGenreString());
        genres.setId("genres");
        Label imdb = new Label(item.getImdbUrl());
        vb.getChildren().addAll(title, date, genres, imdb);

        
        //ArrayList<Item>
        if(rate!=null){
        	DecimalFormat oneDigit = new DecimalFormat("#,##0.0");//format to 1 decimal place
        	double ratio = Double.parseDouble(oneDigit.format(rate));
        	if(ratio>5.0){
        		ratio=5;
        	}
		    Label tsf = new Label(ratio+"");
		    tsf.setId("tsf");
		    hbox.getChildren().add(tsf);
        }
        
        hbox.getChildren().add(vb);
        return hbox;
    }

}

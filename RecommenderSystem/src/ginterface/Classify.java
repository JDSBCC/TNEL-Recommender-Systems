package ginterface;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.ArrayList;

import data.Item;
import data.Rating;
import recommendersystem.RecommenderSystem;


public class Classify {

    private Scene scene;
    private Stage primaryStage;

    public Classify(final Stage primaryStage){
    	
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

        Label title = new Label("Classify the movies");
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
        for (int i = 0; i < RecommenderSystem.num_items;i++) {
        	vb.getChildren().add(getMovie(RecommenderSystem.items.get(i)));
        }

    	ScrollPane scroll = new ScrollPane();
    	scroll.setPrefSize(600, 520);
    	scroll.setContent(vb);
    	scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        return scroll;
    }
    
    public HBox getMovie(final Item item){
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
        
        //Rating
        int rat = RecommenderSystem.getRatingsByUserItem(RecommenderSystem.activeUser, item);//active user
        if(rat!=0){
            Label rating = new Label(rat+"");
            rating.setId("rating");
            hbox.getChildren().add(rating);
        }
    	
        hbox.getChildren().add(vb);
        
        //Classify the movie
        if(rat==0){
        	final ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(1,2,3,4,5));


        	cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
        		@Override
        		public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
        			String value = cb.getItems().get((Integer) number2).toString();
                	int rate=Integer.parseInt(value);
                	
        			RecommenderSystem.clientAgent.sendRating(RecommenderSystem.activeUser,item.getId(),rate);

        			RecommenderSystem.ratings.add(new Rating(RecommenderSystem.activeUser, item.getId(), rate, "123456"));
        		}
        	});
            
            hbox.getChildren().add(cb);
            
        }
        return hbox;
    }

}

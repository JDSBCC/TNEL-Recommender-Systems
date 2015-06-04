package ginterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import recommendersystem.RecommenderSystem;

/**
 * Created by João on 02/03/2015.
 */
public class NewClassification {

    private Scene scene;

    public NewClassification(final Stage primaryStage){
    	
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
        
        hbox.getChildren().addAll(title);
        return hbox;
    }
    
    public ScrollPane moviesList(){
    	
        VBox vb = new VBox();
        vb.setPadding(new Insets(15, 15, 15, 15));
        vb.setSpacing(10);
        vb.setMaxWidth(585);
        vb.setMinWidth(585);
        vb.setId("list");
        for (int i = 0; i < /*RecommenderSystem.items.size()*/100; i++) {
        	vb.getChildren().add(getMovie(RecommenderSystem.items.get(i)));
        }

    	ScrollPane scroll = new ScrollPane();
    	scroll.setPrefSize(600, 520);
    	scroll.setContent(vb);
    	scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        return scroll;
    }
    
    public HBox getMovie(Item item){
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
        int rat2 = RecommenderSystem.getRatingsByUserItem(4, item);//active user
        if(rat2!=0){
            Label tsf = new Label(rat2+"");
            tsf.setId("tsf");
            hbox.getChildren().add(tsf);
        }
        
        hbox.getChildren().add(vb);
        return hbox;
    }

}

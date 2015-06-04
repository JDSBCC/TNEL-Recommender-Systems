package ginterface;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import data.Item;
import recommendersystem.RecommenderSystem;


public class Historic {

    private Scene scene;
    private Stage primaryStage;

    private static int user=RecommenderSystem.activeUser;

    public Historic(final Stage primaryStage){
    	
    	//RecommenderSystem.items;

        primaryStage.setTitle("Recommender System");
        this.primaryStage=primaryStage;

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

        Label title = new Label("Movies List");
        title.setId("title");
        final ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(RecommenderSystem.users.subList(0, RecommenderSystem.num_users)));
        cb.getSelectionModel().select(user-1);
        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
            	String value = cb.getItems().get((Integer) number2).toString().replaceAll("[^0-9]", "");
            	user=Integer.parseInt(value);
				Historic h = new Historic(primaryStage);
				primaryStage.setScene(h.getScene());
            }
          });
        
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
        
        hbox.getChildren().addAll(title, cb, back);
        return hbox;
    }
    
    public ScrollPane moviesList(){
    	
        VBox vb = new VBox();
        vb.setPadding(new Insets(15, 15, 15, 15));
        vb.setSpacing(10);
        vb.setMaxWidth(585);
        vb.setMinWidth(585);
        vb.setId("list");
        for (int i = 0; i < RecommenderSystem.num_items; i++) {
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
        
        //Rating
        int rat = RecommenderSystem.getRatingsByUserItem(user, item);//active user
        if(rat!=0){
            Label rating = new Label(rat+"");
            rating.setId("rating");
            hbox.getChildren().add(rating);
        }
    	
        hbox.getChildren().add(vb);
        return hbox;
    }

}

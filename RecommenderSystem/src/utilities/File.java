package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import data.Genre;
import data.Item;
import data.Rating;
import data.User;
import recommendersystem.RecommenderSystem;

public class File {

	public void read(String name){
		try{
			InputStream in = getClass().getResourceAsStream("/db/u."+name);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String line = br.readLine();
	
		    while (line != null) {
		        String lineSet[] = line.split("\\|");
		        readToObject(name, lineSet);
		        line = br.readLine();
		    }
		    br.close();
		}catch(IOException ex){}
	}
	
	public void readToObject(String name, String lineSet[]){
		switch(name){
		case "user":
			userObject(lineSet);
			break;
		case "genre":
			genreObject(lineSet);
			break;
		case "item":
			itemObject(lineSet);
			break;
		case "data":
			dataObject(lineSet);
			break;
		}
	}
	
	public void userObject(String lineSet[]){
		
		int id = Integer.parseInt(lineSet[0]);
		int age = Integer.parseInt(lineSet[1]);
		Boolean genre = lineSet[2].equals("F");
		
		RecommenderSystem.users.add(new User(id, age, genre, lineSet[3], lineSet[4]));
	}
	
	public void genreObject(String lineSet[]){
		int id = Integer.parseInt(lineSet[1]);
		
		RecommenderSystem.genres.add(new Genre(id, lineSet[0]));
	}
	
	public void itemObject(String lineSet[]){
		int id = Integer.parseInt(lineSet[0]);
		int releaseDate=0;
		try{
			releaseDate = Integer.parseInt(lineSet[2].substring(1, lineSet[2].length()-1));
		}catch(StringIndexOutOfBoundsException ex){
			releaseDate=0;
		}

		Genre[]genre = new Genre[19];
		for(int i=5; i<lineSet.length;i++){
			if(lineSet[i].equals("1")){
				genre[i-5]=RecommenderSystem.genres.get(i-5);
			}else{
				genre[i-5]=new Genre();
			}
		}
		
		RecommenderSystem.items.add(new Item(id, lineSet[1], releaseDate, lineSet[3], lineSet[4], genre));
	}
	
	public void dataObject(String lineSet[]){
		int userId = Integer.parseInt(lineSet[0]);
		int itemId = Integer.parseInt(lineSet[1]);
		int rating = lineSet[2].equals("_")?-1:Integer.parseInt(lineSet[2]);
		
		RecommenderSystem.ratings.add(new Rating(userId, itemId, rating, lineSet[3]));
	}
}

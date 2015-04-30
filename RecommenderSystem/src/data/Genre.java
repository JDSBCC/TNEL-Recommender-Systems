package data;

public class Genre {
	private int id;
	private String genre;
	
	public Genre(int id, String genre){
		this.id=id;
		this.genre=genre;
	}
	
	public Genre(){
		this.id=0;
		this.genre="";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public String toString(){
		return id+"|"+genre;
	}
	
	
}

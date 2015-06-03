package data;

public class Item {
	private int id;
	private String title;
	private int releaseDate;
	private String videoReleaseDate;
	private String imdbUrl;
	private Genre [] genre;
	private Double[] binaryItemSemantic;
	
	public Item(int id, String title, int releaseDate, String videoReleaseDate, String imdbUrl, Genre [] genre, Double[] binaryItemSemantic){
		this.id=id;
		this.title=title;
		this.releaseDate=releaseDate;
		this.videoReleaseDate=videoReleaseDate;
		this.imdbUrl=imdbUrl;
		this.genre=genre;
		this.binaryItemSemantic= binaryItemSemantic;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(int releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getVideoReleaseDate() {
		return videoReleaseDate;
	}

	public void setVideoReleaseDate(String videoReleaseDate) {
		this.videoReleaseDate = videoReleaseDate;
	}

	public String getImdbUrl() {
		return imdbUrl;
	}

	public void setImdbUrl(String imdbUrl) {
		this.imdbUrl = imdbUrl;
	}

	public Genre[] getGenre() {
		return genre;
	}

	public void setGenre(Genre[] genre) {
		this.genre = genre;
	}
	
	
	
	public Double[] getBinaryItemSemantic() {
		return binaryItemSemantic;
	}

	public void setBinaryItemSemantic(Double[] binaryItemSemantic) {
		this.binaryItemSemantic = binaryItemSemantic;
	}

	public String toString(){
		String string=id+"|"+title+"|"+releaseDate+"|"+videoReleaseDate+"|"+imdbUrl;
		for(int i=0; i<genre.length;i++){
			if(genre[i]!=null){
				string += "|"+genre[i].getId();
			}
		}
		return string;
	}
	
	
}

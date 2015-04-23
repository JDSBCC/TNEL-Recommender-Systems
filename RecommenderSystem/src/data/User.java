package data;

public class User {
	private int id;
	private int age;
	private Boolean genre;//0-male/1-female
	private String occupation;
	private int zipcode;
	
	public User(int id, int age, Boolean genre, String occupation, int zipcode){
		this.id=id;
		this.age=age;
		this.genre=genre;
		this.occupation=occupation;
		this.zipcode=zipcode;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Boolean getGenre() {
		return genre;
	}

	public void setGenre(Boolean genre) {
		this.genre = genre;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public int getZipcode() {
		return zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}
}

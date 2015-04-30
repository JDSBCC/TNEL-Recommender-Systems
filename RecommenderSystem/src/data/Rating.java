package data;

import recommendersystem.RecommenderSystem;

public class Rating {
	
	private User user;
	private Item item;
	private int rating;//1-5//0 quando não existe recomendação
	private String timestamp;
	
	public Rating (int userId, int itemId, int rating, String timestamp){
		user=RecommenderSystem.users.get(userId-1);
		item=RecommenderSystem.items.get(itemId-1);
		this.rating=rating;
		this.timestamp=timestamp;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String toString(){
		return user.getId()+"|"+item.getId()+"|"+rating+"|"+timestamp;
	}
	
	
}

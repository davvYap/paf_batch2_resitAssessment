package ibf2022.batch2.paf.server.models;

import java.util.Date;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;

// Do not change this file
public class Comment {

	private String restaurantId;
	private String name;
	private long date = 0l;
	private String comment;
	private int rating;

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getRestaurantId() {
		return this.restaurantId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return this.comment;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getDate() {
		return this.date;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getRating() {
		return this.rating;
	}

	@Override
	public String toString() {
		return "Comment{restaurantId=%s, name=%s, date=%d, comment=%s, rating=%d"
				.formatted(restaurantId, name, date, comment, rating);
	}

	public static Comment convertFromDocument(Document d) {
		Comment c = new Comment();
		c.setName(d.getString("name"));
		c.setComment(d.getString("grade"));
		c.setRating(d.getInteger("score"));
		Date date = d.getDate("date");
		c.setDate(date.getTime());
		return c;
	}

	public JsonObjectBuilder toJsonObjectBuilder() {

		if (this.getName() == null) {
			return Json.createObjectBuilder()
					.add("date", this.getDate())
					.add("comment", this.getComment())
					.add("rating", this.getRating());
		}

		return Json.createObjectBuilder()
				.add("date", this.getDate())
				.add("name", this.getName())
				.add("comment", this.getComment())
				.add("rating", this.getRating());
	}

	public Document toDocumentInsert() {

		Document d = new Document("restaurant_id", this.getRestaurantId())
				.append("name", this.getName())
				.append("rating", this.getRating())
				.append("comment", this.getComment())
				.append("date", this.getDate());
		return d;
	}

	public Document toDocumentUpdate() {
		Date dateUpdate = new Date();

		Document d = new Document("date", dateUpdate)
				.append("grade", this.getComment())
				.append("score", this.getRating());
		return d;
	}

}

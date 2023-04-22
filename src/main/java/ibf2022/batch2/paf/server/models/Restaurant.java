package ibf2022.batch2.paf.server.models;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;

// Do not change this file
public class Restaurant {

	private String restaurantId;
	private String name;
	private String address;
	private String cuisine;
	private List<Comment> comments = new LinkedList<>();

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

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}

	public String getCuisine() {
		return this.cuisine;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
	}

	@Override
	public String toString() {
		return "Restaurant{restaurantId=%s, name=%s, address=%s, cuisine=%s, comments=%s"
				.formatted(restaurantId, name, address, cuisine, comments);
	}

	public static Restaurant convertFromDocument(Document d) {
		Restaurant r = new Restaurant();
		r.setRestaurantId(d.getString("restaurant_id"));
		r.setName(d.getString("name"));
		Address address = Address.convertFromDocument((Document) d.get("address"));
		r.setAddress("%s, %s, %s, %s".formatted(address.getBuilding(), address.getStreet(), address.getZipcode(),
				d.getString("borough")));
		r.setCuisine(d.getString("cuisine"));
		List<Document> c = d.getList("grades", Document.class);
		r.setComments(c.stream().map(doc -> Comment.convertFromDocument(doc)).toList());
		return r;
	}

	public JsonObjectBuilder toJSONRestaurant() {
		return Json.createObjectBuilder()
				.add("restaurantId", this.getRestaurantId())
				.add("name", this.getName());
	}

	public JsonObjectBuilder toJSONRestaurantID() {

		JsonArrayBuilder jsArr = Json.createArrayBuilder();

		for (Comment comment : comments) {
			jsArr.add(comment.toJsonObjectBuilder());
		}

		if (this.getName() == null) {
			return Json.createObjectBuilder()
					.add("restaurant_id", this.getRestaurantId())
					.add("cuisine", this.getCuisine())
					.add("address", this.getAddress())
					.add("comments", jsArr);
		}

		return Json.createObjectBuilder()
				.add("restaurant_id", this.getRestaurantId())
				.add("name", this.getName())
				.add("cuisine", this.getCuisine())
				.add("address", this.getAddress())
				.add("comments", jsArr);
	}
}

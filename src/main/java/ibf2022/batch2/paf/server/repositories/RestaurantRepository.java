package ibf2022.batch2.paf.server.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;
import com.mongodb.internal.operation.UpdateOperation;

import ibf2022.batch2.paf.server.models.Comment;
import ibf2022.batch2.paf.server.models.Restaurant;

@Repository
public class RestaurantRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	// TODO: Task 2
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	// db.restaurants.distinct("cuisine")
	public List<String> getCuisines() {
		Query query = new Query();

		query.with(Sort.by(Sort.Direction.ASC, "cuisine"));

		List<String> cuisinesDocs = mongoTemplate.findDistinct(query, "cuisine", "restaurants", String.class);

		// List<String> cuisines = cuisinesDocs.stream()
		// .map(d -> d.getString("cuisine"))
		// .toList();
		return cuisinesDocs;
	}

	// TODO: Task 3
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	// db.restaurants.find({cuisine : "Asian"}).sort({name : 1})
	public List<Restaurant> getRestaurantsByCuisine(String cuisine) {
		Query query = new Query(Criteria.where("cuisine").is(cuisine));
		query.with(Sort.by(Sort.Direction.ASC, "name"));
		List<Document> restaurantDocs = mongoTemplate.find(query, Document.class, "restaurants");
		List<Restaurant> restaurants = restaurantDocs.stream()
				.map(d -> Restaurant.convertFromDocument(d))
				.toList();
		return restaurants;
	}

	// TODO: Task 4
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	// db.restaurants.find({ restaurant_id: "40356442" })
	public Optional<Restaurant> getRestaurantById(String id) {
		Query query = new Query(Criteria.where("restaurant_id").is(id));
		List<Document> resDoc = mongoTemplate.find(query, Document.class, "restaurants");
		if (resDoc.isEmpty()) {
			return Optional.empty();
		}
		Restaurant res = Restaurant.convertFromDocument(resDoc.get(0));
		return Optional.of(res);
	}

	// TODO: Task 5
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below

	// db.restaurants.updateOne({ restaurant_id: "40356442" },
	// {$push: {grades: { date: new Date(Date.now()) , grade: "not bad", score: 10 }
	// } });

	// db.comments.insertOne({restaurant_id: "40356442", name: "David", rating: 9,
	// comment: "A", date: new Date(Date.now())});

	public void insertRestaurantComment(Comment comment) {
		Query query = new Query(Criteria.where("restaurant_id").is(comment.getRestaurantId()));

		// insert to new Collection => comments
		Document toInsert = comment.toDocumentInsert();
		Document newDoc = mongoTemplate.insert(toInsert, "comments");
		System.out.println("Document Object_id inserted >>> " + newDoc.getObjectId("_id"));

		// update to existing Collection => Restaurant grades Array
		Update updateOperation = new Update().push("grades", new Document(comment.toDocumentUpdate()));
		UpdateResult updateResult = mongoTemplate.updateMulti(query, updateOperation, Document.class, "restaurants");
		System.out.println("Document updated >>> " + updateResult);
	}

}

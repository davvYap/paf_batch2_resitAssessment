package ibf2022.batch2.paf.server.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.batch2.paf.server.models.Comment;
import ibf2022.batch2.paf.server.models.Restaurant;
import ibf2022.batch2.paf.server.services.RestaurantService;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

	@Autowired
	private RestaurantService restaurantService;

	// TODO: Task 2 - request handler
	@GetMapping(path = "/cuisines")
	public ResponseEntity<String> getCuisines() {
		List<String> cuisines = restaurantService.getCuisines();

		JsonArrayBuilder jsArr = Json.createArrayBuilder();

		for (String cuisine : cuisines) {
			jsArr.add(cuisine);
		}

		if (cuisines.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.contentType(MediaType.APPLICATION_JSON)
					.body(Json.createObjectBuilder()
							.add("Error", "No cuisines available")
							.build().toString());
		}
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(jsArr.build().toString());
	}

	// TODO: Task 3 - request handler
	@GetMapping(path = "/restaurants/{cuisine}")
	public ResponseEntity<String> getRestaurantByCuisine(@PathVariable String cuisine) {

		List<Restaurant> restaurants = restaurantService.getRestaurantsByCuisine(cuisine);

		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

		if (restaurants.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.contentType(MediaType.APPLICATION_JSON)
					.body(Json.createObjectBuilder()
							.add("Error", "No restaurant with %s".formatted(cuisine))
							.build().toString());
		}

		for (Restaurant restaurant : restaurants) {
			jsonArrayBuilder.add(restaurant.toJSONRestaurant());
		}

		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(jsonArrayBuilder.build().toString());
	}

	// TODO: Task 4 - request handler
	@GetMapping(path = "/restaurant/{restaurant_id}")
	public ResponseEntity<String> getRestaurantDetailsById(@PathVariable String restaurant_id) {
		Optional<Restaurant> opRes = restaurantService.getRestaurantById(restaurant_id);
		if (opRes.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.contentType(MediaType.APPLICATION_JSON)
					.body(Json.createObjectBuilder()
							.add("Error", "No restaurant with %s".formatted(restaurant_id))
							.build().toString());
		}
		Restaurant res = opRes.get();
		res.getComments().stream().forEach(c -> c.setRestaurantId(restaurant_id));
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(res.toJSONRestaurantID().build().toString());
	}

	// TODO: Task 5 - request handler
	@PostMapping(path = "/restaurant/comment", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> postRestaurantComment(@RequestBody MultiValueMap<String, String> map) {
		Date date = new Date();
		long epoch = date.getTime();
		Comment c = new Comment();
		c.setRestaurantId(map.getFirst("restaurantId"));
		c.setDate(epoch);
		c.setName(map.getFirst("name"));
		c.setRating(Integer.parseInt(map.getFirst("rating")));
		c.setComment(map.getFirst("comment"));
		restaurantService.postRestaurantComment(c);
		return ResponseEntity.status(HttpStatus.CREATED)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Json.createObjectBuilder()
						.add("Message", "Comment created")
						.build().toString());
	}
}

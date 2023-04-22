package ibf2022.batch2.paf.server.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2022.batch2.paf.server.models.Comment;
import ibf2022.batch2.paf.server.models.Restaurant;
import ibf2022.batch2.paf.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	// TODO: Task 2
	// Do not change the method's signature
	public List<String> getCuisines() {
		List<String> cuisines = restaurantRepository.getCuisines().stream()
				.map(c -> c.replaceAll("/", "_"))
				.toList();

		return cuisines;
	}

	// TODO: Task 3
	// Do not change the method's signature
	public List<Restaurant> getRestaurantsByCuisine(String cuisine) {
		String updatedCuisine = cuisine.replaceAll("_", "/");
		return restaurantRepository.getRestaurantsByCuisine(updatedCuisine);
	}

	// TODO: Task 4
	// Do not change the method's signature
	public Optional<Restaurant> getRestaurantById(String id) {
		return restaurantRepository.getRestaurantById(id);
	}

	// TODO: Task 5
	// Do not change the method's signature
	public void postRestaurantComment(Comment comment) {
		restaurantRepository.insertRestaurantComment(comment);
	}
}

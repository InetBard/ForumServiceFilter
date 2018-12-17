package telran.ashkelon2018.forum.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import telran.ashkelon2018.forum.domain.Post;

public interface ForumRepository extends MongoRepository<Post, String> {
	Iterable<Post> findByTagsIn(List<String> tags);
	
	Iterable<Post> findPostByAuthor(String author);

	@Query("{'date':{'$gte':?0, '$lte':?1}}")
	List<Post> findByPostsBetween(LocalDateTime from, LocalDateTime to);
}

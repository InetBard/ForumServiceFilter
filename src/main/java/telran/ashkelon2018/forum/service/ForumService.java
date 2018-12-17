package telran.ashkelon2018.forum.service;

import java.util.List;

import org.springframework.stereotype.Service;

import telran.ashkelon2018.forum.domain.Post;
import telran.ashkelon2018.forum.dto.DatePeriodDTO;
import telran.ashkelon2018.forum.dto.NewCommentDTO;
import telran.ashkelon2018.forum.dto.NewPostDto;
import telran.ashkelon2018.forum.dto.PostUpdateDto;
@Service
public interface ForumService {
	Post addNewPost(NewPostDto newPost);

	Post getPost(String id);

	Post removePost(String id, String token);

	Post updatePost(PostUpdateDto postUpdateDto, String token);

	boolean addLike(String id);

	Post addComment(String id, NewCommentDTO newComment);

	Iterable<Post> findPostsByTags(List<String> tags);

	Iterable<Post> findPostsByAuthor(String author);

	Iterable<Post> findPostsByDate(DatePeriodDTO dateDto);


}

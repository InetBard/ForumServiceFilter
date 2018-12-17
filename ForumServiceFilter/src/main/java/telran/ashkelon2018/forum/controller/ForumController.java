package telran.ashkelon2018.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.ashkelon2018.forum.domain.Post;
import telran.ashkelon2018.forum.dto.DatePeriodDTO;
import telran.ashkelon2018.forum.dto.NewCommentDTO;
import telran.ashkelon2018.forum.dto.NewPostDto;
import telran.ashkelon2018.forum.dto.PostUpdateDto;
import telran.ashkelon2018.forum.service.ForumService;
@RestController
@RequestMapping("/forum")
public class ForumController {
	@Autowired
	ForumService forumService;

	@PostMapping("/posts")
	public Post addNewPost(@RequestBody NewPostDto newPost) {
		return forumService.addNewPost(newPost);
	}

	@GetMapping("/post/{key}")
	public Post getPost(@PathVariable String key) {
		return forumService.getPost(key);
	}

	@DeleteMapping("post/{id}")
	public Post removePost(@PathVariable String id, @RequestHeader("Authorization") String token) {
		return forumService.removePost(id, token);
	}

	@PutMapping("/post/update")
	public Post updatePost(@RequestBody PostUpdateDto postUpdateDto, @RequestHeader("Authorization") String token) {
		return forumService.updatePost(postUpdateDto, token);
	}

	@PutMapping("/post/{id}/likes")
	public boolean addLike(@PathVariable String id) {
		return forumService.addLike(id);
	}

	@PostMapping("/post/{id}/comment")
	public Post addComment(@PathVariable String id, @RequestBody NewCommentDTO newComment) {
		return forumService.addComment(id, newComment);
	}

	@GetMapping("/posts/tags")
	public Iterable<Post> findPostsByTags(@RequestBody List<String> tags) {
		return forumService.findPostsByTags(tags);
	}

	@GetMapping("/posts/author/{author}")
	public Iterable<Post> findPostsByAuthor(@PathVariable String author) {
		return forumService.findPostsByAuthor(author);
	}
	@GetMapping("/posts/period")
	public Iterable<Post> findPostsByDate(@RequestBody DatePeriodDTO dateDto) {
		return forumService.findPostsByDate(dateDto);
	}
}

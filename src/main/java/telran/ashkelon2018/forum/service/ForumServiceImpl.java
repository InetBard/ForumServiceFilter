package telran.ashkelon2018.forum.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.ashkelon2018.forum.configuration.AccountConfiguration;
import telran.ashkelon2018.forum.configuration.AccountUserCredentials;
import telran.ashkelon2018.forum.dao.ForumRepository;
import telran.ashkelon2018.forum.dao.UserAccountRepository;
import telran.ashkelon2018.forum.domain.Comment;
import telran.ashkelon2018.forum.domain.Post;
import telran.ashkelon2018.forum.domain.UserAccount;
import telran.ashkelon2018.forum.dto.DatePeriodDTO;
import telran.ashkelon2018.forum.dto.NewCommentDTO;
import telran.ashkelon2018.forum.dto.NewPostDto;
import telran.ashkelon2018.forum.dto.PostUpdateDto;

@Service
public class ForumServiceImpl implements ForumService {
	@Autowired
	ForumRepository repository;
	@Autowired
	UserAccountRepository userRepository;
	@Autowired
	AccountConfiguration configuration;

	@Override
	public Post addNewPost(NewPostDto newPost) {
		Post post = new Post(newPost.getTitle(), newPost.getContent(), newPost.getAuthor(), newPost.getTags());
		return repository.save(post);
	}

	@Override
	public Post getPost(String key) {
//		List<Post> posts = repository.findAll().stream()
//				.filter(p -> p.getTitle().equals(key) || p.getAuthor().equals(key))
//				.collect(Collectors.toList());
//		if (!posts.isEmpty()) {
//			System.err.println("Found");
//			return posts.get(0);
//				
		return repository.findById(key).orElse(null);
	}

	@Override
	public Post removePost(String id, String token) {
		AccountUserCredentials credentials = configuration.tokenDecode(token);
		UserAccount whoDelete = userRepository.findById(credentials.getLogin()).orElse(null);
		Post post = repository.findById(id).orElse(null);
		if (post==null) {
			throw new RuntimeException("Post is empty");
		}
		if (whoDelete != null) {
			if (whoDelete.getRoles().contains("admin")||whoDelete.getRoles().contains("moderator")) {
				repository.delete(post);
				return post;
			} 
		}
		return null;		
	}

	@Override
	public Post updatePost(PostUpdateDto postUpdateDto, String token) {
		AccountUserCredentials credentials = configuration.tokenDecode(token);
		UserAccount whoDelete = userRepository.findById(credentials.getLogin()).orElse(null);
		if (!whoDelete.getLogin().equals(postUpdateDto.getAuthor())) {
			throw new RuntimeException("You can't update this post. You are not an athor");
		}
		List<Post> posts = repository.findAll().stream().filter(
				p -> p.getTitle().equals(postUpdateDto.getTitle()) || p.getAuthor().equals(postUpdateDto.getAuthor()))
				.collect(Collectors.toList());
		Post post = posts.get(0);
		if (!posts.isEmpty()) {
			post.setContent(postUpdateDto.getContent());
			post.setTitle(postUpdateDto.getTitle());
		}
		return post;
	}

	@Override
	public boolean addLike(String id) {
		Post post = repository.findById(id).orElse(null);
		if (post != null) {
			post.addLike();
			return true;
		}
		return false;
	}

	@Override
	public Post addComment(String id, NewCommentDTO newComment) {
		Comment comment = new Comment(newComment.getAuthor(), newComment.getComment());
		Post post = getPost(id);
		post.addComment(comment);
		repository.save(post);
		System.err.println("Good");
		return post;
	}

	@Override
	public Iterable<Post> findPostsByTags(List<String> tags) {
//		Iterable<Post> posts = repository.findAll().stream()
//		.filter(p -> p.getTags().containsAll(tags))
//		.collect(Collectors.toList());
//		return posts;
		return repository.findByTagsIn(tags);
	}

	@Override
	public Iterable<Post> findPostsByAuthor(String author) {
		System.err.println(author);
		return repository.findPostByAuthor(author);
	}

	@Override
	public Iterable<Post> findPostsByDate(DatePeriodDTO dateDto) {
		LocalDateTime from = dateDto.getFrom();
		LocalDateTime to = dateDto.getTo();
		return repository.findByPostsBetween(from, to);
	}

}

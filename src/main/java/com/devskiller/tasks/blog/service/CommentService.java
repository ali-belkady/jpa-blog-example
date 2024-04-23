package com.devskiller.tasks.blog.service;

import java.time.LocalDateTime;
import java.util.List;

import com.devskiller.tasks.blog.model.Comment;
import com.devskiller.tasks.blog.repository.PostRepository;
import org.springframework.stereotype.Service;

import com.devskiller.tasks.blog.model.dto.CommentDto;
import com.devskiller.tasks.blog.model.dto.NewCommentDto;

@Service
public class CommentService {

	private final PostRepository postRepository;

	public CommentService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	/**
	 * Returns a list of all comments for a blog post with passed id.
	 *
	 * @param postId id of the post
	 * @return list of comments sorted by creation date descending - most recent first
	 */
	public List<CommentDto> getCommentsForPost(Long postId) {
		return postRepository.findById(postId)
			.map(post -> post.getComments().stream()
				.map(comment -> new CommentDto(
					comment.getId(),
					comment.getContent(),
					comment.getAuthor(),
					comment.getCreationDate()
				))
				.toList()
			)
			.orElseThrow(IllegalArgumentException::new);
	}

	/**
	 * Creates a new comment
	 *
	 * @param postId        id of the post
	 * @param newCommentDto data of new comment
	 * @return id of the created comment
	 * @throws IllegalArgumentException if postId is null or there is no blog post for passed postId
	 */
	public Long addComment(Long postId, NewCommentDto newCommentDto) {
		return postRepository.findById(postId)
			.map(post -> {
				Comment comment = new Comment();
				comment.setAuthor(newCommentDto.author());
				comment.setContent(newCommentDto.content());
				comment.setCreationDate(LocalDateTime.now());
				comment.setPost(post);

				post.getComments().add(comment);
				postRepository.save(post);

				return comment.getId();
			})
			.orElseThrow(IllegalArgumentException::new);
	}
}

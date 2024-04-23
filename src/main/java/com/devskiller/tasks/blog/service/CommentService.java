package com.devskiller.tasks.blog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.devskiller.tasks.blog.model.Comment;
import com.devskiller.tasks.blog.repository.CommentRepository;
import com.devskiller.tasks.blog.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.devskiller.tasks.blog.model.dto.CommentDto;
import com.devskiller.tasks.blog.model.dto.NewCommentDto;

@Service
public class CommentService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;

	public CommentService(PostRepository postRepository, CommentRepository commentRepository) {
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
	}

	/**
	 * Returns a list of all comments for a blog post with passed id.
	 *
	 * @param postId id of the post
	 * @return list of comments sorted by creation date descending - most recent first
	 */
	@Transactional
	public List<CommentDto> getCommentsForPost(Long postId) {
		return postRepository.findById(postId)
			.map(post -> {
				List<Comment> comments = post.getComments();
				comments.sort(Comparator.comparing(Comment::getCreationDate).reversed());

				return comments.stream()
					.map(comment -> new CommentDto(
						comment.getId(),
						comment.getContent(),
						comment.getAuthor(),
						comment.getCreationDate()
					))
					.toList();
			})
			.orElse(new ArrayList<>());
	}

	/**
	 * Creates a new comment
	 *
	 * @param postId        id of the post
	 * @param newCommentDto data of new comment
	 * @return id of the created comment
	 * @throws IllegalArgumentException if postId is null or there is no blog post for passed postId
	 */
	@Transactional
	public Long addComment(Long postId, NewCommentDto newCommentDto) {
		return postRepository.findById(postId)
			.map(post -> {
				Comment comment = new Comment();
				comment.setAuthor(newCommentDto.author());
				comment.setContent(newCommentDto.content());
				comment.setCreationDate(LocalDateTime.now());
				comment.setPost(post);
				post.getComments().add(comment);

				return commentRepository.save(comment).getId();
			})
			.orElseThrow(IllegalArgumentException::new);
	}
}

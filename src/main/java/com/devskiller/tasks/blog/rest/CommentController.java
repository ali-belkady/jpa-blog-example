package com.devskiller.tasks.blog.rest;

import com.devskiller.tasks.blog.model.dto.CommentDto;
import com.devskiller.tasks.blog.model.dto.NewCommentDto;
import com.devskiller.tasks.blog.model.dto.PostDto;
import com.devskiller.tasks.blog.service.CommentService;
import com.devskiller.tasks.blog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/posts/{id}/comments")
public class CommentController {

	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<CommentDto> getComments(@PathVariable Long id) {
		return commentService.getCommentsForPost(id);
	}

	@PostMapping
	public ResponseEntity<Long> createComment(@PathVariable Long id, @RequestBody NewCommentDto newCommentDto) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addComment(id, newCommentDto));
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
}

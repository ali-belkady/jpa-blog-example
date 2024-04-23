package com.devskiller.tasks.blog.model.dto;

import java.time.LocalDateTime;

public record CommentDto(Long id, String comment, String author, LocalDateTime creationDate) {
}

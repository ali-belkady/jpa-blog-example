package com.devskiller.tasks.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devskiller.tasks.blog.model.Post;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}

package com.example.board.repository;

import com.example.board.entitiy.Post;
import com.example.board.entitiy.PostCategory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByCategory(PostCategory category, Sort sort);
}

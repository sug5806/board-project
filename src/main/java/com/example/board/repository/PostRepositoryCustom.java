package com.example.board.repository;

import com.example.board.dto.post.search.SearchDTO;
import com.example.board.entity.Post;
import com.example.board.entity.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> postList(PostCategory postCategory, SearchDTO searchDTO, Pageable pageable);
}

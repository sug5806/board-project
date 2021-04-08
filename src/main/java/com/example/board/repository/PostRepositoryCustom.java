package com.example.board.repository;

import com.example.board.dto.SearchDTO;
import com.example.board.entity.Post;
import com.example.board.entity.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

//    List<PostDTO> postSearchList(SearchDTO searchDTO);

    Page<Post> postSearchListPaging(SearchDTO searchDTO, Pageable pageable);

    Page<Post> postList(PostCategory postCategory, Pageable pageable);
}

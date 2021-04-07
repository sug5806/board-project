package com.example.board.repository;

import com.example.board.dto.PostDTO;
import com.example.board.dto.SearchDTO;
import com.example.board.entity.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    List<PostDTO> postSearchList(SearchDTO searchDTO);

    Page<PostDTO> postSearchListPaging(SearchDTO searchDTO, Pageable pageable);

    List<PostDTO> postList(PostCategory postCategory);
}

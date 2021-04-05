package com.example.board.repository;

import com.example.board.dto.PostDTO;
import com.example.board.dto.SearchDTO;
import com.example.board.entity.PostCategory;

import java.util.List;

public interface PostRepositoryCustom {

    List<PostDTO> postSearchList(SearchDTO searchDTO);

    List<PostDTO> postList(PostCategory postCategory);
}

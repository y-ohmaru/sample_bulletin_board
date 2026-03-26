package com.example.bulletinboard.repository;

import com.example.bulletinboard.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDescIdDesc();
}

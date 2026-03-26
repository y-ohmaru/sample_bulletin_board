package com.example.bulletinboard.service;

import com.example.bulletinboard.entity.Post;
import com.example.bulletinboard.repository.PostRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAllByOrderByCreatedAtDescIdDesc();
    }

    @Transactional
    public Post create(String name, String content) {
        return postRepository.save(Post.of(name, content));
    }

    @Transactional
    public boolean deleteById(Long id) {
        return postRepository.findById(id)
                .map(post -> {
                    postRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}

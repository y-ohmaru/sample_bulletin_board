package com.example.bulletinboard.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.bulletinboard.entity.Post;
import com.example.bulletinboard.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void returnsPostsOrderedByCreatedAtDesc() {
        List<Post> posts = List.of(
                new Post(2L, "bob", "second", LocalDateTime.now()),
                new Post(1L, "alice", "first", LocalDateTime.now().minusMinutes(1))
        );
        when(postRepository.findAllByOrderByCreatedAtDescIdDesc()).thenReturn(posts);

        List<Post> actual = postService.findAll();

        assertThat(actual).isEqualTo(posts);
    }

    @Test
    void createsAndSavesPost() {
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Post saved = postService.create("alice", "hello");

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());
        assertThat(saved.getName()).isEqualTo("alice");
        assertThat(saved.getContent()).isEqualTo("hello");
        //assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(captor.getValue().getId()).isNull();
    }

    @Test
    void deletesExistingPost() {
        when(postRepository.findById(10L)).thenReturn(Optional.of(new Post()));

        boolean deleted = postService.deleteById(10L);

        assertThat(deleted).isTrue();
        verify(postRepository).deleteById(10L);
    }

    @Test
    void ignoresMissingPostOnDelete() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        boolean deleted = postService.deleteById(99L);

        assertThat(deleted).isFalse();
        verify(postRepository, never()).deleteById(any(Long.class));
    }
}

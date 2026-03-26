package com.example.bulletinboard.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.bulletinboard.entity.Post;
import com.example.bulletinboard.service.PostService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Test
    void showsPostListPage() throws Exception {
        when(postService.findAll()).thenReturn(List.of(
                new Post(1L, "alice", "hello", LocalDateTime.of(2026, 3, 26, 12, 0))
        ));

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/list"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("postForm"))
                .andExpect(content().string(containsString("alice")))
                .andExpect(content().string(containsString("hello")));
    }

    @Test
    void createsPostAndRedirects() throws Exception {
        mockMvc.perform(post("/posts")
                        .param("name", "alice")
                        .param("content", "hello"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        verify(postService).create("alice", "hello");
    }

    @Test
    void redisplaysFormWhenValidationFails() throws Exception {
        when(postService.findAll()).thenReturn(List.of());

        mockMvc.perform(post("/posts")
                        .param("name", "")
                        .param("content", "a".repeat(101)))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/list"))
                .andExpect(model().attributeHasFieldErrors("postForm", "name", "content"))
                .andExpect(content().string(containsString("Name is required")))
                .andExpect(content().string(containsString("Content must be 100 characters or fewer")));

        verify(postService, never()).create(eq(""), eq("a".repeat(101)));
    }

    @Test
    void deletesPostAndRedirects() throws Exception {
        mockMvc.perform(post("/posts/5/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/posts"));

        verify(postService).deleteById(5L);
    }
}

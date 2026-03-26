package com.example.bulletinboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bulletinboard.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PostApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Test
    void supportsCreateListDeleteAndValidation() throws Exception {
        postRepository.deleteAll();

        mockMvc.perform(post("/posts")
                        .param("name", "alice")
                        .param("content", "hello world"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        assertThat(postRepository.findAll()).hasSize(1);

        String listPage = mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(listPage).contains("alice");
        assertThat(listPage).contains("hello world");

        Long postId = postRepository.findAll().getFirst().getId();
        mockMvc.perform(post("/posts/{id}/delete", postId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        assertThat(postRepository.findAll()).isEmpty();

        String invalidPage = mockMvc.perform(post("/posts")
                        .param("name", "")
                        .param("content", "a".repeat(101)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(invalidPage).contains("Name is required");
        assertThat(invalidPage).contains("Content must be 100 characters or fewer");
    }
}

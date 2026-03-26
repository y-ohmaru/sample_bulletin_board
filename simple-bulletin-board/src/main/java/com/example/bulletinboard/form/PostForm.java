package com.example.bulletinboard.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostForm {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Content is required")
    @Size(max = 100, message = "Content must be 100 characters or fewer")
    private String content;

    public PostForm() {
    }

    public PostForm(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

package com.example.bulletinboard.controller;

import com.example.bulletinboard.form.PostForm;
import com.example.bulletinboard.service.PostService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String list(Model model) {
        if (!model.containsAttribute("postForm")) {
            model.addAttribute("postForm", new PostForm());
        }
        model.addAttribute("posts", postService.findAll());
        return "posts/list";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("postForm") PostForm postForm,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("posts", postService.findAll());
            return "posts/list";
        }

        postService.create(postForm.getName(), postForm.getContent());
        return "redirect:/posts";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = postService.deleteById(id);
        if (!deleted) {
            redirectAttributes.addFlashAttribute("message", "The post to delete was not found");
        }
        return "redirect:/posts";
    }
}

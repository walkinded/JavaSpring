package com.testsite.blog.controllers;

import com.sun.source.tree.OpensTree;
import com.testsite.blog.models.Post;
import com.testsite.blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public String blogMain(Model model) {
        model.addAttribute("title", "Главная страница");
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model) { return "blog-add"; }

    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
        Post post = new Post(title, anons, full_text);
        postRepository.save(post);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String blogDetails(@PathVariable(value = "id") long postId, Model model) {
        if(!postRepository.existsById(postId)) {
            return "redirect:/";
        }
        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> result = new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("post", result);
        return "blog-details";
    }

    @GetMapping("/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long postId, Model model) {
        if(!postRepository.existsById(postId)) {
            return "redirect:/";
        }
        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> result = new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("post", result);
        return "blog-edit";
    }

    @PostMapping("/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") long postId, @RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);
        return "redirect:/";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(@PathVariable(value = "id") long postId, Model model) {
        Post post = postRepository.findById(postId).orElseThrow();
        postRepository.delete(post);
        return "redirect:/";
    }

}

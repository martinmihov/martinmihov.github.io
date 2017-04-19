package martinBlog.controller;

import martinBlog.entity.Article;
import martinBlog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Created by Martin on 4/18/2017.
 */

@Controller
public class HomeController {

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String index(Model model){

        List<Article> articles = this.articleRepository.findAll();

        model.addAttribute("view","home/index");
        model.addAttribute("articles", articles);

        return "base-layout";
    }
}

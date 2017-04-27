package martinBlog.controller;

import martinBlog.entity.Article;
import martinBlog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

        List<Article> recentArticles = this.articleRepository.findAll();
        Collections.reverse(recentArticles);
        recentArticles = recentArticles.stream().limit(5).collect(Collectors.toList());

        List<Article> popularArticles = this.articleRepository.findAll();
        Collections.sort(popularArticles, new Comparator<Article>(){
           public int compare(Article a1, Article a2) {
               return a2.getPageView().compareTo(a1.getPageView());
           }});
        popularArticles = popularArticles.stream().limit(5).collect(Collectors.toList());

        model.addAttribute("view","home/index");
        model.addAttribute("articles", articles);
        model.addAttribute("recentArticles",recentArticles);
        model.addAttribute("popularArticles",popularArticles);

        return "base-layout";
    }
}

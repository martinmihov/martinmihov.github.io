package martinBlog.controller;

import martinBlog.bindingModel.ArticleBindingModel;
import martinBlog.entity.Article;
import martinBlog.entity.User;
import martinBlog.repository.ArticleRepository;
import martinBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by Martin on 4/19/2017.
 */

@Controller
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model){
        model.addAttribute("view","article/create");

        return "base-layout";
    }

    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(ArticleBindingModel articleBindingModel, @RequestParam("image")MultipartFile image){
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());


        String dbImagePath = null;

        String[] allowedContentTypes = {
                "image/png",
                "image/jpg",
                "image/jpeg",
                "image/gif"
        };

        boolean isContentTypeAllowed = Arrays.asList(allowedContentTypes)
                .contains(articleBindingModel.getImage().getContentType());

        if (isContentTypeAllowed) {

            String imagesPath = "\\src\\main\\resources\\static\\images\\";
            String imagePathRoot = System.getProperty("user.dir");
            String imageSaveDirectory = imagePathRoot + imagesPath;
            String filename = articleBindingModel.getImage().getOriginalFilename();
            String savePath = imageSaveDirectory + filename;
            File imageFile = new File(savePath);
            try {
                articleBindingModel.getImage().transferTo(imageFile);
                dbImagePath = "/images/" + filename;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Article articleEntity = new Article(
                articleBindingModel.getTitle(),
                articleBindingModel.getContent(),
                userEntity,
                dbImagePath,
                articleBindingModel.getDate()
        );

        this.articleRepository.saveAndFlush(articleEntity);

        return "redirect:/";
    }

    @GetMapping("/article/{id}")
    @PreAuthorize("isAuthenticated()")
    public String details(Model model, @PathVariable Integer id) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }

        if(!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            User entityUser = this.userRepository.findByEmail(principal.getUsername());

            model.addAttribute("user", entityUser);
        }

        Article article = this.articleRepository.findOne(id);

        model.addAttribute("article", article);
        model.addAttribute("view","article/details");

        return "base-layout";
    }

    @GetMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model) {

        if (!this.articleRepository.exists(id)){
            return "redirect:/";
        }

        Article article = this.articleRepository.findOne(id);

        if(!isUserAuthorOrAdmin(article)){
            return  "redirect:/article/" + id;
        }

        model.addAttribute("view", "article/edit");
        model.addAttribute("article", article);

        return "base-layout";
    }

    @PostMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, ArticleBindingModel articleBindingModel) {
        if(!this.articleRepository.exists(id)){
            return "redirect:/";
        }

        Article article = this.articleRepository.findOne(id);

        if(!isUserAuthorOrAdmin(article)){
            return  "redirect:/article/" + id;
        }

        article.setContent(articleBindingModel.getContent());
        article.setTitle(articleBindingModel.getTitle());

        this.articleRepository.saveAndFlush(article);

        return "redirect:/article/" + article.getId();
    }

    @GetMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model,@PathVariable Integer id) {
        if (!this.articleRepository.exists(id)){
            return "redirect:/";
        }

        Article article = this.articleRepository.findOne(id);

        if(!isUserAuthorOrAdmin(article)){
            return  "redirect:/article/" + id;
        }

        model.addAttribute("article", article);
        model.addAttribute("view","article/delete");

        return "base-layout";
    }

    @PostMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id){
        if (!this.articleRepository.exists(id)){
            return "redirect:/";
        }

        Article article = this.articleRepository.findOne(id);

        if(!isUserAuthorOrAdmin(article)){
            return  "redirect:/article/" + id;
        }

        this.articleRepository.delete(article);

        return "redirect:/";
    }

    @RequestMapping(value="/article/count/{article_id}", method=GET)
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public String count(@PathVariable Integer article_id) {

        Article article = this.articleRepository.findOne(article_id);

        article.setPageView(article.getPageView() + 1);

        this.articleRepository.saveAndFlush(article);

        return "" + article.getPageView();
    }

    private boolean isUserAuthorOrAdmin(Article article) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(article);
    }
}

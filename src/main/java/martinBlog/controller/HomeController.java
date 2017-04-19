package martinBlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Martin on 4/18/2017.
 */

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("view","home/index");

        return "base-layout";
    }
}

package io.github.lasyard.spring.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Nonnull;

@Controller
public class MainController {
    private static final String VIEW_INDEX = "index";

    private static int counter = 0;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(@Nonnull Model model) {
        model.addAttribute("counter", counter++);
        return VIEW_INDEX;
    }
}

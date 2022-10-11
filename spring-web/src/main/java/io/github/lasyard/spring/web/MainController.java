package io.github.lasyard.spring.web;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
    private static final String VIEW_INDEX = "index";

    private static int counter = 0;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(@NonNull Model model) {
        model.addAttribute("counter", counter++);
        return VIEW_INDEX;
    }
}

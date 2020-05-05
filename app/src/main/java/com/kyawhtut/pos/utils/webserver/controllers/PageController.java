package com.kyawhtut.pos.utils.webserver.controllers;

import com.yanzhenjie.andserver.annotation.Controller;
import com.yanzhenjie.andserver.annotation.GetMapping;

/**
 * Created by Zhenjie Yan on 2018/9/12.
 */
@Controller
public class PageController {

    @GetMapping(path = "/")
    public String index() {
        // Equivalent to [return "/index"].
        return "forward:/index.html";
    }
}
package com.icia.rmate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {

    // index : 처음페이지로 이동
    @GetMapping("/cat")
    public String index1(){
        return "cat/index";
    }

    @GetMapping("/cat/board1Form")
    public String board5Form() {
        return "cat/Board/board1";
    }

    @GetMapping("/cat/serviceForm")
    public String board6Form() {
        return "cat/Board/service";
    }

    @GetMapping("/cat/aboutForm")
    public String board7Form() {
        return "cat/Board/about";
    }

    @GetMapping("/cat/questionForm")
    public String board8Form() {
        return "cat/Board/question";
    }

    @GetMapping("/cat/kittenEx")
    public String index0Form() {
        return "cat/kitten";
    }

    @GetMapping("/cat/noneEx")
    public String index1Form() {
        return "cat/none";
    }

    @GetMapping("/cat/shortEx")
    public String index2Form() {
        return "cat/short";
    }

    @GetMapping("/cat/midEx")
    public String index3Form() {
        return "cat/mid";
    }

    @GetMapping("/cat/longEx")
    public String index4Form() {
        return "cat/long";
    }

    @GetMapping("/cat/sizeEx")
    public String index5Form() {
        return "cat/size";
    }

    @GetMapping("/cat/skeletonEx")
    public String index6Form() {
        return "cat/skeleton";
    }

}

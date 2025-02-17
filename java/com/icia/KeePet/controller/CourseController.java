package com.icia.rmate.controller;

import com.icia.rmate.dto.CourseDTO;
import com.icia.rmate.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @GetMapping
    public String showCoursePage() {
        return "course"; // templates/chat.html 파일을 반환
    }
    @GetMapping("/courseList/{bNum}")
    public ResponseEntity<?> getCourseListByBNum(@PathVariable int bNum) {
        try {
            System.out.println(bNum + "courselist bnum확인!");
            List<CourseDTO> courseList = courseService.getCourseListByBNum(bNum);
            System.out.println(courseList);
            return new ResponseEntity<>(courseList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}





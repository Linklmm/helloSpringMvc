package com.spring.controller;

import com.spring.modle.Course;
import com.spring.service.CourseService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private static Logger log=LoggerFactory.getLogger(CourseController.class);
    //@Autowired
    private CourseService courseService;

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @RequestMapping(value = "/view",method = RequestMethod.GET)
    public String viewCourse(@RequestParam("courseId") Integer courseId, Model model){
        log.debug("In viewCourse,courseId={}",courseId);
        Course course=courseService.getCourseById(courseId);
        model.addAttribute(course);
        return "course_overview";
    }
/**
 * 现代的方法
 *
 * */
    @RequestMapping(value = "/view2/{courseId}",method = RequestMethod.GET)
    public String viewCourse2(@PathVariable("courseId") Integer courseId, Map<String,Object> model){
        log.debug("In viewCourse2,courseId={}",courseId);
        Course course=courseService.getCourseById(courseId);
        model.put("course",course);
        return "course_overview";
    }

    /**
     *
     * 传统的方法
     * */
    @RequestMapping("view3")
    public String viewCourse3(HttpServletRequest request){
        Integer courseId=Integer.valueOf(request.getParameter("courseId"));
        log.debug("In viewCourse2,courseId={}",courseId);
        Course course=courseService.getCourseById(courseId);
        request.setAttribute("course",course);
        return "course_overview";
    }

    @RequestMapping(value = "/admin",method = RequestMethod.GET,params = "add")
    public String createCourse(){
        return "course_admin/edit";
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public String doSave(@ModelAttribute Course course){

        log.error("Info of Course:");
        //键值对的形式输出
        log.error(ReflectionToStringBuilder.toString(course));

        course.setCourseId(123);
        return "redirect:view2/"+course.getCourseId();
    }

    @RequestMapping(value = "/upload",method = RequestMethod.GET)
    public String showUploadPage(){
        return "course_admin/file";
    }
    /**
     *
     * 文件上传有spring的multipartFile接口提供
     * */
    @RequestMapping(value = "/doUpload",method = RequestMethod.POST)
    public String doUploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        if (!file.isEmpty()){
            log.debug("Process file:{}",file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File("D:\\test",System.currentTimeMillis()+file.getOriginalFilename()));

        }

        return "success";
    }

    @RequestMapping(value = "/{courseId}",method =RequestMethod.GET)
    public @ResponseBody Course getCourseInJson(@PathVariable Integer courseId){
        return courseService.getCourseById(courseId);
    }

    @RequestMapping(value = "/jsontype/{courseId}",method = RequestMethod.GET)
    public ResponseEntity<Course> getCourseInJson2(@PathVariable Integer courseId){
        Course course=courseService.getCourseById(courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }
}

package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha") // 注解声明 说明路径
public class AlphaContraller {

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello Spring Boot!";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求数据
        System.out.println(request.getMethod()); // 获取请求方式
        System.out.println(request.getServletPath()); // 获取请求路径
        Enumeration<String> enumeration = request.getHeaderNames(); // 获取所有请求行的key （key=value形式） Enumeration:迭代器
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ":" + value);
        }
        System.out.println(request.getParameter("code"));

        // 返回响应数据
        response.setContentType("text/html;charset=utf-8"); // 返回网页类型
        try(
                PrintWriter writer = response.getWriter(); // 获取输出流
                ){
            writer.write("<h1>牛客网</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 快捷操作
    // GET请求 浏览器向服务器获取数据
    // /students?current=1&limit=20
    @RequestMapping(path="/students", method = RequestMethod.GET) // 规定只有GET请求才能得到
    @ResponseBody
    public String getStudents(
            @RequestParam(name="current", required = false, defaultValue = "1") int current,
            @RequestParam(name="limit", required = false, defaultValue = "10")int limit) { // 可以不传 默认值10
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    // /student/123 参数成为路径一部分
    @RequestMapping(path="/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){
        System.out.println(id);
        return "a student";
    }

    // POST请求 浏览器向服务器提交数据
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody // 简单响应 返回字符串
    public String saveStudent(String name, String age) {
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    // 响应HTML数据
    @RequestMapping(path="/teacher", method = RequestMethod.GET)
    // 不加ResponseBody注解默认返回HTML
    public ModelAndView getTeacher() {
        ModelAndView mav = new ModelAndView();
        // add数据和form中的一致
        mav.addObject("name", "张三");
        mav.addObject("age", "30");
        mav.addObject("sex", "男");
        // 设置模板的路径和名字
        // 模板在templates下面 templates在路径中可以不写
        mav.setViewName("/demo/view");
        return mav;
    }

    @RequestMapping(path="/school", method = RequestMethod.GET)
    public String getSchool(Model model){ // model是DispatcherServlet在调用这个方法时自动创建
        model.addAttribute("name", "东南大学");
        model.addAttribute("age", "120");
        return "/demo/view";
    }

    // 响应JSON数据(异步请求 比如在注册B站账号，输入昵称时被判断昵称重复，这时页面是没有刷新的，但悄悄访问了数据库一次)
    // Java对象 -> JSON字符串 -> JS对象
    @RequestMapping(path="/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", "13");
        emp.put("salary", 80000);
        return emp;
    }

    @RequestMapping(path="/emps", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", "13");
        emp.put("salary", 80000);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "李四");
        emp.put("age", "23");
        emp.put("salary", 60000);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "王五");
        emp.put("age", "30");
        emp.put("salary", 6000);
        list.add(emp);
        return list;
    }

}

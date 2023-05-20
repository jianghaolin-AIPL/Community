package com.nowcoder.community.controller;

import com.nowcoder.community.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
public class DataController {

    @Autowired
    private DataService dataService;

    // 打开统计页面
    @RequestMapping(path = "/data", method = {RequestMethod.GET, RequestMethod.POST})
    public String getDataPage() {
        return "/site/admin/data";
    }

    // 统计网站UV
    @RequestMapping(path = "/data/uv", method = RequestMethod.POST)
    // 传参的类型不是普通的基本类型，是Date，页面上传的是日期字符串，日期字符串如何转换成Date，默认是不好转的，因为服务器也不知道你传的日期是什么格式
    // 所以要告诉服务器传的字符串的日期是什么格式
    public String getUV(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model) {
        long uv = dataService.calculateUV(start, end);
        model.addAttribute("uvResult", uv);
        // 当我们在页面输入好日期，点击统计，还是回到这个页面，会把输入的起始时间 结束时间保留显示出来，所以要把这两个参数在传回给模板
        model.addAttribute("uvStartDate", start);
        model.addAttribute("uvEndDate", end);

//        return "/site/admin/data"; // 返回一个模板，返回给DispatcherServlet,DispatcherServlet得到这个模板，让这个模板去做后续处理
        return "forward:/data"; // 转发 声明当前这个方法只能整个请求处理一半，还需要另外一个方法继续处理请求，另外一个方法也是一个和它平级的处理请求的方法，而不是模板
    }

    // 统计活跃用户
    @RequestMapping(path = "/data/dau", method = RequestMethod.POST)
    public String getDAU(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model) {
        long dau = dataService.calculateDAU(start, end);
        model.addAttribute("dauResult", dau);
        model.addAttribute("dauStartDate", start);
        model.addAttribute("dauEndDate", end);

        return "forward:/data";
    }
}

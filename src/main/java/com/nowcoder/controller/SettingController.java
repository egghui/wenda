package com.nowcoder.controller;

import com.nowcoder.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 李丹慧 on 2017/4/2.
 */
@Controller
public class SettingController {
    /*IoC演示
    WendaService wendaService = new WendaService();//传统方式
     */
    //IoC方式
    @Autowired
    WendaService wendaService;

    @RequestMapping(path = {"/setting"}, method = {RequestMethod.GET})
    @ResponseBody
    public String setting() {
        return "Setting: OK" + wendaService.getMessage(1);
    }
}

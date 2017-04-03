package com.nowcoder.controller;

import com.nowcoder.aspect.LogAspect;
import com.nowcoder.model.User;
import com.nowcoder.service.WendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

/**
 * Created by 李丹慧 on 2017/3/30.
 */
@Controller//SpringBoot通过注解的方式表明你这个对象时干嘛的
// 这句话是说"这是一个Controller" 这是一个入口
public class IndexController {//首页的Controller
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /*IoC演示
    WendaService wendaService = new WendaService();//传统方式
    */
    //IoC方式
    @Autowired
    WendaService wendaService;

    //首页的路径是什么
    //@RequestMapping("/")//url是个/
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})//写不写index都访问首页
    //写入数据POST 获取数据GET
    @ResponseBody//返回的是一个字符串而不是模板 如返回复杂对象 注释掉这句话 返回内容区templates里找
    /*public String index() {//这是一个首页
        return "Hello Nowcoder" ;//返回“...”
    }*/
    //演示IoC
    public String index(HttpSession httpSession) {
        logger.info("VISIT HOME");//AOP
        //从session里读取消息 配合下面的redirct
        return  wendaService.getMessage(2) + "Hello Nowcoder " + httpSession.getAttribute("msg");
    }
    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,//PathVariable将路径里的参数解析到变量中区
                           @PathVariable("groupId") String groupId,
                           //@RequestParam("type") int type,//请求而非路径 /profile/user/123?type=2&key=z
                           //@RequestParam("key")  String key
                          @RequestParam(value = "type", defaultValue = "1") int type,//一般用于首页默认值传输
                          @RequestParam(value = "key", defaultValue = "zz", required = false) String key) {
                          // .../profile/user/123 页面显示 Profile Page of user / 11, type: 1, key:zz
        return String.format("Profile Page of %s / %d, type: %d, key:%s",groupId, userId, type, key);//返回“...”
    }
    @RequestMapping(path = "/vm", method = {RequestMethod.GET})
    public String template(Model model) {
        model.addAttribute("value1", "vvvv1");

        //复杂变量 数据结构

        List<String> colors = Arrays.asList(new String[]{"red", "green", "blue"});
        model.addAttribute("colors", colors);

        Map<String ,String>  map = new HashMap<String, String >();
        for (int i = 0; i < 4; ++i) {
            map.put(String.valueOf(i), String.valueOf(i*i));
        }
        model.addAttribute("map", map);

        //除了加基础的变量外 还可以加自定义的对象
        model.addAttribute("user", new User("LEE"));

        return "home";
    }

    //网页请求除了请求过来的model传递参数外， 还有HTTP的request和response
    @RequestMapping(path = "/request",method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model,//model是用来传递变量的
                          HttpServletRequest request,//只要在参数里传入request,框架会
                          //自动把变量所有的数据打包在这里
                          HttpServletResponse response,
                          HttpSession httpsession,//session的例子在redirect里
                          @CookieValue("JSESSIONID") String sessionId) throws IOException {
                          //加上上面那个 会自动从cookie里做解析
        /*看一下request里有什么 request将网页请求里headers的内容包装好了
        response将返回数据里的头等包装好*/
        //网页以.../request?type=2为例
        StringBuilder sb = new StringBuilder();
        sb.append(request.getMethod()+"<br>");//<br>是回车 显示的方法是GET
        sb.append(request.getQueryString()+"<br>");//显示type=2
        sb.append(request.getPathInfo()+"<br>");//显示null
        sb.append(request.getRequestURI()+"<br>");//显示/request
        //把request里的header全部打印出来
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + ":"+ request.getHeader(name) + "<br>");
        }
        /*打印出来的数据里有一条
        user-agent:Mozilla/5.0 (Windows NT 6.1; rv:52.0) Gecko/20100101 Firefox/52.0
        就可以通过这条数据判断用户是手机还是PC 然后进行一些跳转
        * */
        //打印的头里面有cookie 单独处理cookie
        if (request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                sb.append("Cookie: " + cookie.getName() + " value " + cookie.getValue() + "<br>");
            }
        }

        //另一种遍历cookie的方法 通过JSESSIONID 参数里最后一行
        sb.append("Cookie:" + sessionId + "<br>");

        response.addHeader("nowcoderId", "hello");//在页面看不见 但是查看元素header里是有的
        response.addCookie(new Cookie("username", "nowcoder"));
        //查看元素-Cookie-响应Cookie(Response Cookies)里有 Chrome里还有个Expires（有效区）值为session 意为网页一关就没了
        /*try {
            response.sendRedirect("/");//重定向至127.0.0.1:8080/
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //response.getOutputStream().write(二进制);//输出流 写二进制代码将图片读取出来
        //上行验证码功能

        return sb.toString();
    }

    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    /*
    //302临时跳转
    public String redirect(@PathVariable("code") int code,
                            HttpSession httpSession) {
        //请求除了request和response还可以通过session处理
        httpSession.setAttribute("msg","jump form redirect");//跳转前在session里放点东西
        //修改跳转后的页面 此处是index
        return "redirect:/";//默认302的跳转
    }
    */
    //301强制跳转
    public RedirectView redirct(@PathVariable("code") int code) {
        RedirectView red = new RedirectView("/", true);
        if (code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);//301强制跳转
        }
        return red;
    }

    //抛出异常给下面的异常处理用
    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if ("admin".equals(key)) {//http://127.0.0.1:8080/admin?key=admin
            return "hello admin";
        }
        throw new IllegalArgumentException("参数不对");//http://127.0.0.1:8080/admin?key=admin2
    }

    //异常处理
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();//显示error:参数不对
    }
}

package cn.dyx.protal.frame.controller;

import cn.dyx.protal.frame.annotation.SysLog;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description：RestFul接口
 *
 * @author dyx
 * @date 2019/7/16 17:39
 */

@Controller
@EnableAutoConfiguration
public class IndexController {

    @RequestMapping("/")
    @ResponseBody
    @SysLog(description = "系统日志测试")
    String index() {
        return "xxl job executor running.";
    }

}

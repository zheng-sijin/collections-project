package com.zhengsijin.collections.controller;

import java.util.Map;
import com.zhengsijin.collections.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public Result<Map<String, String>> hello(@RequestParam String name) {
        return Result.ok(Map.of("message", "你好，" + name + "！"));
    }
}

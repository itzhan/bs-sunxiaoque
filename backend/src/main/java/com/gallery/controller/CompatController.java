package com.gallery.controller;

import com.gallery.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 兼容SoybeanAdmin框架的接口
 */
@RestController
public class CompatController {

    @GetMapping("/api/v3/system/menus/simple")
    public Result<List<?>> getMenus() {
        return Result.ok(Collections.emptyList());
    }
}

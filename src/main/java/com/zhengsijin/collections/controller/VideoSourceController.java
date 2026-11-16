package com.zhengsijin.collections.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.zhengsijin.collections.common.Result;
import com.zhengsijin.collections.video.VideoSource;
import com.zhengsijin.collections.video.VideoSourceService;

@RestController
@RequestMapping("/api/videos/sources")
public class VideoSourceController {

    private final VideoSourceService service;

    public VideoSourceController(VideoSourceService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result<VideoSource> create(@RequestBody VideoSource source) {
        return Result.created(service.create(source));
    }

    @GetMapping("/{id}/play")
    public Result<VideoSource> getPlayback(@PathVariable Long id) {
        return Result.ok(service.getPlayback(id));
    }
}

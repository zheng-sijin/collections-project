package com.zhengsijin.collections.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.zhengsijin.collections.video.BilibiliVideoUrlParser;
import com.zhengsijin.collections.video.ParsedVideoLink;

@RestController
public class VideoLinkController {

    @PostMapping("/api/videos/parse-link")
    public ParsedVideoLink parseLink(@RequestBody Map<String, String> request) {
        try {
            return BilibiliVideoUrlParser.parse(request.get("url"));
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }
}

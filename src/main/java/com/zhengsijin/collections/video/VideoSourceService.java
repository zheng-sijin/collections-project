package com.zhengsijin.collections.video;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.zhengsijin.collections.video.mapper.VideoSourceMapper;

@Service
public class VideoSourceService {

    private final VideoSourceMapper mapper;

    public VideoSourceService(VideoSourceMapper mapper) {
        this.mapper = mapper;
    }

    public VideoSource create(VideoSource source) {
        if (source == null || blankOrTooLong(source.getTitle(), 200)
                || source.getEpisodeNumber() == null || source.getEpisodeNumber() < 1
                || blankOrTooLong(source.getLineName(), 100)
                || blankOrTooLong(source.getPlayUrl(), 2048)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "标题、正整数集数、线路名和播放地址均为必填项");
        }
        String type = source.getPlayType();
        if (!"MP4".equals(type) && !"HLS".equals(type)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "playType 只能是 MP4 或 HLS");
        }
        try {
            //把播放地址转成uri对象
            URI uri = URI.create(source.getPlayUrl());
            if (!("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()))
                    || uri.getHost() == null) {
                throw new IllegalArgumentException("无效的播放地址");
            }
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "playUrl 必须是有效的 HTTP 或 HTTPS 地址", exception);
        }
        source.setId(null);
        mapper.insert(source);
        return source;
    }

    public VideoSource getPlayback(Long id) {
        VideoSource source = id == null || id < 1 ? null : mapper.selectById(id);
        if (source == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "播放源不存在");
        }
        return source;
    }

    private boolean blankOrTooLong(String value, int maxLength) {
        return value == null || value.isBlank() || value.length() > maxLength;
    }
}

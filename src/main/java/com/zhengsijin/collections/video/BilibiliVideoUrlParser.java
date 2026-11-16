package com.zhengsijin.collections.video;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BilibiliVideoUrlParser {

    private static final Pattern BVID = Pattern.compile("BV[0-9A-Za-z]{10}");
    private static final Pattern VIDEO_PATH = Pattern.compile("/video/(BV[0-9A-Za-z]{10})/?");

    private BilibiliVideoUrlParser() {
    }

    public static ParsedVideoLink parse(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("请输入 B 站视频链接或 BV 号");
        }

        String value = input.strip();
        if (BVID.matcher(value).matches()) {
            return new ParsedVideoLink(value, 1);
        }

        URI uri;
        try {
            uri = URI.create(value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("视频链接格式不正确", exception);
        }

        if (!"https".equalsIgnoreCase(uri.getScheme())
                || !"www.bilibili.com".equalsIgnoreCase(uri.getHost())
                || uri.getUserInfo() != null
                || uri.getPort() != -1) {
            throw new IllegalArgumentException("目前只支持 B 站官网的视频链接");
        }

        String rawPath = uri.getRawPath();
        if (rawPath == null) {
            throw new IllegalArgumentException("链接中没有有效的 BV 号");
        }
        Matcher path = VIDEO_PATH.matcher(rawPath);
        if (!path.matches()) {
            throw new IllegalArgumentException("链接中没有有效的 BV 号");
        }

        return new ParsedVideoLink(path.group(1), parsePage(uri.getRawQuery()));
    }

    private static int parsePage(String query) {
        if (query == null) {
            return 1;
        }

        for (String parameter : query.split("&")) {
            if (parameter.equals("p")) {
                throw new IllegalArgumentException("分集参数 p 必须是正整数");
            }
            if (parameter.startsWith("p=")) {
                String value = parameter.substring(2);
                if (!value.matches("[1-9][0-9]*")) {
                    throw new IllegalArgumentException("分集参数 p 必须是正整数");
                }
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException exception) {
                    throw new IllegalArgumentException("分集参数 p 过大", exception);
                }
            }
        }
        return 1;
    }
}

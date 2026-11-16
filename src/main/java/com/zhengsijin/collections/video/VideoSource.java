package com.zhengsijin.collections.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("video_source")
public class VideoSource {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private Integer episodeNumber;
    private String lineName;
    private String playType;
    private String playUrl;

}

package com.jincou.wechat.utils.common;

import lombok.Data;

import java.io.Serializable;

/**
 * api请求统一响应数据类型
 */
@Data
public class CommonResponse implements Serializable {

    private Long requestId;

    private Integer stateCode;

    private String code;

    private String message;

}

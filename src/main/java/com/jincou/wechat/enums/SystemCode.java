/*
 * Copyright (C) 2016  HangZhou YuShi Technology Co.Ltd  Holdings Ltd. All rights reserved
 *
 * 本代码版权归杭州宇石科技所有，且受到相关的法律保护。
 * 没有经过版权所有者的书面同意，
 * 任何其他个人或组织均不得以任何形式将本文件或本文件的部分代码用于其他商业用途。
 *
 */
package com.jincou.wechat.enums;

import com.jincou.wechat.utils.CodeStatus;

/**
 *【错误码枚举】
 *
 * @author
 */
public enum SystemCode implements CodeStatus {

    SUCCESS("2000", "成功"),
    NEED_LOGIN("2001", "未登录"),
    ACCOUNT_ERROR("2002", "账户异常"),
    NEED_AUTH("2003", "权限不足"),
    PARAM_ERROR("2005", "参数异常"),
    SERVER_ERROR("2006", "服务器异常"),
    SIGN_EMPTY("2007", "签名为空"),
    SIGN_ERROR("2008", "签名不对"),
    APPKEY_EMPTY("2009", "AppKey为空");

    private String code;

    private String message;

    private SystemCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

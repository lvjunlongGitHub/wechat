package com.jincou.wechat.utils.common;

import com.jincou.wechat.utils.CodeStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OperationResult<T> {

    private Boolean succ = true;

    private CodeStatus errorCode;

    private String code;

    private String message;

    private T entity;

    public OperationResult(T entity) {
        this.entity = entity;
    }

    public OperationResult(String code, String message) {
        this.succ = false;
        this.code = code;
        this.message = message;
    }

    public OperationResult(CodeStatus errorCode) {
        this.succ = false;
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public OperationResult(T entity, String code, String messag) {
        this.code = code;
        this.message = messag;
        this.entity = entity;
    }


    public CodeStatus getErrorCode() {
        return errorCode;
    }
}


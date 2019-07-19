package com.jincou.wechat.enums;

/**
 * @author lvjunlong
 * @date 2019/7/19 上午9:35
 */
public enum GenderEnum {

    MALE(0, "男"),
    FEMALE(1, "女"),
    ;

    private Integer code;
    private String message;

    private GenderEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public static GenderEnum getEnum(Integer vCodeType) {
        for (GenderEnum loginTypeEnum: GenderEnum.values()) {
            if (loginTypeEnum.getCode().equals(vCodeType)) {
                return loginTypeEnum;
            }
        }
        return null;
    }

    public static GenderEnum getEnum(String message) {
        for (GenderEnum loginTypeEnum: GenderEnum.values()) {
            if (loginTypeEnum.getMessage().equals(message)) {
                return loginTypeEnum;
            }
        }
        return null;
    }
}

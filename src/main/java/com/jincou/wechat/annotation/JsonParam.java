package com.jincou.wechat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <P>
 *     用来标识一个参数，使之以JSON方式来绑定数据。
 * </P>
 * @author lvjunlong
 * @date 2019/7/19 下午3:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface JsonParam {
}

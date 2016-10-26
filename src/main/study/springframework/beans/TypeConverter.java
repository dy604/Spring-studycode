package study.springframework.beans;


import study.springframework.core.MethodParameter;

import java.lang.reflect.Field;

/**
 * Created by pc on 2016/10/13.
 */
public interface TypeConverter {

    <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException;

    <T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam) throws TypeMismatchException;

    <T> T convertIfNecessary(Object value, Class<T> requiredType, Field field) throws TypeMismatchException;
}

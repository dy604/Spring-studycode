package study.springframework.core.convert;

/**
 * Created by dy on 2016/10/11.
 */
public interface ConversionService {

    boolean canConvert(Class<?> sourceType, Class<?> targetType);

    boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType);

    <T> T convert(Object source, Class<T> targetType);

    Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);

}

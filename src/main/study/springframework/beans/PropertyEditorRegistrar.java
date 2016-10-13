package study.springframework.beans;

/**
 * Created by pc on 2016/10/13.
 */
public interface PropertyEditorRegistrar {

    void registerCustomEditors(PropertyEditorRegistrar registry);
}

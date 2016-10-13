package study.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.util.Currency;

/**
 * Created by pc on 2016/10/13.
 */
public class CurrencyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(Currency.getInstance(text));
    }

    @Override
    public String getAsText() {
        Currency value = (Currency) getValue();
        return (value != null ? value.getCurrencyCode() : "");
    }
}

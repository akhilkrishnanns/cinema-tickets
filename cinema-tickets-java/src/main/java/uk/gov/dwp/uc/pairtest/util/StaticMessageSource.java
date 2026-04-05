package uk.gov.dwp.uc.pairtest.util;

import java.util.Locale;
import java.util.ResourceBundle;


public class StaticMessageSource implements MessageSource {

    /**
     * Implementation of internationalization in error messages<br>
     * @param messageId id of the error message.
     * @param locale Locale to fetch the appropriate message file
     *
     * @return String contains exact message based on the appropriate messages_locale.properties file<br>
     * Currently this method supports default and Welsh(cy) locales.
     * If more needed, add appropriate {@code messages_{locale}.properties} file before updating any change below.

     * */
    @Override
    public String message(String messageId, Locale locale) {
        Locale localeDefault = Locale.getDefault();
        Locale localeCymraeg = Locale.of("cy");

        ResourceBundle bundle = ResourceBundle.getBundle("messages",locale);
        return bundle.getString(messageId);

    }
}

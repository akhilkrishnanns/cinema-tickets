package uk.gov.dwp.uc.pairtest.util;

import java.util.Locale;

public interface MessageSource {


    default String message(String messageId){
        return message(messageId,Locale.getDefault());
    }

    String message(String messageId, Locale locale);


}

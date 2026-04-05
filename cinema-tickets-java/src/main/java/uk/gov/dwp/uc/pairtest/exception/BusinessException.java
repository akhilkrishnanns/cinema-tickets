package uk.gov.dwp.uc.pairtest.exception;

import uk.gov.dwp.uc.pairtest.util.MessageSource;
import uk.gov.dwp.uc.pairtest.util.StaticMessageSource;

import java.util.Locale;

public class BusinessException extends RuntimeException{


    private static MessageSource messageSource = new StaticMessageSource();

    protected BusinessException(String messageId,Locale locale){

        /*
         * set enableSuppression and writableStackTrace from the config.
         */

        super(getLocalMessage(messageId,locale),null,true,false   );

    }

    static String getLocalMessage(String messageId, Locale locale){

        return messageSource.message(messageId,locale);
    }
}

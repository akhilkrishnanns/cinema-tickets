package uk.gov.dwp.uc.pairtest.exception;

import java.util.Locale;

public class InvalidAccountException extends BusinessException{

/**
 * Constructor
 * */
    public InvalidAccountException(String messageId, Locale locale) {
        super(messageId,locale);
    }

}

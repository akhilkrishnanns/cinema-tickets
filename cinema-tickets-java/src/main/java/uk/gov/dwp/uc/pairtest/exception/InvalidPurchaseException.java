package uk.gov.dwp.uc.pairtest.exception;

import java.util.Locale;

public class InvalidPurchaseException extends BusinessException {

    /**
     * Constructor
     * */
    public InvalidPurchaseException(String messageId, Locale locale){

        super(messageId,locale);
    }

}

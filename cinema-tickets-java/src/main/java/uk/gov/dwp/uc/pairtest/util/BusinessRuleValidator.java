package uk.gov.dwp.uc.pairtest.util;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Locale;

import static uk.gov.dwp.uc.pairtest.constants.messageConstants.*;


public class BusinessRuleValidator {

    /**
     * Maximum number of tickets that can be purchased at a time.
     */
    static final int MAX_TICKET_COUNT = 25;

    /**
     * method to validate core business rules.
     * @throws InvalidPurchaseException if any validation fails. Error message can be used to identify the exact
     * error.
     */
    public void validateBusinessRules(TicketRequestData totalTicketData) {

        /*
         * Validates minimum adult ticket requirement
         */
        int adult = totalTicketData.getOrDefault(TicketTypeRequest.Type.ADULT, 0);
        if(adult==0) throw new InvalidPurchaseException(ERROR_PURCHASE_ZERO_ADULT, Locale.getDefault());

        /*
         * Validates Adult to Infant ratio.
         * Number of ticket for Infants should be less than or equal to number of tickets for Adults
         */

        int infant = totalTicketData.getOrDefault(TicketTypeRequest.Type.INFANT, 0);
        if(infant>adult) throw new InvalidPurchaseException(
                ERROR_PURCHASE_INFANT_ADULT_RATIO_MISMATCH, Locale.getDefault()
        );

        /*
         * Validates maximum ticket count requirement
         */
        int child = totalTicketData.getOrDefault(TicketTypeRequest.Type.CHILD, 0);
        int totalTicketCount = adult + child + infant;
        if(MAX_TICKET_COUNT < totalTicketCount) throw new InvalidPurchaseException(
                ERROR_PURCHASE_MAX_TICKET_COUNT_EXCEEDED,Locale.getDefault()
        );



    }
}

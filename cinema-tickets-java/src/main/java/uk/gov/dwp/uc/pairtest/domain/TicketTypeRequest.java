package uk.gov.dwp.uc.pairtest.domain;

/**
 * Immutable Object
 * @implNote
 * Previous class has been updated to a record which satisfies previous functionalities in less LOC.
 */

public record TicketTypeRequest (Type type, int noOfTickets){


    public enum Type {
        ADULT, CHILD , INFANT
    }

}

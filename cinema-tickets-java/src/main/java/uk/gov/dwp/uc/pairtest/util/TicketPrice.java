package uk.gov.dwp.uc.pairtest.util;


/**
 * Enum to Store price per ticket type.
 * price can be defined by using config files/other data sources if there is frequent change required
 * */
public enum TicketPrice {

    ADULT(25),
    CHILD(15),
    INFANT(0);

    private final int price;

    TicketPrice(int price){
        this.price = price;
    }

    /**
     * method to fetch price for the Ticket type
     * @return int price
     * */

    public int getPrice() {
        return price;
    }
}
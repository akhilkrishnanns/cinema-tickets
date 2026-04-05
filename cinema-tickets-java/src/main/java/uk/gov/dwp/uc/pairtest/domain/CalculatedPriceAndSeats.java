package uk.gov.dwp.uc.pairtest.domain;

/**
 * Immutable dto to store calculated price to pay and seats to allocate.
 * */
public record CalculatedPriceAndSeats(int totalAmount, int totalSeats) {

}

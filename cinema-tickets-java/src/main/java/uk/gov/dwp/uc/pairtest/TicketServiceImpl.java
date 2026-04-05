package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.CalculatedPriceAndSeats;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidAccountException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.util.BusinessRuleValidator;
import uk.gov.dwp.uc.pairtest.util.TicketPrice;
import uk.gov.dwp.uc.pairtest.util.TicketRequestData;

import java.util.Locale;

import static uk.gov.dwp.uc.pairtest.constants.messageConstants.ERROR_ACCOUNT_INVALID;
import static uk.gov.dwp.uc.pairtest.constants.messageConstants.ERROR_PURCHASE_INVALID_INPUT_DATA_PROVIDED;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */

    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;
    private final BusinessRuleValidator businessRuleValidator;

    /**
     * Constructor
     * */
    public TicketServiceImpl(
            TicketPaymentService ticketPaymentService,
            SeatReservationService seatReservationService,
            BusinessRuleValidator businessRuleValidator
    ) {

        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
        this.businessRuleValidator = businessRuleValidator;
    }

    /**
     * It is a public method overrides from the parent {@link TicketService}.
     * @param accountId Accepts a valid Long which is greater than zero.
     * @param ticketTypeRequests Accepts var args of {@link TicketTypeRequest}
     * @throws InvalidPurchaseException if any invalid purchase inputs given
     * @throws InvalidAccountException if invalid account/user id given
     * */
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests)
            throws InvalidPurchaseException{

        /*
         * if the input parameter changes to a user DTO, move below validation to validator
         */
        if(accountId<1) throw new InvalidAccountException(ERROR_ACCOUNT_INVALID,Locale.getDefault());
        validateTicketTypeRequestsData(ticketTypeRequests);

        TicketRequestData totalTicketData = getTotalTicketData(ticketTypeRequests);
        businessRuleValidator.validateBusinessRules(totalTicketData);

        CalculatedPriceAndSeats calculatedTotalPriceAndSeats
                = calculateTotalAmountToPayAndSeatsToAllocate(totalTicketData);

        ticketPaymentService.makePayment(accountId, calculatedTotalPriceAndSeats.totalAmount());
        seatReservationService.reserveSeat(accountId,calculatedTotalPriceAndSeats.totalSeats());
    }

    /**
     * Method used to merge multiple {@link  TicketTypeRequest} objects with same
     * {@link TicketTypeRequest.Type}<br>
     * {@code Map.merge} method will sum up the count of same {@link TicketTypeRequest.Type}
     * @param ticketTypeRequests is array of {@link TicketTypeRequest} from the purchase request.
     * @return {@link TicketRequestData}
     * */

    private TicketRequestData getTotalTicketData(TicketTypeRequest[] ticketTypeRequests) {
        var totalTicketData = new TicketRequestData();
        for(TicketTypeRequest typeAndCount: ticketTypeRequests){
            totalTicketData.merge(typeAndCount.type(), typeAndCount.noOfTickets(), Integer::sum);
        }
        return totalTicketData;
    }


    /**
     * Validates ticket type request dato to avoid null pointer Exception and empty array.
     * @param ticketTypeRequests array of {@link TicketTypeRequest}
     * @throws InvalidPurchaseException if any invalid data given.*/

    private void validateTicketTypeRequestsData(TicketTypeRequest[] ticketTypeRequests) {
        if( ticketTypeRequests == null || ticketTypeRequests.length == 0)
            throw new InvalidPurchaseException(
                    ERROR_PURCHASE_INVALID_INPUT_DATA_PROVIDED,
                    Locale.getDefault()
            );
    }

    /**
     * Private method to calculate the total amount to pay and number of seats to allocate as per the purchase.
     * @param totalTicketData is object of {@link TicketRequestData} which extends
     * {@code EnumMap<TicketTypeRequest.Type, Integer>} to combine ticket count based on
     * {@link TicketTypeRequest.Type}.
     * @return object of {@code CalculatedPriceAndSeats} with price and seat count for processing purchase.
     * */
    private CalculatedPriceAndSeats calculateTotalAmountToPayAndSeatsToAllocate(
            TicketRequestData totalTicketData
    ) {

        int adults = totalTicketData.getOrDefault(TicketTypeRequest.Type.ADULT, 0);
        int child = totalTicketData.getOrDefault(TicketTypeRequest.Type.CHILD, 0);

        /*
        Update the below calculation if INFANT ticket price changes
        */
        int totalPayableAmount = TicketPrice.ADULT.getPrice()*adults + TicketPrice.CHILD.getPrice()*child;
        int totalAllocatableSeats = adults+child;
        return new CalculatedPriceAndSeats(totalPayableAmount,totalAllocatableSeats);
    }


}

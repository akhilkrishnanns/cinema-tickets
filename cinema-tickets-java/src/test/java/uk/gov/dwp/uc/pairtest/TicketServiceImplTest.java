package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidAccountException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.util.BusinessRuleValidator;
import uk.gov.dwp.uc.pairtest.util.TestInputConverter;
import uk.gov.dwp.uc.pairtest.util.TicketRequestData;

import java.util.Map;


/**
 * Validating and testing methods from {@link TicketServiceImpl} class
 * */
@DisplayName("Ticket Service Implementation Test")
class TicketServiceImplTest {


    /**
     * Validating and testing Purchase ticket related operations*/
    @Nested
    @DisplayName("Purchase ticket tests")
    class PurchaseTicketTests{


        @Mock
        BusinessRuleValidator businessRuleValidator;

        @InjectMocks
        TicketServiceImpl ticketServiceImpl;


        @Spy
        TicketPaymentService ticketPaymentService;

        @Spy
        SeatReservationService seatReservationService;

//        TicketPaymentService ticketPaymentService = new TicketPaymentServiceImpl();
//
//        SeatReservationService seatReservationService = new SeatReservationServiceImpl();


        @BeforeEach
        void setUp(){

            MockitoAnnotations.openMocks(this);

        }



        /**
         * Parameterized test for successful purchase of tickets when valid inputs provided.
         * Given different scenarios to ensure the system capability.
         * CSV source gives number of tickets to be purchased for {@code ADULT}, {@code INFANT}, {@code CHILD} in each testcase
         * */
        @ParameterizedTest
        @CsvSource({"10,0,0","10,10,5","2,1,0","2,0,2"})
        @DisplayName("Should purchase tickets successfully when valid account id and TicketTypeRequest submitted")
        void shouldPurchaseTicketSuccessfully(int adults, int infants, int children){
            //Given
            long accountId = 1L;
            TicketTypeRequest[] ticketTypeRequests = TestInputConverter
                    .convertCsvDataToTicketTypeRequests(adults, infants, children);

            TicketRequestData businessRuleInputMap = TestInputConverter
                    .convertCsvDataToMap(adults, infants, children);


            Mockito.doNothing().when(businessRuleValidator).validateBusinessRules(businessRuleInputMap);

            //when
            Assertions.assertDoesNotThrow(()->ticketServiceImpl
                    .purchaseTickets(accountId,ticketTypeRequests));


            /*
            * validates the void functions are called exactly one time to successfully process the purchase request
            * */
            //then
            Mockito.verify(ticketPaymentService,Mockito.times(1)).makePayment(1L,(25*adults)+(15*children));
            Mockito.verify(seatReservationService,Mockito.times(1)).reserveSeat(1L,adults+children);


        }


        /**
         * Testing throws {code InvalidPurchaseException} for purchase of tickets when invalid input provided.
         * Given null for {@code TicketTypeRequest[]}*/

        @Test
        @DisplayName("Should throw invalid purchase ex when TicketTypeRequest is null")
        void shouldThrowExceptionOnNullTicketTypeRequest(){


            //Given
            long accountId = 1L;

            //when
            Assertions.assertThrowsExactly(
                    InvalidPurchaseException.class,
                    ()->ticketServiceImpl.purchaseTickets(accountId, (TicketTypeRequest[]) null)
            );

            //then
            Mockito.verify(ticketPaymentService,Mockito.never()).makePayment(Mockito.anyLong(),Mockito.anyInt());
            Mockito.verify(seatReservationService,Mockito.never()).reserveSeat(Mockito.anyLong(),Mockito.anyInt());

        }

        @Test
        @DisplayName("Should throw invalid purchase ex when TicketTypeRequest is Empty Array")
        void shouldThrowExceptionOnEmptyTicketTypeRequestArray(){

            //Given
            long accountId = 1L;


            //when
            Assertions.assertThrowsExactly(InvalidPurchaseException.class,
                    ()->ticketServiceImpl.purchaseTickets(accountId,new TicketTypeRequest[]{}));

            //then
            Mockito.verify(ticketPaymentService,Mockito.never()).makePayment(Mockito.anyLong(),Mockito.anyInt());
            Mockito.verify(seatReservationService,Mockito.never()).reserveSeat(Mockito.anyLong(),Mockito.anyInt());

        }


        

        /**
         * validating by assuring the system throws {@code InvalidUserException}
         * when invalid account id is provided
         * */

        @ParameterizedTest
        @CsvSource({"0","-1","-10"})
        @DisplayName("Should throw Invalid Account Exception when the account id is invalid")
        void ShouldThrowExceptionWhenInvalidAccountIdProvided(long accountId){

            //Given

            TicketTypeRequest[] ticketTypeRequests = TestInputConverter.convertCsvDataToTicketTypeRequests(1, 1, 1);


            //When & Then

            Assertions.assertThrowsExactly(InvalidAccountException.class,()->ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequests));
        }




    }

}
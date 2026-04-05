package uk.gov.dwp.uc.pairtest.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;


/**
 * Validating and testing Business rules from {@link BusinessRuleValidator}
 * */
@DisplayName("Business rule validation check")
class BusinessRuleValidatorTest {

    @InjectMocks
    BusinessRuleValidator businessRuleValidator;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test for all possible valid purchase inputs
     * will test maximum limit can be purchased at single time
     * minimum number of tickets can be purchased
     * will test purchase of only 2 types of tickets including Adult
     * */
    @ParameterizedTest
    @CsvSource({"10,10,5","1,0,0","1,0,1","1,1,0"})
    @DisplayName("Should Successfully validate all business rules when valid inputs given")
    void shouldValidateBusinessRulesSuccessfully(int adults, int infants, int children){

        //Given
        TicketRequestData testDataMap = TestInputConverter.convertCsvDataToMap(adults, infants, children);

        //When & Then
        Assertions.assertDoesNotThrow(()->businessRuleValidator.validateBusinessRules(testDataMap));


    }


    /**
     * validating when business rule by assuring the system throws {@code InvalidPurchaseException}
     * when there is no adult ticket in the kart/ {@code TicketTypeRequest[]}
     * */
    @Test
    @DisplayName("Should throw invalid purchase ex when TicketTypeRequest contains No Adults")
    void shouldThrowExceptionOnTicketTypeRequestHasNoAdults(){

        //Given

        TicketRequestData dataMap = TestInputConverter.convertCsvDataToMap(0, 4, 4);

        //when & then
        Assertions.assertThrowsExactly(
                InvalidPurchaseException.class,
                ()->businessRuleValidator.validateBusinessRules(dataMap)
        );


    }

    /**
     * validating when business rule by assuring the system throws {@code InvalidPurchaseException}
     * when the adult ticket count is less than infants ticket count in the kart/ {@code TicketTypeRequest[]}.
     * Infants do not pay for a ticket and are not allocated a seat.They will be sitting on an Adult's lap.
     * */

    @Test
    @DisplayName("Should throw invalid purchase ex when TicketTypeRequest contains less Adults than infants")
    void shouldThrowExceptionOnTicketTypeRequestHasLessAdultsThanInfants(){

        //Given
        TicketRequestData dataMap = TestInputConverter.convertCsvDataToMap(2, 3, 0);

        //When & Then
        Assertions.assertThrowsExactly(
                InvalidPurchaseException.class,
                ()->businessRuleValidator.validateBusinessRules(dataMap)
        );



    }

    /**
     * validating when business rule by assuring the system throws {@code InvalidPurchaseException}
     * when the adult ticket count is less than infants ticket count in the kart/ {@code TicketTypeRequest[]}.
     * Infants do not pay for a ticket and are not allocated a seat.They will be sitting on an Adult's lap.
     * */

    @Test
    @DisplayName("Should throw invalid purchase ex when total ticket number is more than 25")
    void shouldThrowExceptionOnTicketCountExceedsMaximumLimit(){

        //Given
        TicketRequestData dataMap = TestInputConverter.convertCsvDataToMap(25, 1, 0);

        //When & Then
        Assertions.assertThrowsExactly(
                InvalidPurchaseException.class,
                ()->businessRuleValidator.validateBusinessRules(dataMap)
        );


    }

}
package uk.gov.dwp.uc.pairtest.util;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing Static message source")
class StaticMessageSourceTest {

    private static final String TEST_MESSAGE_ID = "err.account.invalid-id";
    private static final String DEFAULT_TEST_MESSAGE = "Invalid Account Id provided. Account ID should be greater than zero";
    private static final String WELSH_TEST_MESSAGE = "Rhif Adnabod Cyfrif Annilys wedi'i ddarparu. Dylai Rhif Adnabod y Cyfrif fod yn fwy na sero.";

    @InjectMocks
    StaticMessageSource staticMessageSource;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

    }




    @Test
    @DisplayName("Should Successfully show locale message corresponding to the message id and locale")
    void shouldReturnDifferentLocaleMessageSuccessfully(){

        Assertions.assertEquals(
                DEFAULT_TEST_MESSAGE,
                staticMessageSource.message(TEST_MESSAGE_ID,Locale.getDefault())
        );

        Assertions.assertEquals(
                WELSH_TEST_MESSAGE,
                staticMessageSource.message(TEST_MESSAGE_ID,Locale.of("cy"))
        );



    }

}
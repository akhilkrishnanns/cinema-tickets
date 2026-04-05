package uk.gov.dwp.uc.pairtest.util;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.*;

/**
 * Class helps the Test methods to parse/convert the input for parameterized tests.*/
public class TestInputConverter {

    /**
     * A test helper static method used to convert test parameters to the appropriate input format.
     * @param adults number of adult tickets to be purchased.
     * @param children number of child tickets to be purchased.
     * @param infants number of infant tickets to be purchased.
     * @return  {@code TicketTypeRequest[]}
     * */
    public static TicketTypeRequest[] convertCsvDataToTicketTypeRequests(
            int adults, int infants, int children
    ){

        return new TicketTypeRequest[]{
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, adults),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, infants),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, children)
        };
    }

    /**
     * A test helper static method used to convert test parameters to the appropriate input format.
     * @param adults number of adult tickets to be purchased.
     * @param children number of child tickets to be purchased.
     * @param infants number of infant tickets to be purchased.
     * @return  {@code EnumMap<TicketTypeRequest.Type, Integer>}
     * */
    public static TicketRequestData convertCsvDataToMap(int adults, int infants, int children){

        TicketRequestData resultMap = new TicketRequestData();

        resultMap.put(TicketTypeRequest.Type.ADULT,adults);
        resultMap.put(TicketTypeRequest.Type.CHILD,children);
        resultMap.put(TicketTypeRequest.Type.INFANT,infants);

        return resultMap;
    }
}

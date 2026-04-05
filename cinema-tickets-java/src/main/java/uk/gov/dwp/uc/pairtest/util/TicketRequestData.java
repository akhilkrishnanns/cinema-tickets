package uk.gov.dwp.uc.pairtest.util;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.EnumMap;
import java.util.Map;

public class TicketRequestData extends EnumMap<TicketTypeRequest.Type, Integer> {

    public TicketRequestData() {
        super(TicketTypeRequest.Type.class);
    }

    public TicketRequestData(EnumMap<TicketTypeRequest.Type, ? extends Integer> m) {
        super(m);
    }

    public TicketRequestData(Map<TicketTypeRequest.Type, ? extends Integer> m) {
        super(m);
    }
}

package es.udc.ws.bikes.client.service.soap;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.client.service.soap.wsdl.ServiceBikeDto;
import es.udc.ws.bikes.client.service.dto.ClientBikeDto;

public class BikeDtoToSoapBikeDtoConversor {


    public static ClientBikeDto toClientBikeDto(ServiceBikeDto bike) {
        return new ClientBikeDto(bike.getBikeId(), bike.getModel(),
               bike.getDescription(), bike.getFirstDate().toGregorianCalendar(), 
                bike.getPrice(), bike.getUnits());
    }

    public static List<ClientBikeDto> toClientBikeDtos(List<ServiceBikeDto> bikes) {
        List<ClientBikeDto> bikeDtos = new ArrayList<>(bikes.size());
        for (int i = 0; i < bikes.size(); i++) {
            ServiceBikeDto bike = bikes.get(i);
            bikeDtos.add(toClientBikeDto(bike));
        }
        return bikeDtos;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/JerseyClient.java to edit this template
 */
package Client;

import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

/**
 * Jersey REST client generated for REST resource:HotelResource [/hotel]<br>
 * USAGE:
 * <pre>
 *        NewJerseyClient client = new NewJerseyClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author mufff
 */
public class NewJerseyClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/ProjektHotel/webresources";

    public NewJerseyClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("hotel");
    }
    
    public void makeReservation(Object requestEntity) throws ClientErrorException {
        webTarget.path("reservations").request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }
    public List<Room> getText() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("rooms");
        GenericType<List<Room>> responseType = new GenericType<List<Room>>() {};
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }
    
     public List<Reservations> getReservations() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("reservations");
        GenericType<List<Reservations>> responseType = new GenericType<List<Reservations>>() {};
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void updateReservations(Object requestEntity, String reservationId) throws ClientErrorException {
       GenericType<Reservations> responseType = new GenericType<Reservations>() {};
       webTarget.path(java.text.MessageFormat.format("reservations/{0}", new Object[]{reservationId})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), responseType);
    }
   
    public Reservations getReservation(String reservationId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("reservations/{0}", new Object[]{reservationId}));
         GenericType<Reservations> responseType = new GenericType<Reservations>() {};
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }
public Reservations deleteReservation(Long reservationId) {
    WebTarget target = webTarget.path("reservations").path(String.valueOf(reservationId));
    return target.request().delete(Reservations.class);
}
    public void close() {
        client.close();
    }
    
}
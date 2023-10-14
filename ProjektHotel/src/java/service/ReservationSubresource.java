package service;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import model.Room;
import resources.HotelService;

@Path("/")
public class ReservationSubresource {

    private Long reservationId;
    private HotelService hotel;

    public ReservationSubresource(@PathParam("roomId") Long reservationId, HotelService hotel) {
        this.reservationId = reservationId;
        this.hotel = hotel;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public Room getDetails() {
        return hotel.getRoomInfoOfReservation(reservationId);
    }
}

package service;

import exception.NotFoundException;
import java.util.List;
import javax.ejb.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import model.Reservations;
import model.Room;
import resources.HotelService;

@Path("/hotel")
@Singleton
public class HotelResource {

    static HotelService hotelService = new HotelService();

    @GET
    @Path("/rooms")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getText() {
        return hotelService.getAllRooms();
    }

    @GET
    @Path("/reservations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reservations> getReservations() {
        return hotelService.getAllReservations();
    }

    @GET
    @Path("/reservations/{reservationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Reservations getReservation(@PathParam("reservationId") Long id, @Context UriInfo uriInfo) {
        Reservations reservation = hotelService.getReservation(id);
        if (reservation == null) {
            throw new NotFoundException(id);
        }
        String uri = uriInfo.getBaseUriBuilder().path(HotelResource.class).path(String.valueOf(reservation.getId())).build().toString();
        reservation.addLink(uri, "self");

        String uri2 = uriInfo.getBaseUriBuilder()
                .path(HotelResource.class)
                .path(HotelResource.class, "getDetails")
                .resolveTemplate("id", reservation.getId())
                .build()
                .toString();

        reservation.addLink(uri2, "details");
        return reservation;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reservations")
    public Reservations makeReservation(Reservations reservation) {
        Reservations createdReservation = hotelService.makeReservation(reservation);
        return createdReservation;
    }

    @PUT
    @Path("/reservations/{reservationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Reservations updateReservations(Reservations reservation) {
        return hotelService.updateReservation(reservation);
    }

    @DELETE
    @Path("/reservations/{reservationId}")
    public Reservations deleteReservation(@PathParam("reservationId") Long id) {
        //return "post test";
        return HotelService.deleteReservation(id);
    }

    @Path("/reservations/{id}/details")
    public ReservationSubresource getDetails(@PathParam("id") Long reservationId) {
        return new ReservationSubresource(reservationId, hotelService);
    }
}

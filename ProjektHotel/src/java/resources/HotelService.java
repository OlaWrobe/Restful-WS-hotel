package resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Room;
import model.Reservations;

public class HotelService {

    private static Map<Long, Room> room = new HashMap<>();
    //private static Map<Long, Room> room = new HashMap<Long, Room>();
    private static Map<Long, Reservations> reservations = new HashMap<>();
    //private static Map<Long,Reservations> reservations = new HashMap<Long, Reservations>();

    public HotelService() {
        //List<String> stringList = new ArrayList<>();
        //stringList.add("FirstComment");
        room.put(101L, new Room(101L, 1, 250));
        room.put(102L, new Room(102L, 1, 300));
        room.put(103L, new Room(103L, 1, 350));
        room.put(104L, new Room(104L, 2, 250));
        room.put(105L, new Room(105L, 2, 300));
        room.put(106L, new Room(106L, 3, 350));
        room.put(107L, new Room(107L, 3, 370));
        room.put(108L, new Room(108L, 3, 400));
        room.put(109L, new Room(109L, 4, 400));
        room.put(110L, new Room(110L, 4, 500));
        Date date = new Date(2023, 6, 19);
        Date date2 = new Date(2023, 6, 21);
        reservations.put(1L, new Reservations(1, 104, 1, date, date2));
    }

    public List<Room> getAllRooms() {
        return new ArrayList<Room>(room.values());
    }

    public Room getRoom(Long roomN) {
        return room.get(roomN);
    }

    public Reservations makeReservation(Reservations reservation) {
        reservation.setId(reservations.size() + 1);
        reservations.put(reservations.size() + 1L, reservation);
        return reservation;
    }

    public List<Reservations> getAllReservations() {
        return new ArrayList<Reservations>(reservations.values());
    }

    public Reservations updateReservation(Reservations reservation) {
        Long id = (long) reservation.getId();
        return reservations.put(id, reservation);
    }

    public Reservations getReservation(Long id) {
        return reservations.get(id);
    }

    public Room getRoomInfoOfReservation(Long reservationId) {
        long roomNumber = reservations.get(reservationId).getRoomNumber();
        return room.get(roomNumber);
    }

    public void addLink(long reservationId, String path, String name) {
        reservations.get(reservationId).addLink(path, name);
    }

    public static Reservations deleteReservation(Long id) {
        return reservations.remove(id);
    }

    private boolean doDatesOverlap(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
        return (startDate1.before(endDate2) || startDate1.equals(endDate2))
                && (endDate1.after(startDate2) || endDate1.equals(startDate2));
    }
}

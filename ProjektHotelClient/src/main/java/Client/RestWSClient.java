//package Client;
//
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfWriter;
//
//import java.util.Date;
//import java.util.List;
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.GenericType;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import model.Reservations;
//import model.Room;
//import org.glassfish.jersey.server.ResourceConfig;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public abstract class RestWSClient extends ResourceConfig {
//
//    public static void main(String[] args) {
//        List<Room> pokoje = allPokoje();
//        for(Room r : pokoje){
//            System.out.println(r.getRoomNumber());
//        }
//        //allReservations();
//       // int number = 1;
//        //reservationDetails(number);
//        //addReservation();
//        //deleteReservation(number);
//        //updateReservation(number);
//    }
//
//    private static List<Room> allPokoje() {
//        Client client = ClientBuilder.newClient();
//        WebTarget target = client.target("http://localhost:8080/ProjektHotel/webresources/hotel/rooms");
//        Response response = target.request().get();
//        System.out.println("response: " + response);
//        List<Room> lista;
//        lista = target.request(MediaType.APPLICATION_JSON).get(new GenericType<List<Room>>() {
//        });
//        System.out.println("Response : " + lista);
//        return lista;
//    }
//
//    private static void allReservations() {
//        Client client = ClientBuilder.newClient();
//        WebTarget target = client.target("http://localhost:8080/ProjektHotel/webresources/hotel/reservations");
//        Response response = target.request().get();
//        System.out.println("response: " + response);
//        List<Reservations> lista;
//        lista = target.request(MediaType.APPLICATION_JSON).get(new GenericType<List<Reservations>>() {
//        });
//        System.out.println("Response : " + lista);
//    }
//
//    private static void reservationDetails(int number) {
//        Client client = ClientBuilder.newClient();
//        String source = "http://localhost:8080/ProjektHotel/webresources/hotel/reservations/" + number;
//        WebTarget target = client.target(source);
//        Response response = target.request().get();
//        System.out.println("response: " + response);
//
//        String responseBody = response.readEntity(String.class);
//        //GENEROWANIE PDF
//        try {
//            Document document = new Document();
//            PdfWriter.getInstance(document, new FileOutputStream("reservation_details.pdf"));
//            document.open();
//            document.add(new Paragraph(responseBody));
//            document.close();
//            System.out.println("PDF generated successfully.");
//        } catch (DocumentException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void addReservation() {
//        Client client = ClientBuilder.newClient();
//        WebTarget target = client.target("http://localhost:8080/ProjektHotel/webresources/hotel");
//        Date date = new Date(123, 5, 30);
//        Date date2 = new Date(123, 5, 31);
//        Reservations newReservation = new Reservations(2, 102, 1, date, date2);
//        Response response = target.request().post(Entity.json(newReservation));
//        System.out.println("response: " + response);
//    }
//
//    private static void deleteReservation(int id) {
//        Client client = ClientBuilder.newClient();
//        String source = "http://localhost:8080/ProjektHotel/webresources/hotel/reservations/" + id;
//        WebTarget target = client.target(source);
//        Response response = target.request(MediaType.APPLICATION_JSON).delete();
//        System.out.println("response: " + response.getStatus());
//    }
//
//    private static void updateReservation(int id) {
//        Client client = ClientBuilder.newClient();
//        String source = "http://localhost:8080/ProjektHotel/webresources/hotel/reservations/" + id;
//        WebTarget target = client.target(source);
//        Date date = new Date(123, 6, 30);
//        Date date2 = new Date(123, 6, 31);
//        Reservations updateReservation = new Reservations(1, 102, 1, date, date2);
//        Response response = target.request(MediaType.APPLICATION_JSON).put(Entity.json(updateReservation));
//        System.out.println("response: " + response);
//    }
//}

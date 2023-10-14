package Client;

import java.util.List;

public class Client {

    public static void main(String[] args) {
        NewJerseyClient client = new NewJerseyClient();
        List<Room> pokoje = client.getText();

        System.out.println(pokoje);

        //allReservations();
        // int number = 1;
        //reservationDetails(number);
        //addReservation();
        //deleteReservation(number);
        //updateReservation(number);
        
//        //TEGO NIE DOTYKASZ TO MUSI BYC NA POCZATKU
//           NewJerseyClient client = new NewJerseyClient();
//           
//        //LISTA WSZYSTKICH POKOI   
//         List<Room> pokoje = client.getText();
//         
//        System.out.println(pokoje);
//         
//      //LISTA WSZYSTKICH REZERWACJI   
//      List<Reservations> rezerwacje = client.getReservations();
//      System.out.println(rezerwacje);
//      
//      //TWORZENIE NOWEJ REZERWACJI
//     
//        Date date = new Date(123, 7, 18); 
//        Date date2 = new Date(123, 7, 20);
//      Reservations rezerwacja = new Reservations(0,102,2,date,date2);
//      //DODAWANIE NOWEJ REZERWACJI 
//      client.makeReservation(rezerwacja);
//      
//      //CZYTAM SOBIE REZERWACJE
//       rezerwacje = client.getReservations();
//      System.out.println(rezerwacje);
//      
//      //TWORZENIE ISTNIEJACEJ REZERWACJI I ZMIANA JEJ
//      rezerwacja = new Reservations(2,102,10,date,date2);
//      
//      //AKTUALIZACJA REZERWACJI
//      client.updateReservations(rezerwacja,"2");
//      
//      //WYPISUJE SOBIE 
//        for(Reservations r : rezerwacje)
//        {
//            System.out.println(r.getAmountOfGuests());
//        }
//        
//        //USUWANIE REZERWACJI 
//        client.deleteReservation(1L);
//        
//        //WYPISUJE SOBIE
//        rezerwacje = client.getReservations();
//         for(Reservations r : rezerwacje)
//        {
//            System.out.println(r.toString());
//        }
    }
}

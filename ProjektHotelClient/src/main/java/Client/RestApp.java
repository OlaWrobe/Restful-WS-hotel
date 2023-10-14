package Client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import Client.Reservations;
import Client.Room;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author joann
 */
public class RestApp extends Application {

    private boolean isInvalidDateErrorDisplayed = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        NewJerseyClient client = new NewJerseyClient();
        //wybór liczby osób
        Label amountOfPeopleLabel = new Label("Number of people");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("1", "2", "3", "4");
        comboBox.setPromptText("Choose option...");

        //wybór daty zameldowania
        Label checkInDateLabel = new Label("Check-in date");
        DatePicker checkInDate = new DatePicker();
        checkInDate.setValue(LocalDate.now());
        checkInDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isBefore(LocalDate.now())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Incorrect date");
                alert.setContentText("Check in date cannot be earlier than the current day.");
                alert.showAndWait();
                checkInDate.setValue(oldValue);
            }
        });

        //wybór daty wymeldowania
        Label checkOutDateLabel = new Label("Check-out date");
        DatePicker checkOutDate = new DatePicker();
        checkOutDate.setValue(LocalDate.now().plusDays(1));
        checkOutDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent eventa) {
                if (checkOutDate.getValue().isBefore(checkInDate.getValue().plusDays(1))) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Incorrect date");
                    alert.setContentText("Check out date must be at least a day later than check in date.");
                    alert.showAndWait();
                    checkOutDate.setValue(checkInDate.getValue().plusDays(1));
                }
            }
        });

        //guzik "SZUKAJ" (przejście do kolejnego okna)
        Button searchButton = new Button();
        searchButton.setText("Search");
        searchButton.setDisable(true); // Wyłączamy przycisk na początku
        comboBox.setOnAction(event -> {
            // Sprawdzamy, czy combobox został uzupełniony
            if (comboBox.getValue() != null && !comboBox.getValue().isEmpty()) {
                searchButton.setDisable(false); // Włączamy przycisk po uzupełnieniu combobox
            } else {
                searchButton.setDisable(true); // Wyłączamy przycisk, jeśli combobox jest pusty
            }
        });
        searchButton.setOnAction(event -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd M yyyy");
            
            
            //zmiana formatu comboBox'a
            String selectedValue = comboBox.getValue();
            int intValue = Integer.parseInt(selectedValue);

            // Pobierz listę pokoi z serwera
            List<Room> roomList = client.getText();

            // Tworzenie tabeli i kolumn
            TableView<Room> tableView = new TableView<>();
            TableColumn<Room, Integer> roomNumberColumn = new TableColumn<>("Room Number");
            TableColumn<Room, Integer> capacityColumn = new TableColumn<>("Capacity");
            TableColumn<Room, Double> priceColumn = new TableColumn<>("Price");

            // Mapowanie pól klasy Room na odpowiednie kolumny
            roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
            capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

            // Wywołanie listy rezerwacji
            List<Reservations> rezerwacje = client.getReservations();
            String checkInDateString = checkInDate.getValue().format(formatter);
            String checkOutDateString = checkOutDate.getValue().format(formatter);

            String[] date1 = checkInDateString.split(" ");
            String[] date2 = checkOutDateString.split(" ");

            Date checkInDate2 = new Date(Integer.parseInt(date1[0]), Integer.parseInt(date1[1]), Integer.parseInt(date1[2]));

            Date checkOutDate2 = new Date(Integer.parseInt(date2[0]), Integer.parseInt(date2[1]), Integer.parseInt(date2[2]));

            //filtrowanie listy wyszukań
            List<Room> filtered = filterRooms(intValue, checkInDate2, checkOutDate2, rezerwacje, roomList);

            // Dodawanie danych do tabeli
            tableView.getItems().addAll(filtered);

            // Dodawanie kolumn do tabeli
            tableView.getColumns().addAll(roomNumberColumn, capacityColumn, priceColumn);
            TableColumn<Room, Void> bookColumn = new TableColumn<>(" ");
            bookColumn.setCellFactory(column -> {
                return new TableCell<Room, Void>() {
                    private final Button bookButton = new Button("book");

                    {
                        bookButton.setOnAction(event -> {
                            Room room = getTableView().getItems().get(getIndex());

                            Stage confirmationStage = new Stage();
                            confirmationStage.setTitle("Potwierdzenie rezerwacji");

                            Button yesButton = new Button("Confirm");
                            Button noButton = new Button("Cancel");

                            yesButton.setOnAction(e -> {
                                String selectedValue = comboBox.getValue();
                                int intValue = Integer.parseInt(selectedValue);
                                Date dateIn = new Date(Integer.parseInt(date1[0]), Integer.parseInt(date1[1]), Integer.parseInt(date1[2]));
                                Date dateOut = new Date(Integer.parseInt(date2[0]), Integer.parseInt(date2[1]), Integer.parseInt(date2[2]));
                                System.out.println(Integer.parseInt(date1[0]) + "," + Integer.parseInt(date1[1]) + "," + Integer.parseInt(date1[2]));
                                Reservations rezerwacja = new Reservations(0, (int) room.getRoomNumber(), intValue, dateIn, dateOut);

                                client.makeReservation(rezerwacja);
                                getTableView().getItems().remove(room);
                                // Zamykanie okna potwierdzenia rezerwacji
                                confirmationStage.close();
                            });

                            noButton.setOnAction(e -> {
                                // Zamykanie okna potwierdzenia rezerwacji
                                confirmationStage.close();
                            });

                            HBox hbox = new HBox(10);
                            hbox.setAlignment(Pos.CENTER);
                            hbox.getChildren().addAll(yesButton, noButton);

                            VBox vbox = new VBox(10);
                            vbox.setAlignment(Pos.CENTER);
                            vbox.getChildren().addAll(new Label("Are you sure you want to book room number " + room.getRoomNumber() + "?"), hbox);

                            Scene confirmationScene = new Scene(vbox, 300, 200);
                            confirmationStage.setScene(confirmationScene);
                            confirmationStage.show();
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(bookButton);
                        }
                    }
                };
            });

            tableView.getColumns().add(bookColumn);

            // Tworzenie sceny z tabelą
            Scene secondScene = new Scene(new VBox(tableView), 400, 300);
            Stage secondStage = new Stage();
            secondStage.setTitle("Search results");
            secondStage.setScene(secondScene);
            secondStage.show();
        });

        //guzik "MY ACCOUNT" (przejście do kolejnego okna)
        Button myAccount = new Button("My account");
        myAccount.setOnAction(event -> {
            Stage accountStage = new Stage();
            accountStage.setTitle("My Account");

            // Pobierz listę pokoi z serwera
            List<Reservations> reservationList = client.getReservations();

            // Tworzenie tabeli i kolumn
            TableView<Reservations> tableView = new TableView<>();
            TableColumn<Reservations, Integer> idNumberColumn = new TableColumn<>("Id");
            TableColumn<Reservations, Integer> roomNumberColumn = new TableColumn<>("roomNumber");
            TableColumn<Reservations, Integer> capacityColumn = new TableColumn<>("Number of people");
            TableColumn<Reservations, Date> dateColumnIn = new TableColumn<>("Check-in date");
            TableColumn<Reservations, Date> dateColumnOut = new TableColumn<>("Check-out date");
            TableColumn<Reservations, Void> editColumn = new TableColumn<>(" ");
            TableColumn<Reservations, Void> generatePDFColumn = new TableColumn<>(" ");
            TableColumn<Reservations, Void> deleteColumn = new TableColumn<>(" ");
            TableColumn<Reservations, Void> infoColumn = new TableColumn<>(" ");

            // Mapowanie pól klasy Room na odpowiednie kolumny
            idNumberColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
            roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
            capacityColumn.setCellValueFactory(new PropertyValueFactory<>("amountOfGuests"));
            dateColumnIn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
            dateColumnOut.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
            editColumn.setCellFactory(column -> {
                return new TableCell<Reservations, Void>() {
                    private final Button editButton = new Button("edit");
                    {
                        editButton.setOnAction(event -> {
                            Reservations reservation = getTableView().getItems().get(getIndex());

                            Stage editableStage = new Stage();
                            editableStage.setTitle("Editing reservation #" + reservation.getId());

                            DatePicker editcheckInDate = new DatePicker();
                            editcheckInDate.setValue(LocalDate.now());
                            editcheckInDate.valueProperty().addListener((observable, editoldValue, editnewValue) -> {
                                if (editnewValue.isBefore(LocalDate.now())) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error");
                                    alert.setHeaderText("Incorrect date");
                                    alert.setContentText("Check in date cannot be earlier than the current day.");
                                    alert.showAndWait();
                                    editcheckInDate.setValue(editoldValue);
                                }
                            });

                            DatePicker editcheckOutDate = new DatePicker();
                            editcheckOutDate.setValue(LocalDate.now().plusDays(1));
                            editcheckOutDate.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent eventb) {
                                    if (editcheckOutDate.getValue().isBefore(checkInDate.getValue().plusDays(1))) {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Error");
                                        alert.setHeaderText("Incorrect date");
                                        alert.setContentText("Check out date must be at least a day later than check in date.");
                                        alert.showAndWait();
                                        editcheckOutDate.setValue(editcheckOutDate.getValue().plusDays(1));
                                    }
                                }
                            });
                            
                            Button saveButton = new Button();
                            saveButton.setText("save");

                            saveButton.setOnAction(eventx -> {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd M yyyy");

                                String editcheckInDateString = editcheckInDate.getValue().format(formatter);
                                String editcheckOutDateString = editcheckOutDate.getValue().format(formatter);

                                String[] editdate1 = editcheckInDateString.split(" ");
                                String[] editdate2 = editcheckOutDateString.split(" ");

                                Date editcheckInDate2 = new Date(Integer.parseInt(editdate1[0]), Integer.parseInt(editdate1[1]), Integer.parseInt(editdate1[2]));

                                Date editcheckOutDate2 = new Date(Integer.parseInt(editdate2[0]), Integer.parseInt(editdate2[1]), Integer.parseInt(editdate2[2]));

                                String text = Integer.toString(reservation.getId());
                                client.updateReservations(new Reservations(reservation.getId(), reservation.getRoomNumber(), reservation.getAmountOfGuests(), editcheckInDate2, editcheckOutDate2), text);
                                editableStage.close();
                            });

                            GridPane gridPane = new GridPane();
                            // Ustawienie marginesów
                            gridPane.setHgap(10);
                            gridPane.setVgap(10);
                            gridPane.setPadding(new Insets(10));

                            gridPane.add(checkInDateLabel, 1, 0);
                            gridPane.add(checkInDate, 1, 1);
                            gridPane.add(checkOutDateLabel, 2, 0);
                            gridPane.add(checkOutDate, 2, 1);
                            gridPane.add(saveButton, 3, 1);

                            Scene editScene = new Scene(gridPane, 700, 100);

                            editableStage.setTitle("Main page");
                            editableStage.setScene(editScene);
                            editableStage.show();
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(editButton);
                        }
                    }
                };
            });
            generatePDFColumn.setCellFactory(column -> {
                return new TableCell<Reservations, Void>() {
                    private final Button generatePDFButton = new Button("PDF");

                    {
                        generatePDFButton.setOnAction(event -> {
                            Reservations reservation = getTableView().getItems().get(getIndex());
                            Client client = ClientBuilder.newClient();
                            String source = "http://localhost:8080/ProjektHotel/webresources/hotel/reservations/" + reservation.getId();
                            WebTarget target = client.target(source);
                            Response response = target.request().get();
                            System.out.println("response: " + response);
                            String responseBody = response.readEntity(String.class);
                            try {
                                Document document = new Document();
                                PdfWriter.getInstance(document, new FileOutputStream("reservation_details.pdf"));
                                document.open();
                                document.add(new Paragraph(responseBody));
                                document.close();
                                System.out.println("PDF generated successfully.");
                            } catch (DocumentException | IOException e) {
                                e.printStackTrace();
                            }

                            Stage informationStage = new Stage();
                            informationStage.setTitle("");

                            Button CloseButton = new Button("Close");
                            CloseButton.setOnAction(e -> {
                                // Zamykanie okna potwierdzenia rezerwacji
                                informationStage.close();
                            });

                            VBox vbox = new VBox(10);
                            vbox.setAlignment(Pos.CENTER);
                            vbox.getChildren().addAll(new Label("PDF generated succesfully."), CloseButton);

                            Scene informationScene = new Scene(vbox, 160, 100);
                            informationStage.setScene(informationScene);
                            informationStage.show();
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(generatePDFButton);
                        }
                    }
                };
            });
            deleteColumn.setCellFactory(column -> {
                return new TableCell<Reservations, Void>() {
                    private final Button deleteButton = new Button("delete");

                    {
                        deleteButton.setOnAction(event -> {
                            Reservations reservation = getTableView().getItems().get(getIndex());
                            client.deleteReservation((long) reservation.getId());
                            getTableView().getItems().remove(reservation);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            });
            infoColumn.setCellFactory(column -> {
                return new TableCell<Reservations, Void>() {
                    private final Button infoButton = new Button("info");

                    {
                        infoButton.setOnAction(event -> {
                            Reservations reservation = getTableView().getItems().get(getIndex());
                            String text = Integer.toString(reservation.getId());
                            client.getReservation(text).getRoomLink();
                            String linkAsString = client.getReservation(text).getRoomLink().toString();
                            linkAsString = linkAsString.substring(1);
                            int startIndex = linkAsString.indexOf("<");
                            int endIndex = linkAsString.indexOf(">");

                            linkAsString = linkAsString.substring(startIndex + 1, endIndex);
                            System.out.println("Informacje o pokoju znajdziesz pod adresem: " + linkAsString);

                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(infoButton);
                        }
                    }
                };
            });

            // Dodawanie danych do tabeli
            tableView.getItems().addAll(reservationList);

            // Dodawanie kolumn do tabeli
            tableView.getColumns().addAll(idNumberColumn, roomNumberColumn, capacityColumn, dateColumnIn, dateColumnOut, editColumn, generatePDFColumn, deleteColumn, infoColumn);

            // Tworzenie sceny z tabelą
            Scene secondScene = new Scene(new VBox(tableView), 815, 500);
            Stage secondStage = new Stage();
            secondStage.setTitle("My account");
            secondStage.setScene(secondScene);
            secondStage.show();
        });

        GridPane gridPane = new GridPane();
        // Ustawienie marginesów
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        gridPane.add(amountOfPeopleLabel, 0, 0);
        gridPane.add(comboBox, 0, 1);
        gridPane.add(checkInDateLabel, 1, 0);
        gridPane.add(checkInDate, 1, 1);
        gridPane.add(checkOutDateLabel, 2, 0);
        gridPane.add(checkOutDate, 2, 1);
        gridPane.add(searchButton, 3, 1);
        gridPane.add(myAccount, 4, 1);

        Scene firstScene = new Scene(gridPane, 700, 100);

        primaryStage.setTitle("Main page");
        primaryStage.setScene(firstScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static List<Room> filterRooms(int capacity, Date desiredArrDate, Date desiredDepDate, List<Reservations> ReservationsList, List<Room> UnfilteredRoomList) {
        List<Room> filteredList = new ArrayList<Room>();

        for (Room r : UnfilteredRoomList) {
            boolean isRoomAvailable = true;

            for (Reservations re : ReservationsList) {
                if (r.getRoomNumber() == re.getRoomNumber() && (desiredArrDate.before(re.getCheckOutDate())
                        && desiredDepDate.after(re.getCheckInDate()))) {
                    isRoomAvailable = false;
                    break;

                }
            }
            if (r.getCapacity() >= capacity) {
                if (isRoomAvailable) {
                    filteredList.add(r);
                }
            }
        }
        return filteredList;
    }

}

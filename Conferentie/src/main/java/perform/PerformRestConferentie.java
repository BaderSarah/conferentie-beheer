package perform;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import domein.Lokaal;
import domein.Evenement;
import domein.Spreker;

public class PerformRestConferentie {

    private final String SERVER_URI = "http://localhost:8080/rest";
    private final WebClient webClient = WebClient.create();

    public PerformRestConferentie() throws Exception {
        System.out.println("\n------------------ GET ALL ------------------");
        System.out.println("**** ROOMS ****");
        getAllRooms();
        System.out.println("\n**** EVENTS ****");
        getAllEvents();
        System.out.println("\n**** SPEAKERS ****");
        getAllSpeakers();
        
        System.out.println("\n------------------ GET BY ID ------------------");
        System.out.println("**** ROOM 1 ****");
        getRoom(1);
        System.out.println("\n**** EVENT 1 ****");
        getEvent(1);
        System.out.println("\n**** SPEAKER 1 ****");
        getSpeaker(1);
        
        System.out.println("\n---------- GET capacity BY room ID -------------");
        getCapacityByRoomId(1);
        
        System.out.println("\n----------- GET events BY date ------------------");
        getEventsByDate("2025-05-20");

    }

    // ============ methods ============

    // **** Getters - ALL ****

    private void getAllRooms() {
    	webClient.get()
        .uri(SERVER_URI + "/rooms")
        .accept(MediaType.APPLICATION_JSON) 
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<Lokaal>>() {})
        .doOnNext(list -> list.forEach(this::printLokaalData))
        .block();
    }

    private void getAllEvents() {
    	webClient.get()
        .uri(SERVER_URI + "/events")
        .accept(MediaType.APPLICATION_JSON) 
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<Evenement>>() {})
        .doOnNext(list -> list.forEach(this::printEventData))
        .block();
    }

    private void getAllSpeakers() {
        webClient.get().uri(SERVER_URI + "/speakers").retrieve()
            .bodyToFlux(Spreker.class)
            .doOnNext(this::printSprekerData)
            .blockLast();
    }

    // **** Getters - BY ID  ****

    private void getRoom(int number) {
        webClient.get()
            .uri(SERVER_URI + "/rooms/" + number)
            .retrieve()
            .bodyToMono(Lokaal.class)
            .doOnSuccess(this::printLokaalData)
            .block();
    }

    private void getEvent(int number) {
        webClient.get()
            .uri(SERVER_URI + "/events/" + number)
            .retrieve()
            .bodyToMono(Evenement.class)
            .doOnSuccess(this::printEventData)
            .block();
    }

    private void getSpeaker(int number) {
        webClient.get()
            .uri(SERVER_URI + "/speakers/" + number)
            .retrieve()
            .bodyToMono(Spreker.class)
            .doOnSuccess(this::printSprekerData)
            .block();
    }

    // **** GET capacity By room id ****
    
    private void getCapacityByRoomId(int number) {
        webClient.get()
            .uri(SERVER_URI + "/rooms/" + number + "/capacity")
            .retrieve()
            .bodyToMono(Integer.class)
            .doOnSuccess(cap -> System.out.printf("Room ID=%d has capacity=%d%n", number, cap))
            .block();
    }
   
    
	 // **** GET Events by Date ****
	    private void getEventsByDate(String date) {
	        webClient.get()
	            .uri(SERVER_URI + "/events/bydate/" + date)
	            .accept(MediaType.APPLICATION_JSON)
	            .retrieve()
	            .bodyToMono(new ParameterizedTypeReference<List<Evenement>>() {})
	            .doOnNext(list -> {
	                System.out.printf("Events on %s:%n", date);
	                list.forEach(this::printEventData);
	            })
	            .block();
	    }
	    
    // **** PRINTS ****

    private void printLokaalData(Lokaal lokaal) {
        if (lokaal != null)
            System.out.printf("ID=%s, Name=%s, Capacity=%s%n",
                    lokaal.getId(), lokaal.getNaam(), lokaal.getCapaciteit());
    }

    private void printEventData(Evenement event) {
        if (event != null)
            System.out.printf("ID=%s, Name=%s, Datum=%s%n",
                    event.getId(), event.getNaam(), event.getDatum());
    }

    private void printSprekerData(Spreker spreker) {
        if (spreker != null)
            System.out.printf("ID=%s, Firstname=%s Name=%s, Email=%s%n",
                    spreker.getId(), spreker.getVoornaam(), spreker.getNaam(), spreker.getEmail());
    }
}

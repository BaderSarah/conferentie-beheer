package service;

import java.util.List;

import domein.Lokaal;

public interface LokaalService {

	Lokaal createLokaal(Lokaal lokaal);

    void deleteLokaal(Long id); 
    
    Lokaal getLokaal(Long id); 
    
    List<Lokaal> getAllLokaals();
    
    int getCapaciteitVanLokaal(Long id);
    
    Lokaal updateLokaal(Long id, Lokaal lokaalDetails); 

}

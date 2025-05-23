package service;

import java.util.List;

import domein.Spreker;

public interface SprekerService {

    void deleteSpreker(Long id); 
    
    Spreker createSpreker(Spreker spreker);
    
    Spreker getSpreker(Long id); 
    
    List<Spreker> getAllSprekers(); 
}

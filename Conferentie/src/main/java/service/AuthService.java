package service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domein.Gebruiker;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import repository.GebruikerRepository;
import validator.OnCreate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GebruikerRepository repo;
    private final Validator validator;

    @Transactional
    public Gebruiker registreer(Gebruiker nieuw) {

        // ✦ alleen OnCreate valideren
        Set<ConstraintViolation<Gebruiker>> fouten =
                validator.validate(nieuw, OnCreate.class);

        if (!fouten.isEmpty()) {
            throw new ConstraintViolationException(fouten);
        }

        return repo.save(nieuw);
    }
}

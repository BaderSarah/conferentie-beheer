package validator;

import java.time.LocalDate;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domein.Evenement;

public class EvenementValidator implements Validator {

    private static final LocalDate CONFERENTIE_START = LocalDate.of(2025, 5, 1);
    private static final LocalDate CONFERENTIE_EIND = LocalDate.of(2025, 5, 5);

    @Override
    public boolean supports(Class<?> clazz) {
        return Evenement.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Evenement evenement = (Evenement) target;

        LocalDate datum = evenement.getDatum();
        if (datum != null && (datum.isBefore(CONFERENTIE_START) || datum.isAfter(CONFERENTIE_EIND))) {
            errors.rejectValue("datum", "event.err.date.range", "Datum moet binnen de conferentieperiode vallen.");
        }

        if (evenement.getBegintijdstip() != null && evenement.getEindtijdstip() != null) {
            if (evenement.getEindtijdstip().isBefore(evenement.getBegintijdstip())) {
                errors.rejectValue("eindtijdstip", "event.err.end.before.start", "Eindtijdstip moet na begintijdstip liggen.");
            }
        }

        if (evenement.getSprekers() == null || evenement.getSprekers().isEmpty()) {
            errors.rejectValue("sprekers", "event.err.speakers.required", "Er moet minstens één spreker zijn.");
        } else if (evenement.getSprekers().size() > 3) {
            errors.rejectValue("sprekers", "event.err.speakers.max", "Maximum 3 sprekers toegestaan.");
        }
    }
}

package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class IsAfterValidator implements ConstraintValidator<IsAfter, LocalDate> {

    private String currentDate;

    @Override
    public void initialize(IsAfter constraintAnnotation) {
        currentDate = constraintAnnotation.current();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        String[] splitDate = currentDate.split("-");
        return localDate.isAfter(LocalDate.of(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2])));
    }
}

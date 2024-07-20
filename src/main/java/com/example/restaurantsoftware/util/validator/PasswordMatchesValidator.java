package com.example.restaurantsoftware.util.validator;

import com.example.restaurantsoftware.model.dto.RegisterUserDto;
import com.example.restaurantsoftware.util.annotation.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.stereotype.Component;


@Component
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterUserDto> {

    private String message;

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(RegisterUserDto dto, ConstraintValidatorContext context) {
        if (dto.getPassword() == null || dto.getConfirmPassword() == null) {
            return true;
        }

        boolean isValid = dto.getPassword().equals(dto.getConfirmPassword());

        if (!isValid) {
            context.unwrap(HibernateConstraintValidatorContext.class)
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return isValid;
    }
}

package ru.pnapreenko.blogengine.api.components;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.api.utils.ErrorsValidation;
import ru.pnapreenko.blogengine.config.ConfigStrings;
import ru.pnapreenko.blogengine.services.ImageStorageService;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Component
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, Object> errors = new HashMap<>();

        e.getConstraintViolations()
                .forEach(constraint -> errors.put(constraint.getPropertyPath().toString(), constraint.getMessage()));
        return APIResponse.error(ConfigStrings.VALIDATION_MESSAGE.getName(), errors);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, Object> errors = ErrorsValidation.getValidationErrors(e.getBindingResult());
        return APIResponse.error(ConfigStrings.VALIDATION_MESSAGE.getName(), errors);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return APIResponse.error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleStorageExceptions(ImageStorageService.StorageException e) {
        return APIResponse.error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handleStorageFileNotFound(ImageStorageService.StorageFileNotFoundException e) {
        return APIResponse.error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return APIResponse.error(new HashMap<>() {{
            put(e.getName(), String.format(ConfigStrings.ERROR_HANDLER_INVALID_OPTION.getName(),
                    e.getName(), e.getValue()));
        }});
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return APIResponse.error(new HashMap<>() {{
            put(e.getParameterName(), e.getMessage());
        }});
    }
}
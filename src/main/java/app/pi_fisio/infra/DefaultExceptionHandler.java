package app.pi_fisio.infra;

import app.pi_fisio.infra.exception.ExerciseNotFoundException;
import app.pi_fisio.infra.exception.InvalidGoogleTokenException;
import app.pi_fisio.infra.exception.NoJointIntensitiesException;
import app.pi_fisio.infra.exception.UserNotFoundException;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@Log4j2
@RestControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<DefaultErrorMessage> userNotFoundHandler(UserNotFoundException exception) {
        log.error("User not found: {}", exception.getMessage(), exception);
        DefaultErrorMessage error = new DefaultErrorMessage(HttpStatus.NOT_FOUND.value(), "User not found.", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ExerciseNotFoundException.class)
    public ResponseEntity<DefaultErrorMessage> exerciseNotFoundHandler(ExerciseNotFoundException exception) {
        log.error("Exercise not found: {}", exception.getMessage(), exception);
        DefaultErrorMessage error = new DefaultErrorMessage(HttpStatus.NOT_FOUND.value(), "Exercise not found.", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(NoJointIntensitiesException.class)
    public ResponseEntity<DefaultErrorMessage> noJointIntensitiesHandler(NoJointIntensitiesException exception) {
        log.error("Exercises not found (no joint intensities): {}", exception.getMessage(), exception);
        DefaultErrorMessage error = new DefaultErrorMessage(HttpStatus.NOT_FOUND.value(), "Exercises not found.", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidGoogleTokenException.class)
    public ResponseEntity<DefaultErrorMessage> invalidGoogleTokenHandler(InvalidGoogleTokenException exception) {
        log.warn("Invalid Google token: {}", exception.getMessage());
        DefaultErrorMessage error = new DefaultErrorMessage(HttpStatus.UNAUTHORIZED.value(), "Invalid token.", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<DefaultErrorMessage> jwtDecodeHandler(JWTDecodeException exception) {
        log.warn("JWT decode error: {}", exception.getMessage());
        DefaultErrorMessage error = new DefaultErrorMessage(HttpStatus.UNAUTHORIZED.value(), "Invalid token.", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(JWTCreationException.class)
    public ResponseEntity<DefaultErrorMessage> jwtCreationHandler(JWTCreationException exception) {
        log.error("Error creating JWT: {}", exception.getMessage(), exception);
        DefaultErrorMessage error = new DefaultErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Token could not be created.", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<DefaultErrorMessage> tokenExpiredHandler(TokenExpiredException exception) {
        log.warn("Token expired: {}", exception.getMessage());
        DefaultErrorMessage error = new DefaultErrorMessage(HttpStatus.UNAUTHORIZED.value(), "Token expired.", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<DefaultErrorMessage> authenticationHandler(AuthenticationException exception) {
        log.warn("Authentication error: {}", exception.getMessage());
        DefaultErrorMessage error = new DefaultErrorMessage(HttpStatus.UNAUTHORIZED.value(), "The provided credentials are incorrect.", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}

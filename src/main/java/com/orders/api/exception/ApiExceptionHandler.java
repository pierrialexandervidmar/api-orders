package com.orders.api.exception;


import com.orders.api.entity.exception.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Classe responsável por tratar globalmente as exceções lançadas pelos controladores da aplicação.
 *
 * <p>Utiliza a anotação {@link RestControllerAdvice} para interceptar exceções e retornar uma resposta
 * padronizada com o objeto {@link ErrorMessage} e o respectivo status HTTP.</p>
 *
 * <p>Esta classe centraliza o tratamento de erros comuns, como:
 * <ul>
 *   <li>{@link EntityNotFoundException} - quando uma entidade não é encontrada (HTTP 404).</li>
 *   <li>{@code UserNameUniqueViolationException} - quando há violação de unicidade de usuário (HTTP 409).</li>
 *   <li>{@link MethodArgumentNotValidException} - quando há falhas de validação nos parâmetros (HTTP 422).</li>
 * </ul>
 * </p>
 * @author Pierri Alexander Vidmar
 * @since 05/2025
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /**
     * Manipula exceções do tipo {@link EntityNotFoundException}.
     *
     * <p>Retorna uma resposta com status HTTP 404 (Not Found) e uma mensagem de erro detalhada.</p>
     *
     * @param ex      A exceção lançada.
     * @param request A requisição HTTP que gerou a exceção.
     * @return Um {@link ResponseEntity} contendo {@link ErrorMessage} com detalhes do erro.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFoundException(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    /**
     * Manipula exceções do tipo {@link MethodArgumentNotValidException}.
     *
     * <p>Retorna uma resposta com status HTTP 422 (Unprocessable Entity) contendo as mensagens
     * de validação associadas aos campos que falharam.</p>
     *
     * @param ex      A exceção lançada contendo os erros de validação.
     * @param request A requisição HTTP que gerou a exceção.
     * @param result  O resultado da validação contendo os detalhes dos erros.
     * @return Um {@link ResponseEntity} contendo {@link ErrorMessage} com detalhes dos campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> MethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request,
            BindingResult result
    ) {
        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) inválido(s)", result));
    }
}

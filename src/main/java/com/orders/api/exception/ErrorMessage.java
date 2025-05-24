package com.orders.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável por representar a estrutura padronizada de mensagens de erro
 * retornadas pela API em casos de exceções ou falhas de validação.
 *
 * <p>Inclui informações como o caminho da requisição, método HTTP, código de status,
 * descrição do status, mensagem de erro e, opcionalmente, detalhes dos campos inválidos.</p>
 *
 * @author Pierri Alexander Vidmar
 * @since 05/2025
 */
public class ErrorMessage {

    /**
     * Caminho (URI) da requisição onde o erro ocorreu.
     */
    private String path;

    /**
     * Método HTTP utilizado na requisição (ex.: GET, POST).
     */
    private String method;

    /**
     * Código de status HTTP associado ao erro.
     */
    private int status;

    /**
     * Texto descritivo associado ao status HTTP.
     */
    private String statusText;

    /**
     * Mensagem explicativa sobre o erro ocorrido.
     */
    private String message;

    /**
     * Mapa contendo os erros de validação, com o nome do campo e a mensagem correspondente.
     * Este campo é opcional e será incluído apenas quando houver erros de validação.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    /**
     * Construtor padrão sem argumentos.
     * Necessário para frameworks de serialização/deserialização.
     */
    public ErrorMessage() {
    }

    /**
     * Construtor para criação de uma mensagem de erro sem detalhes de validação.
     *
     * @param request Objeto que contém informações sobre a requisição HTTP.
     * @param status  Status HTTP a ser retornado.
     * @param message Mensagem descritiva do erro.
     */
    public ErrorMessage(HttpServletRequest request, HttpStatus status, String message) {
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
        this.message = message;
    }

    /**
     * Construtor para criação de uma mensagem de erro com detalhes sobre falhas de validação.
     *
     * @param request Objeto que contém informações sobre a requisição HTTP.
     * @param status  Status HTTP a ser retornado.
     * @param message Mensagem descritiva do erro.
     * @param result  Resultado da validação contendo os erros dos campos.
     */
    public ErrorMessage(HttpServletRequest request, HttpStatus status, String message, BindingResult result) {
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
        this.message = message;
        addErrors(result);
    }

    /**
     * Método auxiliar responsável por extrair os erros de validação do {@link BindingResult}
     * e adicioná-los ao mapa {@code errors}, associando o nome do campo à mensagem de erro.
     *
     * @param result Resultado da validação contendo os erros.
     */
    private void addErrors(BindingResult result) {
        this.errors = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            this.errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "path='" + path + '\'' +
                ", method='" + method + '\'' +
                ", status=" + status +
                ", statusText='" + statusText + '\'' +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                '}';
    }
}

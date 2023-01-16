package br.com.hubia.correios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.SERVICE_UNAVAILABLE, reason="This service is being installed, please wait a few moments.")
public class NotReadyException extends RuntimeException {
}

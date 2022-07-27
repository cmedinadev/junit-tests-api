package dev.cmedina.test.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import dev.cmedina.test.api.exceptions.DataIntegratyViolationException;
import dev.cmedina.test.api.exceptions.ObjectNotFoundException;
import dev.cmedina.test.api.exceptions.ResourceExceptionHandler;
import dev.cmedina.test.api.exceptions.StandardError;

class ResourceExceptionHandlerTest {

	@InjectMocks
	private ResourceExceptionHandler exceptionHandler;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void whenObjectNotFound() {
		ResponseEntity<StandardError> response = exceptionHandler.objectNotFound(
				new ObjectNotFoundException(), new MockHttpServletRequest());
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(StandardError.class, response.getBody().getClass());
		assertEquals("Objeto não encontrado.", response.getBody().getError());
		assertEquals(404, response.getBody().getStatus());
	}

	@Test
	void whenDataIntegrityViolationException() {
		ResponseEntity<StandardError> response = exceptionHandler.dataIntegrityViolationException(
				new DataIntegratyViolationException("E-mail já cadastrado."), new MockHttpServletRequest());
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(StandardError.class, response.getBody().getClass());
		assertEquals("E-mail já cadastrado.", response.getBody().getError());
		assertEquals(400, response.getBody().getStatus());
	}

}

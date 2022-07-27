package dev.cmedina.test.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.cmedina.test.api.controller.UserController;
import dev.cmedina.test.api.domain.User;
import dev.cmedina.test.api.domain.dto.UserDTO;
import dev.cmedina.test.api.services.UserService;

@SpringBootTest
class UserControllerTest {

	private static final int ID = 1;	
	private static final String NAME = "Medina";
	private static final String EMAIL = "email@gmail.com";
	private static final String PASSWORD = "1234";
	
	@InjectMocks
	private UserController userController;
	
	@Mock
	private UserService userService;
	
	@Mock
	private ModelMapper mapper;
	
	private User user;
	
	private UserDTO userDTO;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		initUser();
	}

	@Test
	void whenFindByIdthenReturnSuccess() {
		when(userService.findById(anyInt())).thenReturn(user);
		when(mapper.map(any(), any())).thenReturn(userDTO);
		
		ResponseEntity<UserDTO> response = userController.findById(ID);
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(UserDTO.class, response.getBody().getClass());
		
		assertEquals(ID, response.getBody().getId());
		
	}

	@Test
	void whenFindAllThenReturnAListOfUserDTO() {
		when(userService.findAll()).thenReturn(List.of(user));
		when(mapper.map(any(), any())).thenReturn(userDTO);
		
		ResponseEntity<List<UserDTO>> response = userController.findAll();

		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(ArrayList.class, response.getBody().getClass());
		assertEquals(UserDTO.class, response.getBody().get(0).getClass());
		
		assertEquals(ID, response.getBody().get(0).getId());
		

	}

	@Test
	void whenCreateThenReturnCreated() {
		when(userService.create(any())).thenReturn(user);
		ResponseEntity<UserDTO> response = userController.create(userDTO);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertNull(response.getBody());
		assertNotNull(response.getHeaders().get("location"));	
	}

	@Test
	void whenUpdateThenReturnSuccess() {
		when(userService.update(userDTO)).thenReturn(user);
		when(mapper.map(any(), any())).thenReturn(userDTO);
		
		ResponseEntity<UserDTO> response = userController.update(ID, userDTO);
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(UserDTO.class, response.getBody().getClass());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		assertEquals(ID, response.getBody().getId());
		
	}

	@Test
	void whenDeleteReturnSuccess() {
		doNothing().when(userService).delete(ID);
		
		ResponseEntity<UserDTO> response = userController.delete(ID);
		
		assertNotNull(response);
		assertEquals(ResponseEntity.class, response.getClass());
		
		verify(userService, times(1)).delete(anyInt());
		
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	private void initUser() {
		user = new User(ID, NAME, EMAIL,PASSWORD);
		userDTO = new UserDTO(ID, NAME, EMAIL,PASSWORD);
	}

}

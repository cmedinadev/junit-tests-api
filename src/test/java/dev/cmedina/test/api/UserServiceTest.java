package dev.cmedina.test.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import dev.cmedina.test.api.domain.User;
import dev.cmedina.test.api.domain.dto.UserDTO;
import dev.cmedina.test.api.exceptions.DataIntegratyViolationException;
import dev.cmedina.test.api.exceptions.ObjectNotFoundException;
import dev.cmedina.test.api.repository.UserRepository;
import dev.cmedina.test.api.services.UserService;

@SpringBootTest
public class UserServiceTest {

	private static final int ID = 1;	
	private static final String NAME = "Medina";
	private static final String EMAIL = "email@gmail.com";
	private static final String PASSWORD = "1234";

	@InjectMocks
	private UserService userService;
	
	@Mock
    private ModelMapper mapper;
    
    @Mock
	private UserRepository repository;

	private User user;
	
	private UserDTO userDTO;
	
	private Optional<User> optionalUser;
	
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		initUser();
	}	
	
	@Test
	void whenFindByIdThenReturnAnUserInstance() {
		when(repository.findById(anyInt())).thenReturn(optionalUser);
		User response = userService.findById(1);
		assertNotNull(response);
		assertEquals(User.class, response.getClass());
		assertEquals(1, response.getId());
	}

	@Test
	void whenFindByIdThenReturnObjectNotFoundException() {
		when(repository.findById(anyInt())).thenThrow(new ObjectNotFoundException());
		try {
			userService.findById(1);
		} catch (Exception e) {
			assertEquals(e.getClass(), ObjectNotFoundException.class);
			assertEquals("Objeto não encontrado.", e.getMessage());
		}
	}

	
	@Test
	void whenFindAllAndReturnAnListOfUsers() {
		when(userService.findAll()).thenReturn(List.of(user));
		
		List<User> response = userService.findAll();
		assertNotNull(response);
		assertEquals(1, response.size());
		assertEquals(User.class, response.get(0).getClass());
		
	}

	@Test
	void whenCreateThenReturnSuccess() {
		
		when(repository.save(any())).thenReturn(user);
		
		User user = userService.create(userDTO);
		assertNotNull(user);
		assertEquals(User.class, user.getClass());
		assertEquals(ID, user.getId());
				
	}


	@Test
	void whenCreateThenReturnAnDataIntegrityViolationException() {
		
		when(repository.findByEmail(anyString())).thenReturn(optionalUser);
		
		try {
			optionalUser.get().setId(2);
			userService.create(userDTO);
		} catch (Exception e) {
			assertEquals(DataIntegratyViolationException.class, e.getClass());
		}
				
	}
	
	@Test
	void whenUpdateThenReturnSuccess() {
		
		when(repository.save(any())).thenReturn(user);
		
		User user = userService.create(userDTO);
		assertNotNull(user);
		assertEquals(User.class, user.getClass());
		assertEquals(ID, user.getId());
				
	}

	@Test
	void whenUpdateThenReturnAnDataIntegrityViolationException() {
		
		when(repository.findByEmail(anyString())).thenReturn(optionalUser);
		
		try {
			optionalUser.get().setId(2);
			userService.update(userDTO);
		} catch (Exception e) {
			assertEquals(DataIntegratyViolationException.class, e.getClass());
		}
				
	}
	
	@Test
	void deleteWithSuccess() {
		when(repository.findById(anyInt())).thenReturn(optionalUser);
		doNothing().when(repository).deleteById(anyInt());
		userService.delete(ID);
		verify(repository, times(1)).deleteById(anyInt());
	}
	
	@Test
	void deleteWithObjectNotFoundException() {
		when(repository.findById(anyInt())).thenThrow(new ObjectNotFoundException());
		try {
			userService.delete(ID);
		} catch(Exception e) {
			assertEquals(ObjectNotFoundException.class, e.getClass());
		}
	}
	
	private void initUser() {
		user = new User(ID, NAME, EMAIL,PASSWORD);
		userDTO = new UserDTO(ID, NAME, EMAIL,PASSWORD);
		optionalUser = Optional.of(user);
	}

}

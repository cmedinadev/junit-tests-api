package dev.cmedina.test.api.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.cmedina.test.api.domain.User;
import dev.cmedina.test.api.domain.dto.UserDTO;
import dev.cmedina.test.api.exceptions.DataIntegratyViolationException;
import dev.cmedina.test.api.exceptions.ObjectNotFoundException;
import dev.cmedina.test.api.repository.UserRepository;

@Service
public class UserService {
	
    @Autowired
    private ModelMapper mapper;
    
	@Autowired
	private UserRepository repository;

	public User findById(Integer id) {
		Optional<User> user = repository.findById(id);
		return user.orElseThrow(ObjectNotFoundException::new);
	}

	public List<User> findAll() {
		return repository.findAll();
	}

    public User create(UserDTO obj) {
        findByEmail(obj);
        return repository.save(mapper.map(obj, User.class));
    }

    public User update(UserDTO obj) {
        findByEmail(obj);
        return repository.save(mapper.map(obj, User.class));
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    private void findByEmail(UserDTO obj) {
        Optional<User> user = repository.findByEmail(obj.getEmail());
        if(user.isPresent() && !user.get().getId().equals(obj.getId())) {
            throw new DataIntegratyViolationException("E-mail já cadastrado no sistema");
        }
    }
}

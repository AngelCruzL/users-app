package dev.angelcruzl.users.app.service.impl;

import dev.angelcruzl.users.app.dto.UserCreateDto;
import dev.angelcruzl.users.app.dto.UserPasswordDto;
import dev.angelcruzl.users.app.dto.UserResponseDto;
import dev.angelcruzl.users.app.dto.UserUpdateDto;
import dev.angelcruzl.users.app.entity.User;
import dev.angelcruzl.users.app.exception.EmailAlreadyTakenException;
import dev.angelcruzl.users.app.exception.ResourceNotFoundException;
import dev.angelcruzl.users.app.exception.UsernameAlreadyTakenException;
import dev.angelcruzl.users.app.mapper.UserMapper;
import dev.angelcruzl.users.app.repository.UserRepository;
import dev.angelcruzl.users.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    private UserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        return repository.findAllByActiveTrue().stream().map(mapper::toUserResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        User user = findByIdOrThrow(id);
        return mapper.toUserResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserCreateDto userDto) {
        Optional<User> userOptional = repository.findByEmail(userDto.getEmail());
        if (userOptional.isPresent()) throw new EmailAlreadyTakenException("Email already in use");
        userOptional = repository.findByUsername(userDto.getUsername());
        if (userOptional.isPresent()) throw new UsernameAlreadyTakenException("Username already in use");

        User user = mapper.toUser(userDto);
        return mapper.toUserResponseDto(repository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateDto userDto) {
        User user = findByIdOrThrow(id);
        if (userDto.getFirstName() != null) user.setFirstName(userDto.getFirstName());
        if (userDto.getLastName() != null) user.setLastName(userDto.getLastName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getUsername() != null) user.setUsername(userDto.getUsername());
        return mapper.toUserResponseDto(repository.save(user));
    }

    @Override
    @Transactional
    public void updatePassword(Long id, UserPasswordDto userDto) {
        User user = findByIdOrThrow(id);
        user.setPassword(userDto.getPassword());
        repository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = findByIdOrThrow(id);
        user.setActive(false);
        repository.save(user);
    }

    private User findByIdOrThrow(Long id) {
        return repository.findByIdAndActiveTrue(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

}

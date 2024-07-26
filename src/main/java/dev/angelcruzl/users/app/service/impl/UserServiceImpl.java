package dev.angelcruzl.users.app.service.impl;

import dev.angelcruzl.users.app.dto.UserCreateDto;
import dev.angelcruzl.users.app.dto.UserPasswordDto;
import dev.angelcruzl.users.app.dto.UserResponseDto;
import dev.angelcruzl.users.app.dto.UserUpdateDto;
import dev.angelcruzl.users.app.entity.Role;
import dev.angelcruzl.users.app.entity.User;
import dev.angelcruzl.users.app.exception.FieldAlreadyTakenException;
import dev.angelcruzl.users.app.exception.ResourceNotFoundException;
import dev.angelcruzl.users.app.exception.WrongFieldException;
import dev.angelcruzl.users.app.exception.WrongPermissionsException;
import dev.angelcruzl.users.app.mapper.UserMapper;
import dev.angelcruzl.users.app.models.IUser;
import dev.angelcruzl.users.app.repository.RoleRepository;
import dev.angelcruzl.users.app.repository.UserRepository;
import dev.angelcruzl.users.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.angelcruzl.users.app.constants.ErrorStatusCodeConstants.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    private RoleRepository roleRepository;

    private UserMapper mapper;

    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> findAll(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable).map(mapper::toUserResponseDto);
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
        if (userOptional.isPresent())
            throw new FieldAlreadyTakenException("Email", userDto.getEmail(), EMAIL_ALREADY_TAKEN);
        userOptional = repository.findByUsername(userDto.getUsername());
        if (userOptional.isPresent())
            throw new FieldAlreadyTakenException("Username", userDto.getUsername(), USERNAME_ALREADY_TAKEN);

        User user = mapper.toUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getUserRoles(userDto));
        return mapper.toUserResponseDto(repository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateDto userDto) {
        User user = findByIdOrThrow(id);
        Optional<User> userOptional = repository.findByEmail(userDto.getEmail());
        if (userOptional.isPresent() && !userOptional.get().getId().equals(user.getId()))
            throw new FieldAlreadyTakenException("Email", userDto.getEmail(), EMAIL_ALREADY_TAKEN);
        userOptional = repository.findByUsername(userDto.getUsername());
        if (userOptional.isPresent() && !userOptional.get().getId().equals(user.getId()))
            throw new FieldAlreadyTakenException("Username", userDto.getUsername(), USERNAME_ALREADY_TAKEN);
        if (!hasPermissionToUpdate(user))
            throw new WrongPermissionsException("You don't have permission to update this user");

        if (userDto.getFirstName() != null) user.setFirstName(userDto.getFirstName());
        if (userDto.getLastName() != null) user.setLastName(userDto.getLastName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getUsername() != null) user.setUsername(userDto.getUsername());
        if (user.isAdmin()) user.setRoles(getUserRoles(userDto));

        return mapper.toUserResponseDto(repository.save(user));
    }

    @Override
    @Transactional
    public void updatePassword(Long id, UserPasswordDto userDto) {
        User user = findByIdOrThrow(id);
        if (!hasPermissionToUpdate(user))
            throw new WrongPermissionsException("You don't have permission to update this user");
        if (!passwordEncoder.matches(userDto.getCurrentPassword(), user.getPassword()))
            throw new WrongFieldException("Current password is incorrect", WRONG_PASSWORD);

        user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
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
        return repository.findByIdAndActiveTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id, USER_NOT_FOUND));
    }

    private List<Role> getUserRoles(IUser user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        optionalRoleUser.ifPresent(roles::add);

        if (user.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }

        return roles;
    }

    private boolean hasPermissionToUpdate(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        return isAdmin || authentication.getName().equals(user.getUsername());
    }

}

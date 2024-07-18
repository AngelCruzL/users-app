package dev.angelcruzl.users.app.service;

import dev.angelcruzl.users.app.dto.UserCreateDto;
import dev.angelcruzl.users.app.dto.UserPasswordDto;
import dev.angelcruzl.users.app.dto.UserResponseDto;
import dev.angelcruzl.users.app.dto.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserResponseDto> findAll(Pageable pageable);

    UserResponseDto findById(Long id);

    UserResponseDto createUser(UserCreateDto userDto);

    UserResponseDto updateUser(Long id, UserUpdateDto userDto);

    void updatePassword(Long id, UserPasswordDto userDto);

    void deleteById(Long id);

}

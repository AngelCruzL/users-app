package dev.angelcruzl.users.app.mapper;

import dev.angelcruzl.users.app.dto.UserCreateDto;
import dev.angelcruzl.users.app.dto.UserResponseDto;
import dev.angelcruzl.users.app.dto.UserUpdateDto;
import dev.angelcruzl.users.app.entity.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {

    private ModelMapper modelMapper;

    public UserResponseDto toUserResponseDto(User user) {
        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
        userResponseDto.setAdmin(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")));
        return userResponseDto;
    }

    public User toUser(UserCreateDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public User toUser(UserUpdateDto userDto) {
        modelMapper.typeMap(UserUpdateDto.class, User.class).addMappings(mapper -> mapper.skip(User::setPassword));
        return modelMapper.map(userDto, User.class);
    }

}

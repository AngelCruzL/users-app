package dev.angelcruzl.users.app.controller;

import dev.angelcruzl.users.app.dto.UserCreateDto;
import dev.angelcruzl.users.app.dto.UserPasswordDto;
import dev.angelcruzl.users.app.dto.UserResponseDto;
import dev.angelcruzl.users.app.dto.UserUpdateDto;
import dev.angelcruzl.users.app.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin({"http://localhost:4200"})
@AllArgsConstructor
public class UserController {

    private UserService service;

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserCreateDto userDto) {
        UserResponseDto createUser = service.createUser(userDto);
        return new ResponseEntity<>(createUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserUpdateDto userDto) {
        return ResponseEntity.ok(service.updateUser(id, userDto));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable("id") Long id, @RequestBody @Valid UserPasswordDto passwordDto) {
        service.updatePassword(id, passwordDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

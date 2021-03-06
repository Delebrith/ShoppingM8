package edu.pw.shoppingm8.user.api;

import edu.pw.shoppingm8.authentication.AuthenticationService;
import edu.pw.shoppingm8.user.UserService;
import edu.pw.shoppingm8.user.api.dto.FcmTokenDto;
import edu.pw.shoppingm8.user.api.dto.UserDto;
import edu.pw.shoppingm8.user.api.dto.UserSearchDto;
import edu.pw.shoppingm8.user.db.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @ApiOperation(value = "Get user info", nickname = "get user info", notes = "",
            authorizations = {@Authorization(value = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "If valid credentials were provided", response = InputStreamResource.class),
            @ApiResponse(code = 400, message = "If invalid data was provided")})
    @GetMapping
    ResponseEntity<Iterable<UserDto>> getUsers(UserSearchDto searchDto) {
        Iterable<UserDto> userDtos = userService.getUsers(searchDto).map(UserDto::of);
        return ResponseEntity.ok(userDtos);
    }


    @ApiOperation(value = "Get user info", nickname = "get user info", notes = "",
            authorizations = {@Authorization(value = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "If valid credentials were provided and user has a picture",
                response = InputStreamResource.class),
            @ApiResponse(code = 204, message = "If valid credentials were provided and user has no picture"),
            @ApiResponse(code = 400, message = "If invalid data was provided")})
    @GetMapping("/{id}/picture")
    ResponseEntity<InputStreamResource> getProfilePicture(@PathVariable Long id) {
        User user = userService.getUser(id);
        byte[] picture = user.getProfilePicture();
        if (picture == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(new ByteArrayInputStream(user.getProfilePicture())));
    }

    @ApiOperation(value = "Get user info", nickname = "get user info", notes = "",
            authorizations = {@Authorization(value = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "If valid credentials were provided and user has a picture",
                    response = InputStreamResource.class),
            @ApiResponse(code = 204, message = "If valid credentials were provided and user has no picture"),
            @ApiResponse(code = 400, message = "If invalid data was provided")})
    @PostMapping("/me/fcm-token")
    ResponseEntity<Void> updateFmcToken(@RequestBody @Valid FcmTokenDto tokenDto) {
        User user = authenticationService.getAuthenticatedUser();
        userService.updateFmcToken(user, tokenDto.getToken());
        return ResponseEntity.noContent().build();
    }
}

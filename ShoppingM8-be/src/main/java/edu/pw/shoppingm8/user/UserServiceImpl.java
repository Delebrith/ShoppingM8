package edu.pw.shoppingm8.user;

import edu.pw.shoppingm8.authentication.api.dto.UserRegistrationDto;
import edu.pw.shoppingm8.authentication.social.SocialMediaProfileDto;
import edu.pw.shoppingm8.user.api.dto.UserSearchDto;
import edu.pw.shoppingm8.user.db.User;
import edu.pw.shoppingm8.user.db.UserRepository;
import edu.pw.shoppingm8.user.db.UserSpecification;
import edu.pw.shoppingm8.user.exception.EmailAlreadyUsedException;
import edu.pw.shoppingm8.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final byte[] secretKey;

    public UserServiceImpl(UserRepository userRepository,
                           @Value("${shoppingM8.security.secretKey}") String secretKey) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.secretKey = secretKey.getBytes();
    }

    @Override
    public User register(UserRegistrationDto registrationDto, byte[] profilePicture) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new EmailAlreadyUsedException();
        }
        User registered = User.builder()
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .name(registrationDto.getName())
                .profilePicture(profilePicture)
                .build();
        return userRepository.save(registered);
    }

    @Override
    public User register(SocialMediaProfileDto socialMediaProfile, byte[] profilePicture) {
        if (userRepository.existsByEmail(socialMediaProfile.getEmail())) {
            throw new EmailAlreadyUsedException();
        }
        User registered = User.builder()
                .email(socialMediaProfile.getEmail())
                .name(socialMediaProfile.getName())
                .profilePicture(profilePicture)
                .build();
        return userRepository.save(registered);
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Optional<User> getUserOptionalByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void deregister(User user) {
        userRepository.delete(user);
    }

    @Override
    public Page<User> getUsers(UserSearchDto searchDto) {
        return userRepository
                .findAll(new UserSpecification(searchDto), PageRequest.of(searchDto.getPageNo(), searchDto.getPageSize()));
    }

    @Override
    public void updateFmcToken(User user, String token) {
        user.setFmcToken(token);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}

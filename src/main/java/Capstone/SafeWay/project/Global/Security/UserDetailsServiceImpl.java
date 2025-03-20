package Capstone.SafeWay.project.Global.Security;

import Capstone.SafeWay.project.User.Exception.UserNotFoundException;
import Capstone.SafeWay.project.User.UserEntity;
import Capstone.SafeWay.project.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException{
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return new UserDetailsImpl(userEntity);
    }
}

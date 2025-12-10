package com.lapoit.api.jwt;

import com.lapoit.api.domain.User;
import com.lapoit.api.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user=userMapper.findByUserId(userId);

        if (user == null) {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다: " + userId);
        }

        return new CustomUserDetails(user);
    }

}

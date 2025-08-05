package ru.gubenko.server1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gubenko.server1.model.entity.Message;
import ru.gubenko.server1.model.entity.Role;
import ru.gubenko.server1.model.entity.User;
import ru.gubenko.server1.repository.MessageRepository;
import ru.gubenko.server1.repository.RoleRepository;
import ru.gubenko.server1.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,RoleRepository roleRepository,PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
        this.passwordEncoder=passwordEncoder;
        initRoles();
        initAdmin();
    }

    private void initRoles(){
        if(roleRepository.count()==0){

            Role userRole=new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);

            Role adminRole=new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }
    }

    private void initAdmin(){
        User admin=new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("1234"));
        admin.setRegistrationDate(LocalDateTime.now());
        admin.setEnabled(true);
        Role adminRole=roleRepository.findByName("ADMIN");
        admin.setRoles(Collections.singletonList(adminRole));
        userRepository.save(admin);
    }


    public void registerNewUser(String username,String password){
        if(userRepository.isPresent(username)){
            throw new RuntimeException("user already exists");
        }
        User user=new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRegistrationDate(LocalDateTime.now());
        user.setEnabled(true);

        Role userRole=roleRepository.findByName("USER");
        if(userRole==null) {
            userRole=new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }
        user.setRoles(Collections.singletonList(userRole));
        userRepository.save(user);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         return getUserOrThrow(username);
    }

    public User findByUserName(String username){
        User user=getUserOrThrow(username);
        return user;
    }

    public void updateUserContactInfo(String username,String email, String phone){
        User user=getUserOrThrow(username);
        if(email!=null && !email.isEmpty()){
            user.setEmail(email);
        }
        if(phone!=null && !phone.isEmpty()){
            user.setPhone(phone);
        }
        userRepository.save(user);
    }

    private User getUserOrThrow(String username){
       return userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("user not found"));
    }
}

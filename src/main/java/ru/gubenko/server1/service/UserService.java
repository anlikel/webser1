package ru.gubenko.server1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gubenko.server1.model.entity.Role;
import ru.gubenko.server1.model.entity.User;
import ru.gubenko.server1.repository.RoleRepository;
import ru.gubenko.server1.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

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
        if(userRepository.findByUsername(username).isPresent()){
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
        User user=userRepository.findByUsername(username).get();
        if(user==null){
            throw new UsernameNotFoundException("user not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, true, true,
                user.getRoles()
        );
    }

    public User findByUserName(String username){
        User user=userRepository.findByUsername(username).get();
        if(user==null){
            throw new UsernameNotFoundException("user not found");
        }
        return user;
    }

}

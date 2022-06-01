package com.assigment.suretime.securityJwt.models;

import com.assigment.suretime.dbSeeders.RoleSeeder;
import com.assigment.suretime.securityJwt.domain.models.ERole;
import com.assigment.suretime.securityJwt.domain.models.Role;
import com.assigment.suretime.securityJwt.domain.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@SpringBootTest
class RoleTest {

    @Autowired
    private RoleRepository roleRepository;
    private static final Logger log = LoggerFactory.getLogger(RoleSeeder.class);

    @Test
    void addAllRoles(){
        Arrays.stream(ERole.values()).forEach(
                eRole -> {
                    roleRepository
                            .findByName(eRole)
                            .ifPresentOrElse(role -> {
                                log.info("Role already exist: "+ role);
                            }, ()->{
                                log.info("Add new role: "+ eRole);
                                roleRepository.insert(new Role(eRole));
                            });
                }
        );

    }
    @Test
    void testAddRolesTwice(){
        addAllRoles();
        addAllRoles();
    }




}
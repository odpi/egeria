/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.init;

import org.odpi.openmetadata.userinterface.uichassis.springboot.domain.Role;
import org.odpi.openmetadata.userinterface.uichassis.springboot.domain.User;
import org.odpi.openmetadata.userinterface.uichassis.springboot.repository.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DemoInMemoryInitializingBean implements InitializingBean {

    private static final String FIRST_NAME = "FNAME";
    private static final String EMPLOYEE_STATUS = "EMPSTATUS";
    private static final String ANUAL_SALARY = "SALARY";
    private static final String DATE_OF_BIRTH = "DATE_OF_BIRTH";

    @Autowired
    private UserRepository userRepository;

    @Override
    public void afterPropertiesSet() {
        addUser("user",
                "John", "user",
                "/resources/img/user.png");
        addUser("admin",
                "Administrator", "admin",
                "/resources/img/admin.png");


        addUser("faith",
                "Faith Broker ", "admin",
                "/resources/img/faith.png");

        addUser("callie",
                "Callie Quartile ", "admin",
                "/resources/img/callie.png");

        /* In addition to original demo users, we add the full coco Pharmaceutical's set to support simple demos #1490 */
        addUser("zach", "Zach Now", "admin", "/resources/img/user.png" );
        addUser("steves", "Steve Starter", "admin", "/resources/img/user.png" );
        addUser("terri", "Terri Daring", "admin", "/resources/img/user.png" );
        addUser("tanyatidie", "Tanya Tide", "admin", "/resources/img/user.png" );
        addUser("pollytasker", "Polly Tasker", "admin", "/resources/img/user.png" );
        addUser("tessatube", "Tessa Tube", "admin", "/resources/img/user.png" );
        addUser("calliequartile", "Callie quartile", "admin", "/resources/img/user.png" );
        addUser("ivorpadlock", "Ivor Padlock", "admin", "/resources/img/user.png" );
        addUser("bobnitter", "Bob Nitter", "admin", "/resources/img/user.png" );
        addUser("faithbroker", "Faith Broker", "admin", "/resources/img/user.png" );
        addUser("sallycounter", "Sally Counter", "admin", "/resources/img/user.png" );
        addUser("lemmiestage", "Lemmie Stage", "admin", "/resources/img/user.png" );
        addUser("erinoverview", "Erin Overview", "admin", "/resources/img/user.png" );
        addUser("harryhopeful", "Harry Hopeful", "admin", "/resources/img/user.png" );
        addUser("garygeeke", "Gary Geeke", "admin", "/resources/img/user.png" );
        addUser("grantable", "Grant Able", "admin", "/resources/img/user.png" );
        addUser("robbierecords", "Robbie Records", "admin", "/resources/img/user.png" );
        addUser("reggiemint", "Reggie Mint", "admin", "/resources/img/user.png" );
        addUser("peterprofile", "Peter Profile", "admin", "/resources/img/user.png" );
        addUser("nancynoah", "Nancy Noah", "admin", "/resources/img/user.png" );
        addUser("sidneyseeker", "Sidney Seeker", "admin", "/resources/img/user.png" );
        addUser("tomtally", "tomtally", "admin", "/resources/img/user.png" );
        addUser("juliestitched", "juliestitched", "admin", "/resources/img/user.png" );
        addUser("designa", "designa", "admin", "/resources/img/user.png" );
        addUser("angelacummings", "angelacummings", "admin", "/resources/img/user.png" );
        addUser("jukeskeeper", "jukeskeeper", "admin", "/resources/img/user.png" );
        addUser("stewFaster", "stewFaster", "admin", "/resources/img/user.png" );
    }

    private void addUser(String username,
                         String name,
                         String password,
                         String avatarUrl) {

        List<String> roles = new ArrayList<>();

        if (username.equals("admin")){
            roles.add(Role.ADMIN.name());
        }else if(username.equals("faith")){
            roles.add(Role.HR.name());
        }else if(username.equals("callie")){
            roles.add(Role.DATA_ANALYST.name());
        }else {
            roles.add(Role.USER.name());
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRoles(roles);
        user.setName(name);
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
    }
}

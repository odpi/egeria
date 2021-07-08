/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth.db;

import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.db.domain.Role;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.db.domain.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(value = "authentication.source", havingValue = "db")
public class DemoInMemoryInitializingBean implements InitializingBean {

    private static final String PASSWORD = "secret";

    @Autowired
    private UserRepository userRepository;

    @Override
    public void afterPropertiesSet() {

        addUser("user","John", "user","/resources/img/user.png");
        addUser("admin","Administrator", "admin","/resources/img/admin.png", Role.COCO_PHARMA_ADMIN);
        addUser("garygeeke", "Gary Geeke", PASSWORD, "/resources/img/user.png",  Role.COCO_PHARMA_ADMIN);

        /* In addition to original demo users, we add the full coco Pharmaceutical's set to support simple demos #1490 */
        addUser("faith","Faith Broker ", PASSWORD,"/resources/img/faith.png");
        addUser("callie","Callie Quartile ", PASSWORD,"/resources/img/callie.png");
        addUser("zach", "Zach Now", PASSWORD, "/resources/img/user.png" );
        addUser("steves", "Steve Starter", PASSWORD, "/resources/img/user.png" );
        addUser("terri", "Terri Daring", PASSWORD, "/resources/img/user.png" );
        addUser("tanyatidie", "Tanya Tide", PASSWORD, "/resources/img/user.png" );
        addUser("pollytasker", "Polly Tasker", PASSWORD, "/resources/img/user.png" );
        addUser("tessatube", "Tessa Tube", PASSWORD, "/resources/img/user.png" );
        addUser("calliequartile", "Callie quartile", PASSWORD, "/resources/img/user.png" );
        addUser("ivorpadlock", "Ivor Padlock", PASSWORD, "/resources/img/user.png" );
        addUser("bobnitter", "Bob Nitter", PASSWORD, "/resources/img/user.png" );
        addUser("faithbroker", "Faith Broker", PASSWORD, "/resources/img/user.png" );
        addUser("sallycounter", "Sally Counter", PASSWORD, "/resources/img/user.png" );
        addUser("lemmiestage", "Lemmie Stage", PASSWORD, "/resources/img/user.png" );
        addUser("erinoverview", "Erin Overview", PASSWORD, "/resources/img/user.png" );
        addUser("harryhopeful", "Harry Hopeful", PASSWORD, "/resources/img/user.png" );
        addUser("grantable", "Grant Able", PASSWORD, "/resources/img/user.png" );
        addUser("robbierecords", "Robbie Records", PASSWORD, "/resources/img/user.png" );
        addUser("reggiemint", "Reggie Mint", PASSWORD, "/resources/img/user.png" );
        addUser("peterprofile", "Peter Profile", PASSWORD, "/resources/img/user.png" );
        addUser("nancynoah", "Nancy Noah", PASSWORD, "/resources/img/user.png" );
        addUser("sidneyseeker", "Sidney Seeker", PASSWORD, "/resources/img/user.png" );
        addUser("tomtally", "tomtally", PASSWORD, "/resources/img/user.png" );
        addUser("juliestitched", "juliestitched", PASSWORD, "/resources/img/user.png" );
        addUser("designa", "designa", PASSWORD, "/resources/img/user.png" );
        addUser("angelacummings", "angelacummings", PASSWORD, "/resources/img/user.png" );
        addUser("jukeskeeper", "jukeskeeper", PASSWORD, "/resources/img/user.png" );
        addUser("stewFaster", "stewFaster", PASSWORD, "/resources/img/user.png" );
    }

    private void addUser(String username,
                         String name,
                         String password,
                         String avatarUrl,
                         Role... roles
                         ) {

        List<String> rolesList= Stream.of(roles).map(Role::name).collect(Collectors.toList());
        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRoles(rolesList);
        user.setName(name);
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
    }

    private void addUser(String username,
                         String name,
                         String password,
                         String avatarUrl
    ) {
        addUser( username, name, password, avatarUrl, Role.COCO_PHARMA_USER );
    }

}

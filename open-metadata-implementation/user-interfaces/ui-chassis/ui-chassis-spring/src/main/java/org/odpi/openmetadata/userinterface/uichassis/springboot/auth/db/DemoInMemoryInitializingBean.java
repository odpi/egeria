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

    @Autowired
    private UserRepository userRepository;

    @Override
    public void afterPropertiesSet() {
        addUser("user","John", "user","/resources/img/user.png");
        addUser("admin","Administrator", "admin","/resources/img/admin.png",Role.ADMIN);

        /* In addition to original demo users, we add the full coco Pharmaceutical's set to support simple demos #1490 */
        addUser("faith","Faith Broker ", "admin","/resources/img/faith.png",Role.DATA_ANALYST);
        addUser("callie","Callie Quartile ", "admin","/resources/img/callie.png",Role.HR);
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
        addUser(username, name, password, avatarUrl, Role.USER);
    }

}

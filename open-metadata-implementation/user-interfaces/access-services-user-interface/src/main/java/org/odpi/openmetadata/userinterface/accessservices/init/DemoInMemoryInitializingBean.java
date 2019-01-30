/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.userinterface.accessservices.init;

import org.odpi.openmetadata.userinterface.accessservices.domain.Item;
import org.odpi.openmetadata.userinterface.accessservices.domain.Role;
import org.odpi.openmetadata.userinterface.accessservices.domain.User;
import org.odpi.openmetadata.userinterface.accessservices.repository.ItemRepository;
import org.odpi.openmetadata.userinterface.accessservices.repository.UserRepository;
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

    @Autowired
    private ItemRepository itemRepository;

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

    private Item addItem(String name,
                         String desc,
                         String owner,
                         Integer rating,
                         String type,
                         String glossaryTerm,
                         String viewType,
                         String classification,
                         Item parent,
                         String tags,
                         String column
    ){
        Item item = new Item();
        item.setName(name);
        item.setDescription(desc);
        item.setOwner(owner);
        item.setRating(rating);
        item.setType(type);
        item.setGlossary_term(glossaryTerm);
        item.setView_type(viewType);
        item.setClassification(classification);
//                item.setChildren(getChildren(item));
        item.setParent(parent);
        item.setTags(tags);

        item.setTable(parent!=null ? parent.getName() : name);
        item.setColumn(column);

        return itemRepository.save(item);

    }
}

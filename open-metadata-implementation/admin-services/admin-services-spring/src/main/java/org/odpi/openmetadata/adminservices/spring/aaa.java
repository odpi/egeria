package org.odpi.openmetadata.adminservices.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;

public class aaa {
public static void main(String[] a) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    // Convert JSON string from file to Object
    ViewServiceConfig v = new ViewServiceConfig();
    v.setOMAGServerName("ddd");
    v.setOMAGServerPlatformRootURL("sss");

    String str = mapper.writeValueAsString(v);
    System.out.println(str);

//    // Convert JSON string to Object
//    String jsonInString = "{\"age\":33,\"messages\":[\"msg 1\",\"msg 2\"],\"name\":\"mkyong\"}";
//    User user1 = mapper.readValue(jsonInString, User.class);
//    System.out.println(user1);
}
}

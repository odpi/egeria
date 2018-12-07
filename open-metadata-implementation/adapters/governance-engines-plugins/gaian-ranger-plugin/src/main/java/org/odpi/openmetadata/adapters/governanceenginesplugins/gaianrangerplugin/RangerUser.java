/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Arrays;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "firstName",
        "lastName",
        "description",
        "groupNameList",
        "userRoleList"
})
@JsonIgnoreProperties
public class RangerUser {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("groupNameList")
    private Set<String> groupNameList;

    @JsonProperty("userRoleList")
    private Set<String> userRoleList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getGroupNameList() {
        return groupNameList;
    }

    public void setGroupNameList(Set<String> groupNameList) {
        this.groupNameList = groupNameList;
    }

    public Set<String> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(Set<String> userRoleList) {
        this.userRoleList = userRoleList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String user = "RangerUser: id " + id + "; name=" + name;
        if (groupNameList != null) {
            final String groups = Arrays.toString(groupNameList.toArray());
            user += " Groups: " + groups;
        }
        return user;
    }
}
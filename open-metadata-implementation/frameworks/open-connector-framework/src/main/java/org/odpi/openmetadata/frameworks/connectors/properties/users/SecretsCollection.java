/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.users;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecretsCollection stores a collection or properties that are used to connect to a particular digital resource,
 * or support a specific type of security service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecretsCollection
{
    private String                             displayName            = null;
    private String                             description            = null;
    private long                               refreshTimeInterval    = 60L;
    private Map<String, String>                secrets                = null;
    private TokenAPI                           tokenAPI               = null;
    private Map<String, UserAccount>           users                  = null;
    private Map<String, NamedList>             namedLists             = null;
    private Map<String, SecurityAccessControl> securityAccessControls = null;


    /**
     * Default constructor
     */
    public SecretsCollection()
    {
    }


    /**
     * Default constructor
     */
    public SecretsCollection(SecretsCollection template)
    {
        if (template != null)
        {
            this.displayName            = template.getDisplayName();
            this.description            = template.getDescription();
            this.refreshTimeInterval    = template.getRefreshTimeInterval();
            this.secrets                = template.getSecrets();
            this.tokenAPI               = template.getTokenAPI();
            this.users                  = template.getUsers();
            this.namedLists             = template.getNamedLists();
            this.securityAccessControls = template.getSecurityAccessControls();
        }
    }


    /**
     * Return the display name for this collection.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name for this collection.
     *
     * @param displayName string
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description for this collection.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description for this collection.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the length of time that secrets can be cached.
     *
     * @return long
     */
    public long getRefreshTimeInterval()
    {
        return refreshTimeInterval;
    }


    /**
     * Set up the length of time that secrets can be cached.
     *
     * @param getRefreshTimeInterval long
     */
    public void setRefreshTimeInterval(long getRefreshTimeInterval)
    {
        this.refreshTimeInterval = getRefreshTimeInterval;
    }


    /**
     * Return the users stored in the secrets collection.
     *
     * @return map of userIds to user details
     */
    public Map<String, UserAccount> getUsers()
    {
        return users;
    }


    /**
     * Set up the users stored in the secrets collection.
     *
     * @param users  map of userIds to user details
     */
    public void setUsers(Map<String, UserAccount> users)
    {
        this.users = users;
    }


    /**
     * Return the list of secrets.
     *
     * @return map of secret values
     */
    public Map<String, String> getSecrets()
    {
        return secrets;
    }


    /**
     * Set up the in memory version of the secrets collection.
     *
     * @param secrets a map of secrets values
     */
    public void setSecrets(Map<String, String> secrets)
    {
        this.secrets = secrets;
    }


    /**
     * Return details of a token API associated with the secrets collection.
     *
     * @return token API definition
     */
    public TokenAPI getTokenAPI()
    {
        return tokenAPI;
    }


    /**
     * Set up the token API for this secrets collection.
     *
     * @param tokenAPI token API definition
     */
    public void setTokenAPI(TokenAPI tokenAPI)
    {
        this.tokenAPI = tokenAPI;
    }


    /**
     * Return the named lists in this collection.  The named lists can represent organization units,
     * security groups, and roles.
     *
     * @return map of named lists
     */
    public Map<String, NamedList> getNamedLists()
    {
        return namedLists;
    }


    /**
     * Set up the named lists in this collection.  The named lists can represent organization units,
     * security groups, and roles.
     *
     * @param namedLists map of named lists
     */
    public void setNamedLists(Map<String, NamedList> namedLists)
    {
        this.namedLists = namedLists;
    }


    /**
     * Return the security access controls in this collection.  The security access controls can represent
     * organization units, security groups, and roles.
     *
     * @return map of security access controls
     */
    public Map<String, SecurityAccessControl> getSecurityAccessControls()
    {
        return securityAccessControls;
    }


    /**
     * Set up the security access controls in this collection.  The security access controls can represent
     * organization units, security groups, and roles.
     *
     * @param securityAccessControls map of security access controls
     */
    public void setSecurityAccessControls(Map<String, SecurityAccessControl> securityAccessControls)
    {
        this.securityAccessControls = securityAccessControls;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SecretsCollection{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", refreshTimeInterval=" + refreshTimeInterval +
                ", secrets=" + secrets +
                ", tokenAPI=" + tokenAPI +
                ", users=" + users +
                ", namedLists=" + namedLists +
                ", securityAccessControls=" + securityAccessControls +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        SecretsCollection that = (SecretsCollection) objectToCompare;
        return refreshTimeInterval == that.refreshTimeInterval &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(users, that.users) &&
                Objects.equals(secrets, that.secrets) &&
                Objects.equals(tokenAPI, that.tokenAPI) &&
                Objects.equals(namedLists, that.namedLists) &&
                Objects.equals(securityAccessControls, that.securityAccessControls);
    }

    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(displayName, description, refreshTimeInterval, users, secrets, tokenAPI, namedLists, securityAccessControls);
    }
}

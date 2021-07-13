/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.samples;

import java.util.Properties;
import java.util.StringTokenizer;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;

public class CocoPharmaServerLDAPSecurityConnector extends AbstractCocoPharmaServerSecurityConnector {
    private DirContext connection;
    private String[] firstNames = {"Zach", "Steve", "Terri", "Tanya", "Polly", "Tessa", "Callie",
            "Ivor", "Bob", "Faith", "Sally", "Lemmie", "Erin", "Harry", " Gary", "Grant", "Robbie", "Reggie",
            "Peter", "Nancy", "Sidney", "Tom", "Julie", "Des", "Angela", "Jules", "Stew"};
    private String[] lastNames = {"Now", "Starter", "Daring", "Tidie", "Tasker", "Tube", "Quartile",
            "Padlock", "Nitter", "Broker", "Counter", "Stage", "Overview", "Hopeful", "Geeke", "Able", "Records",
            "Mint", "Profile", "Noah", "Seeker", "Tally", "Stiched", "Signa", "Cumming", "Keeper", "Faster"};

    public void makeAConnection() throws NamingException {
        //Using Java Properties to create a connection to the LDAP server present in Apache LDAP Directory
        Properties environment = new Properties();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL, "ldap://localhost:10389"); //Server details
        environment.put(Context.SECURITY_PRINCIPAL, "uid=admin, ou=system"); //Current Admin User
        environment.put(Context.SECURITY_CREDENTIALS, "secret"); //Admin Password

        this.connection = new InitialDirContext(environment);
        //Change this to Java Logging later
        System.out.println("Trying connection to LDAP Apache Server....\n" + connection);
    }

    /**
     * Searches currently connected LDAP Apache Server for all users and prints them to console.
     * @throws NamingException
     */
    public void getAllUsers() throws NamingException {
        String searchFilter = "(objectClass=inetOrgPerson)";
        String[] regAttribute = {"cn"}; //Searching by Common Name, could add more filters if necessary
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(regAttribute);

        NamingEnumeration users = connection.search("ou=users,ou=system" ,searchFilter ,controls);

        SearchResult result = null;
        while (users.hasMore()) {
            result = (SearchResult) users.next();
            Attributes attribute = result.getAttributes();
            System.out.println(attribute.get("cn"));
        }
    }

    /**
     * Adds an individual user to the LDAP Apache Server
     * @param commonName - First name of individual to be added
     * @param surName - Last name of individual to be added
     * @throws NamingException
     */
    public void addUser(String commonName, String surName) throws NamingException {
        Attributes attributes = new BasicAttributes();
        Attribute attribute = new BasicAttribute("objectClass");
        attribute.add("inetOrgPerson");

        attributes.put(attribute);
        attributes.put("sn", surName);
        attributes.put("cn", commonName);

        connection.createSubcontext("ou=users,ou=system", attributes);

    }

    /**
     * Adds an individual user to group of choosing on LDAP Apache Server
     * @param user - User to be added
     * @param groupName - Group to be added to
     * @throws NamingException
     */
    public void addUserToGroup(String user, String groupName) throws NamingException {
        ModificationItem[] modifications = new ModificationItem[1];
        Attribute attribute = new BasicAttribute("uniqueMember", "cn=" + user + ",ou=users,ou=system");
        modifications[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, attribute);
        connection.modifyAttributes("cn=" + groupName + ",ou=groups,ou=system", modifications);
    }

    /**
     * Helper method to add all users to the LDAP server
     * @throws NamingException
     */
    public void addAllUsers() throws NamingException {
        for (int i = 0; i < firstNames.length; i++) {
            addUser(firstNames[i], lastNames[i]);
        }
    }

    /**
     * Gets all users in group of choice
     * @param groupName
     * @throws NamingException
     */
    public void getAllUsersInGroup(String groupName) throws NamingException {
        String searchFilter = "(objectClass=groupOfUniqueNames)";
        String[] regAttribute = {"uniqueMember"}; //Searching for all users in group
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(regAttribute);

        NamingEnumeration users = connection.search("cn=" + groupName + ",ou=groups,ou=system" ,searchFilter ,controls);

        SearchResult result = null;
        while (users.hasMore()) {
            result = (SearchResult) users.next();
            Attributes attribute = result.getAttributes();
            System.out.println(attribute.get("cn"));
        }
    }

    /**
     * Adding all users to the all users group
     * @throws NamingException
     */
    public void addUsersToAllUsersGroup() throws NamingException {
        for (int i = 0; i < firstNames.length; i++) {
            addUserToGroup(firstNames[i], "All Users");
        }
    }

    /**
     * Adding all users to the all employees group
     */
    public void addUsersToAllEmployeesGroup() {

    }

    /**
     * Adding all users to the asset onboarding group
     */
    public void addUsersToAssetOnboardingGroup() {

    }

    /**
     * Adding all users to the asset onboarding exit group
     */
    public void addUsersToAssetOnboardingExitGroup() {

    }

    /**
     * Adding all users to the server admin group
     */
    public void addUsersToServerAdminsGroup() throws NamingException {
        addUserToGroup("Gary", "Administrators");
    }

    /**
     * Adding all users to the server operators group
     */
    public void addUsersToServerOperatorsGroup() throws NamingException {
        addUserToGroup("Gary", "Operators");
    }

    /**
     * Adding all users to server investigators group
     */
    public void addUsersToServerInvestigatorsGroup() throws NamingException {
        addUserToGroup("Gary", "Investigators");
    }

    /**
     * Adding all users to metadata architects group
     */
    public void addUsersToMetadataArchitectsGroup() throws NamingException {
        addUserToGroup("Erin", "Metadata Architects");
        addUserToGroup("Peter", "Metadata Architects");
    }

    /**
     * Adding all users to external users group
     */
    public void addUsersToExternalUsersGroup() throws NamingException {
        addUserToGroup("Grant", "External Users");
        addUserToGroup("Julie", "External Users");
        addUserToGroup("Angela", "External Users");
        addUserToGroup("Robbie","External Users");
    }

    /**
     * Adding all users to NPA accounts group
     */
    public void addUsersToNpaAccountsGroup() {

    }
}

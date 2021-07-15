/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.samples;

import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;

public class CocoPharmaServerLDAPSecurityConnector extends AbstractCocoPharmaServerSecurityConnector {
    private DirContext connection;

    /**
     * Calls all methods to build LDAP server and populate it with values
     * @throws NamingException incorrect spelling of name
     */
    public void CocoPharmaServerLDAPSecurityConnector() throws NamingException {
        makeAConnection(10389);
    }

    /**
     * Create an LDAP Apache Server on port 10389
     * @throws NamingException incorrect spelling of name
     */
    public void makeAConnection(int portNumber) throws NamingException {
        Properties environment = new Properties();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL, "ldap://localhost:" + portNumber);
        environment.put(Context.SECURITY_PRINCIPAL, "uid=admin, ou=system"); //Admin user & password
        environment.put(Context.SECURITY_CREDENTIALS, "secret");

        this.connection = new InitialDirContext(environment);
    }

    /**
     * Search connected LDAP server for all users and print them to console
     * @throws NamingException incorrect spelling of name
     * @return ArrayList of results found
     */
    public ArrayList<Attribute> getAllUsers() throws NamingException {
        String searchFilter = "(objectClass=inetOrgPerson)";
        String[] regAttribute = {"cn"}; //Search by first name, could add more filters if necessary
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(regAttribute);

        NamingEnumeration users = connection.search("ou=users,ou=system" ,searchFilter ,controls);

        ArrayList<Attribute> results = new ArrayList<Attribute>();
        SearchResult result = null;
        while (users.hasMore()) {
            result = (SearchResult) users.next();
            Attributes attribute = result.getAttributes();
            results.add(attribute.get("cn"));
        }
        return results;
    }

    /**
     * Get all users in group of choice
     * @param groupName - Name of group to be added
     * @throws NamingException incorrect spelling of name
     * @return ArrayList of results found
     */
    public ArrayList<Attribute> getAllUsersInGroup(String groupName) throws NamingException {
        String searchFilter = "(objectClass=groupOfUniqueNames)";
        String[] regAttribute = {"uniqueMember"}; //Searching for all users in group
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(regAttribute);

        NamingEnumeration users = connection.search("cn=" + groupName + ",ou=groups,ou=system" ,searchFilter ,controls);

        ArrayList<Attribute> results = new ArrayList<Attribute>();
        SearchResult result;
        while (users.hasMore()) {
            result = (SearchResult) users.next();
            Attributes attribute = result.getAttributes();
            results.add(attribute.get("cn"));
        }
        return results;
    }

    public void addUsersToAllUsersGroup() throws NamingException {

    }

    public void addUsersToAllEmployeesGroup() {

    }

    public void addUsersToAssetOnboardingGroup() {

    }

    public void addUsersToAssetOnboardingExitGroup() {

    }

    public void addUsersToServerAdminsGroup() throws NamingException {

    }

    public void addUsersToServerOperatorsGroup() throws NamingException {

    }

    public void addUsersToServerInvestigatorsGroup() throws NamingException {

    }

    public void addUsersToMetadataArchitectsGroup() throws NamingException {

    }

    public void addUsersToExternalUsersGroup() throws NamingException {

    }

    public void addUsersToNpaAccountsGroup() {

    }
}

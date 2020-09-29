<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
# ranger-gaian-plugin

**Introduction**

This plugin provides support for Ranger policies to be implemented when access data through Gaian.
For example:

* Permit/Deny access to Gaian Resources
* Supports Data Masking [tbd]
* Ranger Tag support (including being sourced from Apache Atlas) [tbd]

See doc/SQLBehaviour.md for example SQL statements and how they will behave

LIMITATIONS
 * Currently the user group determined is always 'users'

**Building the plugin**

This project contains multiple modules for
 - The ranger plugin (in the 'plugin' subdirectory)
 - the Impersonation support (in the 'impersonation' subdirectory)
 
Ensure the required build requirements are installed
* Java 8 (151 or above)
* Maven 3.50 or later

Next extract the source & build:

    `git clone https://github.com/GaianRangerPlugin/ranger-gaian-plugin.git`
    `cd ranger-gaian-plugin`
    `mvn clean install`

This should produce the ranger plugin built in the 'plugin\target' directory called ranger-gaian-plugin-1.0.0-SNAPSHOT.jar . 
This contains the plugin AND the dependent libraries.

NOTE: If you had an earlier version of this plugin you may have
dependent libraries in the policy directory. These are no longer required,
you ONLY need the jar file and the config files

Also note the root 'target' directory does not currently contain any packaged deployable
components (to be aggregated later) and can be ignored.

**Running Unit Tests**

**Deploying the plugin to Gaian**

First modify the 'launchGaianServer.sh' script provided by Gaian to add additional directories to the classpath, which is where
we will install the plugin. The best place to do this is just before the section of code labelled 'automatic jar discovery' :

    `export CLASSPATH="$CLASSPATH:/root/gaiandb/gaiandb/policy/*"`
    `export CLASSPATH="$CLASSPATH:/root/gaiandb/gaiandb/policy/conf/"`

* copy plugin/target/ranger-gaian-plugin-1.0.0-SNAPSHOT.jar from your build tree, to this policy folder on Gaian.

* Copy the configuration files found in this project under plugin/src/main/resources/conf to policy/conf on gaian. These are the configuration files
for the plugin which we will edit below

* configure Gaian to use RangerPolicyResultFilter, by adding this line at the end of gaiandb_config.properties:

    `SQL_RESULT_FILTER=org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.RangerPolicyResultFilter`

* configure Gaian to use Table Functions
    `MANAGE_LTVIEWS_WITH_TABLE_FUNCTIONS=true`
    
    Note that if this is changed when gaian is stopped there appears to be a bug whereby it is not picked up...  in which case it can be forced by deleting the 'gaian' subdirectory which will cause views etc to be recreated..... I'll raise a separate issue on this in gaian GitHub.

**Deploying the Service Definition to Ranger**

This needs to be done once only (unless the service definition changes)

    `cd resources/service-defs`
    `curl -u admin:admin -X POST -H "Accept: application/json" -H "Content-Type: application/json" --data @ranger-servicedef-gaian.json http://localhost:6080/service/plugins/definitions`

Replace localhost with the IP /port of your Ranger Server

Once posted, the ranger access manager page should now show 'Gaian' as a selectable component

In some cases (in particular when deploying to a standalone clean ranger, as opposed to HDP) you may 
need to copy the plugin to the ranger server too. When complete this would be done to support the
resource name typeahead feature

Make the directory & copy the plugin jar to /opt/ranger-1.0.0-SNAPSHOT-admin/ews/webapp/WEB-INF/classes/ranger-plugins/gaian

Adjust the path as appropriate based on the install location

**Updating an existing service definition**

First delete any existing gaian services through the ranger UI

Then find out the id of the gaian servicedef. This would either have been returned when deployed, or
you can pull all definitions to find it ie

    `curl -u admin:admin localhost:6080/service/public/v2/api/servicedef/ | jsonpp | less`

'jsonpp' is one way to format a json file on macOS (installed using the 'brew' environment ie brew install jsonpp)

Look for gaian & then find the id....

To delete:

    `curl  -u admin:admin -X DELETE -H "Accept: application/json" -H "Content-Type: application/json"  http://9.20.65.115:6080/service/public/v2/api/servicedef/102`

**Configuring the Gaian plugin**

Modify the following files on gaian under policy/conf:

* ranger-gaian-audit.xml

    To use solr modify the property 'xasecure.audit.solr.is.enabled' to true, and set the correct hostname/solr endpoint in xasecure.audit.solr.solr_url

    Alternatively to log directly to a RDBMS such as mysql modify the property 'xasecure.audit.db.is.enabled' to true, and set the correct hostname/db uri in xasecure.audit.jpa.javax.persistence.jdbc.url

* ranger-gaian-security.xml

    In order for the gaian plugin to be able to retrieve policies, we must specify the REST endpoint. If this is
    incorrect, or there is no access (firewall etc) ranger policies will not be updated/work
    
    Specify this in the ranger.plugin.gaian.policy.rest.url property
    
    Do not change the property ranger.plugin.gaian.service.name and leave it set to 'gaian'
    
* log4J.properties

    This is a simple example to outlog debug logs to the console. Modify as required. It is likely helpful for debugging
    in this stage of development, but in an eventual deployment log4j.properties would likely be deployed elsewhere in the 
    environment

**Adding support for User Impersonation**

The impersonation/ directory provides a module to allow a valid gaian user to assert that they are in fact
acting on behalf of another user, which does not need to be pre-configured in gaian. This allows
Ranger policies to operate on behalf of a end-user rather than a generic service account.

To install this support 

* copy the jar

    Copy impersonation/target/gaian-impersonation-1.0.0-SNAPSHOT.jar to the 'policy' directory in Gaian (the same directory where you 
    added the ranger plugin)
    
* modify derby.properties

    Change the auth class in the configuration similar to as follows (the old entry is commented out):
    
    `#derby.authentication.provider=BUILTIN`
    `#derby.authentication.provider=com.ibm.gaiandb.GaianAuthenticator`
    `derby.authentication.provider=org.apache.gaiandb.security.ProxyUserAuthenticator`
    `derby.connection.requireAuthentication=TRUE`
    
Check gaian works by starting it. If the support is not installed gaian will fail to start with many errors, and
in gaiandb.log you will find an entry stating:

    `2018-02-22 14:32:54.780 ********** GDB_WARNING: ENGINE_JDBC_CONN_ATTEMPT_ERROR: Failed JDBC Connection attempt in 172 ms for: jdbc:derby:gaiandb;create=true, cause: java.sql.SQLNonTransientConnectionException: Connection refused : FATAL: There is no Authentication Service for the system; Common issues: missing jdbc driver, network/database unavailability (e.g. firewall), incorrect user/password and/or insufficient database access rights (e.g. if derby.database.defaultConnectionMode=noAccess in derby.properties)`

If this is seen check the jar file, properties entry, classpath & location ... derby.log may have further information bb

To make use of this support connect to the database with properties set as follows:
* User

The user that derby/gaian should act on behalf of, once authentication is completed

* Password

If proxy-user/pwd is not used this is the password of the User. 
If not, it has to be specified as a non null string, but is ignored

* proxy-user

Specifies the service account (aka NPA) to authenticate as

* proxy-pwd

Specifies the proxy account's password

So for example instead of using a string similar to
    `jdbc:derby://localhost:6414/gaiandb;user=gaiandb;password=passw0rd`
Use
    `jdbc:derby://localhost:6414/gaiandb;user=nigel;password=x;proxy-user=gaiandb;proxy-pwd=passw0rd`

After authentication derby/gaian will see the user as 'nigel' even though gaiandb/passw0rd is the user/pass setup in gaian.
Therefore ranger policies can be used that refer to 'nigel' or the groups they are a member of (when implemented)

Note that with this plugin installed 
* you can still use the original way of connecting as a generic user
* with either method the service user specified must have the correct password provided    

You can check correct proxy auth by looking in gaiandb.log for lines similar to:

    2018-02-22 15:06:49.806~869155397 ProxyUserAuthenticator -----> Performing proxy authentication with user:gaiandb on behalf of:nigel
    2018-02-22 15:21:51.567~626209601 ProxyUserAuthenticator -----> authentication was successful
    
or for regular users:

    2018-02-22 15:21:51.567~626125459 ProxyUserAuthenticator -----> Performing regular authentication for user:gaiandb
    2018-02-22 15:21:51.567~626209601 ProxyUserAuthenticator -----> authentication was successful

whilst a failure case will look like:

    2018-02-22 15:24:04.519~577348241 ProxyUserAuthenticator -----> Performing proxy authentication with user:gaiandb on behalf of:nigel
    2018-02-22 15:24:04.519~577422022 ProxyUserAuthenticator -----> Performing regular authentication for user:nigel
    2018-02-22 15:24:04.519~577484342 ProxyUserAuthenticator -----> authentication failed

since we first try proxy auth (if parms specified) & then fall back to regular authentication

Note that Gaian converts all users to upper case. However most users in ldap are typically lower case. Ranger is case sensitive meaning that
policies will not match. In the current version therefore ALL userids through gaian are mapped to lower case before policy checks occur.

**Verifying the environment**

(tbd)
    
**Creating Ranger Policies**

* Create a ranger service def by logging onto the Ranger UI and selecting resource policies. You should see a section labelled 'Gaian'. Create a new instance of a Gaian service. You MUST use the name 'gaian'. Tag service can be left blank for now, and whilst user/password have to be filled in, they
will be ignored.



**Testing Policies**

* A quick test:
0. Run testGaianDB.sh, it should show an empty table.
1. Create a policy of schema *, table LT0, column * on Ranger UI.
2. run testGaianDB.sh, it should show table LT0 correctly.

* Test with specific column query:
For this to be able to work correctly, must use derby virtual table syntax. For example, if only query column LOCATION in table LT0, the syntax is:

select firstname,lastname,birth_date from TABLE(VEMPLOYEE('VEMPLOYEE')) VEMP FETCH FIRST 100 rows only

and then create column access/deny policies for testing.

If instead VTI syntax is used, the user id cannot be retrieved and will be set to <UNKNOWN>
If regular table/view syntax is used it is assumed ALL columns are access so policies will be unnecessarily restrictive

This is due to current derby/gaian integration limitations.

Data masking should also work, but due to a current issue it will fail if columns in the result set
(before masking) are null & table functions are in use. 

See RangerPolicyResultFilter:filterRowsBatch for the explanation. This will be addressed in future

**Todos**

* Add tag support to plugin
* Add info on verifying environment
* Create a 'dist' directory or similar with the two relevant jars + instructions + servicedef + policy configuration files
* check dependency versions. some are back level, though it may depend what ranger built with
 
**Troubleshooting**

_No Audit log entries_

Look in the ranger plugin output (log4J) ie:
```
018-02-23 09:43:29 ERROR SolrAuditProvider:192 - Error sending message to Solr
java.lang.reflect.UndeclaredThrowableException
	at org.apache.hadoop.security.UserGroupInformation.doAs(UserGroupInformation.java:1713)
	at org.apache.ranger.audit.provider.MiscUtil.executePrivilegedAction(MiscUtil.java:524)
	at org.apache.ranger.audit.provider.solr.SolrAuditProvider.log(SolrAuditProvider.java:170)
	at org.apache.ranger.audit.provider.MultiDestAuditProvider.log(MultiDestAuditProvider.java:114)
	at org.apache.ranger.audit.provider.AsyncAuditProvider.run(AsyncAuditProvider.java:153)
	at java.lang.Thread.run(Thread.java:748)
Caused by: org.apache.solr.client.solrj.SolrServerException: Server refused connection at: http://9.20.65.115:6083/solr/ranger_audits
```
In this case check the hostname/port specified for solr logging

_Policies are not cached_

If you see

    `2018-02-22 15:48:55 ERROR PolicyRefresher:396 - failed to save policies to cache file '/root/gaiandb/gaiandb/policycache/gaian_gaian.json'`
    `java.io.FileNotFoundException: /root/gaiandb/gaiandb/policycache/gaian_gaian.json (No such file or directory)`

check the specification of PolicyCache in the plugin configuration file & ensure the directory
exists and is writeable   

_Cannot create gaian service on ranger server_

If you get an error 'cannot find class XXX' copy the ranger gaian plugin to the ranger server as 
documented above in the section on deploying the servicedef.

Return to [governance-engine-plugins](..) module.
 
 ----
 License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
 Copyright Contributors to the ODPi Egeria project.

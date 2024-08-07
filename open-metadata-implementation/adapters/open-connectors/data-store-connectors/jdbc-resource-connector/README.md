<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# JDBC Resource Connector

Provides a basic implementation of the interface javax.sql.DataSource interface in order to establish a connection to target database. Because of a method clash, the interface has been implemented as an inner class of the connector and to get the implementation, one must call: 
```
jdbcConnector.asDataSource()
```

Its Jar file includes the PostgreSQL client driver.

See [JDBC Resource Connector](https://egeria-project.org/connectors/resource/jdbc-resource-connector/) for documentation.
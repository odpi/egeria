<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# JDBC Resource Connector

The JDBC Resource Connector provides a DataSource, which in turn is used to get a connection to an underlying database.  It provides access to both the schema metadata and the business data content.

![Figure 1](docs/jdbc-resource-connector.png)
> **Figure 1:** JDBC resource connector

## Using the connector

The connector hands out connections from a [HikariCP](https://github.com/brettwooldridge/HikariCP) pool.  A JDBC connection carries a single database transaction and cannot be used by two threads at once, so a caller takes a connection out for the duration of one unit of work and gives it back at the end:

```java
try (Connection jdbcConnection = jdbcResourceConnector.getDataSource().getConnection())
{
    ... statements ...

    jdbcConnection.commit();   // only if the unit of work made changes
}
```

Two rules follow from this, and both matter:

**Always close the connection.**  Closing returns it to the pool; it does not close the network connection.  A caller that holds on to a connection is holding a slot in a pool that defaults to five, so code that forgets will exhaust the pool and subsequent callers will fail after `jdbcConnectionWaitTimeout`.  Use try-with-resources and the problem does not arise.

**Commit your own writes.**  The connector runs with auto-commit disabled, and none of its helper methods - `issueSQLCommand`, `addDatabaseDefinitions`, `insertRowIntoTable` and the rest - commit on your behalf.  When a connection goes back to the pool with a transaction still open, the pool rolls it back.  So a write path that does not call `commit()` does not leave a dangling transaction; it silently loses its changes.  This is deliberate: an abandoned unit of work should discard its work rather than leave a connection idle-in-transaction until the database kills it.

## Configuration

The connector is initialized using the connection information attached to the [RelationalDatabase](https://egeria-project.org/types/2/0224-Databases) asset in the open metadata ecosystem.

![Figure 2](docs/jdbc-resource-connector-use.png)
> **Figure 2:** Connection information used to create an instance of the JDBC resource connector

### Connecting to the database

* `jdbcDriverManagerClassName` - requests the named class to be loaded and registered as a driver.  This property only needs to be defined if the connector is experiencing exceptions related to a missing DriverManager class for the database URL.
* `jdbcConnectionTimeout` - the maximum time in seconds the driver will wait while opening a new network connection to the database.  The default value is 0, which means use the system default timeout, if any; otherwise it means no timeout.  This is not the same as `jdbcConnectionWaitTimeout` below.
* `jdbcDatabaseName` - provides a name to use in messages about the database.  If it is not set then the connection URL string is used.
* `additionalConnectionProperties` - properties passed straight through to the JDBC driver on every connection.  For example, Oracle's driver needs `remarksReporting=true` to return table and column REMARKS through `DatabaseMetaData`; without it they are silently omitted.

### Sizing and managing the pool

* `jdbcMaximumPoolSize` - the maximum number of database connections this connector holds open at once.  Default 5.
* `jdbcMinimumIdle` - the minimum number of idle connections kept ready.  Default 1.  Setting this equal to `jdbcMaximumPoolSize` gives a fixed-size pool, which is the recommended configuration for steady workloads.
* `jdbcConnectionWaitTimeout` - milliseconds a caller waits for a free connection from the pool before failing.  Default 30000.
* `jdbcMaximumConnectionLifetime` - milliseconds a connection may live before the pool retires and replaces it.  Default 1800000 (30 minutes).
* `jdbcConnectionKeepAlive` - how often, in milliseconds, the pool probes an idle connection to check it is still usable.  Default 120000 (2 minutes); zero disables it.  Must be smaller than `jdbcMaximumConnectionLifetime`.
* `jdbcConnectionLeakThreshold` - how long, in milliseconds, a connection may be held by a caller before the connector logs a stack trace naming whoever took it out.  Default 0 (disabled).  This is a diagnostic aid for finding code that fails to close its connections; it does not itself reclaim the connection.

**A pool exists per connector instance, and Egeria creates one connector instance per catalog target.**  So `jdbcMaximumPoolSize` is multiplied by the number of catalog targets when sizing the database server.  Twenty catalog targets at the default of five is a hundred connections against that server.

`jdbcMaximumConnectionLifetime` must be set comfortably below any idle or lifetime limit imposed by the database server or by intervening infrastructure such as a firewall or load balancer.  If it is not, the pool will hand out connections that have already been closed at the far end.

### Keeping connections alive

A connection whose peer disappears silently - a dropped network, a firewall idle timeout - leaves the pool holding a socket that will never be read from again.  HikariCP calls this out as the cause of a pool that [drains to zero and does not recover](https://github.com/brettwooldridge/HikariCP#legitimate-uses-for-tcp-keepalive).  The protection is socket level TCP keepalive, which is separate from the pool's own `jdbcConnectionKeepAlive` probe; both are wanted.

The driver property that switches it on is driver specific, and some drivers reject properties they do not recognise, so the connector sets it only where the spelling is known:

| Database   | Property set automatically |
|------------|----------------------------|
| PostgreSQL | `tcpKeepAlive=true`        |
| Oracle     | `oracle.net.keepAlive=true`|

An explicit setting in `additionalConnectionProperties` always wins over the automatic one.  For any other database, supply the driver's equivalent property through `additionalConnectionProperties`, or configure keepalive at the operating system level - see [TCP keepalive for a better PostgreSQL experience](https://www.percona.com/blog/2019/02/25/tcp-keepalives-for-a-better-postgresql-experience/) for the background and the OS-level settings.

When keepalive is enabled, the connector logs `JDBC-RESOURCE-CONNECTOR-0010` naming the property it set.

### Example

Below is an example connection for a PostgreSQL database:

```json
{
    "class": "Connection",
    "connectorType": {
      "class": "ConnectorType",
      "connectorProviderClassName": "org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider"
    },
    "endpoint": {
      "class": "Endpoint",
      "address": "jdbc:postgresql://localhost:5432/myDatabase"
    },
    "userId": "xxxxx",
    "clearPassword": "xxxx",
    "configurationProperties": {
      "jdbcDriverManagerClassName": "org.postgresql.Driver",
      "jdbcConnectionTimeout": "10",
      "jdbcDatabaseName": "MyDatabase",
      "jdbcMaximumPoolSize": "5"
    }
}
```

## Implementation

The connector is implemented in the `JDBCResourceConnector` class.  The pool is built during `start()` and shut down during `disconnect()`, which closes every connection it holds.

The pool is reached through `getDataSource()` and is a separate object rather than the connector itself.  This is forced by the framework: `JDBCResourceConnector` extends `ConnectorBase`, which already declares `getConnection()` returning the OCF `Connection` bean that describes how to connect.  Java does not allow two methods that differ only by return type, so the connector cannot also implement `javax.sql.DataSource` and return a `java.sql.Connection` from a method of the same name.  Delegating to a separate object is the way round that, and it is why `getDataSource().getConnection()` is the route to a JDBC connection.

Its Jar file includes the PostgreSQL client driver and HikariCP.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

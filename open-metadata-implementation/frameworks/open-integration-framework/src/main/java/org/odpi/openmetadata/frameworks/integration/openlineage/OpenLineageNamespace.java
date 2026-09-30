/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * OpenLineageNamespace is the parsed form of the location of a data source.  It is created either from an
 * OpenLineage dataset namespace (for example postgres://db.example.com:5432, see
 * <a href="https://openlineage.io/docs/spec/naming/">the OpenLineage naming conventions</a>) or from the network
 * address of an Egeria endpoint (for example jdbc:postgresql://db.example.com:5432/sales), so that the two can be
 * compared.  Schemes are reduced to the OpenLineage spelling (postgresql becomes postgres, sqlserver becomes mssql)
 * and missing ports are filled in with the technology's default port.
 *
 * @param scheme canonical scheme, or null if the address has none (for example a Kafka bootstrap server host:port)
 * @param host host name in lower case, or null
 * @param port port number, or null if it is not known
 * @param database database (or Oracle service) named in the address, or null
 * @param schema database schema named in the address, or null
 */
public record OpenLineageNamespace(String scheme,
                                   String host,
                                   String port,
                                   String database,
                                   String schema)
{
    /**
     * Namespace for datasets that are identified by the qualified name of an element in open metadata.
     */
    public static final String EGERIA_NAMESPACE = "egeria";

    /**
     * OpenLineage namespaces that are just a scheme.
     */
    private static final Set<String> SCHEME_ONLY_NAMESPACES = Set.of("file", "bigquery", "pubsub", "inmemory", EGERIA_NAMESPACE);

    /**
     * Alternative spellings of schemes, mapped to the spelling used in the OpenLineage naming conventions.
     */
    private static final Map<String, String> SCHEME_ALIASES = Map.of("postgresql", "postgres",
                                                                     "sqlserver",  "mssql",
                                                                     "athena",     "awsathena",
                                                                     "mariadb",    "mysql",
                                                                     "s3a",        "s3",
                                                                     "s3n",        "s3",
                                                                     "gcs",        "gs");

    /**
     * Ports that a technology listens on when the address does not name one.
     */
    private static final Map<String, String> DEFAULT_PORTS = Map.ofEntries(Map.entry("postgres",  "5432"),
                                                                           Map.entry("mysql",     "3306"),
                                                                           Map.entry("mssql",     "1433"),
                                                                           Map.entry("oracle",    "1521"),
                                                                           Map.entry("db2",       "50000"),
                                                                           Map.entry("redshift",  "5439"),
                                                                           Map.entry("hive",      "10000"),
                                                                           Map.entry("cassandra", "9042"),
                                                                           Map.entry("teradata",  "1025"),
                                                                           Map.entry("kafka",     "9092"),
                                                                           Map.entry("http",      "80"),
                                                                           Map.entry("https",     "443"));


    /**
     * Return the spelling of a scheme used in the OpenLineage naming conventions.
     *
     * @param scheme scheme from a namespace or network address
     * @return lower case, canonical scheme (null if scheme is null)
     */
    public static String getCanonicalScheme(String scheme)
    {
        if (scheme == null)
        {
            return null;
        }

        String lowerCaseScheme = scheme.toLowerCase();

        return SCHEME_ALIASES.getOrDefault(lowerCaseScheme, lowerCaseScheme);
    }


    /**
     * Is the value shaped like an OpenLineage dataset namespace?  This distinguishes the OpenLineage namespace
     * stored in an asset's namespacePath from other uses of that property (such as a Unity Catalog
     * catalog.schema prefix).
     *
     * @param value value to test
     * @return boolean
     */
    public static boolean isOpenLineageNamespace(String value)
    {
        if (value == null)
        {
            return false;
        }

        return (value.contains("://")) || (value.startsWith("arn:")) || (SCHEME_ONLY_NAMESPACES.contains(value.toLowerCase()));
    }


    /**
     * Parse an OpenLineage dataset namespace such as postgres://host:5432, kafka://broker:9092 or file.
     *
     * @param namespace OpenLineage namespace
     * @return parsed namespace (null if namespace is null)
     */
    public static OpenLineageNamespace fromNamespace(String namespace)
    {
        if (namespace == null)
        {
            return null;
        }

        int schemeSeparator = namespace.indexOf("://");

        if (schemeSeparator > 0)
        {
            String scheme    = getCanonicalScheme(namespace.substring(0, schemeSeparator));
            String authority = getAuthority(namespace.substring(schemeSeparator + 3));

            return fromAuthority(scheme, authority, null, null);
        }

        int colon = namespace.indexOf(':');

        if (colon > 0)
        {
            return new OpenLineageNamespace(getCanonicalScheme(namespace.substring(0, colon)), null, null, null, null);
        }

        return new OpenLineageNamespace(getCanonicalScheme(namespace), null, null, null, null);
    }


    /**
     * Parse the network address of an Egeria endpoint.  The recognized forms are the JDBC URLs used by Egeria's
     * database templates, URLs with a scheme and authority, and plain host:port addresses (as used for Kafka
     * bootstrap servers).
     *
     * @param networkAddress network address
     * @return parsed address, or null if the address is not recognized
     */
    public static OpenLineageNamespace fromNetworkAddress(String networkAddress)
    {
        if ((networkAddress == null) || (networkAddress.isBlank()))
        {
            return null;
        }

        String address = networkAddress.trim();

        if (address.toLowerCase().startsWith("jdbc:"))
        {
            return fromJDBCURL(address.substring(5));
        }

        int schemeSeparator = address.indexOf("://");

        if (schemeSeparator > 0)
        {
            String scheme    = getCanonicalScheme(address.substring(0, schemeSeparator));
            String remainder = address.substring(schemeSeparator + 3);
            String authority = getAuthority(remainder);
            String path      = remainder.substring(authority.length());

            return fromAuthority(scheme, authority, getFirstPathSegment(path), null);
        }

        if ((! address.contains("/")) && (address.indexOf(':') > 0))
        {
            return fromAuthority(null, address, null, null);
        }

        return null;
    }


    /**
     * Do the two locations describe the same server?  The schemes must match when both are known, and the hosts
     * and ports must match (after default ports are filled in).
     *
     * @param other other location
     * @return boolean
     */
    public boolean isSameServer(OpenLineageNamespace other)
    {
        if ((other == null) || (host == null) || (other.host == null))
        {
            return false;
        }

        if ((scheme != null) && (other.scheme != null) && (! scheme.equals(other.scheme)))
        {
            return false;
        }

        String effectivePort      = getEffectivePort(this, other);
        String otherEffectivePort = getEffectivePort(other, this);

        return (host.equals(other.host)) && (Objects.equals(effectivePort, otherEffectivePort));
    }


    /**
     * Return the port of a location, using the default port of the other location's scheme if the location has
     * neither a port nor a scheme of its own.
     *
     * @param location location
     * @param other location it is being compared with
     * @return port or null
     */
    private static String getEffectivePort(OpenLineageNamespace location,
                                           OpenLineageNamespace other)
    {
        if (location.port != null)
        {
            return location.port;
        }

        String scheme = (location.scheme != null) ? location.scheme : other.scheme;

        return (scheme == null) ? null : DEFAULT_PORTS.get(scheme);
    }


    /**
     * Parse a JDBC URL (without its jdbc: prefix).
     *
     * @param url JDBC URL after jdbc:
     * @return parsed address or null
     */
    private static OpenLineageNamespace fromJDBCURL(String url)
    {
        int colon = url.indexOf(':');

        if (colon <= 0)
        {
            return null;
        }

        String scheme    = getCanonicalScheme(url.substring(0, colon));
        String remainder = url.substring(colon + 1);

        if ("oracle".equals(scheme))
        {
            /*
             * jdbc:oracle:thin:@//host:port/service, jdbc:oracle:thin:@host:port/service or jdbc:oracle:thin:@host:port:sid
             */
            int at = remainder.indexOf('@');

            if (at < 0)
            {
                return null;
            }

            String address = remainder.substring(at + 1);

            if (address.startsWith("//"))
            {
                address = address.substring(2);
            }

            String[] hostPortService = address.split("[:/]", 3);

            if (hostPortService.length < 2)
            {
                return null;
            }

            String service = (hostPortService.length == 3) ? getFirstPathSegment("/" + hostPortService[2]) : null;

            return new OpenLineageNamespace(scheme, hostPortService[0].toLowerCase(), hostPortService[1], service, null);
        }

        if (! remainder.startsWith("//"))
        {
            /*
             * For example jdbc:duckdb:/path/to/file - there is no server.
             */
            return new OpenLineageNamespace(scheme, null, null, null, null);
        }

        remainder = remainder.substring(2);

        String authority = getAuthority(remainder);
        String rest      = remainder.substring(authority.length());
        String database  = getFirstPathSegment(rest);
        String schema    = getParameter(rest, "currentSchema");

        if (database == null)
        {
            database = getParameter(rest, "databaseName");
        }

        if (database == null)
        {
            database = getParameter(rest, "database");
        }

        return fromAuthority(scheme, authority, database, schema);
    }


    /**
     * Build a location from a host[:port] authority.
     *
     * @param scheme canonical scheme
     * @param authority host and optional port (and optional user information)
     * @param database database name or null
     * @param schema schema name or null
     * @return location
     */
    private static OpenLineageNamespace fromAuthority(String scheme,
                                                      String authority,
                                                      String database,
                                                      String schema)
    {
        String hostAndPort = authority;
        int    userInfoEnd = hostAndPort.lastIndexOf('@');

        if (userInfoEnd >= 0)
        {
            hostAndPort = hostAndPort.substring(userInfoEnd + 1);
        }

        if (hostAndPort.isEmpty())
        {
            return new OpenLineageNamespace(scheme, null, null, database, schema);
        }

        String host = hostAndPort;
        String port = null;
        int    portSeparator = hostAndPort.lastIndexOf(':');

        if ((portSeparator > 0) && (! hostAndPort.endsWith("]")))
        {
            host = hostAndPort.substring(0, portSeparator);
            port = hostAndPort.substring(portSeparator + 1);
        }

        if ((port == null) || (port.isEmpty()))
        {
            port = (scheme == null) ? null : DEFAULT_PORTS.get(scheme);
        }

        return new OpenLineageNamespace(scheme, host.toLowerCase(), port, database, schema);
    }


    /**
     * Return the authority at the start of the text after the scheme separator - it ends at the first path,
     * query or parameter separator.
     *
     * @param text text after ://
     * @return authority (may be empty)
     */
    private static String getAuthority(String text)
    {
        int end = text.length();

        for (char separator : new char[]{'/', '?', ';', '#'})
        {
            int index = text.indexOf(separator);

            if ((index >= 0) && (index < end))
            {
                end = index;
            }
        }

        return text.substring(0, end);
    }


    /**
     * Return the first segment of a path such as /sales?currentSchema=public.
     *
     * @param path path (may start with parameters rather than a path)
     * @return segment or null
     */
    private static String getFirstPathSegment(String path)
    {
        if ((path == null) || (! path.startsWith("/")))
        {
            return null;
        }

        String segment = path.substring(1);
        int    end     = segment.length();

        for (char separator : new char[]{'/', '?', ';', '#'})
        {
            int index = segment.indexOf(separator);

            if ((index >= 0) && (index < end))
            {
                end = index;
            }
        }

        segment = segment.substring(0, end);

        return segment.isEmpty() ? null : segment;
    }


    /**
     * Return a parameter from the query (?name=value&amp;...) or SQL Server style (;name=value;...) part of a URL.
     *
     * @param text text after the authority
     * @param name parameter name (matched ignoring case)
     * @return value or null
     */
    private static String getParameter(String text,
                                       String name)
    {
        for (String parameter : text.split("[?;&]"))
        {
            int equals = parameter.indexOf('=');

            if ((equals > 0) && (parameter.substring(0, equals).equalsIgnoreCase(name)))
            {
                String value = parameter.substring(equals + 1);

                return value.isEmpty() ? null : value;
            }
        }

        return null;
    }
}

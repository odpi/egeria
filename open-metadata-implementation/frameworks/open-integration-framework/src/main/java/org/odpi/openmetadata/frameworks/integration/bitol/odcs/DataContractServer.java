/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolCustomProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a server (infrastructure location) where the data described by an Open Data Contract
 * Standard (ODCS) data contract resides. The "type" of server (postgresql, kafka, s3, bigquery, ...) determines
 * which of the location properties are relevant. This bean holds the union of the location properties defined
 * for all server types by the standard, and captures any others in additionalProperties so that nothing is lost
 * on a round trip.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataContractServer
{
    private String                    id = null;
    private String                    server = null;
    private String                    type = null;
    private String                    description = null;
    private String                    environment = null;
    private List<DataContractRole>    roles = null;
    private String                    account = null;
    private String                    catalog = null;
    private String                    database = null;
    private String                    dataset = null;
    private String                    schema = null;
    private String                    host = null;
    private Object                   port = null;
    private String                    location = null;
    private String                    endpointUrl = null;
    private String                    path = null;
    private String                    format = null;
    private String                    delimiter = null;
    private String                    project = null;
    private String                    region = null;
    private String                    regionName = null;
    private String                    serviceName = null;
    private String                    stagingDir = null;
    private String                    warehouse = null;
    private String                    stream = null;
    private List<BitolCustomProperty> customProperties = null;
    private Map<String, Object>       additionalProperties = null;
    private String encoding = null;
    private String workgroup = null;
    private String catalogUrl = null;
    private String namespace = null;


    /**
     * Default constructor
     */
    public DataContractServer()
    {
    }


    /**
     * Return the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @return string identifier
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @param id string identifier
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the identifier of the server.
     *
     * @return string name
     */
    public String getServer()
    {
        return server;
    }


    /**
     * Set up the identifier of the server.
     *
     * @param server string name
     */
    public void setServer(String server)
    {
        this.server = server;
    }


    /**
     * Return the type of server.  Standard values are defined in DataContractServerType.
     *
     * @return string type name
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the type of server.  Standard values are defined in DataContractServerType.
     *
     * @param type string type name
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the description of the server.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the server.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the environment of the server, for example production or development.
     *
     * @return string environment name
     */
    public String getEnvironment()
    {
        return environment;
    }


    /**
     * Set up the environment of the server, for example production or development.
     *
     * @param environment string environment name
     */
    public void setEnvironment(String environment)
    {
        this.environment = environment;
    }


    /**
     * Return the list of access roles specific to this server.
     *
     * @return list of roles
     */
    public List<DataContractRole> getRoles()
    {
        return roles;
    }


    /**
     * Set up the list of access roles specific to this server.
     *
     * @param roles list of roles
     */
    public void setRoles(List<DataContractRole> roles)
    {
        this.roles = roles;
    }


    /**
     * Return the account (for example Snowflake, Redshift, Glue).
     *
     * @return string account name
     */
    public String getAccount()
    {
        return account;
    }


    /**
     * Set up the account (for example Snowflake, Redshift, Glue).
     *
     * @param account string account name
     */
    public void setAccount(String account)
    {
        this.account = account;
    }


    /**
     * Return the catalog (for example Databricks, Trino, Presto, Athena).
     *
     * @return string catalog name
     */
    public String getCatalog()
    {
        return catalog;
    }


    /**
     * Set up the catalog (for example Databricks, Trino, Presto, Athena).
     *
     * @param catalog string catalog name
     */
    public void setCatalog(String catalog)
    {
        this.catalog = catalog;
    }


    /**
     * Return the database.
     *
     * @return string database name
     */
    public String getDatabase()
    {
        return database;
    }


    /**
     * Set up the database.
     *
     * @param database string database name
     */
    public void setDatabase(String database)
    {
        this.database = database;
    }


    /**
     * Return the dataset (for example BigQuery).
     *
     * @return string dataset name
     */
    public String getDataset()
    {
        return dataset;
    }


    /**
     * Set up the dataset (for example BigQuery).
     *
     * @param dataset string dataset name
     */
    public void setDataset(String dataset)
    {
        this.dataset = dataset;
    }


    /**
     * Return the schema within the database.
     *
     * @return string schema name
     */
    public String getSchema()
    {
        return schema;
    }


    /**
     * Set up the schema within the database.
     *
     * @param schema string schema name
     */
    public void setSchema(String schema)
    {
        this.schema = schema;
    }


    /**
     * Return the host name (and optionally port) of the server.
     *
     * @return string host name
     */
    public String getHost()
    {
        return host;
    }


    /**
     * Set up the host name (and optionally port) of the server.
     *
     * @param host string host name
     */
    public void setHost(String host)
    {
        this.host = host;
    }


    /**
     * Return the port number of the server.  It is normally an integer but may be a string holding a variable reference such as ${DB_PORT}.
     *
     * @return integer value or null if not specified
     */
    public Object getPort()
    {
        return port;
    }


    /**
     * Set up the port number of the server.  It is normally an integer but may be a string holding a variable reference such as ${DB_PORT}.
     *
     * @param port integer value or null if not specified
     */
    public void setPort(Object port)
    {
        this.port = port;
    }


    /**
     * Return the location URI (for example s3://, abfss://, sftp:// or an API URL).
     *
     * @return string URI
     */
    public String getLocation()
    {
        return location;
    }


    /**
     * Set up the location URI (for example s3://, abfss://, sftp:// or an API URL).
     *
     * @param location string URI
     */
    public void setLocation(String location)
    {
        this.location = location;
    }


    /**
     * Return the endpoint URL (for example an S3 compatible endpoint).
     *
     * @return string URL
     */
    public String getEndpointUrl()
    {
        return endpointUrl;
    }


    /**
     * Set up the endpoint URL (for example an S3 compatible endpoint).
     *
     * @param endpointUrl string URL
     */
    public void setEndpointUrl(String endpointUrl)
    {
        this.endpointUrl = endpointUrl;
    }


    /**
     * Return the file path (for local servers).
     *
     * @return string path
     */
    public String getPath()
    {
        return path;
    }


    /**
     * Set up the file path (for local servers).
     *
     * @param path string path
     */
    public void setPath(String path)
    {
        this.path = path;
    }


    /**
     * Return the data format (for example parquet, json, avro, csv).
     *
     * @return string format name
     */
    public String getFormat()
    {
        return format;
    }


    /**
     * Set up the data format (for example parquet, json, avro, csv).
     *
     * @param format string format name
     */
    public void setFormat(String format)
    {
        this.format = format;
    }


    /**
     * Return the delimiter for delimited formats.
     *
     * @return string delimiter
     */
    public String getDelimiter()
    {
        return delimiter;
    }


    /**
     * Set up the delimiter for delimited formats.
     *
     * @param delimiter string delimiter
     */
    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }


    /**
     * Return the project (for example BigQuery or Pub/Sub).
     *
     * @return string project name
     */
    public String getProject()
    {
        return project;
    }


    /**
     * Set up the project (for example BigQuery or Pub/Sub).
     *
     * @param project string project name
     */
    public void setProject(String project)
    {
        this.project = project;
    }


    /**
     * Return the cloud region (for example Kinesis or Redshift).
     *
     * @return string region name
     */
    public String getRegion()
    {
        return region;
    }


    /**
     * Set up the cloud region (for example Kinesis or Redshift).
     *
     * @param region string region name
     */
    public void setRegion(String region)
    {
        this.region = region;
    }


    /**
     * Return the cloud region name (for example Athena).
     *
     * @return string region name
     */
    public String getRegionName()
    {
        return regionName;
    }


    /**
     * Set up the cloud region name (for example Athena).
     *
     * @param regionName string region name
     */
    public void setRegionName(String regionName)
    {
        this.regionName = regionName;
    }


    /**
     * Return the service name (for example Oracle).
     *
     * @return string service name
     */
    public String getServiceName()
    {
        return serviceName;
    }


    /**
     * Set up the service name (for example Oracle).
     *
     * @param serviceName string service name
     */
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }


    /**
     * Return the staging directory URI (for example Athena).
     *
     * @return string URI
     */
    public String getStagingDir()
    {
        return stagingDir;
    }


    /**
     * Set up the staging directory URI (for example Athena).
     *
     * @param stagingDir string URI
     */
    public void setStagingDir(String stagingDir)
    {
        this.stagingDir = stagingDir;
    }


    /**
     * Return the warehouse (for example Snowflake).
     *
     * @return string warehouse name
     */
    public String getWarehouse()
    {
        return warehouse;
    }


    /**
     * Set up the warehouse (for example Snowflake).
     *
     * @param warehouse string warehouse name
     */
    public void setWarehouse(String warehouse)
    {
        this.warehouse = warehouse;
    }


    /**
     * Return the stream name (for example Kinesis).
     *
     * @return string stream name
     */
    public String getStream()
    {
        return stream;
    }


    /**
     * Set up the stream name (for example Kinesis).
     *
     * @param stream string stream name
     */
    public void setStream(String stream)
    {
        this.stream = stream;
    }


    /**
     * Return the list of custom (key/value) properties attached to this element.
     *
     * @return list of custom properties
     */
    public List<BitolCustomProperty> getCustomProperties()
    {
        return customProperties;
    }


    /**
     * Set up the list of custom (key/value) properties attached to this element.
     *
     * @param customProperties list of custom properties
     */
    public void setCustomProperties(List<BitolCustomProperty> customProperties)
    {
        this.customProperties = customProperties;
    }


    /**
     * Return any server properties that are not explicitly modelled by this bean.
     *
     * @return map from property name to value
     */
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up any server properties that are not explicitly modelled by this bean.
     *
     * @param additionalProperties map from property name to value
     */
    public void setAdditionalProperties(Map<String, Object> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Add a single property that is not explicitly modelled by this bean.  Called by Jackson for each unrecognized property
     * so that no part of the document is lost on a round trip.
     *
     * @param propertyName name of the property
     * @param propertyValue value of the property
     */
    @JsonAnySetter
    public void setAdditionalProperty(String propertyName,
                                      Object propertyValue)
    {
        if (additionalProperties == null)
        {
            additionalProperties = new HashMap<>();
        }

        additionalProperties.put(propertyName, propertyValue);
    }


    /**
     * Return the character encoding of the data payloads exposed through this server, for example UTF-8.
     *
     * @return String
     */
    public String getEncoding()
    {
        return encoding;
    }


    /**
     * Set up the character encoding of the data payloads exposed through this server, for example UTF-8.
     *
     * @param encoding String
     */
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }


    /**
     * Return the Amazon Athena workgroup.
     *
     * @return String
     */
    public String getWorkgroup()
    {
        return workgroup;
    }


    /**
     * Set up the Amazon Athena workgroup.
     *
     * @param workgroup String
     */
    public void setWorkgroup(String workgroup)
    {
        this.workgroup = workgroup;
    }


    /**
     * Return the URL of an Apache Iceberg REST catalog.
     *
     * @return String
     */
    public String getCatalogUrl()
    {
        return catalogUrl;
    }


    /**
     * Set up the URL of an Apache Iceberg REST catalog.
     *
     * @param catalogUrl String
     */
    public void setCatalogUrl(String catalogUrl)
    {
        this.catalogUrl = catalogUrl;
    }


    /**
     * Return the namespace within an Apache Iceberg catalog.
     *
     * @return String
     */
    public String getNamespace()
    {
        return namespace;
    }


    /**
     * Set up the namespace within an Apache Iceberg catalog.
     *
     * @param namespace String
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataContractServer{" +
                       "id='" + id + '\'' +
                       ", server='" + server + '\'' +
                       ", type='" + type + '\'' +
                       ", description='" + description + '\'' +
                       ", environment='" + environment + '\'' +
                       ", roles=" + roles +
                       ", account='" + account + '\'' +
                       ", catalog='" + catalog + '\'' +
                       ", database='" + database + '\'' +
                       ", dataset='" + dataset + '\'' +
                       ", schema='" + schema + '\'' +
                       ", host='" + host + '\'' +
                       ", port=" + port +
                       ", location='" + location + '\'' +
                       ", endpointUrl='" + endpointUrl + '\'' +
                       ", path='" + path + '\'' +
                       ", format='" + format + '\'' +
                       ", delimiter='" + delimiter + '\'' +
                       ", project='" + project + '\'' +
                       ", region='" + region + '\'' +
                       ", regionName='" + regionName + '\'' +
                       ", serviceName='" + serviceName + '\'' +
                       ", stagingDir='" + stagingDir + '\'' +
                       ", warehouse='" + warehouse + '\'' +
                       ", stream='" + stream + '\'' +
                       ", customProperties=" + customProperties +
                       ", additionalProperties=" + additionalProperties +
                       ", encoding=" + encoding +
                       ", workgroup=" + workgroup +
                       ", catalogUrl=" + catalogUrl +
                       ", namespace=" + namespace +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        DataContractServer that = (DataContractServer) objectToCompare;
        return Objects.equals(id, that.id) &&
                       Objects.equals(server, that.server) &&
                       Objects.equals(type, that.type) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(environment, that.environment) &&
                       Objects.equals(roles, that.roles) &&
                       Objects.equals(account, that.account) &&
                       Objects.equals(catalog, that.catalog) &&
                       Objects.equals(database, that.database) &&
                       Objects.equals(dataset, that.dataset) &&
                       Objects.equals(schema, that.schema) &&
                       Objects.equals(host, that.host) &&
                       Objects.equals(port, that.port) &&
                       Objects.equals(location, that.location) &&
                       Objects.equals(endpointUrl, that.endpointUrl) &&
                       Objects.equals(path, that.path) &&
                       Objects.equals(format, that.format) &&
                       Objects.equals(delimiter, that.delimiter) &&
                       Objects.equals(project, that.project) &&
                       Objects.equals(region, that.region) &&
                       Objects.equals(regionName, that.regionName) &&
                       Objects.equals(serviceName, that.serviceName) &&
                       Objects.equals(stagingDir, that.stagingDir) &&
                       Objects.equals(warehouse, that.warehouse) &&
                       Objects.equals(stream, that.stream) &&
                       Objects.equals(customProperties, that.customProperties) &&
                       Objects.equals(additionalProperties, that.additionalProperties) &&
                       Objects.equals(encoding, that.encoding) &&
                       Objects.equals(workgroup, that.workgroup) &&
                       Objects.equals(catalogUrl, that.catalogUrl) &&
                       Objects.equals(namespace, that.namespace);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(id, server, type, description, environment, roles, account, catalog, database, dataset, schema, host, port, location, endpointUrl, path, format, delimiter, project, region, regionName, serviceName, stagingDir, warehouse, stream, customProperties, additionalProperties, encoding, workgroup, catalogUrl, namespace);
    }
}

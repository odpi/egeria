/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.MatchCriteria;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyCondition;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OpenLineageDataSetResolver finds every element in open metadata that describes an OpenLineage dataset.  The
 * dataset's namespace names the server (for example postgres://host:5432) and its name the resource within it (for
 * example sales.public.orders).  The matches are divided into two lists:
 * <ul>
 *     <li><b>Physical elements</b> describe the physical landscape - the database table, topic or file itself.
 *     These are the elements that the lineage from OpenLineage events is attached to.</li>
 *     <li><b>Abstractions</b> - TabularDataSet and TabularDataSetCollection assets - are views over the physical
 *     data (for example the data sets of digital products).  They are linked to the physical elements with
 *     DataSetContent rather than carrying lineage themselves.</li>
 * </ul>
 * The matches come from:
 * <ul>
 *     <li>the qualified name, for datasets in the "egeria" namespace;</li>
 *     <li>the resourceName and namespacePath properties, which hold the OpenLineage name and namespace for elements
 *     catalogued from OpenLineage events or by connectors that follow the OpenLineage naming conventions;</li>
 *     <li>the endpoints of the connections of the elements catalogued by Egeria's technology connectors:
 *     <ul>
 *         <li>relational tables (postgres, mssql, oracle and db2 namespaces with a {database}.{schema}.{table} name):
 *         the database's endpoint (a JDBC URL naming the host, port and database) leads to the RelationalDatabase
 *         or DeployedDatabaseSchema asset, and the table is the RelationalTable schema attribute that the JDBC
 *         integration connector catalogued beneath it ({databaseQN}::{schema}::{table} or {schemaQN}::{table});</li>
 *         <li>relational schemas ({database}.{schema} name, as used for the endpoint of a tabular data set
 *         collection): the DeployedDatabaseSchema;</li>
 *         <li>Kafka topics: the topic whose endpoint names the topic and whose connection's bootstrap servers include
 *         the broker;</li>
 *         <li>local files: the data stores whose pathName is the dataset name;</li>
 *         <li>Unity Catalog tables: the table named {deployedImplementationType}::{server URL}::{full name} by the
 *         Unity Catalog connectors.</li>
 *     </ul></li>
 * </ul>
 * An abstraction that is the dataset itself (for example a tabular data set collection named in the egeria
 * namespace) is also resolved through its own endpoint to the physical element it is a view over.
 * <p>
 * The physical elements are ranked so that the first one is the element to use: elements catalogued by technology
 * connectors come before elements that were created from OpenLineage events (whose qualified name is
 * {typeName}::{namespace}::{name}), and within each group the oldest comes first.
 * <p>
 * Results are cached for a short time because the same datasets appear in every run of a job.
 */
public class OpenLineageDataSetResolver
{
    /**
     * An element that describes an OpenLineage dataset.
     *
     * @param guid unique identifier of the element
     * @param qualifiedName qualified name of the element
     * @param typeName open metadata type of the element
     * @param createTime when the element was created
     * @param schemaAttribute the element is a schema attribute (for example a RelationalTable) rather than an asset
     * @param createdFromOpenLineage the element was created from OpenLineage events (it follows the
     *                               {typeName}::{namespace}::{name} naming)
     * @param parentAssetGUID for a schema attribute, the asset it belongs to (for example the DeployedDatabaseSchema
     *                        or RelationalDatabase of a RelationalTable)
     * @param parentAssetQualifiedName qualified name of the parent asset
     * @param coversParent for an abstraction, it is a view over the dataset's parent (for example a tabular data set
     *                     collection over the schema that holds the table) rather than over the dataset itself
     */
    public record MatchedElement(String  guid,
                                 String  qualifiedName,
                                 String  typeName,
                                 Date    createTime,
                                 boolean schemaAttribute,
                                 boolean createdFromOpenLineage,
                                 String  parentAssetGUID,
                                 String  parentAssetQualifiedName,
                                 boolean coversParent) {}


    /**
     * The elements that describe an OpenLineage dataset.
     *
     * @param physical physical elements, ranked so that the first is the one to use
     * @param abstractions tabular data sets and tabular data set collections that are views over the dataset
     */
    public record ResolvedDataSet(List<MatchedElement> physical,
                                  List<MatchedElement> abstractions)
    {
        /**
         * Return the physical element to use for the dataset.
         *
         * @return matched element or null if there is none
         */
        public MatchedElement primary()
        {
            return physical.isEmpty() ? null : physical.get(0);
        }
    }


    private record CachedResolution(ResolvedDataSet resolvedDataSet, long expiryTime) {}

    private static final long   CACHE_MILLISECONDS       = 5 * 60 * 1000L;
    private static final String QUALIFIED_NAME_SEPARATOR = "::";
    private static final String UNKNOWN_NAMESPACE        = "default";

    /**
     * The qualified names of Unity Catalog tables start with this deployed implementation type
     * (UnityCatalogDeployedImplementationType.OSS_UC_TABLE).
     */
    private static final String UNITY_CATALOG_TABLE = "Unity Catalog Table";

    /**
     * Databases that fold unquoted identifiers to upper case, so their catalogued names may be in upper case even
     * when the OpenLineage producer reports them in lower case.
     */
    private static final Set<String> UPPER_CASE_SCHEMES = Set.of("oracle", "db2");

    /**
     * Schemes whose datasets are relational tables named {database}.{schema}.{table}.
     */
    private static final Set<String> RELATIONAL_SCHEMES = Set.of("postgres", "mssql", "oracle", "db2");

    private final ConnectorContextBase           context;
    private final String                         sourceName;
    private final PropertyHelper                 propertyHelper = new PropertyHelper();
    private final Map<String, CachedResolution>  cache          = new ConcurrentHashMap<>();


    /**
     * Constructor.
     *
     * @param context context of the calling connector or governance service
     * @param sourceName name of the caller (for logging and property helper calls)
     */
    public OpenLineageDataSetResolver(ConnectorContextBase context,
                                      String               sourceName)
    {
        this.context    = context;
        this.sourceName = sourceName;
    }


    /**
     * Return the qualified name that elements created from OpenLineage events are given.
     *
     * @param typeName open metadata type of the element
     * @param namespace OpenLineage namespace
     * @param name OpenLineage name
     * @return qualified name
     */
    public static String getOpenLineageQualifiedName(String typeName,
                                                     String namespace,
                                                     String name)
    {
        return typeName + QUALIFIED_NAME_SEPARATOR + ((namespace == null) ? UNKNOWN_NAMESPACE : namespace) + QUALIFIED_NAME_SEPARATOR + name;
    }


    /**
     * Is the element an abstraction over physical data (a TabularDataSet or TabularDataSetCollection)?
     *
     * @param elementHeader header of the element
     * @return boolean
     */
    public boolean isAbstraction(ElementControlHeader elementHeader)
    {
        return (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.TABULAR_DATA_SET.typeName)) ||
               (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName));
    }


    /**
     * Find every element that describes an OpenLineage dataset.
     *
     * @param namespace dataset namespace
     * @param name dataset name
     * @return resolved dataset (the lists are empty if nothing matches)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    public ResolvedDataSet resolve(String namespace,
                                   String name) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        if (name == null)
        {
            return new ResolvedDataSet(new ArrayList<>(), new ArrayList<>());
        }

        String           key    = namespace + "|" + name;
        CachedResolution cached = cache.get(key);

        if ((cached != null) && (cached.expiryTime() > System.currentTimeMillis()))
        {
            return cached.resolvedDataSet();
        }

        Map<String, MatchedElement> physical     = new LinkedHashMap<>();
        Map<String, MatchedElement> abstractions = new LinkedHashMap<>();

        if (OpenLineageNamespace.EGERIA_NAMESPACE.equals(namespace))
        {
            OpenMetadataElement element = context.getOpenMetadataStore().getMetadataElementByUniqueName(name, OpenMetadataProperty.QUALIFIED_NAME.name);

            if (element != null)
            {
                addMatch(element, namespace, name, false, null, null, physical, abstractions);
            }
        }
        else
        {
            for (OpenMetadataElement element : findByOpenLineageIdentity(namespace, name))
            {
                addMatch(element, namespace, name, false, null, null, physical, abstractions);
            }

            addTechnologyMatches(OpenLineageNamespace.fromNamespace(namespace), namespace, name, physical, abstractions);
        }

        /*
         * An abstraction that is the dataset itself is a view over a physical element - find it through the
         * abstraction's own endpoint.
         */
        for (MatchedElement abstraction : new ArrayList<>(abstractions.values()))
        {
            if (! abstraction.coversParent())
            {
                addMatchesForAbstraction(abstraction, physical, abstractions);
            }
        }

        List<MatchedElement> rankedPhysical = new ArrayList<>(physical.values());

        rankedPhysical.sort(Comparator.comparing(MatchedElement::createdFromOpenLineage)
                                      .thenComparing(MatchedElement::createTime, Comparator.nullsLast(Comparator.naturalOrder()))
                                      .thenComparing(MatchedElement::guid));

        ResolvedDataSet resolvedDataSet = new ResolvedDataSet(rankedPhysical, new ArrayList<>(abstractions.values()));

        cache.put(key, new CachedResolution(resolvedDataSet, System.currentTimeMillis() + CACHE_MILLISECONDS));

        return resolvedDataSet;
    }


    /**
     * Forget a cached resolution, for example because an element has been created for the dataset.
     *
     * @param namespace dataset namespace
     * @param name dataset name
     */
    public void forget(String namespace,
                       String name)
    {
        cache.remove(namespace + "|" + name);
    }


    /**
     * Classify a matching element and add it to the physical or abstraction list.
     *
     * @param element matching element
     * @param namespace dataset namespace
     * @param name dataset name
     * @param coversParent the element is an abstraction over the dataset's parent
     * @param parentAssetGUID parent asset of a schema attribute
     * @param parentAssetQualifiedName qualified name of the parent asset
     * @param physical physical elements found so far
     * @param abstractions abstractions found so far
     */
    private void addMatch(OpenMetadataElement         element,
                          String                      namespace,
                          String                      name,
                          boolean                     coversParent,
                          String                      parentAssetGUID,
                          String                      parentAssetQualifiedName,
                          Map<String, MatchedElement> physical,
                          Map<String, MatchedElement> abstractions)
    {
        if ((element == null) || (element.getElementGUID() == null) || (element.getType() == null) || (isArchived(element)))
        {
            return;
        }

        String  typeName      = element.getType().getTypeName();
        String  qualifiedName = getStringProperty(element, OpenMetadataProperty.QUALIFIED_NAME.name);
        Date    createTime    = (element.getVersions() == null) ? null : element.getVersions().getCreateTime();
        boolean isAbstraction = isAbstraction(element);

        MatchedElement matchedElement = new MatchedElement(element.getElementGUID(),
                                                           qualifiedName,
                                                           typeName,
                                                           createTime,
                                                           propertyHelper.isTypeOf(element, OpenMetadataType.SCHEMA_ATTRIBUTE.typeName),
                                                           isCreatedFromOpenLineage(qualifiedName, namespace, name),
                                                           parentAssetGUID,
                                                           parentAssetQualifiedName,
                                                           isAbstraction && coversParent);

        /*
         * An element created from OpenLineage events for the dataset is always the physical element, whatever its
         * type - older releases created tabular data sets for tables.
         */
        if ((isAbstraction) && (! matchedElement.createdFromOpenLineage()))
        {
            abstractions.putIfAbsent(matchedElement.guid(), matchedElement);
        }
        else
        {
            physical.putIfAbsent(matchedElement.guid(), matchedElement);
        }
    }


    /**
     * Has the element been archived (it has the Memento classification)?  An archived element no longer describes
     * live data - for example a dataset that an OpenLineage event reported as dropped - so it is not a match.
     *
     * @param element element
     * @return boolean
     */
    private boolean isArchived(OpenMetadataElement element)
    {
        if (element.getClassifications() != null)
        {
            for (AttachedClassification classification : element.getClassifications())
            {
                if ((classification != null) && (OpenMetadataType.MEMENTO_CLASSIFICATION.typeName.equals(classification.getClassificationName())))
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Was the element created from OpenLineage events for this dataset?  Such elements are named
     * {typeName}::{namespace}::{name}.
     *
     * @param qualifiedName qualified name of the element
     * @param namespace dataset namespace
     * @param name dataset name
     * @return boolean
     */
    private boolean isCreatedFromOpenLineage(String qualifiedName,
                                             String namespace,
                                             String name)
    {
        if ((qualifiedName == null) || (OpenLineageNamespace.EGERIA_NAMESPACE.equals(namespace)))
        {
            return false;
        }

        return qualifiedName.endsWith(QUALIFIED_NAME_SEPARATOR + ((namespace == null) ? UNKNOWN_NAMESPACE : namespace) + QUALIFIED_NAME_SEPARATOR + name);
    }


    /**
     * Return the elements whose resourceName and namespacePath are the dataset's OpenLineage name and namespace.
     *
     * @param namespace dataset namespace
     * @param name dataset name
     * @return matching elements
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private List<OpenMetadataElement> findByOpenLineageIdentity(String namespace,
                                                                String name) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        if (namespace == null)
        {
            return new ArrayList<>();
        }

        List<PropertyCondition> conditions = propertyHelper.addStringProperty(null, OpenMetadataProperty.RESOURCE_NAME.name, name, PropertyComparisonOperator.EQ);

        conditions = propertyHelper.addStringProperty(conditions, OpenMetadataProperty.NAMESPACE_PATH.name, namespace, PropertyComparisonOperator.EQ);

        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(conditions);
        searchProperties.setMatchCriteria(MatchCriteria.ALL);

        return findElements(OpenMetadataType.REFERENCEABLE.typeName, searchProperties);
    }


    /**
     * Add the elements that Egeria's technology connectors catalogued for the dataset.
     *
     * @param location parsed namespace
     * @param namespace dataset namespace
     * @param name dataset name
     * @param physical physical elements found so far
     * @param abstractions abstractions found so far
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void addTechnologyMatches(OpenLineageNamespace        location,
                                      String                      namespace,
                                      String                      name,
                                      Map<String, MatchedElement> physical,
                                      Map<String, MatchedElement> abstractions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        if ((location == null) || (location.scheme() == null))
        {
            return;
        }

        if (RELATIONAL_SCHEMES.contains(location.scheme()))
        {
            addRelationalMatches(location, namespace, name, physical, abstractions);
        }
        else if ("kafka".equals(location.scheme()))
        {
            addTopicMatches(location, namespace, name, physical, abstractions);
        }
        else if (("file".equals(location.scheme())) && (location.host() == null))
        {
            for (OpenMetadataElement dataStore : findElements(OpenMetadataType.DATA_STORE.typeName, OpenMetadataProperty.PATH_NAME.name, name, PropertyComparisonOperator.EQ))
            {
                addMatch(dataStore, namespace, name, false, null, null, physical, abstractions);
            }
        }
        else if ("unitycatalog".equals(location.scheme()))
        {
            addUnityCatalogMatches(location, namespace, name, physical, abstractions);
        }
    }


    /**
     * Add the catalogued relational tables (for a {database}.{schema}.{table} name) or schemas (for a
     * {database}.{schema} name), and the tabular data sets and collections that are views over them.
     *
     * @param location parsed namespace
     * @param namespace dataset namespace
     * @param name dataset name
     * @param physical physical elements found so far
     * @param abstractions abstractions found so far
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void addRelationalMatches(OpenLineageNamespace        location,
                                      String                      namespace,
                                      String                      name,
                                      Map<String, MatchedElement> physical,
                                      Map<String, MatchedElement> abstractions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        String[] nameParts = name.split("\\.", 3);

        if ((nameParts.length < 2) || (location.host() == null))
        {
            return;
        }

        String  databaseName = nameParts[0];
        String  schemaName   = nameParts[1];
        String  tableName    = (nameParts.length == 3) ? nameParts[2] : null;

        for (OpenMetadataElement endpoint : findElements(OpenMetadataType.ENDPOINT.typeName,
                                                         OpenMetadataProperty.NETWORK_ADDRESS.name,
                                                         location.host(),
                                                         PropertyComparisonOperator.CASE_INSENSITIVE_LIKE))
        {
            OpenLineageNamespace address = OpenLineageNamespace.fromNetworkAddress(getStringProperty(endpoint, OpenMetadataProperty.NETWORK_ADDRESS.name));

            if ((address == null) || (! location.isSameServer(address)) || (! databaseName.equalsIgnoreCase(address.database())))
            {
                continue;
            }

            for (OpenMetadataElement asset : getAssetsForEndpoint(endpoint.getElementGUID()))
            {
                String assetQualifiedName = getStringProperty(asset, OpenMetadataProperty.QUALIFIED_NAME.name);

                if (assetQualifiedName == null)
                {
                    continue;
                }

                if (isAbstraction(asset))
                {
                    addRelationalAbstraction(asset, address, namespace, name, databaseName, schemaName, tableName, physical, abstractions);
                }
                else if (propertyHelper.isTypeOf(asset, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName))
                {
                    /*
                     * A schema catalogued from its own template: its endpoint names the schema.
                     */
                    if ((address.schema() == null) || (schemaName.equalsIgnoreCase(address.schema())))
                    {
                        if (tableName == null)
                        {
                            addMatch(asset, namespace, name, false, null, null, physical, abstractions);
                        }
                        else
                        {
                            addTable(assetQualifiedName, null, tableName, asset.getElementGUID(), assetQualifiedName, location.scheme(), namespace, name, physical, abstractions);
                        }
                    }
                }
                else if (propertyHelper.isTypeOf(asset, OpenMetadataType.RELATIONAL_DATABASE.typeName))
                {
                    /*
                     * The JDBC integration connector names a schema under a database {databaseQN}::{schema}.  The
                     * table's parent asset is that schema if it is catalogued, otherwise the database.
                     */
                    OpenMetadataElement schema = findByQualifiedName(assetQualifiedName + QUALIFIED_NAME_SEPARATOR + schemaName,
                                                                     OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                                                                     location.scheme());

                    if (tableName == null)
                    {
                        if (schema != null)
                        {
                            addMatch(schema, namespace, name, false, null, null, physical, abstractions);
                        }
                    }
                    else if (schema != null)
                    {
                        String schemaQualifiedName = getStringProperty(schema, OpenMetadataProperty.QUALIFIED_NAME.name);

                        addTable(schemaQualifiedName, null, tableName, schema.getElementGUID(), schemaQualifiedName, location.scheme(), namespace, name, physical, abstractions);
                    }
                    else
                    {
                        addTable(assetQualifiedName, schemaName, tableName, asset.getElementGUID(), assetQualifiedName, location.scheme(), namespace, name, physical, abstractions);
                    }
                }
            }
        }
    }


    /**
     * Add a tabular data set or collection whose endpoint matches the dataset's database.  A collection is a view
     * over a schema; a tabular data set is a view over a single table (its display name is
     * {database}.{schema}.{table}).
     *
     * @param asset tabular data set or collection
     * @param address parsed endpoint of the asset
     * @param namespace dataset namespace
     * @param name dataset name
     * @param databaseName database of the dataset
     * @param schemaName schema of the dataset
     * @param tableName table of the dataset (null for a schema)
     * @param physical physical elements found so far
     * @param abstractions abstractions found so far
     */
    private void addRelationalAbstraction(OpenMetadataElement         asset,
                                          OpenLineageNamespace        address,
                                          String                      namespace,
                                          String                      name,
                                          String                      databaseName,
                                          String                      schemaName,
                                          String                      tableName,
                                          Map<String, MatchedElement> physical,
                                          Map<String, MatchedElement> abstractions)
    {
        if ((address.schema() != null) && (! schemaName.equalsIgnoreCase(address.schema())))
        {
            return;
        }

        if (propertyHelper.isTypeOf(asset, OpenMetadataType.TABULAR_DATA_SET_COLLECTION.typeName))
        {
            /*
             * The collection is a view over the schema: of the dataset itself if the dataset is the schema, or of
             * the table's parent if the dataset is a table.
             */
            addMatch(asset, namespace, name, tableName != null, null, null, physical, abstractions);
        }
        else if (tableName != null)
        {
            String displayName = getStringProperty(asset, OpenMetadataProperty.DISPLAY_NAME.name);
            String fullName    = databaseName + "." + schemaName + "." + tableName;

            if ((fullName.equalsIgnoreCase(displayName)) || (tableName.equalsIgnoreCase(displayName)))
            {
                addMatch(asset, namespace, name, false, null, null, physical, abstractions);
            }
        }
    }


    /**
     * Add the RelationalTable that the JDBC integration connector catalogued for a table.  Databases that fold
     * identifiers to upper case are also tried in upper case.
     *
     * @param parentQualifiedName qualified name of the database or schema asset
     * @param schemaName schema name (null when the parent is the schema)
     * @param tableName table name
     * @param parentAssetGUID asset that the table belongs to
     * @param parentAssetQualifiedName qualified name of that asset
     * @param scheme canonical scheme
     * @param namespace dataset namespace
     * @param name dataset name
     * @param physical physical elements found so far
     * @param abstractions abstractions found so far
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void addTable(String                      parentQualifiedName,
                          String                      schemaName,
                          String                      tableName,
                          String                      parentAssetGUID,
                          String                      parentAssetQualifiedName,
                          String                      scheme,
                          String                      namespace,
                          String                      name,
                          Map<String, MatchedElement> physical,
                          Map<String, MatchedElement> abstractions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        String suffix = (schemaName == null) ? tableName : schemaName + QUALIFIED_NAME_SEPARATOR + tableName;

        OpenMetadataElement table = findByQualifiedName(parentQualifiedName + QUALIFIED_NAME_SEPARATOR + suffix,
                                                        OpenMetadataType.RELATIONAL_TABLE.typeName,
                                                        scheme);

        if (table != null)
        {
            addMatch(table, namespace, name, false, parentAssetGUID, parentAssetQualifiedName, physical, abstractions);
        }
    }


    /**
     * Retrieve an element of a type by qualified name, trying the upper case form for databases that fold
     * identifiers to upper case.
     *
     * @param qualifiedName qualified name
     * @param typeName expected type
     * @param scheme canonical scheme
     * @return element or null
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private OpenMetadataElement findByQualifiedName(String qualifiedName,
                                                    String typeName,
                                                    String scheme) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        OpenMetadataStore   store   = context.getOpenMetadataStore();
        OpenMetadataElement element = store.getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);

        if ((element == null) && (UPPER_CASE_SCHEMES.contains(scheme)))
        {
            int    separator = qualifiedName.lastIndexOf(QUALIFIED_NAME_SEPARATOR);
            String upperCase = qualifiedName.substring(0, separator) + qualifiedName.substring(separator).toUpperCase();

            if (! upperCase.equals(qualifiedName))
            {
                element = store.getMetadataElementByUniqueName(upperCase, OpenMetadataProperty.QUALIFIED_NAME.name);
            }
        }

        if ((element != null) && (propertyHelper.isTypeOf(element, typeName)))
        {
            return element;
        }

        return null;
    }


    /**
     * Add the catalogued topics for a Kafka dataset: topics whose endpoint names the topic and whose connection's
     * bootstrap servers include the broker named in the namespace.
     *
     * @param location parsed namespace
     * @param namespace dataset namespace
     * @param topicName dataset name
     * @param physical physical elements found so far
     * @param abstractions abstractions found so far
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void addTopicMatches(OpenLineageNamespace        location,
                                 String                      namespace,
                                 String                      topicName,
                                 Map<String, MatchedElement> physical,
                                 Map<String, MatchedElement> abstractions) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        for (OpenMetadataElement endpoint : findElements(OpenMetadataType.ENDPOINT.typeName,
                                                         OpenMetadataProperty.NETWORK_ADDRESS.name,
                                                         topicName,
                                                         PropertyComparisonOperator.EQ))
        {
            for (OpenMetadataElement connection : getRelatedElements(endpoint.getElementGUID(), OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName))
            {
                if (isBootstrapServer(connection, location))
                {
                    for (OpenMetadataElement asset : getAssetsForConnection(connection))
                    {
                        if (propertyHelper.isTypeOf(asset, OpenMetadataType.TOPIC.typeName))
                        {
                            addMatch(asset, namespace, topicName, false, null, null, physical, abstractions);
                        }
                    }
                }
            }
        }
    }


    /**
     * Add the catalogued Unity Catalog tables for a {catalog}.{schema}.{table} dataset.  The namespace gives the host
     * and port but not whether the server URL uses http or https.
     *
     * @param location parsed namespace
     * @param namespace dataset namespace
     * @param name dataset name
     * @param physical physical elements found so far
     * @param abstractions abstractions found so far
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void addUnityCatalogMatches(OpenLineageNamespace        location,
                                        String                      namespace,
                                        String                      name,
                                        Map<String, MatchedElement> physical,
                                        Map<String, MatchedElement> abstractions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        if (location.host() == null)
        {
            return;
        }

        String authority = (location.port() == null) ? location.host() : location.host() + ":" + location.port();

        for (String protocol : new String[]{"http", "https"})
        {
            OpenMetadataElement table = context.getOpenMetadataStore().getMetadataElementByUniqueName(UNITY_CATALOG_TABLE + QUALIFIED_NAME_SEPARATOR +
                                                                                                              protocol + "://" + authority +
                                                                                                              QUALIFIED_NAME_SEPARATOR + name,
                                                                                                      OpenMetadataProperty.QUALIFIED_NAME.name);

            addMatch(table, namespace, name, false, null, null, physical, abstractions);
        }
    }


    /**
     * Resolve an abstraction that is the dataset itself to the physical element it is a view over, using the
     * abstraction's own endpoint.  A tabular data set collection's endpoint names a schema; a tabular data set's
     * endpoint names a schema and its display name ({database}.{schema}.{table}) names the table.
     *
     * @param abstraction abstraction
     * @param physical physical elements found so far
     * @param abstractions abstractions found so far
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void addMatchesForAbstraction(MatchedElement              abstraction,
                                          Map<String, MatchedElement> physical,
                                          Map<String, MatchedElement> abstractions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        OpenMetadataElement abstractionElement = context.getOpenMetadataStore().getMetadataElementByGUID(abstraction.guid());

        if (abstractionElement == null)
        {
            return;
        }

        for (String networkAddress : getNetworkAddresses(abstraction.guid()))
        {
            OpenLineageNamespace address = OpenLineageNamespace.fromNetworkAddress(networkAddress);

            if ((address == null) || (address.scheme() == null) || (address.host() == null) ||
                (address.database() == null) || (address.schema() == null) || (! RELATIONAL_SCHEMES.contains(address.scheme())))
            {
                continue;
            }

            String namespace = address.scheme() + "://" + address.host() + ((address.port() == null) ? "" : ":" + address.port());
            String name      = address.database() + "." + address.schema();

            if (propertyHelper.isTypeOf(abstractionElement, OpenMetadataType.TABULAR_DATA_SET.typeName))
            {
                String displayName = getStringProperty(abstractionElement, OpenMetadataProperty.DISPLAY_NAME.name);

                if ((displayName == null) || (! displayName.toLowerCase().startsWith(name.toLowerCase() + ".")))
                {
                    continue;
                }

                name = name + displayName.substring(name.length());
            }

            addRelationalMatches(OpenLineageNamespace.fromNamespace(namespace), namespace, name, physical, abstractions);
        }
    }


    /**
     * Return the network addresses of the endpoints of an asset's connections, including those of connections
     * embedded in a virtual connection.
     *
     * @param assetGUID asset
     * @return network addresses
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private Set<String> getNetworkAddresses(String assetGUID) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        Set<String>               networkAddresses = new LinkedHashSet<>();
        List<OpenMetadataElement> connections      = new ArrayList<>();

        for (String relationshipTypeName : new String[]{OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName, OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName})
        {
            for (OpenMetadataElement connection : getRelatedElements(assetGUID, relationshipTypeName))
            {
                if (propertyHelper.isTypeOf(connection, OpenMetadataType.CONNECTION.typeName))
                {
                    connections.add(connection);

                    if (propertyHelper.isTypeOf(connection, OpenMetadataType.VIRTUAL_CONNECTION.typeName))
                    {
                        for (OpenMetadataElement embeddedConnection : getRelatedElements(connection.getElementGUID(), OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName))
                        {
                            if (! embeddedConnection.getElementGUID().equals(connection.getElementGUID()))
                            {
                                connections.add(embeddedConnection);
                            }
                        }
                    }
                }
            }
        }

        for (OpenMetadataElement connection : connections)
        {
            for (OpenMetadataElement endpoint : getRelatedElements(connection.getElementGUID(), OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName))
            {
                String networkAddress = getStringProperty(endpoint, OpenMetadataProperty.NETWORK_ADDRESS.name);

                if (networkAddress != null)
                {
                    networkAddresses.add(networkAddress);
                }
            }
        }

        return networkAddresses;
    }


    /**
     * Do the connection's bootstrap servers (in its configuration properties, possibly nested under the
     * producer/consumer properties) include the broker named in the namespace?
     *
     * @param connection connection element
     * @param location parsed namespace
     * @return boolean
     */
    private boolean isBootstrapServer(OpenMetadataElement  connection,
                                      OpenLineageNamespace location)
    {
        final String methodName = "isBootstrapServer";

        Map<String, Object> configurationProperties = propertyHelper.getMapFromProperty(sourceName,
                                                                                        OpenMetadataProperty.CONFIGURATION_PROPERTIES.name,
                                                                                        connection.getElementProperties(),
                                                                                        methodName);

        Set<String> bootstrapServers = new LinkedHashSet<>();

        collectBootstrapServers(configurationProperties, bootstrapServers);

        for (String bootstrapServer : bootstrapServers)
        {
            if (location.isSameServer(OpenLineageNamespace.fromNetworkAddress(bootstrapServer.trim())))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Gather the values of every bootstrap.servers property in a (possibly nested) map.
     *
     * @param properties map to search
     * @param bootstrapServers set to add the individual servers to
     */
    private void collectBootstrapServers(Map<?, ?>   properties,
                                         Set<String> bootstrapServers)
    {
        if (properties == null)
        {
            return;
        }

        for (Map.Entry<?, ?> property : properties.entrySet())
        {
            if (("bootstrap.servers".equals(property.getKey())) && (property.getValue() instanceof String servers))
            {
                for (String server : servers.split(","))
                {
                    bootstrapServers.add(server);
                }
            }
            else if (property.getValue() instanceof Map<?, ?> nestedProperties)
            {
                collectBootstrapServers(nestedProperties, bootstrapServers);
            }
        }
    }


    /**
     * Return the assets whose connections use an endpoint.
     *
     * @param endpointGUID endpoint
     * @return assets
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private List<OpenMetadataElement> getAssetsForEndpoint(String endpointGUID) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        List<OpenMetadataElement> assets = new ArrayList<>();

        for (OpenMetadataElement connection : getRelatedElements(endpointGUID, OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName))
        {
            assets.addAll(getAssetsForConnection(connection));
        }

        return assets;
    }


    /**
     * Return the assets that a connection describes.  An endpoint is often attached to a connection embedded in a
     * virtual connection (for example, one that adds the secrets store), in which case the assets are attached to the
     * virtual connection.
     *
     * @param connection connection element
     * @return assets
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private List<OpenMetadataElement> getAssetsForConnection(OpenMetadataElement connection) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        List<OpenMetadataElement> assets = getDirectAssetsForConnection(connection.getElementGUID());

        if (assets.isEmpty())
        {
            for (OpenMetadataElement virtualConnection : getRelatedElements(connection.getElementGUID(), OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName))
            {
                if (propertyHelper.isTypeOf(virtualConnection, OpenMetadataType.VIRTUAL_CONNECTION.typeName))
                {
                    assets.addAll(getDirectAssetsForConnection(virtualConnection.getElementGUID()));
                }
            }
        }

        return assets;
    }


    /**
     * Return the assets directly attached to a connection.
     *
     * @param connectionGUID connection
     * @return assets
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private List<OpenMetadataElement> getDirectAssetsForConnection(String connectionGUID) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        List<OpenMetadataElement> assets = new ArrayList<>();

        for (String relationshipTypeName : new String[]{OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName, OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName})
        {
            for (OpenMetadataElement element : getRelatedElements(connectionGUID, relationshipTypeName))
            {
                if (propertyHelper.isTypeOf(element, OpenMetadataType.ASSET.typeName))
                {
                    assets.add(element);
                }
            }
        }

        return assets;
    }


    /**
     * Return the elements related to an element through a type of relationship (at either end).
     *
     * @param elementGUID starting element
     * @param relationshipTypeName relationship type
     * @return related elements
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private List<OpenMetadataElement> getRelatedElements(String elementGUID,
                                                         String relationshipTypeName) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        List<OpenMetadataElement> elements  = new ArrayList<>();
        OpenMetadataStore         store     = context.getOpenMetadataStore();
        int                       pageSize  = context.getMaxPageSize();
        int                       startFrom = 0;

        RelatedMetadataElementList related = store.getRelatedMetadataElements(elementGUID, 0, relationshipTypeName, startFrom, pageSize);

        while ((related != null) && (related.getElementList() != null) && (! related.getElementList().isEmpty()))
        {
            for (RelatedMetadataElement relatedElement : related.getElementList())
            {
                if ((relatedElement != null) && (relatedElement.getElement() != null))
                {
                    elements.add(relatedElement.getElement());
                }
            }

            if (related.getElementList().size() < pageSize)
            {
                break;
            }

            startFrom = startFrom + pageSize;
            related   = store.getRelatedMetadataElements(elementGUID, 0, relationshipTypeName, startFrom, pageSize);
        }

        return elements;
    }


    /**
     * Return the elements of a type whose string property matches a value.  LIKE matches values that include the
     * supplied string (CASE_INSENSITIVE_LIKE ignoring case); EQ matches the value exactly.  None of them treats
     * the value as a regular expression.
     *
     * @param typeName type of element (subtypes are included)
     * @param propertyName property to match
     * @param value value to match
     * @param operator LIKE, CASE_INSENSITIVE_LIKE or EQ
     * @return matching elements
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private List<OpenMetadataElement> findElements(String                     typeName,
                                                   String                     propertyName,
                                                   String                     value,
                                                   PropertyComparisonOperator operator) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(propertyHelper.addStringProperty(null, propertyName, value, operator));

        return findElements(typeName, searchProperties);
    }


    /**
     * Return the elements of a type that match the search properties.
     *
     * @param typeName type of element (subtypes are included)
     * @param searchProperties conditions
     * @return matching elements
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private List<OpenMetadataElement> findElements(String           typeName,
                                                   SearchProperties searchProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        List<OpenMetadataElement> elements  = new ArrayList<>();
        OpenMetadataStore         store     = context.getOpenMetadataStore();
        int                       pageSize  = context.getMaxPageSize();
        int                       startFrom = 0;

        List<OpenMetadataElement> found = store.findMetadataElements(typeName, null, searchProperties, null, startFrom, pageSize);

        while ((found != null) && (! found.isEmpty()))
        {
            elements.addAll(found);

            if (found.size() < pageSize)
            {
                break;
            }

            startFrom = startFrom + pageSize;
            found     = store.findMetadataElements(typeName, null, searchProperties, null, startFrom, pageSize);
        }

        return elements;
    }


    /**
     * Return a string property of an element.
     *
     * @param element element
     * @param propertyName property name
     * @return value or null
     */
    private String getStringProperty(OpenMetadataElement element,
                                     String              propertyName)
    {
        final String methodName = "getStringProperty";

        return propertyHelper.getStringProperty(sourceName, propertyName, element.getElementProperties(), methodName);
    }
}

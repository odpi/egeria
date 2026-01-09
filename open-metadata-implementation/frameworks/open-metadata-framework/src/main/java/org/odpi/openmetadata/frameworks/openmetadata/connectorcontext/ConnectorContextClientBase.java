/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Base class for the connector context client objects.
 */
public class ConnectorContextClientBase
{
    protected final PropertyHelper propertyHelper = new PropertyHelper();
    protected final String         egeriaRelease  = "6.0-SNAPSHOT";

    protected final ConnectorContextBase parentContext;
    protected final String               localServerName;
    protected final String               localServiceName;
    protected final String               connectorUserId;
    protected final String               connectorGUID;

    protected       String   externalSourceGUID;
    protected       String   externalSourceName;
    protected final AuditLog auditLog;
    protected final int                  maxPageSize;

    /*
     * These properties are used to control common parameters on calls.
     */
    protected boolean             forLineage                   = false;
    protected boolean             forDuplicateProcessing       = false;
    protected boolean             externalSourceIsHome         = true;
    protected boolean             useCurrentEffectiveTime      = true;
    private   Date                effectiveTimeDefault         = null;
    protected Date                asOfTimeDefault              = null;
    protected List<String>        governanceZonesFilterDefault = null;
    protected SequencingOrder     sequencingOrderDefault       = SequencingOrder.CREATION_DATE_RECENT;
    protected String              sequencingPropertyDefault    = null;
    protected List<ElementStatus> limitResultsByStatusDefault  = null;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public ConnectorContextClientBase(ConnectorContextBase parentContext,
                                      String               localServerName,
                                      String               localServiceName,
                                      String               connectorUserId,
                                      String               connectorGUID,
                                      String               externalSourceGUID,
                                      String               externalSourceName,
                                      AuditLog             auditLog,
                                      int                  maxPageSize)
    {
        this.parentContext      = parentContext;
        this.localServerName    = localServerName;
        this.localServiceName   = localServiceName;
        this.connectorUserId    = connectorUserId;
        this.connectorGUID      = connectorGUID;
        this.externalSourceGUID = externalSourceGUID;
        this.externalSourceName = externalSourceName;
        this.auditLog           = auditLog;
        this.maxPageSize        = maxPageSize;
    }


    /**
     * Constructor for connector context client.
     *
     * @param template connector's context
     */
    public ConnectorContextClientBase(ConnectorContextClientBase template)
    {
        assert (template != null);

        this.parentContext                = template.parentContext;
        this.localServerName              = template.localServerName;
        this.localServiceName             = template.localServiceName;
        this.connectorUserId              = template.connectorUserId;
        this.connectorGUID                = template.connectorGUID;
        this.externalSourceGUID           = template.externalSourceGUID;
        this.externalSourceName           = template.externalSourceName;
        this.auditLog                     = template.auditLog;
        this.maxPageSize                  = template.maxPageSize;
        this.forLineage                   = template.forLineage;
        this.forDuplicateProcessing       = template.forDuplicateProcessing;
        this.externalSourceIsHome         = template.externalSourceIsHome;
        this.useCurrentEffectiveTime      = template.useCurrentEffectiveTime;
        this.effectiveTimeDefault         = template.effectiveTimeDefault;
        this.asOfTimeDefault              = template.asOfTimeDefault;
        this.governanceZonesFilterDefault = template.governanceZonesFilterDefault;
        this.sequencingOrderDefault       = template.sequencingOrderDefault;
        this.sequencingPropertyDefault    = template.sequencingPropertyDefault;
        this.limitResultsByStatusDefault  = template.limitResultsByStatusDefault;
    }


    /* ========================================================
     * Return the current Egeria release
     */

    /**
     * Return the current release of egeria that is running.  This can be used to set up the versionIdentifier attribute
     * or other release related values.
     *
     * @return string release name
     */
    public String getEgeriaRelease()
    {
        return egeriaRelease;
    }



    /* ========================================================
     * Return the connectorGUID for setting up lineage relationships
     */

    /**
     * Return the unique identifier of this connector.
     *
     * @return string guid
     */
    public String getConnectorGUID()
    {
        return connectorGUID;
    }


    /* ========================================================
     * Set up the forLineage flag
     */

    /**
     * Return whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @return boolean flag
     */
    public boolean isForLineage()
    {
        return forLineage;
    }


    /**
     * Set up whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @param forLineage boolean flag
     */
    public void setForLineage(boolean forLineage)
    {
        this.forLineage = forLineage;
    }


    /* ========================================================
     * Set up the forDuplicateProcessing flag
     */

    /**
     * Return whether retrieval requests from this service are to avoid merging duplicates or not.
     *
     * @return boolean flag
     */
    public boolean isForDuplicateProcessing()
    {
        return forDuplicateProcessing;
    }


    /**
     * Set up whether retrieval requests from this service are to avoid merging duplicates or not.
     *
     * @param forDuplicateProcessing boolean flag
     */
    public void setForDuplicateProcessing(boolean forDuplicateProcessing)
    {
        this.forDuplicateProcessing = forDuplicateProcessing;
    }


    /* ========================================================
     * Set up whether metadata is owned by the external source.
     */

    /**
     * Set up the flag that controls the ownership of metadata created for this infrastructure manager. Default is true.
     *
     * @param externalSourceIsHome should the metadata be marked as owned by the infrastructure manager so others can not update?
     */
    public void setExternalSourceIsHome(boolean externalSourceIsHome)
    {
        this.externalSourceIsHome = externalSourceIsHome;
    }

    public void setExternalSource(String externalSourceGUID,
                                  String externalSourceName)
    {
        this.externalSourceGUID = externalSourceGUID;
        this.externalSourceName = externalSourceName;
    }


    /* =========================================================
     * Working with effective time.
     */

    /**
     * Return the boolean setting for whether the current time for requests or null.
     *
     * @return boolean
     */
    public boolean isUseCurrentEffectiveTime()
    {
        return useCurrentEffectiveTime;
    }


    /**
     * Set up the boolean setting for whether the current time for requests or the predefined default.
     *
     * @param useCurrentEffectiveTime boolean
     */
    public void setUseCurrentEffectiveTime(boolean useCurrentEffectiveTime)
    {
        this.useCurrentEffectiveTime = useCurrentEffectiveTime;
    }


    /**
     * Set up the value to use for effective time - this is used if testing a future or past effective time.
     *
     * @param effectiveTimeDefault date
     */
    public void setEffectiveTimeDefault(Date effectiveTimeDefault)
    {
        this.effectiveTimeDefault = effectiveTimeDefault;
    }


    /**
     * Return the appropriate effectiveTime for a request.
     *
     * @return date
     */
    protected Date getEffectiveTime()
    {
        if (useCurrentEffectiveTime)
        {
            return new Date();
        }

        return effectiveTimeDefault;
    }


    /* =========================================================
     * Working with asOfTime - ie what was active in the repository as a moment in time - set as UTC date.
     */

    /**
     * Set up the value to use for asOfTime - this is used to test the contents of the repository at different times.
     *
     * @param asOfTimeDefault date
     */
    public void setAsOfTimeDefault(Date asOfTimeDefault)
    {
        this.asOfTimeDefault = asOfTimeDefault;
    }


    /* ============================================================
     * The Governance zones control the visibility of assets.
     */
    public void setGovernanceZonesFilterDefault(List<String> governanceZonesFilterDefault)
    {
        this.governanceZonesFilterDefault = governanceZonesFilterDefault;
    }


    /* =================================================
     * Controlling the order that results are returned.
     */

    /**
     * Set up the default ordering of results.
     *
     * @param sequencingOrderDefault enum determining value to sequence on
     * @param sequencingPropertyDefault name of property if sequencing order is by property
     */
    public void setSequencingOrderDefault(SequencingOrder sequencingOrderDefault,
                                          String          sequencingPropertyDefault)
    {
        this.sequencingOrderDefault = sequencingOrderDefault;
        this.sequencingPropertyDefault = sequencingPropertyDefault;
    }

    /* ====================================================================================
     * Working with the element status that elements must be in to be returned on a query.
     */

    /**
     * Set up the list of element statuses of that returned elements can be set to. The
     * default of null means every status but DELETED.  The DELETED status returns statuses that are soft-deleted.
     *
     * @param limitResultsByStatusDefault list of element statuses or null
     */
    public void setLimitResultsByStatusDefault(List<ElementStatus> limitResultsByStatusDefault)
    {
        this.limitResultsByStatusDefault = limitResultsByStatusDefault;
    }


    /**
     * Convert an element properties with optional effectivity dates into an NewElementProperties object.
     *
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties properties
     * @return new element properties
     */
    public NewElementProperties getNewElementProperties(Date              effectiveFrom,
                                                        Date              effectiveTo,
                                                        ElementProperties properties)
    {
        NewElementProperties newElementProperties = null;

        if ((properties != null) || (effectiveFrom != null) || (effectiveTo != null))
        {
            newElementProperties = new NewElementProperties(properties);

            newElementProperties.setEffectiveFrom(effectiveFrom);
            newElementProperties.setEffectiveTo(effectiveTo);
        }

        return newElementProperties;
    }


    /**
     * Return the default get options.
     *
     * @return default values
     */
    public GetOptions getGetOptions()
    {
        GetOptions getOptions = new GetOptions();

        getOptions.setForLineage(forLineage);
        getOptions.setForDuplicateProcessing(forDuplicateProcessing);
        getOptions.setEffectiveTime(getEffectiveTime());
        getOptions.setAsOfTime(asOfTimeDefault);

        return getOptions;
    }



    /**
     * Return the default query options.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @return default values
     */
    public QueryOptions getQueryOptions(int startFrom,
                                        int pageSize)
    {
        QueryOptions queryOptions = this.getQueryOptions();

        queryOptions.setStartFrom(startFrom);
        queryOptions.setPageSize(pageSize);

        return queryOptions;
    }


    /**
     * Return the default query options.
     *
     * @param metadataElementTypeName type of interest (null means any element type)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @return default values
     */
    public QueryOptions getQueryOptions(String       metadataElementTypeName,
                                        int          startFrom,
                                        int          pageSize)
    {
        QueryOptions queryOptions = this.getQueryOptions(startFrom, pageSize);

        queryOptions.setMetadataElementTypeName(metadataElementTypeName);

        return queryOptions;
    }


    /**
     * Return the default query options.
     *
     * @param metadataElementTypeName type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                           include in the search results. Null means all subtypes.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @return default values
     */
    public QueryOptions getQueryOptions(String       metadataElementTypeName,
                                        List<String> metadataElementSubtypeNames,
                                        int          startFrom,
                                        int          pageSize)
    {
        QueryOptions queryOptions = this.getQueryOptions(metadataElementTypeName, startFrom, pageSize);

        queryOptions.setMetadataElementSubtypeNames(metadataElementSubtypeNames);

        return queryOptions;
    }


    /**
     * Return the default metadata source options.
     *
     * @return default values
     */
    public MetadataSourceOptions getMetadataSourceOptions()
    {
        MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();

        metadataSourceOptions.setForLineage(forLineage);
        metadataSourceOptions.setForDuplicateProcessing(forDuplicateProcessing);
        metadataSourceOptions.setEffectiveTime(getEffectiveTime());

        if (externalSourceIsHome)
        {
            metadataSourceOptions.setExternalSourceGUID(externalSourceGUID);
            metadataSourceOptions.setExternalSourceName(externalSourceName);
        }

        return metadataSourceOptions;
    }



    /**
     * Return the default query options.
     *
     * @return default values
     */
    public QueryOptions getQueryOptions()
    {
        QueryOptions queryOptions = new QueryOptions(getGetOptions());

        queryOptions.setLimitResultsByStatus(limitResultsByStatusDefault);
        queryOptions.setSequencingOrder(sequencingOrderDefault);
        queryOptions.setSequencingProperty(sequencingPropertyDefault);
        queryOptions.setStartFrom(0);
        queryOptions.setPageSize(maxPageSize);
        queryOptions.setGovernanceZoneFilter(governanceZonesFilterDefault);

        return queryOptions;
    }


    /**
     * Return the default search options.
     *
     * @return default values
     */
    public SearchOptions getSearchOptions()
    {
        return new SearchOptions(getQueryOptions());
    }


    /**
     * Return the default search options.
     *
     * @param metadataElementTypeName type of interest (null means any element type)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @return default values
     */
    public SearchOptions getSearchOptions(String metadataElementTypeName,
                                          int    startFrom,
                                          int    pageSize)
    {
        return new SearchOptions(getQueryOptions(metadataElementTypeName, startFrom, pageSize));
    }


    /**
     * Return the default search options.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @return default values
     */
    public SearchOptions getSearchOptions(int startFrom,
                                          int pageSize)
    {
        return new SearchOptions(getQueryOptions(startFrom, pageSize));
    }


    /**
     * Return the default update options with an override for mergeUpdate.
     *
     * @param mergeUpdate flag to indicate whether to completely replace the existing properties with the
     *                    new properties, or just update the individual properties specified on the request.  The default is false.
     * @return a structure for the additional options when updating an element
     */
    public UpdateOptions getUpdateOptions(boolean mergeUpdate)
    {
        UpdateOptions updateOptions = new UpdateOptions(this.getMetadataSourceOptions());

        updateOptions.setMergeUpdate(mergeUpdate);

        return updateOptions;
    }


    /**
     * Return the standard options for deleting an element or relationship.
     *
     * @param cascadedDelete     boolean indicating whether the delete request can cascade to dependent elements
     * @return delete options
     */
    public DeleteOptions getDeleteOptions(boolean cascadedDelete)
    {
        DeleteOptions deleteOptions = new DeleteOptions(this.getMetadataSourceOptions());

        deleteOptions.setCascadedDelete(cascadedDelete);
        deleteOptions.setArchiveProcess(parentContext.connectorName);
        deleteOptions.setDeleteMethod(parentContext.defaultDeleteMethod);

        return deleteOptions;
    }



    /* ==========================
     * Other useful information
     */


    /**
     * Get the maximum paging size.
     *
     * @return maxPagingSize new value
     */
    public int getMaxPagingSize() { return maxPageSize; }


    /**
     * Extract the qualified name from the supplied element.
     *
     * @param rootElement element to query
     * @return qualified name or null if this object is null, or not a referenceable
     */
    public String getQualifiedName(OpenMetadataRootElement rootElement)
    {
        if (rootElement != null)
        {
            if (rootElement.getProperties() instanceof ReferenceableProperties referenceableProperties)
            {
                return referenceableProperties.getQualifiedName();
            }
        }

        return null;
    }


    /**
     * Extract the display name from the supplied element.
     *
     * @param rootElement element to query
     * @return display name or null if this object is null, or not a referenceable
     */
    public String getDisplayName(OpenMetadataRootElement rootElement)
    {
        if (rootElement != null)
        {
            if (rootElement.getProperties() instanceof ReferenceableProperties referenceableProperties)
            {
                return referenceableProperties.getDisplayName();
            }
        }

        return null;
    }


    /**
     * Extract the additional properties from the supplied element.
     *
     * @param rootElement element to query
     * @return additional properties
     */
    public Map<String, String> getAdditionalProperties(OpenMetadataRootElement rootElement)
    {
        if (rootElement != null)
        {
            if (rootElement.getProperties() instanceof ReferenceableProperties referenceableProperties)
            {
                return referenceableProperties.getAdditionalProperties();
            }
        }

        return null;
    }


    /* ==========================
     * Work with GovernanceZones
     */

    /**
     * Set the governance zones to the  "publishZones".
     *
     * @param elementGUID unique element to update
     */
    public void publishElement(String elementGUID)
    {
        // todo
    }


    /**
     * Set the governance zones to the  "publishZones".
     *
     * @param elementGUID unique element to update
     */
    public void withdrawElement(String elementGUID)
    {
        // todo
    }
}

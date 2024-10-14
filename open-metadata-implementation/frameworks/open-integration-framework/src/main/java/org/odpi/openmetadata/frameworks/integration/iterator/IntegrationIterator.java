/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.iterator;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationHeader;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.context.OpenMetadataAccess;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IntegrationIterator provides the common logic for an integration iterator.
 */
public abstract class IntegrationIterator
{
    protected final PropertyHelper           propertyHelper = new PropertyHelper();
    protected final String                   metadataCollectionGUID;
    protected final String                   metadataCollectionQualifiedName;
    protected final String                   externalScopeGUID;
    protected final String                   externalScopeName;
    protected final String                   catalogTargetName;
    protected final String                   connectorName;
    protected final String                   metadataTypeName;
    protected final OpenMetadataAccess       openMetadataAccess;
    protected final PermittedSynchronization targetPermittedSynchronization;
    protected final int                      maxPageSize;
    protected final AuditLog                 auditLog;

    protected List<OpenMetadataElement> elementCache = null;
    protected int                       startFrom    = 0;


    /**
     * Create the iterator.
     *
     * @param metadataCollectionGUID unique identifier of the metadata collection
     * @param metadataCollectionQualifiedName unique name of the metadata collection
     * @param externalScopeGUID unique identifier for the owning scope (typically a catalog)
     * @param externalScopeName unique name for the owning scope (typically a catalog)
     * @param catalogTargetName name of target
     * @param connectorName name of the calling connector
     * @param metadataTypeName type of element to receive
     * @param openMetadataAccess client to access metadata
     * @param targetPermittedSynchronization the synchronization policy for this target
     * @param maxPageSize max page size for the server
     * @param auditLog logging destination
     */
    public IntegrationIterator(String                   metadataCollectionGUID,
                               String                   metadataCollectionQualifiedName,
                               String                   externalScopeGUID,
                               String                   externalScopeName,
                               String                   catalogTargetName,
                               String                   connectorName,
                               String                   metadataTypeName,
                               OpenMetadataAccess       openMetadataAccess,
                               PermittedSynchronization targetPermittedSynchronization,
                               int                      maxPageSize,
                               AuditLog                 auditLog)
    {
        this.metadataCollectionGUID          = metadataCollectionGUID;
        this.metadataCollectionQualifiedName = metadataCollectionQualifiedName;
        this.externalScopeGUID               = externalScopeGUID;
        this.externalScopeName               = externalScopeName;
        this.catalogTargetName               = catalogTargetName;
        this.connectorName                   = connectorName;
        this.metadataTypeName                = metadataTypeName;
        this.openMetadataAccess              = openMetadataAccess;
        this.targetPermittedSynchronization  = targetPermittedSynchronization;
        this.maxPageSize                     = maxPageSize;
        this.auditLog                        = auditLog;
    }


    /**
     * Retrieve information from the open metadata repositories.
     *
     * @return boolean where true means there are elements to process.
     *
     * @throws InvalidParameterException problem with a parameter value
     * @throws PropertyServerException repository not working properly
     * @throws UserNotAuthorizedException permissions problem
     */
    protected abstract boolean fillCache() throws InvalidParameterException,
                                                  PropertyServerException,
                                                  UserNotAuthorizedException;


    /**
     * Is there information from the open metadata repositories to process?
     *
     * @return boolean where true means there are elements to process.
     * @throws InvalidParameterException problem with a parameter value
     * @throws PropertyServerException repository not working properly
     * @throws UserNotAuthorizedException permissions problem
     */
    public boolean moreToReceive() throws InvalidParameterException,
                                          PropertyServerException,
                                          UserNotAuthorizedException
    {
        return fillCache();
    }


    /**
     * Return the next element.
     *
     * @return next member in the repository.
     * @throws InvalidParameterException problem with a parameter value
     * @throws PropertyServerException repository not working properly
     * @throws UserNotAuthorizedException permissions problem
     */
    public MemberElement getNextMember() throws InvalidParameterException,
                                                PropertyServerException,
                                                UserNotAuthorizedException
    {
        OpenMetadataElement element =  elementCache.remove(0);

        return this.fillOutMemberElement(element, true);
    }


    /**
     * Retrieve the member element by qualified name.  This is used in sweep 2 when elements from the third party
     * system are being retrieved and mapped to open metadata elements.  It is possible that there is no open metadata
     * element, or it has been deleted.
     *
     * @param qualifiedName anticipated qualified name.
     * @return member element - may be empty - but not null
     *
     * @throws InvalidParameterException problem with a parameter value
     * @throws PropertyServerException repository not working properly
     * @throws UserNotAuthorizedException permissions problem
     */
    public MemberElement getMemberByQualifiedName(String qualifiedName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        OpenMetadataElement element = openMetadataAccess.getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);

        if (element != null)
        {
            return this.fillOutMemberElement(element, true);
        }

        element = openMetadataAccess.getLineageElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);
        if (element != null)
        {
            return this.fillOutMemberElement(element, false);
        }

        element = openMetadataAccess.getDeletedElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);

        return this.fillOutMemberElement(element, false);
    }


    /**
     * Create the member element.
     *
     * @param element element retrieved from the open metadata repositories.
     * @param isElementActive is the element retrieved either archived or deleted (false) or still actively available (true)
     *
     * @return member element
     * @throws InvalidParameterException problem with a parameter value
     * @throws PropertyServerException repository not working properly
     * @throws UserNotAuthorizedException permissions problem
     */
    private MemberElement fillOutMemberElement(OpenMetadataElement element,
                                               boolean             isElementActive) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "fillOutMemberElement";

        List<MetadataCorrelationHeader>    correlationHeaders = null;
        RelatedMetadataElement             rootSchemaType     = null;
        Map<String, Map<String, String>>   vendorProperties   = null;

        if (isElementActive)
        {
            if (externalScopeGUID == null)
            {
                correlationHeaders = openMetadataAccess.getMetadataCorrelationHeaders(element.getElementGUID(),
                                                                                      propertyHelper.getStringProperty(element.getElementGUID(),
                                                                                                                       OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                       element.getElementProperties(),
                                                                                                                       methodName),
                                                                                      element.getElementGUID(),
                                                                                      element.getType().getTypeName());
            }
            else
            {
                correlationHeaders = openMetadataAccess.getMetadataCorrelationHeaders(externalScopeGUID,
                                                                                      externalScopeName,
                                                                                      element.getElementGUID(),
                                                                                      element.getType().getTypeName());
            }

            vendorProperties = openMetadataAccess.getVendorProperties(element.getElementGUID(),
                                                                      element.getType().getTypeName());


            rootSchemaType = openMetadataAccess.getRelatedMetadataElement(element.getElementGUID(),
                                                                          1,
                                                                          OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                                                          new Date());
        }

        if (vendorProperties == null)
        {
            vendorProperties = new HashMap<>();
        }

        if (externalScopeGUID != null)
        {
            return new MemberElement(element,
                                     rootSchemaType,
                                     correlationHeaders,
                                     externalScopeGUID,
                                     vendorProperties,
                                     isElementActive,
                                     catalogTargetName,
                                     connectorName,
                                     targetPermittedSynchronization,
                                     auditLog);
        }
        else if (element != null)
        {
            return new MemberElement(element,
                                     rootSchemaType,
                                     correlationHeaders,
                                     element.getElementGUID(),
                                     vendorProperties,
                                     isElementActive,
                                     catalogTargetName,
                                     connectorName,
                                     targetPermittedSynchronization,
                                     auditLog);
        }
        else
        {
            return new MemberElement(element,
                                     rootSchemaType,
                                     correlationHeaders,
                                     null,
                                     vendorProperties,
                                     isElementActive,
                                     catalogTargetName,
                                     connectorName,
                                     targetPermittedSynchronization,
                                     auditLog);
        }
    }
}

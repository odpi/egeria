/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.iterator;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationHeader;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.*;
import org.odpi.openmetadata.frameworks.integration.context.OpenMetadataAccess;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.PropertyFacetValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetadataCollectionIterator
{
    private final PropertyHelper           propertyHelper = new PropertyHelper();
    private final String                   metadataCollectionGUID;
    private final String                   metadataCollectionQualifiedName;
    private final String                   catalogTargetName;
    private final String                   connectorName;
    private final String                   metadataTypeName;
    private final OpenMetadataAccess       openMetadataAccess;
    private final PermittedSynchronization targetPermittedSynchronization;
    private final int                      maxPageSize;
    private final AuditLog                 auditLog;

    private List<OpenMetadataElement> elementCache = null;
    private int                       startFrom    = 0;


    /**
     * Create the iterator.
     *
     * @param metadataCollectionGUID unique identifier of the metadata collection
     * @param metadataCollectionQualifiedName unique name of the metadata collection
     * @param catalogTargetName name of target
     * @param connectorName name of the calling connector
     * @param metadataTypeName type of element to receive
     * @param openMetadataAccess client to access metadata
     * @param targetPermittedSynchronization the synchronization policy for this target
     * @param maxPageSize max page size for the server
     * @param auditLog logging destination
     */
    public MetadataCollectionIterator(String                   metadataCollectionGUID,
                                      String                   metadataCollectionQualifiedName,
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
    private boolean fillCache() throws InvalidParameterException,
                                       PropertyServerException,
                                       UserNotAuthorizedException
    {
        if ((elementCache == null) || (elementCache.isEmpty()))
        {
            elementCache = openMetadataAccess.findMetadataElements(metadataTypeName,
                                                                   null,
                                                                   propertyHelper.getSearchPropertiesForMetadataCollectionName(metadataCollectionQualifiedName),
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   startFrom,
                                                                   maxPageSize);
            startFrom = startFrom + maxPageSize;
        }

        return (elementCache != null);
    }


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
        if (element != null)
        {
            return this.fillOutMemberElement(element, false);
        }

        return null;
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
        List<MetadataCorrelationHeader>    correlationHeaders = null;
        Map<String, Map<String, String>>   vendorProperties   = null;

        if (isElementActive)
        {
            correlationHeaders = openMetadataAccess.getMetadataCorrelationHeaders(metadataCollectionGUID,
                                                                                  metadataCollectionQualifiedName,
                                                                                  element.getElementGUID(),
                                                                                  element.getType().getTypeName());

            vendorProperties = openMetadataAccess.getVendorProperties(element.getElementGUID(),
                                                                      element.getType().getTypeName());
        }

        if (vendorProperties == null)
        {
            vendorProperties = new HashMap<>();
        }

        return new MemberElement(element,
                                 correlationHeaders,
                                 vendorProperties,
                                 isElementActive,
                                 catalogTargetName,
                                 connectorName,
                                 targetPermittedSynchronization,
                                 auditLog);
    }
}

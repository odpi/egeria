/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.iterator;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationManagerClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.List;

/**
 * IntegrationIterator provides the common logic for an iterator that is comparing the elements in
 * open metadata and the elements a third party to identify where synchronization is needed.
 */
public abstract class IntegrationIterator
{
    protected final PropertyHelper           propertyHelper = new PropertyHelper();
    protected final String                   metadataCollectionGUID;
    protected final String                   catalogTargetName;
    protected final String                   connectorName;
    protected final String                   metadataTypeName;
    protected final IntegrationContext       integrationContext;
    protected final PermittedSynchronization targetPermittedSynchronization;
    protected final int                      maxPageSize;
    protected final AuditLog                 auditLog;

    protected List<OpenMetadataRootElement> elementCache = null;
    protected int                           startFrom    = 0;


    /**
     * Create the iterator.
     *
     * @param metadataCollectionGUID unique identifier of the metadata collection
     * @param catalogTargetName name of target
     * @param connectorName name of the calling connector
     * @param metadataTypeName type of element to receive
     * @param integrationContext clients to access metadata
     * @param targetPermittedSynchronization the synchronization policy for this target
     * @param maxPageSize max page size for the server
     * @param auditLog logging destination
     * @throws UserNotAuthorizedException the connector has been disconnected
     */
    public IntegrationIterator(String                   metadataCollectionGUID,
                               String                   catalogTargetName,
                               String                   connectorName,
                               String                   metadataTypeName,
                               IntegrationContext       integrationContext,
                               PermittedSynchronization targetPermittedSynchronization,
                               int                      maxPageSize,
                               AuditLog                 auditLog) throws UserNotAuthorizedException
    {
        this.metadataCollectionGUID          = metadataCollectionGUID;
        this.catalogTargetName               = catalogTargetName;
        this.connectorName                   = connectorName;
        this.metadataTypeName                = metadataTypeName;
        this.integrationContext              = integrationContext;
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
        OpenMetadataRootElement element =  elementCache.remove(0);

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
        ClassificationManagerClient classificationManagerClient = integrationContext.getClassificationManagerClient();

        OpenMetadataRootElement element = classificationManagerClient.getRootElementByUniqueName(qualifiedName,
                                                                                                 OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                 classificationManagerClient.getQueryOptions());

        if (element != null)
        {
            return this.fillOutMemberElement(element, true);
        }

        element = classificationManagerClient.getLineageElementByUniqueName(qualifiedName,
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                            classificationManagerClient.getGetOptions());
        if (element != null)
        {
            return this.fillOutMemberElement(element, false);
        }

        element = classificationManagerClient.getDeletedElementByUniqueName(qualifiedName,
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                            classificationManagerClient.getGetOptions());

        return this.fillOutMemberElement(element, false);
    }


    /**
     * Create the member element.
     *
     * @param element element retrieved from the open metadata repositories.
     * @param isElementActive is the element retrieved either archived or deleted (false) or still actively available (true)
     *
     * @return member element
     */
    private MemberElement fillOutMemberElement(OpenMetadataRootElement element,
                                               boolean                 isElementActive)
    {
        if (metadataCollectionGUID != null)
        {
            return new MemberElement(element,
                                     metadataCollectionGUID,
                                     isElementActive,
                                     catalogTargetName,
                                     connectorName,
                                     targetPermittedSynchronization,
                                     auditLog);
        }
        else if (element != null)
        {
            return new MemberElement(element,
                                     element.getElementHeader().getGUID(),
                                     isElementActive,
                                     catalogTargetName,
                                     connectorName,
                                     targetPermittedSynchronization,
                                     auditLog);
        }
        else
        {
            return new MemberElement(null,
                                     null,
                                     isElementActive,
                                     catalogTargetName,
                                     connectorName,
                                     targetPermittedSynchronization,
                                     auditLog);
        }
    }
}

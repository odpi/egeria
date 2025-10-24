/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.modules;


import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ApacheAtlasIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationAuditCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasAttributeDef;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasClassificationDef;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasTypesDef;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ValidValueDefinitionClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AtlasReferenceClassificationsIntegrationModule synchronizes a valid value set of classification definitions from the open metadata ecosystem
 * to Apache Atlas as classification definitions.  This is used to
 * It is called after the registered integration modules have established the key assets into the open metadata ecosystem.
 */
public class AtlasReferenceClassificationsIntegrationModule extends AtlasIntegrationModuleBase
{
    /**
     * Unique name of this module for messages.
     */
    private static final String referenceClassificationsModuleName = "Apache Atlas Reference Classifications Integration Module";
    private static final String classificationQualifiedNamePrefix = "Apache Atlas Reference Classification: ";

    private final String classificationReferenceSetName;
    private final String classificationReferenceSetPolicy;

    private       String                                   classificationReferenceSetGUID       = null;
    private final Map<String, OpenMetadataRootElement> classificationNameToReferenceElement = new HashMap<>();

    private final ValidValueDefinitionClient validValueDefinitionClient;

    /**
     * Constructor for the module is supplied with the runtime context in order to operate.
     *
     * @param connectorName name of the connector (for messages)
     * @param connectionDetails connection properties used to start the connector
     * @param auditLog logging destination
     * @param myContext integration context
     * @param targetRootURL URL to connect to Apache Atlas
     * @param atlasClient client to connect to Apache Atlas
     * @param embeddedConnectors list of any embedded connectors (such as secrets connector and topic connector
     * @param classificationReferenceSetName determines what type of mapping to perform for informal tags
     * @param classificationReferenceSetPolicy determines the direction of exchange between the open metadata ecosystem and Apache Atlas
     * @throws UserNotAuthorizedException security problem
     */
    public AtlasReferenceClassificationsIntegrationModule(String                   connectorName,
                                                          Connection               connectionDetails,
                                                          AuditLog                 auditLog,
                                                          IntegrationContext       myContext,
                                                          String                   targetRootURL,
                                                          ApacheAtlasRESTConnector atlasClient,
                                                          List<Connector>          embeddedConnectors,
                                                          String                   classificationReferenceSetName,
                                                          String                   classificationReferenceSetPolicy) throws UserNotAuthorizedException
    {
        super(connectorName,
              referenceClassificationsModuleName,
              connectionDetails,
              auditLog,
              myContext,
              targetRootURL,
              atlasClient,
              embeddedConnectors);

        this.classificationReferenceSetName = classificationReferenceSetName;
        this.classificationReferenceSetPolicy = classificationReferenceSetPolicy;

        validValueDefinitionClient = myContext.getValidValueDefinitionClient();
        validValueDefinitionClient.setForLineage(false);
        validValueDefinitionClient.setForDuplicateProcessing(false);
    }


    /**
     * Exchange Atlas classification types as defined by the classification reference set.
     *
     * @return map of classification names to reference classification definitions
     */
    public Map<String, OpenMetadataRootElement> synchronizeClassificationDefinitions()
    {
        final String methodName = "synchronizeClassificationDefinitions";

        /*
         * This function is only enabled if the classificationReferenceSetName is set up.
         */
        if (classificationReferenceSetName != null)
        {
            /*
             * Get the unique identifier of the classification reference data set.
             */
            if (classificationReferenceSetGUID == null)
            {
                try
                {
                    classificationReferenceSetGUID = this.getClassificationReferenceSetGUID();
                }
                catch (Exception error)
                {
                    if (auditLog != null)
                    {
                        auditLog.logException(methodName,
                                              AtlasIntegrationAuditCode.UNABLE_TO_SET_UP_CLASSIFICATION_REFERENCE_SET.getMessageDefinition(connectorName,
                                                                                                                                           error.getClass().getName(),
                                                                                                                                           classificationReferenceSetName,
                                                                                                                                           error.getMessage()),
                                              error);
                    }

                    return classificationNameToReferenceElement;
                }
            }

            /*
             * Retrieve the existing reference classification definitions
             */
            /* todo
            try
            {

                List<ValidValueMember> referenceClassifications = validValueDefinitionClient.getValidValueSetMembers(classificationReferenceSetGUID,
                                                                                                                     0,
                                                                                                                     myContext.getMaxPageSize(),
                                                                                                                     new Date());
                int startFrom = 0;

                while (referenceClassifications != null)
                {
                    for (ValidValueMember referenceClassification : referenceClassifications)
                    {
                        if (referenceClassification != null)
                        {
                            classificationNameToReferenceElement.put(referenceClassification.getValidValueElement().getValidValueProperties().getDisplayName(),
                                                                     referenceClassification.getValidValueElement());
                        }
                    }

                    startFrom = startFrom + myContext.getMaxPageSize();
                    referenceClassifications = validValueDefinitionClient.getValidValueSetMembers(classificationReferenceSetGUID,
                                                                                                  startFrom,
                                                                                                  myContext.getMaxPageSize(),
                                                                                                  new Date());


                }
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          AtlasIntegrationAuditCode.UNABLE_TO_GET_CLASSIFICATION_REFERENCE_SET_MEMBERS.getMessageDefinition(connectorName,
                                                                                                                                            error.getClass().getName(),
                                                                                                                                            classificationReferenceSetName,
                                                                                                                                            error.getMessage()),
                                          error);
                }

                return classificationNameToReferenceElement;
            }

             */

            /*
             * Set to keep track of the classifications defined in Apache Atlas
             */
            Set<String> atlasClassificationNames = new HashSet<>();

            /*
             * Copy the Atlas Classification to the Classification Reference Set.
             */
            if ((classificationReferenceSetPolicy.equals(ApacheAtlasIntegrationProvider.CLASSIFICATION_REFERENCE_SET_FROM_ATLAS_CONFIGURATION_PROPERTY_VALUE)) ||
                (classificationReferenceSetPolicy.equals(ApacheAtlasIntegrationProvider.CLASSIFICATION_REFERENCE_SET_BOTH_WAYS_CONFIGURATION_PROPERTY_VALUE)))
            {
                try
                {
                    /*
                     * Retrieve all the classifications defined an Apache Atlas and make sure they are listed in the reference data set.
                     */
                    AtlasTypesDef atlasTypes = atlasClient.getAllTypes();

                    if (atlasTypes != null)
                    {
                        List<AtlasClassificationDef> atlasClassifications = atlasTypes.getClassificationDefs();

                        if (atlasClassifications != null)
                        {
                            for (AtlasClassificationDef atlasClassification : atlasClassifications)
                            {
                                if (atlasClassification != null)
                                {
                                    atlasClassificationNames.add(atlasClassification.getName());

                                    if (classificationNameToReferenceElement.get(atlasClassification.getName()) == null)
                                    {
                                        /*
                                         * The Atlas Classification has not been added to the classification reference set.
                                         */
                                        ValidValueDefinitionProperties properties = new ValidValueDefinitionProperties();

                                        properties.setQualifiedName(classificationQualifiedNamePrefix + atlasClassification.getName());
                                        properties.setDisplayName(atlasClassification.getName());
                                        properties.setDescription(atlasClassification.getDescription());

                                        if (atlasClassification.getAttributeDefs() != null)
                                        {
                                            Map<String, String> additionalProperties = new HashMap<>();
                                            for (AtlasAttributeDef atlasAttributeDef : atlasClassification.getAttributeDefs())
                                            {
                                                if (atlasAttributeDef != null)
                                                {
                                                    additionalProperties.put(atlasAttributeDef.getName(), atlasAttributeDef.getDescription());
                                                }
                                            }

                                            if (! additionalProperties.isEmpty())
                                            {
                                                properties.setAdditionalProperties(additionalProperties);
                                            }
                                        }

                                        String classificationReferenceGUID = validValueDefinitionClient.createValidValueDefinition(classificationReferenceSetGUID,
                                                                                                                                   properties,
                                                                                                                                   false);

                                        if (classificationReferenceGUID != null)
                                        {
                                            classificationNameToReferenceElement.put(atlasClassification.getName(), validValueDefinitionClient.getValidValueDefinitionByGUID(classificationReferenceGUID, null));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                catch (Exception error)
                {
                    if (auditLog != null)
                    {
                        auditLog.logException(methodName,
                                              AtlasIntegrationAuditCode.UNABLE_TO_BUILD_CLASSIFICATION_REFERENCE_SET_FROM_ATLAS.getMessageDefinition(connectorName,
                                                                                                                                                     error.getClass().getName(),
                                                                                                                                                     classificationReferenceSetName,
                                                                                                                                                     error.getMessage()),
                                              error);
                    }
                }
            }

            /*
             * Add any classifications defined in the Classification Reference Set that is not yet in Apache Atlas.
             */
            if ((classificationReferenceSetPolicy.equals(ApacheAtlasIntegrationProvider.CLASSIFICATION_REFERENCE_SET_TO_ATLAS_CONFIGURATION_PROPERTY_VALUE)) ||
                (classificationReferenceSetPolicy.equals(ApacheAtlasIntegrationProvider.CLASSIFICATION_REFERENCE_SET_BOTH_WAYS_CONFIGURATION_PROPERTY_VALUE)))
            {
                try
                {
                    for (String referenceClassificationName : classificationNameToReferenceElement.keySet())
                    {
                        if ((referenceClassificationName != null) && (! atlasClassificationNames.contains(referenceClassificationName)))
                        {
                            OpenMetadataRootElement referenceClassification = classificationNameToReferenceElement.get(referenceClassificationName);

                            if (referenceClassification.getProperties() instanceof ValidValueDefinitionProperties referenceClassificationProperties)
                            atlasClient.addClassificationType(referenceClassificationName,
                                                              referenceClassificationProperties.getDescription(),
                                                              referenceClassificationProperties.getAdditionalProperties());

                            atlasClassificationNames.add(referenceClassificationName);
                        }
                    }

                }
                catch (Exception error)
                {
                    if (auditLog != null)
                    {
                        auditLog.logException(methodName,
                                              AtlasIntegrationAuditCode.UNABLE_TO_ADD_CLASSIFICATION_REFERENCE_SET_TO_ATLAS.getMessageDefinition(connectorName,
                                                                                                                                                 error.getClass().getName(),
                                                                                                                                                 classificationReferenceSetName,
                                                                                                                                                 error.getMessage()),
                                              error);
                    }
                }
            }
        }

        return classificationNameToReferenceElement;
    }


    /**
     * Return the unique identifier of the reference data set qualified name.
     *
     * @return string guid
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public String getClassificationReferenceSetGUID() throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        /*
         * Retrieve the valid value sets with matching names.
         */
        List<OpenMetadataRootElement> matchingValidValues = validValueDefinitionClient.getValidValueDefinitionsByName(classificationReferenceSetName,
                                                                                                                          validValueDefinitionClient.getQueryOptions(0, 0));

        /*
         * If a valid value set is found with the right name then use it
         */
        if (matchingValidValues != null)
        {
            for (OpenMetadataRootElement validValueDefinitionElement : matchingValidValues)
            {
                if ((validValueDefinitionElement != null) && (validValueDefinitionElement.getProperties() instanceof ValidValueDefinitionProperties validValueDefinitionProperties))
                {
                    if (classificationReferenceSetName.equals(validValueDefinitionProperties.getQualifiedName()))
                    {
                        return validValueDefinitionElement.getElementHeader().getGUID();
                    }
                }
            }
        }

        ValidValueDefinitionProperties validValueDefinitionProperties = new ValidValueDefinitionProperties();

        validValueDefinitionProperties.setQualifiedName(classificationReferenceSetName);

        return null; // todo return validValueDefinitionClient.createValidValueDefinition(null, validValueDefinitionProperties);
    }
}
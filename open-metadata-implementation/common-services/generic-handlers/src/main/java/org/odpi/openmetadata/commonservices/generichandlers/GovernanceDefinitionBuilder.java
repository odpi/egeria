/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * GovernanceDefinitionBuilder creates the parts for an entity that represents a governance definition.
 * This includes GovernanceStrategy, GovernancePolicy, all types of GovernanceControl, Licenses and Certifications.
 */
public class GovernanceDefinitionBuilder extends ReferenceableBuilder
{
    private String       title                     = null;
    private String       summary                   = null;
    private String       description               = null;
    private String       scope                     = null;
    private int          domainIdentifier          = 0;
    private String       priority                  = null;
    private List<String> implications              = null;
    private List<String> outcomes                  = null;
    private List<String> results                   = null;
    private List<String> businessImperatives       = null;
    private String       jurisdiction              = null;
    private String       implementationDescription = null;
    private String       namePattern               = null;
    private String       details                   = null;
    private String       distinguishedName         = null;


    /**
     * Generic governance constructor
     *
     * @param qualifiedName unique name for the governance definition
     * @param title short display name for the governance definition
     * @param summary brief description of the governance definition
     * @param description description of the governance definition
     * @param scope breadth of coverage of the governance definition
     * @param domainIdentifier identifier that indicates which governance domain this definition belongs to (0=all)
     * @param priority relative importance of the governance definition
     * @param implications implications to the business in adopting this governance definition
     * @param outcomes expected outcomes from implementing this governance definition
     * @param results actual results achieved from implementing this governance definition
     * @param businessImperatives for the GovernanceStrategy - how does it link to business imperatives
     * @param jurisdiction for Regulations - where does this regulation apply
     * @param implementationDescription for GovernanceControl - how should this be implemented
     * @param namePattern for NamingStandardsRule - the pattern used to for new names
     * @param details for License or Certification - additional details about the definition
     * @param distinguishedName for Security groups - qualified name for LDAP
     * @param additionalProperties additional properties for a governance definition
     * @param typeGUID unique identifier of the type of the governance definition
     * @param typeName unique name of the type of the governance definition
     * @param extendedProperties  properties for a governance definition subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GovernanceDefinitionBuilder(String               qualifiedName,
                                String               title,
                                String               summary,
                                String               description,
                                String               scope,
                                int                  domainIdentifier,
                                String               priority,
                                List<String>         implications,
                                List<String>         outcomes,
                                List<String>         results,
                                List<String>         businessImperatives,
                                String               jurisdiction,
                                String               implementationDescription,
                                String               namePattern,
                                String               details,
                                String               distinguishedName,
                                Map<String, String>  additionalProperties,
                                String               typeGUID,
                                String               typeName,
                                Map<String, Object>  extendedProperties,
                                OMRSRepositoryHelper repositoryHelper,
                                String               serviceName,
                                String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              typeGUID,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.title = title;
        this.summary = summary;
        this.description = description;
        this.scope = scope;
        this.domainIdentifier = domainIdentifier;
        this.priority = priority;
        this.implications = implications;
        this.outcomes = outcomes;
        this.results = results;
        this.businessImperatives = businessImperatives;
        this.jurisdiction = jurisdiction;
        this.implementationDescription = implementationDescription;
        this.namePattern = namePattern;
        this.details = details;
        this.distinguishedName = distinguishedName;
    }


    /**
     * Minimal constructor
     *
     * @param qualifiedName unique name for the governance definition
     * @param title short display name for the governance definition
     * @param description description of the governance definition
     * @param typeGUID unique identifier of the type of the governance definition
     * @param typeName unique name of the type of the governance definition
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GovernanceDefinitionBuilder(String               qualifiedName,
                                String               title,
                                String               description,
                                String               typeGUID,
                                String               typeName,
                                OMRSRepositoryHelper repositoryHelper,
                                String               serviceName,
                                String               serverName)
    {
        super(qualifiedName,
              null,
              typeGUID,
              typeName,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.title = title;
        this.description = description;
    }


    /**
     * Classification constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GovernanceDefinitionBuilder(String               typeGUID,
                                String               typeName,
                                OMRSRepositoryHelper repositoryHelper,
                                String               serviceName,
                                String               serverName)
    {
        super(typeGUID,
              typeName,
              repositoryHelper,
              serviceName,
              serverName);
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.TITLE_PROPERTY_NAME,
                                                                  title,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.SUMMARY.name,
                                                                  summary,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.SCOPE_PROPERTY_NAME,
                                                                  scope,
                                                                  methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME,
                                                               domainIdentifier,
                                                               methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.IMPORTANCE_PROPERTY_NAME,
                                                                  priority,
                                                                  methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataType.IMPLICATIONS_PROPERTY_NAME,
                                                                       implications,
                                                                       methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataType.OUTCOMES_PROPERTY_NAME,
                                                                       outcomes,
                                                                       methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataType.RESULTS_PROPERTY_NAME,
                                                                       results,
                                                                       methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataType.BUSINESS_IMPERATIVES_PROPERTY_NAME,
                                                                       businessImperatives,
                                                                       methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.JURISDICTION_PROPERTY_NAME,
                                                                  jurisdiction,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.IMPLEMENTATION_DESCRIPTION_PROPERTY_NAME,
                                                                  implementationDescription,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.NAME_PATTERN_PROPERTY_NAME,
                                                                  namePattern,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.DETAILS_PROPERTY_NAME,
                                                                  details,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DISTINGUISHED_NAME.name,
                                                                  distinguishedName,
                                                                  methodName);

        return properties;
    }
}

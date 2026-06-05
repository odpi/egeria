/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.definitions;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.List;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * DeployedImplementationType describes the standard deployed implementation types supplied with Egeria. These are encoded in the
 * CoreContentPack.omarchive and are available in the open metadata repository as valid values.
 */
public interface DeployedImplementationTypeDefinition
{
    /**
     * Return the guid for the deployed technology type - can be null.
     *
     * @return string
     */
    String getGUID();


    /**
     * Return preferred value for deployed implementation type.
     * 
     * @return string
     */
    String getDeployedImplementationType();


    /**
     * Return the optional deployed implementation type that this technology is a tye of.
     *
     * @return deployed implementation type enum
     */
    DeployedImplementationTypeDefinition getIsATypeOf();


    /**
     * Return the type name that this deployed implementation type is associated with.
     * 
     * @return string
     */
    String getAssociatedTypeName();


    /**
     * Return the optional classification name that this deployed implementation type is associated with.
     *
     * @return string
     */
    String getAssociatedClassification();


    /**
     * Return the qualified name for this deployed implementation type.
     *
     * @return string
     */
    default String getQualifiedName()
    {
        return constructValidValueQualifiedName(getAssociatedTypeName(),
                                                OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                null,
                                                getDeployedImplementationType());
    }


    /**
     * Return the description for this value.
     * 
     * @return string
     */
    String getDescription();


    /**
     * Return the URL to more information.
     *
     * @return string url
     */
    String getWikiLink();


    /**
     * Return the optional unique identifier of the solution component that this deployed implementation type is associated with.
     *
     * @return string
     */
    String getSolutionComponentGUID();


    /**
     * Return the solution component type that this deployed implementation type is associated with.
     *
     * @return string
     */
    String getSolutionComponentType();


    /**
     * Return the solution component identifier that this deployed implementation type is associated with.
     *
     * @return string
     */
    String getSolutionComponentIdentifier();


    /**
     * Return the optional list of subcomponents.
     *
     * @return null or list
     */
    default List<SolutionComponentDefinition> getSubComponents()
    {
        return null;
    }

    /**
     * Return the solution component that this deployed implementation type is associated with.
     *
     * @return solution component definition
     */
    default SolutionComponentDefinition getSolutionComponent()
    {
        return new SolutionComponentDefinition()
        {
            @Override
            public String getGUID()
            {
                return DeployedImplementationTypeDefinition.this.getSolutionComponentGUID();
            }

            @Override
            public String getQualifiedName()
            {
                return DeployedImplementationTypeDefinition.this.getQualifiedName() + "::" + getSolutionComponentIdentifier();
            }

            @Override
            public String getComponentType()
            {
                return DeployedImplementationTypeDefinition.this.getSolutionComponentType();
            }

            @Override
            public String getImplementationType()
            {
                return DeployedImplementationTypeDefinition.this.getDeployedImplementationType();
            }

            @Override
            public List<SolutionComponentDefinition> getSubComponents()
            {
                return DeployedImplementationTypeDefinition.this.getSubComponents();
            }

            /**
             * Return the segments that preceded this segment.
             *
             * @return list of segments
             */
            @Override
            public List<InformationSupplyChainDefinition> getLinkedFromSegment()
            {
                return List.of();
            }

            @Override
            public String getImplementationResource()
            {
                return null;
            }

            @Override
            public String getIdentifier()
            {
                return DeployedImplementationTypeDefinition.this.getSolutionComponentIdentifier();
            }

            @Override
            public String getDisplayName()
            {
                return DeployedImplementationTypeDefinition.this.getDeployedImplementationType();
            }

            @Override
            public String getDescription()
            {
                return DeployedImplementationTypeDefinition.this.getDescription();
            }

            @Override
            public String getURL()
            {
                return DeployedImplementationTypeDefinition.this.getWikiLink();
            }
        };
    }
}

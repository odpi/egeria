/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.definitions;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.ScopeDefinition;

import java.util.List;

/**
 * A description of the predefined information supply chains.  There are two formats - one for templates and
 * the other for normal information supply chains.
 */
public interface InformationSupplyChainDefinition extends ReferenceableDefinition
{
    /**
     * Return the scope of the information supply chain.
     *
     * @return string
     */
    ScopeDefinition getScope();


    /**
     * Return the purposes of the information supply chain.
     *
     * @return list of strings
     */
    List<String> getDataProcessingPurposes();


    /**
     * Return the identifier of the owner.
     *
     * @return string
     */
    String getOwner();


    /**
     * Return the type name of the element describing the owner.
     *
     * @return string
     */
    String getOwnerTypeName();

    /**
     * Return the property name used to identify the owner.
     *
     * @return string
     */
    String getOwnerPropertyName();


    /**
     * Return the supply chain that this segment is a part of.
     *
     * @return information supply chain
     */
    String getOwningSupplyChain();


    /**
     * Should this information supply chain be anchored to its parent?
     *
     * @return boolean
     */
    boolean isOwningInformationSupplyChainAnchor();

    /**
     * Return the GUID of the anchor scope for this information supply chain.
     *
     * @return string
     */
    String getAnchorScopeGUID();


    /**
     * Return the template name.
     *
     * @return string
     */
    String getTemplateName();


    /**
     * Return the template description.
     *
     * @return string
     */
    String getTemplateDescription();
}

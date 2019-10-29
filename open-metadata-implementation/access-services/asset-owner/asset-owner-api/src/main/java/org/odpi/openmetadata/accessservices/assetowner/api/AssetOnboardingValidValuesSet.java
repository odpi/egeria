/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.api;


import org.odpi.openmetadata.accessservices.assetowner.properties.ValidValue;

import java.util.List;

/**
 * AssetOnboardingValidValuesSet provides the API operations to create and maintain lists of valid
 * value definitions grouped into a valid value set.  Both valid value definitions and valid value sets have
 * the same attributes and so inherit from ValidValue where all of the attributes are defined.
 *
 * A set is just grouping of valid values.   Valid value definitions and sets can be nested many times in other
 * valid values.
 */
public interface AssetOnboardingValidValuesSet
{
    String  createValidValueSet();


    void    updateValidValueSet();


    void    deleteValidValueSet();


    String  createValidValueDefinition();


    void    updateValidValueDefinition();


    void    deleteValidValueDefinition();


    void    attachValidValueToSet();


    void    detachValidValueFromSet();


    void    addValidValueImplementation();


    void    assignValidValueToConsumer();

    ValidValue getValidValueByGUID();
    ValidValue   getValidValueByName();

    List<ValidValue> findValidValues();
}

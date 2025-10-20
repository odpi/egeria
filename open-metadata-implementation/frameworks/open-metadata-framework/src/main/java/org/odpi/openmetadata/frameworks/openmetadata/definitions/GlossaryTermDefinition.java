/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.definitions;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * The GlossaryTermDefinition is used to populate the sustainability glossary.
 */
public interface GlossaryTermDefinition extends ReferenceableDefinition
{
    /**
     * Return the type of this element.
     *
     * @return string
     */
    @Override
    default String getTypeName() { return OpenMetadataType.GLOSSARY_TERM.typeName; }


    String getSummary();


    String getAbbreviation();


    String getUrl();


}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.APIOperation;

import java.util.Objects;

/**
 * An asset that is a DeployedAPI has a schema made up of operations.  These operations have a header, request and a response.
 * Each of these has a list of schema attributes.
 */
public  class DeployedAPIOperation extends APIOperation
{
    protected SchemaAttributes headerAttributes   = null;
    protected SchemaAttributes requestAttributes  = null;
    protected SchemaAttributes responseAttributes = null;


    /**
     * Constructor used by the subclasses
     */
    protected DeployedAPIOperation()
    {
        super();
    }



    /**
     * Bean constructor
     *
     * @param schemaBean bean containing the schema properties
     */
    public DeployedAPIOperation(APIOperation schemaBean)
    {
        super(schemaBean);
    }


    /**
     * Copy/clone Constructor.
     *
     * @param template template object to copy.
     */
    public DeployedAPIOperation(DeployedAPIOperation template)
    {
        super(template);

        if (template != null)
        {
            this.headerAttributes = template.getHeaderAttributes();
            this.requestAttributes = template.getRequestAttributes();
            this.responseAttributes = template.getResponseAttributes();
        }
    }


    /**
     * Return the list of schema attributes in this API operation's header.
     *
     * @return SchemaAttributes
     */
    public SchemaAttributes getHeaderAttributes()
    {
        return headerAttributes;
    }


    /**
     * Return the list of schema attributes in this API operation's request.
     *
     * @return SchemaAttributes
     */
    public SchemaAttributes getRequestAttributes()
    {
        return requestAttributes;
    }

    /**
     * Return the list of schema attributes in this API operation's response.
     *
     * @return SchemaAttributes
     */
    public SchemaAttributes getResponseAttributes()
    {
        return responseAttributes;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DeployedAPIOperation{" +
                       "headerAttributes=" + headerAttributes +
                       ", requestAttributes=" + requestAttributes +
                       ", responseAttributes=" + responseAttributes +
                       ", command='" + getCommand() + '\'' +
                       ", headerSchemaType=" + getHeaderSchemaType() +
                       ", requestSchemaType=" + getRequestSchemaType() +
                       ", responseSchemaType=" + getResponseSchemaType() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", meanings=" + getMeanings() +
                       ", searchKeywords=" + getSearchKeywords() +
                       ", headerVersion=" + getHeaderVersion() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", isDeprecated=" + getIsDeprecated() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", calculatedValue=" + getIsCalculatedValue() +
                       ", expression='" + getExpression() + '\'' +
                       ", formula='" + getFormula() + '\'' +
                       ", queries=" + getQueries() +
                       ", versionNumber='" + getVersionNumber() + '\'' +
                       ", author='" + getAuthor() + '\'' +
                       ", usage='" + getUsage() + '\'' +
                       ", encodingStandard='" + getEncodingStandard() + '\'' +
                       ", namespace='" + getNamespace() + '\'' +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DeployedAPIOperation that = (DeployedAPIOperation) objectToCompare;
        return Objects.equals(headerAttributes, that.headerAttributes) &&
                       Objects.equals(requestAttributes, that.requestAttributes) &&
                       Objects.equals(responseAttributes, that.responseAttributes);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), headerAttributes, requestAttributes, responseAttributes);
    }
}
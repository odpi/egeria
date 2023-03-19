/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.context;



/**
 * IntegrationGovernanceContext provides access to extra services from the Governance Action Framework (GAF).
 */
public class IntegrationGovernanceContext
{
    protected final OpenMetadataAccess      openMetadataAccess;
    protected final MultiLanguageManagement multiLanguageManagement;
    protected final StewardshipAction       stewardshipAction;
    protected final ValidMetadataValues     validMetadataValues;


    /**
     * Create an integration context.
     *
     * @param openMetadataAccess Governance Action Framework (GAF) extension for accessing open metadata
     * @param multiLanguageManagement Governance Action Framework (GAF) extension for managing multi-language content
     * @param stewardshipAction Governance Action Framework (GAF) extension to requesting help from stewards
     * @param validMetadataValues Governance Action Framework (GAF) extension for working with valid values for metadata
     */
    public IntegrationGovernanceContext(OpenMetadataAccess       openMetadataAccess,
                                        MultiLanguageManagement  multiLanguageManagement,
                                        StewardshipAction        stewardshipAction,
                                        ValidMetadataValues      validMetadataValues)
    {
        this.openMetadataAccess          = openMetadataAccess;
        this.multiLanguageManagement     = multiLanguageManagement;
        this.stewardshipAction           = stewardshipAction;
        this.validMetadataValues         = validMetadataValues;
    }


    /**
     * Return an extension to the context that provides access to the open metadata store from the Governance Action Framework (GAF).
     *
     * @return open metadata store context extension
     */
    public OpenMetadataAccess getOpenMetadataAccess()
    {
        return openMetadataAccess;
    }


    /**
     * Return an extension to the context that provides access to the multi-language support from the Governance Action Framework (GAF).
     *
     * @return multi-language management context extension
     */
    public MultiLanguageManagement getMultiLanguageManagement()
    {
        return multiLanguageManagement;
    }


    /**
     * Return an extension to the context that provides access to the stewardship action support from the Governance Action Framework (GAF).
     * This supports methods to create incident reports and to dos.
     *
     * @return stewardship action context extension
     */
    public StewardshipAction getStewardshipAction()
    {
        return stewardshipAction;
    }


    /**
     * Return an extension to the context that provides access to the valid values defined for open metadata from the Governance Action Framework
     * (GAF).
     *
     * @return valid metadata values context extension
     */
    public ValidMetadataValues getValidMetadataValues()
    {
        return validMetadataValues;
    }
}

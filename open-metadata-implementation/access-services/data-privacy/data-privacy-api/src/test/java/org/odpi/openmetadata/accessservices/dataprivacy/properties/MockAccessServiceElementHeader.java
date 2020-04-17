/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataprivacy.properties;


/**
 * MockAccessServiceElementHeader provides a concrete class to test the access service's element header
 */
public class MockAccessServiceElementHeader extends DataPrivacyElementHeader
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public MockAccessServiceElementHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to clone
     */
    public MockAccessServiceElementHeader(MockAccessServiceElementHeader  template)
    {
        super(template);
    }
}

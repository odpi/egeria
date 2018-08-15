/* SPDX-License-Identifier: Apache-2.0 */
package org.apache.atlas.virtualiser;


import org.apache.atlas.virtualiser.util.PropertiesHelper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

/**
 * KafkaVirtualiserTest is not able to run without local Gaian running.
 * And you should see the test is succeed. And there are two Logical Tables created for the local Gaian.
 */
public class KafkaVirtualizerTest {
    //json file received from Information View OMAS
    private String IVJson = null;
    private KafkaVirtualiser kafkaVirtualiser;
    private static final String TESTIN = "json/testIn.json";
    private ClassLoader classLoader;

    @BeforeMethod
    public void setUp() throws Exception {
        classLoader = this.getClass().getClassLoader();
        IVJson = JsonReadHelper.readFile(new File(classLoader.getResource(TESTIN).getFile()));
        KafkaVirtualiserProducerForTest kafkaVirtualiserProducerForTest = new KafkaVirtualiserProducerForTest();
        kafkaVirtualiserProducerForTest.runProducer(IVJson);
        kafkaVirtualiserProducerForTest.closeProducer();
        kafkaVirtualiser = new KafkaVirtualiser();
        PropertiesHelper.loadProperties();
    }

    @Test
    public void testListenToIVOMAS() throws Exception {
        kafkaVirtualiser.listenToIVOMAS();
    }


}
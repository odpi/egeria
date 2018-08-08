/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram;


import org.odpi.openmetadata.accessservices.governanceprogram.properties.DataStrategy;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.Regulation;

import java.util.List;

/**
 * The GovernanceDriversInterface supports the documentation of the business imperatives and regulations that
 * will drive the governance program.   The results of this process is a data strategy definition and a documented
 * list of regulations.  Together, they define the requirements that feed into the governance program's policy
 * making phase.
 */
public interface GovernanceDriversInterface
{
    String  createDataStrategy();
    void    updateDataStrategy();
    void    deleteDatastrategy();

    DataStrategy  getDataStrategyByGUID();
    DataStrategy  getDataStrategyByDocId();

    List<String> getAllDataStrategies();

    String createRegulation();
    void   updateRegulation();
    void   deleteRegulation();

    Regulation getRegulationByGUID();
    Regulation getRegulationByDocId();

    List<String> getAllRegulations();



    void linkGovernanceDrivers();




}

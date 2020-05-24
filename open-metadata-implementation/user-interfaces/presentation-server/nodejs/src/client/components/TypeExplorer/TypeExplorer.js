/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext } from "react";

import { IdentificationContext } from "../../contexts/IdentificationContext";

export default function TypeExplorer() {
  const identificationContext = useContext(IdentificationContext);
  console.log("Type explorer identificationContext", identificationContext);

  return (
    <div>
       type explorer
    </div>
  );
}
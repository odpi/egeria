/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import StartingNodeNavigation from "./StartingNodeNavigation";

export default function StartingCategoryNavigation({ match }) {
  return (
    <StartingNodeNavigation match={match} nodeTypeName="category" />
  );
}

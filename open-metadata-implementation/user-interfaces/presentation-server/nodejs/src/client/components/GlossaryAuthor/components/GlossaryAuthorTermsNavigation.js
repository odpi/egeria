/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import CardViewChildrenNavigation from "./CardViewChildrenNavigation";
import { withRouter } from "react-router-dom";
/**
 * This is a card view of Terms
 */
const GlossaryAuthorTermsNavigation = (props) => {
  return (
      <CardViewChildrenNavigation
        nodeTypeName="term"
        match={props.match}
        getURLForChildren={props.getTermsURL}
      />
  );
}
export default withRouter(GlossaryAuthorTermsNavigation);

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import TermCardViewChildrenNavigation from "./TermCardViewChildrenNavigation";
import { withRouter } from "react-router-dom";
/**
 * This is a card view of Terms
 */
const GlossaryAuthorTermsNavigation = (props) => {
  return (
      <TermCardViewChildrenNavigation
        match={props.match}
        getURLForChildren={props.getTermsURL}
      />
  );
}
export default withRouter(GlossaryAuthorTermsNavigation);

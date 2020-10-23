/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import CardViewNavigation from "./CardViewNavigation";
/**
 * This is a card view of Glossaries.
 */
export default function GlossaryAuthorNavigation(props) {
  return <CardViewNavigation nodeTypeName="glossary" match={props.match} />;
}

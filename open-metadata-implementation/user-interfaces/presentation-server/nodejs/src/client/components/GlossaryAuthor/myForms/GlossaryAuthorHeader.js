/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { Component } from "react";
import GlossaryAuthorMyGlossary from "./GlossaryAuthorMyGlossary";
import GlossaryAuthorMyProject from "./GlossaryAuthorMyProject";

export default class GlossaryAuthorHeader extends Component {

  render() {
    return (
     <div>
        <GlossaryAuthorMyProject/>
       <GlossaryAuthorMyGlossary/>
     </div>
    );
  }
}

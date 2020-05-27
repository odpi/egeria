/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState } from "react";
import PropTypes from "prop-types";
import { Button } from "react-bootstrap";
import NodeCreate from "../NodeCreate.js";

const GlossaryAuthorMyNode = (props) => {
  const [create, setCreate] = useState(false);
  const handleOnClick = e => {
    e.preventDefault();
    setCreate(true);
  };
  if (useState(create)) {
    return <NodeCreate typeKey={props.typeKey} />;
  } else {
    return (
      <div class="bx--grid">
        <div class="bx--row">
          <div class="bx--col">
            <Button kind="primary">Recent</Button>
          </div>
          <div class="bx--col">
            <Button kind="primary">Favourites</Button>
          </div>
          <div class="bx--col">
            <Button kind="primary" onClick={handleOnClick}>
              Create New
            </Button>
          </div>
          <div class="bx--col">
            <Button kind="primary">All {props.typeKey}</Button>
          </div>
        </div>
      </div>
    );
  }
};

export default GlossaryAuthorMyNode;
/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { Component } from "react";
import Home from "./components/Home";
import GlossaryAuthor from "./components/GlossaryAuthor";
import TypeExplorer from "./components/TypeExplorer";
import { Link, Route, Switch } from "react-router-dom";
import PropTypes from "prop-types";
import { Content } from "carbon-components-react/lib/components/UIShell";

export default class StoryContent extends Component {
  function GetId() {

    const { id } = useParams();
    console.log(id);

    return (
        <div>
            <TaskDetail taskId={id} />
        </div>
    );
}
  render() {
    const propTypes = {
      match: PropTypes.object.isRequired,
      location: PropTypes.object.isRequired,
      history: PropTypes.object.isRequired
    };

    return (
      <Content id="main-content">
        <div>
          <Route path={this.props.match.url + "/home"} component={Home} />
          <Route path={this.props.match.url + "/glossary-author"}>
            <GlossaryAuthor />
          </Route>
          <Route path={this.props.match.url + "/type-explorer"}>
            <TypeExplorer />
          </Route>
        </div>
      </Content>
    );
  }
}

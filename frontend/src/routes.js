import React from "react";
import { BrowserRouter, Route, Switch, Redirect } from "react-router-dom";
import AuthenticationService from "./services/authenticationService";
import SignInComponent from "./components/SignInComponent";
import HomeComponent from "./components/HomeComponent";

const PrivateRoute = ({ component: Component, ...rest }) => (
  <Route
    {...rest}
    render={props =>
      AuthenticationService.isAuthenticated() ? (
        <Component {...props} />
      ) : (
        <Redirect to={{ pathname: "/login", state: { from: props.location } }} />
      )
    }
  />
);

const Routes = () => (
  <BrowserRouter>
    <Switch>
      <Redirect exact from="/" to="/login" />
      <Route exact path="/login" component={SignInComponent} />
      <PrivateRoute path="/home" component={HomeComponent} />
      <Route path="*" component={() => <h1>Page not found</h1>} />
    </Switch>
  </BrowserRouter>
);

export default Routes;
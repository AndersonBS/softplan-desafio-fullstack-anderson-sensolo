import React from "react";
import { BrowserRouter, Route, Switch, Redirect } from "react-router-dom";
import AuthenticationService from "./services/authenticationService";
import SignInComponent from "./components/SignInComponent";
import HomeComponent from "./components/HomeComponent";
import ListUsuarioComponent from "./components/usuario/ListUsuarioComponent";
import FormUsuarioComponent from "./components/usuario/FormUsuarioComponent";
import ListProcessoComponent from "./components/processo/ListProcessoComponent";
import FormProcessoComponent from "./components/processo/FormProcessoComponent";
import ListProcessoUsuarioComponent from "./components/processoUsuario/ListProcessoUsuarioComponent";
import FormProcessoUsuarioComponent from "./components/processoUsuario/FormProcessoUsuarioComponent";
import ListParecerComponent from "./components/parecer/ListParecerComponent";
import FormParecerComponent from "./components/parecer/FormParecerComponent";

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
      <PrivateRoute path="/usuarios" component={ListUsuarioComponent} />
      <PrivateRoute path="/usuario/:codigo" component={FormUsuarioComponent} />
      <PrivateRoute path="/processos" component={ListProcessoComponent} />
      <PrivateRoute path="/processo/:codigo/atribuicoes" component={ListProcessoUsuarioComponent} />
      <PrivateRoute path="/processo/:codigo/atribuicao" component={FormProcessoUsuarioComponent} />
      <PrivateRoute path="/processo/:codigo" component={FormProcessoComponent} />
      <PrivateRoute path="/pareceres" component={ListParecerComponent} /> 
      <PrivateRoute path="/parecer/:codigoParecer/processo/:codigoProcesso/autor/:codigoAutor" component={FormParecerComponent} />
      <PrivateRoute path="/proibido" component={() => <h1 className="form-signin-heading text-muted"><b>403</b><br />Sem permissões!</h1>} />
      <Route path="*" component={() => <h1 className="form-signin-heading text-muted"><b>404</b><br />Página não encontrada!</h1>} />
    </Switch>
  </BrowserRouter>
);

export default Routes;
import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import AuthenticationService from "../services/authenticationService";
import FooterComponent from "./FooterComponent";
import HeaderComponent from "./HeaderComponent";

class HomeComponent extends Component {
    constructor(props) {
        super(props);

        const user = AuthenticationService.getLoggedInUser();

        this.state = {
            nome: user.nome,
            permissao: user.permissao
        }
    }

    render() {
        return (
            <div>
                <HeaderComponent />
                <div className="container">
                    <h1 className="form-signin-heading text-muted">Olá {this.state.nome}!</h1>
                    <br /><hr /><br />
                    <h3 className="form-signin-heading text-muted">O que vamos fazer hoje?</h3>
                    <br />
                    <ul className="nav justify-content-center">
                        {this.state.permissao === 'Administrador' &&
                            <li className="nav-item px-3">
                                <a className="nav-link active" href="/usuarios">
                                    <img height="128px" src={process.env.PUBLIC_URL + '/usuarios.png'} alt="Usuários" />
                                    <h4 className="form-signin-heading text-muted">Usuários</h4>
                                </a>
                            </li>
                        }
                        {(this.state.permissao === 'Administrador' || this.state.permissao === 'Triador' || this.state.permissao === 'Finalizador') &&
                            <li className="nav-item px-3">
                                <a className="nav-link" href="/processos">
                                    <img height="128px" src={process.env.PUBLIC_URL + '/processos.png'} alt="Processos" />
                                    <h4 className="form-signin-heading text-muted">Processos</h4>
                                </a>
                            </li>
                        }
                        {(this.state.permissao === 'Administrador' || this.state.permissao === 'Finalizador') &&
                            <li className="nav-item px-3">
                                <a className="nav-link" href="/pareceres">
                                    <img height="128px" src={process.env.PUBLIC_URL + '/pareceres.png'} alt="Pareceres" />
                                    <h4 className="form-signin-heading text-muted">Pareceres</h4>
                                </a>
                            </li>
                        }
                    </ul>
                </div>
                <FooterComponent />
            </div>
        );
    }
}

export default withRouter(HomeComponent);
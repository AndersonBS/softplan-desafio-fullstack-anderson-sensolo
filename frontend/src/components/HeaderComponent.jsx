import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import AuthenticationService from "../services/authenticationService";
import '../styles/headerStyle.css';

class HeaderComponent extends Component {
    constructor(props) {
        super(props);

        const user = AuthenticationService.getLoggedInUser();

        this.state = {
            codigo: user.codigo,
            nome: user.nome,
            email: user.email,
            permissao: user.permissao
        }

        this.logout = this.logout.bind(this)
    }

    logout() {
        this.props.history.push('/login')
    }

    render() {
        return (
            <nav className="navbar fixed-top navbar-expand-lg navbar-dark bg-dark">
                <a className="navbar-brand .align-middle" href="/home">
                    <img className="my-brand" src={process.env.PUBLIC_URL + '/favicon-32x32.png'} alt="Softplan" />
                    Softplan
                </a>
                <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" 
                        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav mr-auto">
                        {this.state.permissao === 'Administrador' &&
                            <li className="nav-item">
                                <a className="nav-link" href="/usuarios">Usuarios</a>
                            </li>
                        }
                        {(this.state.permissao === 'Administrador' || this.state.permissao === 'Triador' || this.state.permissao === 'Finalizador') &&
                            <li className="nav-item">
                                <a className="nav-link" href="/processos">Processos</a>
                            </li>
                        }
                        {(this.state.permissao === 'Administrador' || this.state.permissao === 'Finalizador') &&
                            <li className="nav-item">
                                <a className="nav-link" href="/pareceres">Pareceres</a>
                            </li>
                        }
                    </ul>
                    <span className="navbar-text">
                        {this.state.codigo} - {this.state.nome} ({this.state.email})
                    </span>
                    <button className="btn btn-outline-danger my-2 my-sm-0" onClick={this.logout}>Sair</button>
                </div>
            </nav>
        );
    }
}

export default withRouter(HeaderComponent);
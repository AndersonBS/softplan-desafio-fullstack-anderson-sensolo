import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import AuthenticationService from "../services/authenticationService";
import '../styles/signInStyles.css';

class SignInComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            username: "",
            password: "",
            error: ""
        };

        AuthenticationService.logout();
    }

    handleSignIn = async e => {
        e.preventDefault();
        const { username, password } = this.state;
        if (!username || !password) {
            this.setState({ error: "Preencha login e senha para continuar!" });
        } else {
            try {
                await AuthenticationService.login(username, password);
                this.props.history.push('/home');
            } catch (e) {
                this.setState({ error: "Houve um problema com o login, verifique suas credenciais!" })
            }
        }
    };

    render() {
        return (
            <div className="container">
                {this.state.error && <div className="alert alert-danger" role="alert">{this.state.error}</div>}
                <form className="form-signin" onSubmit={this.handleSignIn}>
                    <h1 className="form-signin-heading text-muted">Softplan</h1>
                    <input type="username" className="form-control" placeholder="Login"
                            onChange={e => this.setState({ username: e.target.value })} />
                    <input type="password" className="form-control" placeholder="Senha"
                            onChange={e => this.setState({ password: e.target.value })} />
                    <button className="btn btn-lg btn-primary btn-block" type="submit">
                        Entrar
                    </button>
                    <br /><br /><br />
                    <h6 className="form-signin-footer text-muted"><b>Login & Senha</b></h6>
                    <h6 className="form-signin-footer text-muted">Administrador: admin</h6>
                    <h6 className="form-signin-footer text-muted">Triador: triad</h6>
                    <h6 className="form-signin-footer text-muted">Finalizador: final</h6>
                </form> 
            </div>
        );
    }
}

export default withRouter(SignInComponent);
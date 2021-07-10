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

    componentDidMount() {
        const cardEffect = document.querySelector(".card-effect");
        const card = document.querySelector(".card");
        const title = document.querySelector(".title");
        const inputs = document.querySelector(".inputs");
        const submit = document.querySelector(".submit");
        const info = document.querySelector(".info");

        cardEffect.addEventListener("mousemove", (e) => {
            let xAxis = (window.innerWidth / 2 - e.pageX) / 20;
            let yAxis = (window.innerHeight / 2 - e.pageY) / 20;
            card.style.transform = `rotateY(${xAxis}deg) rotateX(${yAxis}deg)`;
        });

        cardEffect.addEventListener("mouseenter", (e) => {
            card.style.transition = "none";
            title.style.transition = "none";
            inputs.style.transition = "none";
            submit.style.transition = "none";
            info.style.transition = "none";
            title.style.transform = "translateZ(50px)";
            inputs.style.transform = "translateZ(45px)";
            submit.style.transform = "translateZ(40px)";
            info.style.transform = "translateZ(35px)";
        });

        cardEffect.addEventListener("mouseleave", (e) => {
            card.style.transition = "all 0.5s ease";
            title.style.transition = "all 0.5s ease";
            inputs.style.transition = "all 0.5s ease";
            submit.style.transition = "all 0.5s ease";
            info.style.transition = "all 0.5s ease";
            card.style.transform = `rotateY(0deg) rotateX(0deg)`;
            title.style.transform = "translateZ(0px)";
            inputs.style.transform = "translateZ(0px)";
            submit.style.transform = "translateZ(0px)";
            info.style.transform = "translateZ(0px)";
        });
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
                <div className="singin-container">
                    <div className="card-effect">
                        <div className="card">
                            {this.state.error && <div className="alert alert-danger" role="alert">{this.state.error}</div>}
                            <form className="form-signin" onSubmit={this.handleSignIn}>
                                <h1 className="form-signin-heading text-muted title">Softplan</h1>
                                <div className="inputs">
                                    <input type="username" className="form-control" placeholder="Login"
                                            onChange={e => this.setState({ username: e.target.value })} />
                                    <input type="password" className="form-control" placeholder="Senha"
                                            onChange={e => this.setState({ password: e.target.value })} />
                                </div>
                                <button className="btn btn-lg btn-primary btn-block submit" type="submit">
                                    Entrar
                                </button>
                                <br /><br /><br />
                                <div className="info">
                                    <h6 className="form-signin-footer text-muted"><b>Login & Senha</b></h6>
                                    <h6 className="form-signin-footer text-muted">Administrador: admin</h6>
                                    <h6 className="form-signin-footer text-muted">Triador: triad</h6>
                                    <h6 className="form-signin-footer text-muted">Finalizador: final</h6>
                                </div>
                            </form>
                        </div> 
                    </div>
                </div> 
            </div>
        );
    }
}

export default withRouter(SignInComponent);
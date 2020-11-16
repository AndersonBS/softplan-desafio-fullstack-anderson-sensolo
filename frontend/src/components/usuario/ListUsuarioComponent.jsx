import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import FooterComponent from "../FooterComponent";
import HeaderComponent from "../HeaderComponent";
import UsuarioService from "../../services/usuarioService";
import AuthenticationService from "../../services/authenticationService";

class ListUsuarioComponent extends Component {
    constructor(props) {
        super(props);

        const user = AuthenticationService.getLoggedInUser();
        if (user.permissao !== 'Administrador') {
            this.props.history.push('/proibido');
        }

        this.state = {
            usuarios: [],
            message: null,
            messageType: null
        };

        this.getUsuarios = this.getUsuarios.bind(this);
        this.deleteUsuarioClicked = this.deleteUsuarioClicked.bind(this);
        this.updateUsuarioClicked = this.updateUsuarioClicked.bind(this);
        this.addUsuarioClicked = this.addUsuarioClicked.bind(this);
        this.goBackClicked = this.goBackClicked.bind(this);
    }

    componentDidMount() {
        this.getUsuarios();
    }

    getUsuarios() {
        UsuarioService.getUsuarios()
            .then(response => {
                this.setState({ usuarios: response.data });
            })
            .catch(this.setState({ usuarios: [] }));
    }

    deleteUsuarioClicked(codigoUsuario) {
        UsuarioService.deleteUsuario(codigoUsuario)
            .then(() => {
                this.getUsuarios();
                this.setState({ message: `Usuário ${codigoUsuario} excluído com sucesso!` });
                this.setState({ messageType: 'success' });
            })
            .catch(() => {
                this.setState({ message: `Ocorreu um erro ao tentar excluir o usuário ${codigoUsuario}, verifique suas dependências!` });
                this.setState({ messageType: 'danger' });
            })
    }

    updateUsuarioClicked(codigoUsuario) {
        this.props.history.push(`/usuario/${codigoUsuario}`)
    }

    addUsuarioClicked() {
        this.props.history.push(`/usuario/-1`)
    }

    goBackClicked() {
        this.props.history.push('/home');
    }

    render() {
        return (
            <div>
                <HeaderComponent />
                <div className="container">
                    <h1 className="form-signin-heading text-muted">
                        <img className="px-3" height="64px" src={process.env.PUBLIC_URL + '/usuarios.png'} alt="Usuários" />
                        Usuários
                    </h1>
                    <br />
                    {this.state.message && <div className={'alert alert-' + this.state.messageType} role="alert">{this.state.message}</div>}
                    <div className="table-responsive">
                        <table className="table table-sm table-striped table-hover">
                            <thead className="thead-dark">
                                <tr>
                                    <th className="text-center">Código</th>
                                    <th>Nome</th>
                                    <th>Permissão</th>
                                    <th className="text-center">Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    this.state.usuarios.map(
                                        usuario =>
                                            <tr key={usuario.codigo}>
                                                <td className="text-center">{usuario.codigo}</td>
                                                <td>{usuario.nome}</td>
                                                <td>{usuario.permissao}</td>
                                                <td className="text-center">
                                                    <button className="btn btn-warning mx-2" onClick={() => this.updateUsuarioClicked(usuario.codigo)}>Editar</button>
                                                    <button className="btn btn-danger mx-2" onClick={() => this.deleteUsuarioClicked(usuario.codigo)}>Excluir</button>
                                                </td>
                                            </tr>
                                    )
                                }
                            </tbody>
                        </table>
                    </div>
                    <div className="row justify-content-center">
                        <button className="btn btn-primary mx-2" type="submit" onClick={this.goBackClicked}>Voltar</button>
                        <button className="btn btn-success mx-2" onClick={this.addUsuarioClicked}>Adicionar</button>
                    </div>
                </div>
                <FooterComponent />
            </div>
        );
    }
}

export default withRouter(ListUsuarioComponent);
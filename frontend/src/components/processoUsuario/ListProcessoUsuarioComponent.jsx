import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import FooterComponent from "../FooterComponent";
import HeaderComponent from "../HeaderComponent";
import ProcessoService from "../../services/processoService";
import AuthenticationService from "../../services/authenticationService";

class ListProcessoUsuarioComponent extends Component {
    constructor(props) {
        super(props);

        const user = AuthenticationService.getLoggedInUser();
        if (user.permissao !== 'Administrador' && user.permissao !== 'Triador') {
            this.props.history.push('/proibido');
        }

        this.state = {
            codigoProcesso: this.props.match.params.codigo,
            processoUsuarios: [],
            message: null,
            messageType: null,
            permissao: user.permissao
        };

        this.getProcessoUsuarios = this.getProcessoUsuarios.bind(this);
        this.removeProcessoUsuarioClicked = this.removeProcessoUsuarioClicked.bind(this);
        this.addProcessoUsuarioClicked = this.addProcessoUsuarioClicked.bind(this);
        this.goBackClicked = this.goBackClicked.bind(this);
    }

    componentDidMount() {
        this.getProcessoUsuarios();
    }

    getProcessoUsuarios() {
        ProcessoService.getProcessoUsuarios(this.state.codigoProcesso)
            .then(response => {
                this.setState({ processoUsuarios: response.data });
            })
            .catch(this.setState({ processoUsuarios: [] }));
    }

    removeProcessoUsuarioClicked(codigoUsuario) {
        ProcessoService.removeProcessoUsuario(this.state.codigoProcesso, codigoUsuario)
            .then(() => {
                this.getProcessoUsuarios();
                this.setState({ message: `Atribuição do usuário ${codigoUsuario} ao processo ${this.state.codigoProcesso} removida com sucesso!` });
                this.setState({ messageType: 'success' });
            })
            .catch(() => {
                this.setState({ message: `Ocorreu um erro ao tentar remover o usuário ${codigoUsuario} do processo ${this.state.codigoProcesso}, verifique suas dependências!` });
                this.setState({ messageType: 'danger' });
            });
    }

    addProcessoUsuarioClicked() {
        this.props.history.push(`/processo/${this.state.codigoProcesso}/atribuicao`)
    }

    goBackClicked() {
        this.props.history.push('/processos');
    }

    render() {
        return (
            <div>
                <HeaderComponent />
                <div className="container">
                    <h1 className="form-signin-heading text-muted">
                        <img className="px-3" height="64px" src={process.env.PUBLIC_URL + '/usuarios.png'} alt="Usuários" />
                        Atribuições
                        <img className="px-3" height="64px" src={process.env.PUBLIC_URL + '/processos.png'} alt="Processos" />
                    </h1>
                    <br />
                    {this.state.message && <div className={'alert alert-' + this.state.messageType} role="alert">{this.state.message}</div>}
                    <div className="table-responsive">
                        <table className="table table-sm table-striped table-hover">
                            <thead className="thead-dark">
                                <tr>
                                    <th>Processo</th>
                                    <th>Usuário</th>
                                    <th className="text-center">Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    this.state.processoUsuarios.map(
                                        processoUsuario =>
                                            <tr key={processoUsuario.processo + processoUsuario.usuario}>
                                                <td>{processoUsuario.processo}</td>
                                                <td>{processoUsuario.usuario}</td>
                                                <td className="text-center">
                                                    {(this.state.permissao === 'Administrador' || this.state.permissao === 'Triador') &&
                                                        <button className="btn btn-danger mx-2" onClick={() => 
                                                            this.removeProcessoUsuarioClicked(processoUsuario.usuario.charAt(0))}>Remover</button>
                                                    }
                                                </td>
                                            </tr>
                                    )
                                }
                            </tbody>
                        </table>
                    </div>
                    <div className="row justify-content-center">
                        <button className="btn btn-primary mx-2" type="submit" onClick={this.goBackClicked}>Voltar</button>
                        {(this.state.permissao === 'Administrador' || this.state.permissao === 'Triador') &&
                            <button className="btn btn-success mx-2" onClick={this.addProcessoUsuarioClicked}>Adicionar</button>
                        }
                    </div>
                </div>
                <FooterComponent />
            </div>
        );
    }
}

export default withRouter(ListProcessoUsuarioComponent);
import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import FooterComponent from "../FooterComponent";
import HeaderComponent from "../HeaderComponent";
import ProcessoService from "../../services/processoService";
import AuthenticationService from "../../services/authenticationService";

class ListProcessoComponent extends Component {
    constructor(props) {
        super(props);

        const user = AuthenticationService.getLoggedInUser();
        if (user.permissao !== 'Administrador' && user.permissao !== 'Triador' && user.permissao !== 'Finalizador') {
            this.props.history.push('/proibido');
        }

        this.state = {
            processos: [],
            message: null,
            messageType: null,
            permissao: user.permissao,
            codigoAutor: user.codigo
        };

        this.getProcessos = this.getProcessos.bind(this);
        this.deleteProcessoClicked = this.deleteProcessoClicked.bind(this);
        this.updateProcessoClicked = this.updateProcessoClicked.bind(this);
        this.addProcessoClicked = this.addProcessoClicked.bind(this);
        this.goBackClicked = this.goBackClicked.bind(this);
        this.atribuirClicked = this.atribuirClicked.bind(this);
    }

    componentDidMount() {
        this.getProcessos();
    }

    getProcessos() {
        ProcessoService.getProcessos()
            .then(response => {
                this.setState({ processos: response.data });
            })
            .catch(this.setState({ processos: [] }));
    }

    deleteProcessoClicked(codigoProcesso) {
        ProcessoService.deleteProcesso(codigoProcesso)
            .then(() => {
                this.getProcessos();
                this.setState({ message: `Processo ${codigoProcesso} excluído com sucesso!` });
                this.setState({ messageType: 'success' });
            })
            .catch(() => {
                this.setState({ message: `Ocorreu um erro ao tentar excluir o processo ${codigoProcesso}, verifique suas dependências!` });
                this.setState({ messageType: 'danger' });
            })
    }

    updateProcessoClicked(codigoProcesso) {
        this.props.history.push(`/processo/${codigoProcesso}`)
    }

    addProcessoClicked() {
        this.props.history.push(`/processo/-1`)
    }

    goBackClicked() {
        this.props.history.push('/home');
    }

    atribuirClicked(codigoProcesso) {
        this.props.history.push(`/processo/${codigoProcesso}/atribuicoes`);
    }

    parecerClicked(codigoProcesso) {
        this.props.history.push(`/parecer/-1/processo/${codigoProcesso}/autor/${this.state.codigoAutor}`);
    }

    render() {
        return (
            <div>
                <HeaderComponent />
                <div className="container">
                    <h1 className="form-signin-heading text-muted">
                        <img className="px-3" height="64px" src={process.env.PUBLIC_URL + '/processos.png'} alt="Usuários" />
                        Processos
                    </h1>
                    <br />
                    {this.state.message && <div className={'alert alert-' + this.state.messageType} role="alert">{this.state.message}</div>}
                    <div className="table-responsive">
                        <table className="table table-sm table-striped table-hover">
                            <thead className="thead-dark">
                                <tr>
                                    <th className="text-center">Código</th>
                                    <th>Nome</th>
                                    <th>Status</th>
                                    <th>Responsável</th>
                                    <th className="text-center">Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    this.state.processos.map(
                                        processo =>
                                            <tr key={processo.codigo}>
                                                <td className="text-center">{processo.codigo}</td>
                                                <td>{processo.nome}</td>
                                                <td>{processo.status}</td>
                                                <td>{processo.responsavel}</td>
                                                <td className="text-center">
                                                    {processo.parecerPendente && 
                                                        <button className="btn btn-primary mx-2" onClick={() => this.parecerClicked(processo.codigo)}>Parecer</button>
                                                    }
                                                    {(this.state.permissao === 'Administrador' || this.state.permissao === 'Triador') &&
                                                        <button className="btn btn-success mx-2" onClick={() => this.atribuirClicked(processo.codigo)}>Atribuir</button>
                                                    }
                                                    {(this.state.permissao === 'Administrador' || this.state.permissao === 'Triador') &&
                                                        <button className="btn btn-warning mx-2" onClick={() => this.updateProcessoClicked(processo.codigo)}>Editar</button>
                                                    }
                                                    {(this.state.permissao === 'Administrador' || this.state.permissao === 'Triador') &&
                                                        <button className="btn btn-danger mx-2" onClick={() => this.deleteProcessoClicked(processo.codigo)}>Excluir</button>
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
                            <button className="btn btn-success mx-2" onClick={this.addProcessoClicked}>Adicionar</button>
                        }
                    </div>
                </div>
                <FooterComponent />
            </div>
        );
    }
}

export default withRouter(ListProcessoComponent);
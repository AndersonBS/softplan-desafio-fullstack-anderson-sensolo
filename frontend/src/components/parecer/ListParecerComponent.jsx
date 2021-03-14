import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import ReactPaginate from "react-paginate";
import FooterComponent from "../FooterComponent";
import HeaderComponent from "../HeaderComponent";
import ParecerService from "../../services/parecerService";
import AuthenticationService from "../../services/authenticationService";

class ListParecerComponent extends Component {
    constructor(props) {
        super(props);

        const user = AuthenticationService.getLoggedInUser();
        if (user.permissao !== 'Administrador' && user.permissao !== 'Finalizador') {
            this.props.history.push('/proibido');
        }

        this.state = {
            pareceres: [],
            message: null,
            messageType: null,
            permissao: user.permissao,
            pageSize: 7,
            pageCount: 1,
            selectedPage : 1,
            pageRangeDisplayed: 5,
            marginPagesDisplayed: 2
        };

        this.getPareceres = this.getPareceres.bind(this);
        this.deleteParecerClicked = this.deleteParecerClicked.bind(this);
        this.updateParecerClicked = this.updateParecerClicked.bind(this);
        this.goBackClicked = this.goBackClicked.bind(this);
    }

    componentDidMount() {
        this.getPareceres(0);
    }

    getPareceres(selectedPage) {
        ParecerService.getPareceres(selectedPage, this.state.pageSize)
            .then(response => {
                this.setState({ 
                    pareceres: response.data.pareceres,
                    pageCount: response.data.totalPages,
                    selectedPage: response.data.selectedPage  
                });
            })
            .catch(this.setState({ pareceres: [] }));
    }

    deleteParecerClicked(codigoParecer) {
        ParecerService.deleteParecer(codigoParecer)
            .then(() => {
                this.getPareceres();
                this.setState({ message: `Parecer ${codigoParecer} excluído com sucesso!` });
                this.setState({ messageType: 'success' });
            })
            .catch(() => {
                this.setState({ message: `Ocorreu um erro ao tentar excluir o parecer ${codigoParecer}, verifique suas dependências!` });
                this.setState({ messageType: 'danger' });
            })
    }

    updateParecerClicked(codigoParecer) {
        this.props.history.push(`/parecer/${codigoParecer}/processo/-1/autor/-1`);
    }

    goBackClicked() {
        this.props.history.push('/home');
    }

    handlePageClick = (page) => {
        this.getPareceres(page.selected);
    };

    render() {
        return (
            <div>
                <HeaderComponent />
                <div className="container">
                    <h1 className="form-signin-heading text-muted">
                        <img className="px-3" height="64px" src={process.env.PUBLIC_URL + '/pareceres.png'} alt="Usuários" />
                        Pareceres
                    </h1>
                    <br />
                    {this.state.message && <div className={'alert alert-' + this.state.messageType} role="alert">{this.state.message}</div>}
                    <div className="table-responsive">
                        <table className="table table-sm table-striped table-hover">
                            <thead className="thead-dark">
                                <tr>
                                    <th className="text-center">Código</th>
                                    <th>Processo</th>
                                    <th>Autor</th>
                                    <th>Descrição</th>
                                    <th>Data</th>
                                    <th className="text-center">Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    this.state.pareceres.map(
                                        parecer =>
                                            <tr key={parecer.codigo}>
                                                <td className="text-center">{parecer.codigo}</td>
                                                <td>{parecer.processo}</td>
                                                <td>{parecer.autor}</td>
                                                <td>{parecer.descricao}</td>
                                                <td>{parecer.data}</td>
                                                <td className="text-center">
                                                    <button className="btn btn-warning mx-2" onClick={() => this.updateParecerClicked(parecer.codigo)}>Editar</button>
                                                    {this.state.permissao === 'Administrador' &&
                                                        <button className="btn btn-danger mx-2" onClick={() => this.deleteParecerClicked(parecer.codigo)}>Excluir</button>
                                                    }
                                                </td>
                                            </tr>
                                    )
                                }
                            </tbody>
                        </table>
                    </div>
                    {this.state.pareceres && this.state.pareceres.length > 0 &&
                    <ReactPaginate
                        previousLabel={"Anterior"}
                        nextLabel={"Próxima"}
                        breakLabel={"..."}
                        pageCount={this.state.pageCount}
                        pageRangeDisplayed={this.state.pageRangeDisplayed}
                        marginPagesDisplayed={this.state.marginPagesDisplayed}
                        forcePage={this.state.selectedPage}
                        onPageChange={this.handlePageClick}
                        containerClassName={"pagination justify-content-center"}
                        breakClassName="page-item"
                        breakLabel={<a className="page-link">...</a>}
                        pageClassName="page-item"
                        previousClassName="page-item"
                        nextClassName="page-item"
                        pageLinkClassName="page-link"
                        previousLinkClassName="page-link"
                        nextLinkClassName="page-link"
                        activeClassName={"active"} />
                    }
                    <div className="row justify-content-center">
                        <button className="btn btn-primary mx-2" type="submit" onClick={this.goBackClicked}>Voltar</button>
                    </div>
                </div>
                <FooterComponent />
            </div>
        );
    }
}

export default withRouter(ListParecerComponent);
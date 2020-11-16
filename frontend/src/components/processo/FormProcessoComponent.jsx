import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import { Formik, Form, Field, ErrorMessage } from "formik";
import FooterComponent from "../FooterComponent";
import HeaderComponent from "../HeaderComponent";
import ProcessoService from "../../services/processoService";
import AuthenticationService from "../../services/authenticationService";

class FormProcessoComponent extends Component {
    constructor(props) {
        super(props);

        const user = AuthenticationService.getLoggedInUser();
        if (user.permissao !== 'Administrador' && user.permissao !== 'Triador' && user.permissao !== 'Finalizador') {
            this.props.history.push('/proibido');
        }

        this.state = {
            responsaveis: [],
            codigo: this.props.match.params.codigo,
            nome: '',
            descricao: '',
            status: '',
            dataInicio: '',
            dataTermino: '',
            responsavel: '',
            message: ''
        };

        this.onSubmit = this.onSubmit.bind(this);
        this.validate = this.validate.bind(this);
        this.goBackClicked = this.goBackClicked.bind(this);
    }

    componentDidMount() {
        if (this.state.codigo !== '-1') {
            ProcessoService.getProcesso(this.state.codigo)
                .then(response => this.setState({
                    dataInclusao: response.data.dataInclusao,
                    nome: response.data.nome,
                    descricao: response.data.descricao,
                    status: response.data.status,
                    dataInicio: response.data.dataInicio,
                    dataTermino: response.data.dataTermino,
                    responsavel: response.data.responsavel
                }));
        }

        ProcessoService.getResponsaveis()
            .then(response => {
                this.setState({ 
                    responsaveis: response.data
                });
                if (this.state.codigo === '-1') {
                    this.setState({
                        responsavel: response.data[0].codigoNomeUsuario
                    });
                }
            });
    }

    onSubmit(values) {
        let processo = {
            nome: values.nome,
            descricao: values.descricao,
            status: values.status,
            dataInicio: values.dataInicio,
            dataTermino: values.dataTermino,
            responsavel: values.responsavel.charAt(0)
        }

        if (this.state.codigo === '-1') {
            ProcessoService.createProcesso(processo)
                .then(() => this.props.history.push('/processos'))
                .catch(error => {
                    if (error.response) {
                        this.setState({ message: error.response.data.mensagem });
                    } else {
                        this.setState({ message: 'Ocorreu um erro ao criar o processo!' });
                    }
                    
                });
        } else {
            ProcessoService.updateProcesso(this.state.codigo, processo)
                .then(() => this.props.history.push('/processos'))
                .catch(error => {
                    if (error.response) {
                        this.setState({ message: error.response.data.mensagem });
                    } else {
                        this.setState({ message: 'Ocorreu um erro ao atualizar o processo!' });
                    }
                });
        }
    }

    validate(values) {
        let errors = {}
        if (!values.nome) {
            errors.nome = 'Preencha o nome!';
        } else if (values.nome.length > 255) {
            errors.nome = 'O nome deve ter no máximo 63 caracteres!'
        }
        if (!values.descricao) {
            errors.descricao = 'Preencha a descrição!';
        } else if (values.descricao.length > 255) {
            errors.descricao = 'A descrição deve ter no máximo 255 caracteres!';
        }
        if (!values.responsavel) {
            errors.responsavel = 'Selecione o responsável!';
        }
        return errors;
    }

    goBackClicked() {
        this.props.history.push('/processos');
    }

    render() {
        let { codigo, status, nome, descricao, dataInicio, dataTermino, responsavel } = this.state;
        return (
            <div>
                <HeaderComponent />
                <div className="container">
                    <div className="row justify-content-md-center">
                        <div className="col col-lg-6">
                            <h1 className="form-signin-heading text-muted">
                                <img className="px-3" height="64px" src={process.env.PUBLIC_URL + '/processos.png'} alt="Processos" />
                                Processo
                            </h1>
                            {this.state.message && <div className="alert alert-danger" role="alert">{this.state.message}</div>}
                            <Formik
                                initialValues={{ codigo, status, nome, descricao, dataInicio, dataTermino, responsavel }}
                                onSubmit={this.onSubmit}
                                validateOnChange={false}
                                validateOnBlur={true}
                                validate={this.validate}
                                enableReinitialize={true}
                            >
                                {(
                                    <Form>
                                        <fieldset className="form-group">
                                            <div className="row">
                                                <div className="col">
                                                    <label>Código:</label>
                                                    <Field className="form-control" type="text" name="codigo" disabled />
                                                </div>
                                                <div className="col">
                                                    <label>Status:</label>
                                                    <Field className="form-control" type="text" name="status" disabled />
                                                </div>
                                            </div>
                                        </fieldset>
                                        <ErrorMessage name="nome" component="div" className="alert alert-danger" />
                                        <fieldset className="form-group">
                                            <label>Nome:</label>
                                            <Field className="form-control" type="text" name="nome" />
                                        </fieldset>
                                        <ErrorMessage name="descricao" component="div" className="alert alert-danger" />
                                        <fieldset className="form-group">
                                            <label>Descrição:</label>
                                            <Field className="form-control" type="text" name="descricao" />
                                        </fieldset>
                                        <fieldset className="form-group">
                                            <div className="row">
                                                <div className="col">
                                                    <label>Data de início:</label>
                                                    <Field className="form-control" type="date" name="dataInicio" disabled />
                                                </div>
                                                <div className="col">
                                                <label>Data de término:</label>
                                                    <Field className="form-control" type="date" name="dataTermino" disabled />
                                                </div>
                                            </div>
                                        </fieldset>
                                        <ErrorMessage name="responsavel" component="div" className="alert alert-danger" />
                                        <fieldset className="form-group">
                                            <label>Responsável:</label>
                                            <Field className="form-control" name="responsavel" as="select">
                                                {
                                                    this.state.responsaveis.map(
                                                        responsavel =>
                                                            <option key={responsavel.codigoUsuario} value={responsavel.codigoNomeUsuario}>{responsavel.codigoNomeUsuario}</option>
                                                    )
                                                }
                                            </Field>
                                        </fieldset>
                                        <div className="row justify-content-center">
                                            <button className="btn btn-primary mx-2" type="submit" onClick={this.goBackClicked}>Voltar</button>
                                            <button className="btn btn-success mx-2" type="submit">Salvar</button>
                                        </div>
                                    </Form>
                                )}
                            </Formik>
                        </div>
                    </div>
                </div>
                <FooterComponent />
            </div>
        );
    }
}

export default withRouter(FormProcessoComponent);
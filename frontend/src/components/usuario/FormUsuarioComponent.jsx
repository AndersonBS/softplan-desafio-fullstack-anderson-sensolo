import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import { Formik, Form, Field, ErrorMessage } from "formik";
import FooterComponent from "../FooterComponent";
import HeaderComponent from "../HeaderComponent";
import UsuarioService from "../../services/usuarioService";
import AuthenticationService from "../../services/authenticationService";

class FormUsuarioComponent extends Component {
    constructor(props) {
        super(props);

        const user = AuthenticationService.getLoggedInUser();
        if (user.permissao !== 'Administrador') {
            this.props.history.push('/proibido');
        }

        this.state = {
            codigo: this.props.match.params.codigo,
            nome: '',
            login: '',
            email: '',
            senha: '',
            permissao: 'Administrador',
            message: ''
        };

        this.onSubmit = this.onSubmit.bind(this);
        this.validate = this.validate.bind(this);
        this.goBackClicked = this.goBackClicked.bind(this);
    }

    componentDidMount() {
        if (this.state.codigo !== '-1') {
            UsuarioService.getUsuario(this.state.codigo)
                .then(response => this.setState({
                    dataInclusao: response.data.dataInclusao,
                    nome: response.data.nome,
                    login: response.data.login,
                    email: response.data.email,
                    senha: response.data.senha,
                    permissao: response.data.permissao
                }));
        }
    }

    onSubmit(values) {
        let usuario = {
            nome: values.nome,
            login: values.login,
            email: values.email,
            senha: values.senha,
            permissao: values.permissao
        }

        if (this.state.codigo === '-1') {
            UsuarioService.createUsuario(usuario)
                .then(() => this.props.history.push('/usuarios'))
                .catch(error => {
                    if (error.response) {
                        this.setState({ message: error.response.data.mensagem });
                    } else {
                        this.setState({ message: 'Ocorreu um erro ao criar o usuário!' });
                    }
                    
                });
        } else {
            UsuarioService.updateUsuario(this.state.codigo, usuario)
                .then(() => this.props.history.push('/usuarios'))
                .catch(error => {
                    if (error.response) {
                        this.setState({ message: error.response.data.mensagem });
                    } else {
                        this.setState({ message: 'Ocorreu um erro ao atualizar o usuário!' });
                    }
                });
        }
    }

    validate(values) {
        let errors = {}
        if (!values.nome) {
            errors.nome = 'Preencha o nome!';
        } else if (values.nome.length < 5 || values.nome.length > 255) {
            errors.nome = 'O nome deve ter entre 5 e 255 caracteres!'
        }
        if (!values.login) {
            errors.login = 'Preencha o login!';
        } else if (values.login.length < 5 || values.login.length > 255) {
            errors.login = 'O login deve ter entre 5 e 255 caracteres!';
        }
        if (!values.email) {
            errors.email = 'Preencha o e-mail!';
        } else if (values.email.length > 63) {
            errors.email = 'O e-mail deve ter no máximo 63 caracteres!';
        } else if (!values.email.match(/^([\w.%+-]+)@([\w-]+\.)+([\w]{2,})$/i)) {
            errors.email = 'O e-mail inserido não é válido!';
        }
        if (!values.senha) {
            errors.senha = 'Preencha a senha!';
        } else if (values.senha.length < 5 || values.senha.length > 63) {
            errors.senha = 'A senha deve ter entre 5 e 63 caracteres!';
        }
        if (!values.permissao) {
            errors.permissao = 'Selecione a permissão!';
        }
        return errors;
    }

    goBackClicked() {
        this.props.history.push('/usuarios');
    }

    render() {
        let { codigo, dataInclusao, nome, login, email, senha, permissao } = this.state;
        return (
            <div>
                <HeaderComponent />
                <div className="container">
                    <div className="row justify-content-md-center">
                        <div className="col col-lg-6">
                            <h1 className="form-signin-heading text-muted">
                                <img className="px-3" height="64px" src={process.env.PUBLIC_URL + '/usuarios.png'} alt="Usuários" />
                                Usuário
                            </h1>
                            {this.state.message && <div className="alert alert-danger" role="alert">{this.state.message}</div>}
                            <Formik
                                initialValues={{ codigo, dataInclusao, nome, login, email, senha, permissao }}
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
                                                    <label>Data de inclusão:</label>
                                                    <Field className="form-control" type="date" name="dataInclusao" disabled />
                                                </div>
                                            </div>
                                        </fieldset>
                                        <ErrorMessage name="nome" component="div" className="alert alert-danger" />
                                        <fieldset className="form-group">
                                            <label>Nome:</label>
                                            <Field className="form-control" type="text" name="nome" />
                                        </fieldset>
                                        <ErrorMessage name="login" component="div" className="alert alert-danger" />
                                        <fieldset className="form-group">
                                            <label>Login:</label>
                                            <Field className="form-control" type="text" name="login" />
                                        </fieldset>
                                        <ErrorMessage name="email" component="div" className="alert alert-danger" />
                                        <fieldset className="form-group">
                                            <label>E-mail:</label>
                                            <div className="input-group">
                                                <div className="input-group-prepend">
                                                    <div className="input-group-text">@</div>
                                                </div>
                                                <Field className="form-control" type="email" name="email" />
                                            </div>
                                        </fieldset>
                                        <ErrorMessage name="senha" component="div" className="alert alert-danger" />
                                        <fieldset className="form-group">
                                            <label>Senha:</label>
                                            <Field className="form-control" type="password" name="senha" />
                                        </fieldset>
                                        <ErrorMessage name="permissao" component="div" className="alert alert-danger" />
                                        <fieldset className="form-group">
                                            <label>Permissão:</label>
                                            <Field className="form-control" name="permissao" as="select">
                                                <option value="Administrador">Administrador</option>
                                                <option value="Triador">Triador</option>
                                                <option value="Finalizador">Finalizador</option>
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

export default withRouter(FormUsuarioComponent);
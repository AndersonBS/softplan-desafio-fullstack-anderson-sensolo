import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import { Formik, Form, Field, ErrorMessage } from "formik";
import FooterComponent from "../FooterComponent";
import HeaderComponent from "../HeaderComponent";
import ParecerService from "../../services/parecerService";
import AuthenticationService from "../../services/authenticationService";

class FormParecerComponent extends Component {
    constructor(props) {
        super(props);

        const user = AuthenticationService.getLoggedInUser();
        if (user.permissao !== 'Administrador' && user.permissao !== 'Finalizador') {
            this.props.history.push('/proibido');
        }

        this.state = {
            codigoParecer: this.props.match.params.codigoParecer,
            processo: this.props.match.params.codigoProcesso,
            autor: this.props.match.params.codigoAutor,
            descricao: '',
            message: ''
        };

        this.onSubmit = this.onSubmit.bind(this);
        this.validate = this.validate.bind(this);
        this.goBackClicked = this.goBackClicked.bind(this);
    }

    componentDidMount() {
        if (this.state.codigoParecer !== '-1') {
            ParecerService.getParecer(this.state.codigoParecer)
                .then(response => this.setState({
                    processo: response.data.processo,
                    autor: response.data.autor,
                    descricao: response.data.descricao
                }));
        }
    }

    onSubmit(values) {
        let parecer = {
            processo: values.processo.charAt(0),
            autor: values.autor.charAt(0),
            descricao: values.descricao
        }

        if (this.state.codigoParecer === '-1') {
            ParecerService.createParecer(parecer)
                .then(() => this.props.history.push('/pareceres'))
                .catch(error => {
                    if (error.response) {
                        this.setState({ message: error.response.data.mensagem });
                    } else {
                        this.setState({ message: 'Ocorreu um erro ao criar o parecer!' });
                    }
                    
                });
        } else {
            ParecerService.updateParecer(this.state.codigoParecer, parecer)
                .then(() => this.props.history.push('/pareceres'))
                .catch(error => {
                    if (error.response) {
                        this.setState({ message: error.response.data.mensagem });
                    } else {
                        this.setState({ message: 'Ocorreu um erro ao atualizar o parecer!' });
                    }
                });
        }
    }

    validate(values) {
        let errors = {}
        if (!values.descricao) {
            errors.descricao = 'Preencha a descrição!';
        } else if (values.descricao.length > 255) {
            errors.descricao = 'A descrição deve ter no máximo 255 caracteres!';
        }
        return errors;
    }

    goBackClicked() {
        this.props.history.push('/pareceres');
    }

    render() {
        let { codigoParecer, processo, autor, descricao } = this.state;
        return (
            <div>
                <HeaderComponent />
                <div className="container">
                    <div className="row justify-content-md-center">
                        <div className="col col-lg-6">
                            <h1 className="form-signin-heading text-muted">
                                <img className="px-3" height="64px" src={process.env.PUBLIC_URL + '/pareceres.png'} alt="Parecers" />
                                Parecer
                            </h1>
                            {this.state.message && <div className="alert alert-danger" role="alert">{this.state.message}</div>}
                            <Formik
                                initialValues={{ codigoParecer, processo, autor, descricao }}
                                onSubmit={this.onSubmit}
                                validateOnChange={false}
                                validateOnBlur={true}
                                validate={this.validate}
                                enableReinitialize={true}
                            >
                                {(
                                    <Form>
                                        <fieldset className="form-group">
                                            <label>Código:</label>
                                            <Field className="form-control" type="text" name="codigoParecer" disabled />
                                        </fieldset>
                                        <fieldset className="form-group">
                                            <label>Processo:</label>
                                            <Field className="form-control" type="text" name="processo" disabled />
                                        </fieldset>
                                        <fieldset className="form-group">
                                            <label>Autor:</label>
                                            <Field className="form-control" type="text" name="autor" disabled />
                                        </fieldset>
                                        <ErrorMessage name="descricao" component="div" className="alert alert-danger" />
                                        <fieldset className="form-group">
                                            <label>Descrição:</label>
                                            <Field className="form-control" type="text" name="descricao" />
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

export default withRouter(FormParecerComponent);
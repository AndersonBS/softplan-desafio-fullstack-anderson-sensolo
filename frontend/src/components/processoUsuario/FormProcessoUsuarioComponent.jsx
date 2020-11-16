import React, { Component } from "react";
import { withRouter } from "react-router-dom";
import { Formik, Form, Field, ErrorMessage } from "formik";
import FooterComponent from "../FooterComponent";
import HeaderComponent from "../HeaderComponent";
import ProcessoService from "../../services/processoService";
import AuthenticationService from "../../services/authenticationService";

class FormProcessoUsuarioComponent extends Component {
    constructor(props) {
        super(props);

        const user = AuthenticationService.getLoggedInUser();
        if (user.permissao !== 'Administrador' && user.permissao !== 'Triador') {
            this.props.history.push('/proibido');
        }

        this.state = {
            finalizadores: [],
            codigoProceso: this.props.match.params.codigo,
            processo: '',
            usuario: '',    
            message: ''
        };

        this.onSubmit = this.onSubmit.bind(this);
        this.validate = this.validate.bind(this);
        this.goBackClicked = this.goBackClicked.bind(this);
    }

    componentDidMount() {
        ProcessoService.getProcesso(this.state.codigoProceso)
            .then(response => {
                this.setState({ processo: response.data.codigo + ' - ' + response.data.nome })
            })

        ProcessoService.getFinalizadores(this.state.codigoProceso)
            .then(response => {
                if (response.data.length === 0) {
                    this.setState({ message: 'Não existem usuários aptos para esta atribuição!' });
                } else {
                    this.setState({ 
                        finalizadores: response.data,
                        usuario: response.data[0].codigoNomeUsuario
                    });
                }
            });
    }

    onSubmit(values) {
        ProcessoService.addProcessoUsuario(this.state.codigoProceso, values.usuario.charAt(0))
            .then(() => this.props.history.push(`/processo/${this.state.codigoProceso}/atribuicoes`))
            .catch(() => this.setState({ message: 'Ocorreu um erro ao adicionar o usuário ao processo!' }));
    }

    validate(values) {
        let errors = {}
        if (!values.usuario) {
            errors.usuario = 'Selecione o usuário!';
        }
        return errors;
    }

    goBackClicked() {
        this.props.history.push(`/processo/${this.state.codigoProceso}/atribuicoes`);
    }

    render() {
        let { processo, usuario } = this.state;
        return (
            <div>
                <HeaderComponent />
                <div className="container">
                    <div className="row justify-content-md-center">
                        <div className="col col-lg-6">
                            <h1 className="form-signin-heading text-muted">
                                <img className="px-3" height="64px" src={process.env.PUBLIC_URL + '/usuarios.png'} alt="Usuários" />
                                Atribuição
                                <img className="px-3" height="64px" src={process.env.PUBLIC_URL + '/processos.png'} alt="Processos" />
                            </h1>
                            {this.state.message && <div className="alert alert-danger" role="alert">{this.state.message}</div>}
                            <Formik
                                initialValues={{ processo, usuario }}
                                onSubmit={this.onSubmit}
                                validateOnChange={false}
                                validateOnBlur={true}
                                validate={this.validate}
                                enableReinitialize={true}
                            >
                                {(
                                    <Form>
                                        <fieldset className="form-group">
                                            <label>Processo:</label>
                                            <Field className="form-control" type="text" name="processo" disabled />
                                        </fieldset>
                                        <ErrorMessage name="usuario" component="div" className="alert alert-danger" />
                                        <fieldset className="form-group">
                                            <label>Usuário:</label>
                                            <Field className="form-control" name="usuario" as="select">
                                                {
                                                    this.state.finalizadores.map(
                                                        finalizador =>
                                                            <option key={finalizador.codigoUsuario} value={finalizador.codigoNomeUsuario}>{finalizador.codigoNomeUsuario}</option>
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

export default withRouter(FormProcessoUsuarioComponent);
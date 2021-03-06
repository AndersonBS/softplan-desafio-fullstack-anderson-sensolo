import axios from 'axios';
import jwtHeader from './jwtService';

const SERVICE_URL = `http://localhost:8080/api/usuario`;

class UsuarioService {
    getUsuarios(selectedPage, pageSize) {
        return axios.get(`${SERVICE_URL}/get?selectedPage=${selectedPage}&pageSize=${pageSize}`, { headers: jwtHeader() });
    }

    getUsuario(codigoUsuario) {
        return axios.get(`${SERVICE_URL}/get/${codigoUsuario}`, { headers: jwtHeader() });
    }

    deleteUsuario(codigoUsuario) {
        return axios.delete(`${SERVICE_URL}/delete/${codigoUsuario}`, { headers: jwtHeader() });
    } 

    createUsuario(usuario) {
        return axios.post(`${SERVICE_URL}/create`, usuario, { headers: jwtHeader() });
    }

    updateUsuario(codigoUsuario, usuario) {
        return axios.put(`${SERVICE_URL}/update/${codigoUsuario}`, usuario, { headers: jwtHeader() });
    }
}

export default new UsuarioService();
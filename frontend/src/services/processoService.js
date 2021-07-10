import axios from "axios";
import jwtHeader from './jwtService';

const SERVICE_URL = `http://localhost:8080/api/processo`;

class ProcessoService {
    getProcessos(selectedPage, pageSize) {
        return axios.get(`${SERVICE_URL}/get?selectedPage=${selectedPage}&pageSize=${pageSize}`, { headers: jwtHeader() });
    }

    getProcesso(codigoProcesso) {
        return axios.get(`${SERVICE_URL}/get/${codigoProcesso}`, { headers: jwtHeader() });
    }

    deleteProcesso(codigoProcesso) {
        return axios.delete(`${SERVICE_URL}/delete/${codigoProcesso}`, { headers: jwtHeader() });
    } 

    createProcesso(processo) {
        return axios.post(`${SERVICE_URL}/create`, processo, { headers: jwtHeader() });
    }

    updateProcesso(codigo, processo) {
        return axios.put(`${SERVICE_URL}/update/${codigo}`, processo, { headers: jwtHeader() });
    }

    addProcessoUsuario(codigoProcesso, codigoUsuario) {
        return axios.put(`${SERVICE_URL}/${codigoProcesso}/add/usuario/${codigoUsuario}`, null, { headers: jwtHeader() });
    }

    removeProcessoUsuario(codigoProcesso, codigoUsuario) {
        return axios.put(`${SERVICE_URL}/${codigoProcesso}/remove/usuario/${codigoUsuario}`, null, { headers: jwtHeader() });
    }

    getProcessoUsuarios(codigoProcesso, selectedPage, pageSize) {
        return axios.get(`${SERVICE_URL}/${codigoProcesso}/get/usuarios?selectedPage=${selectedPage}&pageSize=${pageSize}`, { headers: jwtHeader() });
    }

    getResponsaveis() {
        return axios.get(`${SERVICE_URL}/get/responsaveis`, { headers: jwtHeader() });
    }

    getFinalizadores(codigoProcesso) {
        return axios.get(`${SERVICE_URL}/${codigoProcesso}/get/finalizadores`, { headers: jwtHeader() });
    }
}

export default new ProcessoService();
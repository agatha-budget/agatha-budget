import type { AxiosInstance, InternalAxiosRequestConfig } from "axios";
import axios from "axios";
import KeyCloakService from "../security/KeycloakService";

const HttpMethods = {
  GET: "GET",
  POST: "POST",
  DELETE: "DELETE",
};

const _axios = axios.create();

const cb = (config: InternalAxiosRequestConfig) => {
  config.headers.Authorization = `Bearer ${KeyCloakService.GetAccesToken()}`;
  return config;
};

const configureAxiosKeycloak = (): void => {
  _axios.interceptors.request.use(
    (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
      let _config = config
      if (KeyCloakService.IsLoggedIn()) {
        KeyCloakService.UpdateToken(undefined);
        _config = cb(config);
      }
      return _config;
    }
  );
};

const getAxiosClient = (): AxiosInstance => _axios;

const HttpService = {
  HttpMethods,
  configureAxiosKeycloak,
  getAxiosClient,
};

export const globalAxios = HttpService.getAxiosClient()
export default HttpService;

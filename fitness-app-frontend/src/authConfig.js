//import { AuthContext, AuthProvider, TAuthConfig, TRefreshTokenExpiredEvent } from "react-oauth2-code-pkce"

export const authConfig = {
    clientId: 'oauth2-pkce-client',
    authorizationEndpoint: 'http://localhost:8180/realms/fitness-oauth2/protocol/openid-connect/auth',
    tokenEndpoint: 'http://localhost:8180/realms/fitness-oauth2/protocol/openid-connect/token',
    redirectUri: 'http://localhost:5173',
    scope: 'openid email offline_access profile',
    onRefreshTokenExpire: (event) => event.logIn(),
  }
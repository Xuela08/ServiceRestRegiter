package es.tfg.controlador.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RestAuthenticationFilter implements javax.servlet.Filter {
    
    public static final String TOKEN_HEADER = "Token";
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filter) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String authToken = httpServletRequest
                    .getHeader(TOKEN_HEADER);
            String peticion = httpServletRequest.getMethod();
            String url = httpServletRequest.getPathInfo();
            
            
            AuthenticationService authenticationService = new AuthenticationService();

            //Si usuToken es null, el token no es correcto a expirado
            String usuToken = authenticationService.authenticate(authToken);
            
            boolean authoritationStatus = false;
            if(authToken != null){
                authoritationStatus = authenticationService
                    .authoritate(usuToken, peticion, url);
            }else{
                //en caso de registro debemos permitir acceder sin token
                if(peticion.equals("POST")&&url.equals("/personas")){
                    filter.doFilter(request, response);
                }
            }
            
            if (authoritationStatus) {
                filter.doFilter(request, response);
            } else {
                if (response instanceof HttpServletResponse) {
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    httpServletResponse
                            .setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}
/*
@Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filter) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String authCredentials = httpServletRequest
                    .getHeader(AUTHENTICATION_HEADER);
            String peticion = httpServletRequest.getMethod();
            String url = httpServletRequest.getPathInfo();
            AuthenticationService authenticationService = new AuthenticationService();

            boolean authenticationStatus = authenticationService
                    .authenticate(authCredentials);
            
            boolean authoritationStatus = false;
            if(authenticationStatus){
                authoritationStatus = authenticationService
                    .authoritate(authCredentials, peticion, url);
            }
            
            if (authenticationStatus && authoritationStatus) {
                filter.doFilter(request, response);
            } else {
                if (response instanceof HttpServletResponse) {
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    httpServletResponse
                            .setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
    }*/
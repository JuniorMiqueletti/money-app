package com.juniormiqueletti.moneyapp.token;

import com.juniormiqueletti.moneyapp.config.property.MoneyApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {

    @Autowired
    private MoneyApiProperty property;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.getMethod().getName().equals("postAccessToken");
    }

    @Override
    public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken oAuth2AccessToken, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        HttpServletRequest servletRequest = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();
        String refreshToken = oAuth2AccessToken.getRefreshToken().getValue();

        DefaultOAuth2AccessToken defaultRefreshToken = (DefaultOAuth2AccessToken) oAuth2AccessToken;

        addRefreshTokenOnCookie(servletRequest, servletResponse, refreshToken);
        removeRefreshTokenFromBody(defaultRefreshToken);

        return defaultRefreshToken;
    }

    private void removeRefreshTokenFromBody(DefaultOAuth2AccessToken token) {
        token.setRefreshToken(null);
    }

    private void addRefreshTokenOnCookie(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String refreshToken) {
        Cookie refreshTokenCoockie = new Cookie("refreshToken", refreshToken);
        refreshTokenCoockie.setHttpOnly(true);
        refreshTokenCoockie.setSecure(property.getSeguranca().isEnableHttps());
        refreshTokenCoockie.setPath(servletRequest.getContextPath() + "/oauth/token");
        refreshTokenCoockie.setMaxAge(2592000);
        servletResponse.addCookie(refreshTokenCoockie);
    }
}

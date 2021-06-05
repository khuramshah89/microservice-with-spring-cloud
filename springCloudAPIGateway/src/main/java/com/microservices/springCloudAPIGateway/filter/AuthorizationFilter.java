package com.microservices.springCloudAPIGateway.filter;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.config> {


    @Autowired
    Environment env;

    public AuthorizationFilter() {
        super(config.class);
    }

    public static class config {

    }

    @Override
    public GatewayFilter apply(config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No Authorization code ");
            }
            String authorization = Objects.requireNonNull(request.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
            String jwt = authorization.replace("Bearer ", "");
            String subject = null;

            try {
                subject = Jwts.parser()
                        .setSigningKey(env.getProperty("jwt.token"))
                        .parseClaimsJws(jwt)
                        .getBody()
                        .getSubject();
            } catch (Exception e) {
                System.out.println( e.getMessage());
            }
            if (subject == null || subject.isEmpty()) {
                return onError(exchange, "JWT token not valid ");
            }
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String msg) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();

    }

}

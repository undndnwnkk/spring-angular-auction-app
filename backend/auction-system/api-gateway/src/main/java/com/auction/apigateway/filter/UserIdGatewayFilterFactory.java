package com.auction.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class UserIdGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    public UserIdGatewayFilterFactory() {
        super(Object.class);
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            return ReactiveSecurityContextHolder.getContext()
                    .mapNotNull(SecurityContext::getAuthentication)
                    .filter(auth -> auth instanceof JwtAuthenticationToken)
                    .map(auth -> (JwtAuthenticationToken) auth)
                    .map(jwtAuth -> jwtAuth.getToken().getSubject())
                    .flatMap(userId -> {

                        var mutatedRequest = exchange.getRequest()
                                .mutate()
                                .header("X-User-Id", userId)
                                .build();

                        var mutatedExchange = exchange.mutate()
                                .request(mutatedRequest)
                                .build();

                        return chain.filter(mutatedExchange);
                    })
                    .switchIfEmpty(chain.filter(exchange));
        };
    }
}

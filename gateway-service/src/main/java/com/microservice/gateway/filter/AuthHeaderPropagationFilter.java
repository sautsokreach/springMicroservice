package com.microservice.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class AuthHeaderPropagationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> ctx.getAuthentication())
            .filter(auth -> auth instanceof JwtAuthenticationToken)
            .cast(JwtAuthenticationToken.class)
            .map(JwtAuthenticationToken::getToken)
            .flatMap(jwt -> {
                String userId = jwt.getSubject();
                String email = jwt.getClaimAsString("email");
                String roles = extractRoles(jwt.getClaim("realm_access"));

                ServerWebExchange mutated = exchange.mutate()
                    .request(r -> r
                        .header("X-Auth-User-Id", userId != null ? userId : "")
                        .header("X-Auth-User-Email", email != null ? email : "")
                        .header("X-Auth-User-Roles", roles)
                    )
                    .build();
                return chain.filter(mutated);
            })
            .switchIfEmpty(chain.filter(exchange));
    }

    @SuppressWarnings("unchecked")
    private String extractRoles(Object realmAccess) {
        if (realmAccess instanceof Map<?, ?> map) {
            Object roles = map.get("roles");
            if (roles instanceof List<?> list) {
                return String.join(",", (List<String>) list);
            }
        }
        return "";
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

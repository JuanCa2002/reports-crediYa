package co.com.pragma.api.config;

import co.com.pragma.api.exception.GlobalExceptionHandler;
import co.com.pragma.api.helper.CustomReactiveAuthenticationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         CustomReactiveAuthenticationManager jwtAuthenticationManager,
                                                         GlobalExceptionHandler globalExceptionHandler) {

        AuthenticationWebFilter jwtFilter =
                new AuthenticationWebFilter(jwtAuthenticationManager);

        jwtFilter.setServerAuthenticationConverter(jwtAuthenticationManager.authenticationConverter());

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/configuration/**").permitAll()
                        .pathMatchers(HttpMethod.GET,"/api/v1/reportes/**").hasAnyAuthority("ADMINISTRADOR")
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(globalExceptionHandler)
                        .accessDeniedHandler(globalExceptionHandler)
                )
                .build();
    }
}

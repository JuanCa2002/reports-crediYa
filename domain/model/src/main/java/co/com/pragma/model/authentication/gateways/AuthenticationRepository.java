package co.com.pragma.model.authentication.gateways;

import co.com.pragma.model.authentication.Authentication;
import reactor.core.publisher.Mono;

public interface AuthenticationRepository {

    Mono<Authentication> validateToken(String token);
}

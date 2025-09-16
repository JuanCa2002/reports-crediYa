package co.com.pragma.api.metric;

import co.com.pragma.api.config.BasePath;
import co.com.pragma.api.metric.config.MetricPath;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class MetricRouterRest {

    private final BasePath basePath;
    private final MetricPath metricPath;
    private final MetricHandler metricHandler;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/reportes",
                    method = {RequestMethod.GET},
                    beanClass = MetricHandler.class,
                    beanMethod = "listenGetReport"
            )
    })
    public RouterFunction<ServerResponse> metricRoutes(MetricHandler metricHandler) {
        return RouterFunctions
                .route()
                .path(basePath.getBasePath(), builder -> builder
                        .GET(metricPath.getReports(), this.metricHandler::listenGetReport)
                )
                .build();
    }
}

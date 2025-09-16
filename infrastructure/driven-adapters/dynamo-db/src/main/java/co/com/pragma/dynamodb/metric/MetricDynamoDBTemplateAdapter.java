package co.com.pragma.dynamodb.metric;

import co.com.pragma.dynamodb.entity.MetricEntity;
import co.com.pragma.dynamodb.helper.TemplateAdapterOperations;
import co.com.pragma.model.metric.Metric;
import co.com.pragma.model.metric.gateways.MetricRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;


@Repository
public class MetricDynamoDBTemplateAdapter extends TemplateAdapterOperations<Metric, String, MetricEntity> implements MetricRepository {

    @Value("${report.id}")
    private String reportId;

    public MetricDynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {
        super(connectionFactory, mapper, d -> mapper.map(d, Metric.class), "metrics");
    }

    public Mono<List<Metric>> getEntityByKey(String id) {
        QueryEnhancedRequest queryExpression = generateQueryExpression(id);
        return query(queryExpression);
    }

    private QueryEnhancedRequest generateQueryExpression(String partitionKey) {
        return QueryEnhancedRequest.builder()
                .queryConditional(
                        QueryConditional.keyEqualTo(
                                Key.builder()
                                        .partitionValue(partitionKey)
                                        .build()
                        )
                )
                .build();
    }

    @Override
    public Mono<Metric> getReport() {
        return getEntityByKey(reportId)
                .flatMapMany(Flux::fromIterable)
                .next();
    }

    @Override
    public Mono<Void> updateMetric(Metric metric) {
        return super.save(metric)
                .then();
    }
}

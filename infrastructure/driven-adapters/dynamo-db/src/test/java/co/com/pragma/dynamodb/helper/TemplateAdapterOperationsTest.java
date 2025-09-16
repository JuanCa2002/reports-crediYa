package co.com.pragma.dynamodb.helper;

import co.com.pragma.dynamodb.metric.MetricDynamoDBTemplateAdapter;
import co.com.pragma.dynamodb.entity.MetricEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class TemplateAdapterOperationsTest {

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private DynamoDbAsyncTable<MetricEntity> customerTable;

    private MetricEntity metricEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(dynamoDbEnhancedAsyncClient.table("table_name", TableSchema.fromBean(MetricEntity.class)))
                .thenReturn(customerTable);

        metricEntity = new MetricEntity();
        metricEntity.setId("id");
        metricEntity.setAtr1("atr1");
    }

    @Test
    void modelEntityPropertiesMustNotBeNull() {
        MetricEntity metricEntityUnderTest = new MetricEntity("id", "atr1");

        assertNotNull(metricEntityUnderTest.getId());
        assertNotNull(metricEntityUnderTest.getAtr1());
    }

    @Test
    void testSave() {
        when(customerTable.putItem(metricEntity)).thenReturn(CompletableFuture.runAsync(()->{}));
        when(mapper.map(metricEntity, MetricEntity.class)).thenReturn(metricEntity);

        MetricDynamoDBTemplateAdapter metricDynamoDBTemplateAdapter =
                new MetricDynamoDBTemplateAdapter(dynamoDbEnhancedAsyncClient, mapper);

        StepVerifier.create(metricDynamoDBTemplateAdapter.save(metricEntity))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testGetById() {
        String id = "id";

        when(customerTable.getItem(
                Key.builder().partitionValue(AttributeValue.builder().s(id).build()).build()))
                .thenReturn(CompletableFuture.completedFuture(metricEntity));
        when(mapper.map(metricEntity, Object.class)).thenReturn("value");

        MetricDynamoDBTemplateAdapter metricDynamoDBTemplateAdapter =
                new MetricDynamoDBTemplateAdapter(dynamoDbEnhancedAsyncClient, mapper);

        StepVerifier.create(metricDynamoDBTemplateAdapter.getById("id"))
                .expectNext("value")
                .verifyComplete();
    }

    @Test
    void testDelete() {
        when(mapper.map(metricEntity, MetricEntity.class)).thenReturn(metricEntity);
        when(mapper.map(metricEntity, Object.class)).thenReturn("value");

        when(customerTable.deleteItem(metricEntity))
                .thenReturn(CompletableFuture.completedFuture(metricEntity));

        MetricDynamoDBTemplateAdapter metricDynamoDBTemplateAdapter =
                new MetricDynamoDBTemplateAdapter(dynamoDbEnhancedAsyncClient, mapper);

        StepVerifier.create(metricDynamoDBTemplateAdapter.delete(metricEntity))
                .expectNext("value")
                .verifyComplete();
    }
}
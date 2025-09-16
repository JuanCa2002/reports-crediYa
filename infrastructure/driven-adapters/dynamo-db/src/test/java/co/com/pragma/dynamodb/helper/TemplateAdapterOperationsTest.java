package co.com.pragma.dynamodb.helper;

import co.com.pragma.dynamodb.entity.MetricEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

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

        when(dynamoDbEnhancedAsyncClient.table("metrics", TableSchema.fromBean(MetricEntity.class)))
                .thenReturn(customerTable);

        metricEntity = new MetricEntity();
        metricEntity.setId("id");
        metricEntity.setProposalApprovedQuantity(0L);
        metricEntity.setTotalApprovedAmount(100.0);
    }

    @Test
    void modelEntityPropertiesMustNotBeNull() {
        MetricEntity metricEntityUnderTest = new MetricEntity(1L, 1000.0);

        assertNotNull(metricEntityUnderTest.getProposalApprovedQuantity());
        assertNotNull(metricEntityUnderTest.getTotalApprovedAmount());
    }
}
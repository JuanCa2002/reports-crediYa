package co.com.pragma.consumer.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.dynamodb2.DynamoDBLockProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.metrics.MetricPublisher;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class ShedLockConfig {

    @Value("${shedlock.table}")
    private String shedLockTableName;

    @Bean
    @Profile({"dev"})
    public DynamoDbClient dynamoDbClientDev(MetricPublisher publisher,
                                         @Value("${aws.region}") String region) {
        return DynamoDbClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create())
                .region(Region.of(region))
                .overrideConfiguration(o -> o.addMetricPublisher(publisher))
                .build();
    }

    @Bean
    @Profile({"prod", "cer", "pdn"})
    public DynamoDbClient dynamoDbClientProd(MetricPublisher publisher,
                                         @Value("${aws.region}") String region) {
        return DynamoDbClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.builder().build())
                .region(Region.of(region))
                .overrideConfiguration(o -> o.addMetricPublisher(publisher))
                .build();
    }

    @Bean
    public LockProvider lockProvider(DynamoDbClient dynamoDbClient) {
        return new DynamoDBLockProvider(dynamoDbClient, shedLockTableName);
    }
}

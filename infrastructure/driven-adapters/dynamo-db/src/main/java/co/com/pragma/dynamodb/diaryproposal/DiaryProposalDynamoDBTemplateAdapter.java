package co.com.pragma.dynamodb.diaryproposal;

import co.com.pragma.dynamodb.entity.DiaryProposalEntity;
import co.com.pragma.dynamodb.helper.TemplateAdapterOperations;
import co.com.pragma.model.diaryproposal.DiaryProposal;
import co.com.pragma.model.diaryproposal.gateways.DiaryProposalRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import java.math.BigInteger;
import java.util.List;

@Repository
public class DiaryProposalDynamoDBTemplateAdapter extends TemplateAdapterOperations<DiaryProposal, BigInteger, DiaryProposalEntity> implements DiaryProposalRepository {

    public DiaryProposalDynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {
        super(connectionFactory, mapper, d -> mapper.map(d, DiaryProposal.class), "diary_proposals");
    }

    @Override
    public Mono<List<DiaryProposal>> getAll() {
        return scan();
    }
}

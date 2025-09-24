package co.com.pragma.model.diaryproposal.gateways;

import co.com.pragma.model.diaryproposal.DiaryProposal;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DiaryProposalRepository {

    Mono<List<DiaryProposal>> getAll();
}

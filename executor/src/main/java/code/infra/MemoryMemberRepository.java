package code.infra;

import org.springframework.stereotype.Repository;

import code.domain.Member;
import code.domain.MemberRepository;
import reactor.core.publisher.Mono;

// TODO
@Repository
public class MemoryMemberRepository implements MemberRepository {
    @Override
    public Mono<Member> findById(String memberId) {
        return null;
    }
}

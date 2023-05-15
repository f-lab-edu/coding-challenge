package code.domain;

import reactor.core.publisher.Mono;

public interface MemberRepository {
    Mono<Member> findById(String memberId);

    Mono<Member> save(Member member);
}

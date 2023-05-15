package code.infra;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import code.domain.Member;
import code.domain.MemberRepository;
import reactor.core.publisher.Mono;

@Repository
public class MemoryMemberRepository implements MemberRepository {
    private final Map<String, Member> map = new HashMap<>();

    @Override
    public Mono<Member> findById(String memberId) {
        return Mono.just(map.get(memberId));
    }

    @Override
    public Mono<Member> save(Member member) {
        if (member.getMemberId() != null) {
            map.put(member.getMemberId(), member);
            return Mono.just(member);
        }
        var newMember = new Member(UUID.randomUUID().toString());
        map.put(newMember.getMemberId(), newMember);
        return Mono.just(member);
    }
}

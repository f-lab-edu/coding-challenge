package code.infra;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import code.domain.Member;
import code.domain.MemberRepository;
import reactor.core.publisher.Mono;

@Repository
public class MemoryMemberRepository implements MemberRepository {
    private final LinkedHashMap<String, Member> map = new LinkedHashMap<>();

    @Override
    public Mono<Member> findById(String memberId) {
        return Mono.just(map.get(memberId));
    }

    @Override
    public Mono<Member> save(Member member) {
        adjustMaximumCapacity();
        if (member.getMemberId() != null) {
            map.put(member.getMemberId(), member);
            return Mono.just(member);
        }
        var newMember = new Member(UUID.randomUUID().toString());
        map.put(newMember.getMemberId(), newMember);
        return Mono.just(member);
    }

    public Mono<Void> deleteAll() {
        return Mono.fromRunnable(map::clear);
    }

    private void adjustMaximumCapacity() {
        if (map.size() > 200) {
            String id = map.values().stream()
                           .findFirst()
                           .get().getMemberId();
            map.remove(id);
        }
    }
}

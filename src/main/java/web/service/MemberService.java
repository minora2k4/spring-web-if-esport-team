package web.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dto.request.MemberRequest;
import web.dto.response.MemberResponse;
import web.model.Game;
import web.model.Member;
import web.repository.GameRepository;
import web.repository.MemberRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService extends BaseService<Member, Long> {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;

    @Override
    protected JpaRepository<Member, Long> getRepository() { return memberRepository; }

    @Override
    protected String getEntityName() { return "Member"; }

    public List<MemberResponse> getAll() {
        return memberRepository.findAllWithGames().stream().map(MemberResponse::from).toList();
    }

    public List<MemberResponse> getActive() {
        return memberRepository.findByIsActiveTrue().stream().map(MemberResponse::from).toList();
    }

    public MemberResponse getById(Long id) {
        return MemberResponse.from(findOrThrow(id));
    }

    public MemberResponse create(MemberRequest request) {
        Member member = buildMember(new Member(), request);
        Member saved = memberRepository.save(member);
        log.info("[MEMBER] Created: {}", saved.getInGameName());
        return MemberResponse.from(saved);
    }

    public MemberResponse update(Long id, MemberRequest request) {
        Member member = findOrThrow(id);
        buildMember(member, request);
        log.info("[MEMBER] Updated: {}", member.getInGameName());
        return MemberResponse.from(memberRepository.save(member));
    }

    public void delete(Long id) {
        findOrThrow(id);
        memberRepository.deleteById(id);
        log.info("[MEMBER] Deleted id: {}", id);
    }

    private Member buildMember(Member member, MemberRequest request) {
        member.setInGameName(request.getInGameName());
        member.setRealName(request.getRealName());
        member.setRole(request.getRole());
        member.setAchievement(request.getAchievement());
        member.setAvatarUrl(request.getAvatarUrl());
        member.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        if (request.getGameIds() != null && !request.getGameIds().isEmpty()) {
            List<Game> games = gameRepository.findAllById(request.getGameIds());
            member.setGames(games);
        }
        return member;
    }
}
package web.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dto.request.TeamLogRequest;
import web.dto.response.TeamLogResponse;
import web.model.Member;
import web.model.TeamLog;
import web.repository.MemberRepository;
import web.repository.TeamLogRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamLogService extends BaseService<TeamLog, Long> {

    private static final Logger log = LoggerFactory.getLogger(TeamLogService.class);
    private final TeamLogRepository teamLogRepository;
    private final MemberRepository memberRepository;

    @Override
    protected JpaRepository<TeamLog, Long> getRepository() { return teamLogRepository; }

    @Override
    protected String getEntityName() { return "TeamLog"; }

    public List<TeamLogResponse> getAll() {
        return teamLogRepository.findAllWithMember().stream()
                .map(TeamLogResponse::from).toList();
    }

    public TeamLogResponse create(TeamLogRequest request) {
        TeamLog teamLog = buildLog(new TeamLog(), request);
        TeamLog saved = teamLogRepository.save(teamLog);
        log.info("[TEAMLOG] Created: {}", saved.getDescription());
        return TeamLogResponse.from(saved);
    }

    public TeamLogResponse update(Long id, TeamLogRequest request) {
        TeamLog teamLog = findOrThrow(id);
        buildLog(teamLog, request);
        log.info("[TEAMLOG] Updated id: {}", id);
        return TeamLogResponse.from(teamLogRepository.save(teamLog));
    }

    public void delete(Long id) {
        findOrThrow(id);
        teamLogRepository.deleteById(id);
        log.info("[TEAMLOG] Deleted id: {}", id);
    }

    private TeamLog buildLog(TeamLog teamLog, TeamLogRequest request) {
        teamLog.setDescription(request.getDescription());
        teamLog.setType(request.getType());
        teamLog.setEventDate(request.getEventDate());
        if (request.getMemberId() != null) {
            Member member = memberRepository.findById(request.getMemberId())
                    .orElseThrow(() -> new RuntimeException("Member not found: " + request.getMemberId()));
            teamLog.setMember(member);
        }
        return teamLog;
    }
}
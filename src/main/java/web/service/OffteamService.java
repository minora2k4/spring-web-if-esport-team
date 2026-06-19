package web.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dto.request.OffteamEventRequest;
import web.dto.response.OffteamEventResponse;
import web.model.OffteamEvent;
import web.model.OffteamPhoto;
import web.repository.OffteamEventRepository;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OffteamService extends BaseService<OffteamEvent, Long> {

    private static final Logger log = LoggerFactory.getLogger(OffteamService.class);
    private final OffteamEventRepository offteamEventRepository;

    @Override
    protected JpaRepository<OffteamEvent, Long> getRepository() { return offteamEventRepository; }

    @Override
    protected String getEntityName() { return "Offteam"; }

    public List<OffteamEventResponse> getAll() {
        return offteamEventRepository.findAllOrderByDate().stream()
                .map(OffteamEventResponse::from).toList();
    }

    public OffteamEventResponse getById(Long id) {
        OffteamEvent event = offteamEventRepository.findByIdWithPhotos(id)
                .orElseThrow(() -> new RuntimeException("Offteam not found: " + id));
        return OffteamEventResponse.from(event);
    }

    public OffteamEventResponse create(OffteamEventRequest request) {
        OffteamEvent event = buildEvent(new OffteamEvent(), request);
        OffteamEvent saved = offteamEventRepository.save(event);
        log.info("[OFFTEAM] Created: {}", saved.getTitle());
        return OffteamEventResponse.from(saved);
    }

    public OffteamEventResponse update(Long id, OffteamEventRequest request) {
        OffteamEvent event = findOrThrow(id);
        buildEvent(event, request);
        log.info("[OFFTEAM] Updated: {}", event.getTitle());
        return OffteamEventResponse.from(offteamEventRepository.save(event));
    }

    public void delete(Long id) {
        findOrThrow(id);
        offteamEventRepository.deleteById(id);
        log.info("[OFFTEAM] Deleted id: {}", id);
    }

    private OffteamEvent buildEvent(OffteamEvent event, OffteamEventRequest request) {
        event.setTitle(request.getTitle());
        event.setEventDate(request.getEventDate());
        event.setCoverPhotoUrl(request.getCoverPhotoUrl());
        if (request.getPhotoUrls() != null) {
            List<OffteamPhoto> photos = new ArrayList<>(request.getPhotoUrls().stream()
                    .map(url -> {
                        OffteamPhoto photo = new OffteamPhoto();
                        photo.setPhotoUrl(url);
                        photo.setEvent(event);
                        return photo;
                    }).toList());
            if (event.getPhotos() != null) {
                event.getPhotos().clear();
                event.getPhotos().addAll(photos);
            } else {
                event.setPhotos(photos);
            }
        }
        return event;
    }
}
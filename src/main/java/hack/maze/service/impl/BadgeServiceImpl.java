package hack.maze.service.impl;

import hack.maze.dto.BadgeDTO;
import hack.maze.entity.Badge;
import hack.maze.repository.BadgeRepo;
import hack.maze.service.AzureService;
import hack.maze.service.BadgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static hack.maze.constant.AzureConstant.IMAGES_BLOB_CONTAINER_BADGES;
import static hack.maze.utils.GlobalMethods.nullMsg;

@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeServiceImpl implements BadgeService {

    private final BadgeRepo badgeRepo;
    private final AzureService azureService;

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createBadge(BadgeDTO badgeDTO) throws IOException {
        Badge badge = new Badge();
        validateBadgeDTOInfo(badgeDTO);
        badge.setTitle(badgeDTO.title());
        Badge savedBadge = badgeRepo.save(badge);
        savedBadge.setImage(azureService.sendImageToAzure(badgeDTO.image(), IMAGES_BLOB_CONTAINER_BADGES, savedBadge.getId()));
        return "new badge with title = [" + badge.getTitle() + "] created successfully";
    }

    private void validateBadgeDTOInfo(BadgeDTO badgeDTO) {
        Objects.requireNonNull(badgeDTO.title(), nullMsg("title"));
//        Objects.requireNonNull(badgeDTO.image(), nullMsg("image"));
    }

    @Override
    public Badge getSingleBadge(long badgeId) {
        return badgeRepo.findById(badgeId).orElseThrow(() -> new RuntimeException("badge with id = [" + badgeId + "] not exist"));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String updateBadge(long badgeId, BadgeDTO badgeDTO) throws IOException {
        Badge targetBadge = getSingleBadge(badgeId);
        if (badgeDTO.title() != null) {
            targetBadge.setTitle(badgeDTO.title());
        }
        if (badgeDTO.image() != null) {
            targetBadge.setImage(azureService.sendImageToAzure(badgeDTO.image(), IMAGES_BLOB_CONTAINER_BADGES, targetBadge.getId()));
        }
        return "Badge updated successfully";
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String deleteBadge(long badgeId) {
        Badge targetBadge = getSingleBadge(badgeId);
        azureService.removeImageFromAzure(IMAGES_BLOB_CONTAINER_BADGES, targetBadge.getId().toString() + "/");
        badgeRepo.delete(targetBadge);
        return "Badge with title = [" + targetBadge.getTitle() + "] deleted successfully";
    }

    @Override
    public List<Badge> getAllBadges() {
        return badgeRepo.findAll();
    }
}

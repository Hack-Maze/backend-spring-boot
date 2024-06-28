package hack.maze.service.impl;

import hack.maze.dto.PageRequestDTO;
import hack.maze.dto.PageResponseDTO;
import hack.maze.entity.Maze;
import hack.maze.entity.Page;
import hack.maze.entity.ProfileMazeProgress;
import hack.maze.repository.PageRepo;
import hack.maze.repository.ProfileMazeProgressRepo;
import hack.maze.service.MazeService;
import hack.maze.service.PageService;
import hack.maze.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

import static hack.maze.config.UserContext.getCurrentUser;
import static hack.maze.mapper.PageMapper.fromPageToPageResponseDTO;
import static hack.maze.utils.GlobalMethods.checkUserAuthority;
import static hack.maze.utils.GlobalMethods.nullMsg;

@Service
@Slf4j
@RequiredArgsConstructor
public class PageServiceImpl implements PageService {

    private final PageRepo pageRepo;
    private final MazeService mazeService;
    private final UserService userService;
    private final ProfileMazeProgressRepo profileMazeProgressRepo;

    @Override
    @Transactional
    public Long createPage(long mazeId, PageRequestDTO pageRequestDTO) {
        validatePageInfo(pageRequestDTO);
        Maze maze = mazeService._getSingleMaze(mazeId);
        Page savedPage = pageRepo.save(fillPageInfo(pageRequestDTO, maze));
        updateProfileMazeProgressAsNotCompleted(savedPage.getMaze().getId());
        return savedPage.getId();
    }

    @Transactional
    protected void updateProfileMazeProgressAsNotCompleted(Long mazeId) {
        List<ProfileMazeProgress> pmps = profileMazeProgressRepo.findAllByMazeId(mazeId);
        for (ProfileMazeProgress pmp : pmps) {
            if (pmp.isCompleted()) {
                pmp.setCompleted(false);
            }
        }
    }

    private Page fillPageInfo(PageRequestDTO pageRequestDTO, Maze maze) {
        return Page
                .builder()
                .title(pageRequestDTO.title())
                .description(pageRequestDTO.description())
                .content(pageRequestDTO.content())
                .maze(maze)
                .build();
    }

    private void validatePageInfo(PageRequestDTO page) {
        Objects.requireNonNull(page.title(), nullMsg("title"));
        Objects.requireNonNull(page.description(), nullMsg("description"));
        Objects.requireNonNull(page.content(), nullMsg("content"));
    }

    @Override
    public List<PageResponseDTO> getAllPagesInSpecificMaze(long mazeId) {
        return fromPageToPageResponseDTO(pageRepo.getAllPagesInSpecificMaze(mazeId));
    }

    @Override
    public PageResponseDTO getSinglePage(long pageId) {
        return fromPageToPageResponseDTO(_getSinglePage(pageId));
    }

    @Override
    public Page _getSinglePage(long pageId) {
        return pageRepo.findById(pageId).orElseThrow(() -> new RuntimeException("page with id = [" + pageId + "] not exist"));
    }

    @Override
    @Transactional
    public String updatePage(long pageId, PageRequestDTO pageRequestDTO) throws AccessDeniedException {
        Page taragetPage = _getSinglePage(pageId);
        checkUserAuthority(userService.getSingleUser(getCurrentUser()), taragetPage);
        if (pageRequestDTO.title() != null) {
            taragetPage.setTitle(pageRequestDTO.title());
        }
        if (pageRequestDTO.content() != null) {
            taragetPage.setContent(pageRequestDTO.content());
        }
        if (pageRequestDTO.description() != null) {
            taragetPage.setDescription(pageRequestDTO.description());
        }

        return "Page with id = [" + pageId + "] updated successfully";
    }

    @Override
    public String deletePage(long pageId) throws AccessDeniedException {
        Page taragetPage = _getSinglePage(pageId);
        checkUserAuthority(userService.getSingleUser(getCurrentUser()), taragetPage);
        pageRepo.delete(taragetPage);
        return "Page with title = [" + taragetPage.getTitle() + "] deleted successfully";
    }

}

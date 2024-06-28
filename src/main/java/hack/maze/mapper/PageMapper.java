package hack.maze.mapper;

import hack.maze.dto.MazePageDTO;
import hack.maze.dto.PageProgressDTO;
import hack.maze.dto.PageResponseDTO;
import hack.maze.entity.Page;
import java.util.List;
import java.util.stream.Collectors;

public class PageMapper {

    public static List<MazePageDTO> fromPageToMazePageDTO(List<Page> pages) {
        return pages.stream().map(PageMapper::fromPageToMazePageDTO).collect(Collectors.toList());
    }

    public static MazePageDTO fromPageToMazePageDTO(Page page) {
        return MazePageDTO
                .builder()
                .id(page.getId())
                .title(page.getTitle())
                .description(page.getDescription())
                .build();
    }

    public static List<PageProgressDTO> fromPageToPageProgressDTO(List<Page> pages) {
        return pages.stream().map(PageMapper::fromPageToPageProgressDTO).collect(Collectors.toList());
    }

    public static PageProgressDTO fromPageToPageProgressDTO(Page page) {
        return PageProgressDTO
                .builder()
                .id(page.getId())
                .title(page.getTitle())
                .build();
    }

    public static List<PageResponseDTO> fromPageToPageResponseDTO(List<Page> pages) {
        return pages.stream().map(PageMapper::fromPageToPageResponseDTO).collect(Collectors.toList());
    }

    public static PageResponseDTO fromPageToPageResponseDTO(Page page) {
        return PageResponseDTO
                .builder()
                .id(page.getId())
                .title(page.getTitle())
                .description(page.getDescription())
                .content(page.getContent())
                .questions(QuestionMapper.fromQuestionToQuestionResponseDTO(page.getQuestions()))
                .build();
    }

}

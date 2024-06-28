package hack.maze.repository;

import hack.maze.dto.LeaderboardMazeDTO;
import hack.maze.entity.Maze;
import hack.maze.entity.ProfileMazeProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfileMazeProgressRepo extends JpaRepository<ProfileMazeProgress, Long> {

    @Query("SELECT pmp FROM ProfileMazeProgress pmp WHERE pmp.profile.id = ?1 AND pmp.maze.id = ?2")
    Optional<ProfileMazeProgress> findByProfileIdAndMazeId(Long profileId, Long mazeId);

    @Query("SELECT pmp FROM ProfileMazeProgress pmp WHERE pmp.maze.id = ?1")
    List<ProfileMazeProgress> findAllByMazeId(Long mazeId);

    @Query("SELECT pmp.maze FROM ProfileMazeProgress pmp WHERE pmp.profile.id = ?1 AND pmp.isCompleted = false")
    List<Maze> notCompletedMazes(Long profileId);
}

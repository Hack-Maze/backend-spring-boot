package hack.maze.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Maze {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(columnDefinition = "text")
    @Basic(fetch = FetchType.LAZY)
    private String description;

    @Column(columnDefinition = "text")
    @Basic(fetch = FetchType.LAZY)
    private String summary;
    private LocalDateTime createdAt;
    private boolean visibility;
    private String image;
    private String file;
    private String dockerImageName;

    @ElementCollection(targetClass = String.class)
    private List<String> ports;

    @ElementCollection
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    @CollectionTable(name = "maze_env_template", joinColumns = @JoinColumn(name = "maze_id"))
    private Map<String, String> envTemplate;

    @OneToMany(mappedBy = "maze")
    private List<AzureContainer> azureContainers;

    @Enumerated(EnumType.STRING)
    private Type type;
    private int totalPoints;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @ManyToMany
    @JoinTable(
            name = "maze_profile_enrollment",
            joinColumns = @JoinColumn(name = "maze_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id"))
    @JsonIgnore
    private List<Profile> enrolledUsers;

    @ManyToMany
    @JoinTable(
            name = "maze_profile_solvers",
            joinColumns = @JoinColumn(name = "maze_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id"))
    @JsonIgnore
    private List<Profile> solvers;

    @ManyToMany
    @JoinTable(
            name = "maze_tag",
            joinColumns = @JoinColumn(name = "maze_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonManagedReference
    private List<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @JsonManagedReference
    private Profile author;

    @OneToMany(mappedBy = "maze", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Page> pages;

    @OneToMany(mappedBy = "maze", cascade = CascadeType.REMOVE)
    private List<ProfileMazeProgress> profileMazeProgresses;

}

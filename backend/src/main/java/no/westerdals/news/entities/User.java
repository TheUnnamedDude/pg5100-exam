package no.westerdals.news.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@NamedQueries(
        @NamedQuery(
                name = User.GET_BY_USERNAME,
                query = "select u from User as u where u.userId = :userId"
        )
)
@Entity
@Getter
@Setter
public class User {
    public static final String GET_BY_USERNAME = "User#getByUserName";

    @Id
    @Pattern(regexp = "[a-zA-Z0-9]{3,25}") // 3 to 25 letters long
    private String userId;
    @Size(min = 1, max = 20)
    private String firstName;
    @Size(max = 20)
    private String middleName;
    @Size(min = 1, max = 20)
    private String surname;
    @NotNull
    private String salt;
    @NotNull
    private String hashedPassword;
    @NotNull
    private Date registrationDate;
    @OneToMany
    private List<Post> posts;
    private Long karmaPoints;
}

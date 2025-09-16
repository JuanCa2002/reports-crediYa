package co.com.pragma.model.authentication;
import lombok.*;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Authentication {

    private String userName;
    private String role;
    private String expirationDate;
}

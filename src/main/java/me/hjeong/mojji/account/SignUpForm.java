package me.hjeong.mojji.account;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data // Getter, Setter, NoArgsConstructor, EqualsAndHashCode
public class SignUpForm {

    @NotBlank // 비어 있으면 안된다
    @Length(min = 3, max = 9) // Ctrl+P 들어가는 아규먼트 값 확인
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$", message = "공백은 사용할 수 없습니다.") // 정규표현식. ^: start, $: end, 3자 이상 20자 이하
    private String nickname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password_again;

}
package app.pi_fisio.dto;

import java.util.List;
@Deprecated
public record UserPageDTO(List<UserDTO> users, Long totalElements, int totalPages) {
}

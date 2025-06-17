package app.pi_fisio.dto;

import java.util.List;

public record UserPageDTO(List<UserDTO> users, Long totalElements, int totalPages) {
}

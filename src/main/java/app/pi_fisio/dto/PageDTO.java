package app.pi_fisio.dto;

import java.util.List;

public record PageDTO<T>(List<T> content, Long totalElements, int totalPages) {}

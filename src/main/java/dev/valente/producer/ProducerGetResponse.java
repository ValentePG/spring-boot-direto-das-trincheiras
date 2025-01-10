package dev.valente.producer;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProducerGetResponse {

    private Long id;
    private String name;
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "ProducerGetResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

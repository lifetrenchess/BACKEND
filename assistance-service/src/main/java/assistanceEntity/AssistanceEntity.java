package assistanceEntity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assistance_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssistanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    private Long userId;
    private String issueDescription;
    private String status;
    private LocalDateTime resolutionTime;
    private String resolutionMessage; // <-- New field for admin's reply
}
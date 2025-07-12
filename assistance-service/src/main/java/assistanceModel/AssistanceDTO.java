package assistanceModel;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssistanceDTO {

    // Remove @NotNull here, as it's auto-generated on creation
    private Long requestId;

    @NotNull(message="User ID should not be null")
    private Long userId;

    @NotBlank(message="Issue description cannot be blank")
    private String issueDescription;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime resolutionTime;

    @JsonInclude(JsonInclude.Include.NON_NULL) // Only include if present
    private String resolutionMessage; // <-- New field for admin's reply
}
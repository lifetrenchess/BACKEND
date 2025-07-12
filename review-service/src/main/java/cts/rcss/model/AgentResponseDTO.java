package cts.rcss.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AgentResponseDTO {

    @NotBlank(message = "Agent response cannot be blank")
    @Size(max = 500, message = "Agent response must be less than 500 characters")
    private String agentResponse;

	public String getAgentResponse() {
		return agentResponse;
	}

	public void setAgentResponse(String agentResponse) {
		this.agentResponse = agentResponse;
	}

    // Getter and setter...
}

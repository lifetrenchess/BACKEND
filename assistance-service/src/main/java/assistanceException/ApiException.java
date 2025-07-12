package assistanceException;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Builder
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public class ApiException {
	    private int code;
	    private String message;
	    private String path;
	    private Date when;
	}


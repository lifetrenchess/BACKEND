package assistanceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import assistanceEntity.AssistanceEntity;
import java.util.List;

public interface AssistanceRepository extends JpaRepository<AssistanceEntity,Long> {
	
	/**
	 * Find all assistance requests by user ID.
	 * 
	 * @param userId The ID of the user
	 * @return List of assistance requests for the user
	 */
	List<AssistanceEntity> findByUserId(Long userId);
}



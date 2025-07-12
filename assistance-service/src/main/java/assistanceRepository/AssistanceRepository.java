package assistanceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import assistanceEntity.AssistanceEntity;


public interface AssistanceRepository extends JpaRepository<AssistanceEntity,Long> {
	

}



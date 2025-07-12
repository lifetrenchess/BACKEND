package cts.tpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cts.tpm.entity.TravelPackage;
import java.util.List;

public interface TravelPackageRepository extends JpaRepository<TravelPackage, Long> {
	List<TravelPackage> findByActiveTrue();
	List<TravelPackage> findByCreatedByAgentId(Long agentId);
}
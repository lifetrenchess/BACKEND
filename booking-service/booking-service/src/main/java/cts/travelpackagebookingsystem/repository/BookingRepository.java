package cts.travelpackagebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cts.travelpackagebookingsystem.entity.Booking;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	List<Booking> findByUserId(Long userId);
	Optional<Booking> findByUserIdAndPackageId(Long userId, Long packageId);

}

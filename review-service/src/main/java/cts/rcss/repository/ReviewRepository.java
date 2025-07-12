package cts.rcss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cts.rcss.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}

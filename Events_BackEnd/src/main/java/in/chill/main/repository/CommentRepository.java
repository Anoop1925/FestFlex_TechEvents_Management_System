package in.chill.main.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.chill.main.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // Find recent comments ordered by creation date (most recent first)
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.user ORDER BY c.createdAt DESC")
    List<Comment> findAllOrderByCreatedAtDesc();
    
    // Find recent comments with limit using Pageable
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.user ORDER BY c.createdAt DESC")
    List<Comment> findRecentComments(Pageable pageable);
    
    // Find comments by user
    List<Comment> findByUserUserIdOrderByCreatedAtDesc(Long userId);
    
    // Count total comments
    @Query("SELECT COUNT(c) FROM Comment c")
    Long countAllComments();
}
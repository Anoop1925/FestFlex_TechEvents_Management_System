package in.chill.main.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.chill.main.entity.Comment;
import in.chill.main.repository.CommentRepository;

@Service
public class CommentServiceImplementation implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    
    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAllOrderByCreatedAtDesc();
    }

    @Override
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Comment not found with id: " + id);
        }
    }
    
    @Override
    public List<Comment> getRecentComments(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return commentRepository.findRecentComments(pageable);
    }
    
    @Override
    public List<Comment> getCommentsByUserId(Long userId) {
        return commentRepository.findByUserUserIdOrderByCreatedAtDesc(userId);
    }
    
    @Override
    public Long getCommentsCount() {
        return commentRepository.countAllComments();
    }
}
package in.chill.main.services;

import java.util.List;
import java.util.Optional;

import in.chill.main.dto.CommentDTO;
import in.chill.main.entity.Comment;

public interface CommentService {
    
    public List<Comment> getAllComments();
    
    public List<CommentDTO> getAllCommentsDTO();
    
    public Optional<Comment> getCommentById(Long id);
    
    public Comment createComment(Comment comment);
    
    public void deleteComment(Long id);
    
    public List<Comment> getRecentComments(int limit);
    
    public List<Comment> getCommentsByUserId(Long userId);
    
    public Long getCommentsCount();
}
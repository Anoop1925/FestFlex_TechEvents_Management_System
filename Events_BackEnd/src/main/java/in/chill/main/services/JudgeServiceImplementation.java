package in.chill.main.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.chill.main.entity.Judge;
import in.chill.main.repository.JudgeRepository;

@Service
public class JudgeServiceImplementation implements JudgeService {

    @Autowired
    private JudgeRepository judgeRepository;
    
    @Override
    public List<Judge> getAllJudges() {
        return judgeRepository.findAll();
    }

    @Override
    public Optional<Judge> getJudgeById(int id) {
        return judgeRepository.findById(id);
    }

    @Override
    public Judge createJudge(Judge judge) {
        return judgeRepository.save(judge);
    }

    @Override
    public Judge updateJudge(int id, Judge judge) {
        Judge existingJudge = judgeRepository.findById(id).orElse(null);
        if (existingJudge != null) {
            existingJudge.setJudge_name(judge.getJudge_name());
            return judgeRepository.save(existingJudge);
        } else {
            throw new RuntimeException("Judge not found with id: " + id);
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteJudge(int id) {
        if (!judgeRepository.existsById(id)) {
            throw new RuntimeException("Judge not found with id: " + id);
        }
        // Delete in order: child records before parent
        // Delete records from tables that reference events judged by this judge
        judgeRepository.deleteResultsByJudgeId(id);
        judgeRepository.deleteParticipationsByJudgeId(id);
        judgeRepository.deleteBudgetsByJudgeId(id);
        judgeRepository.deleteVolunteersByJudgeId(id);
        judgeRepository.deleteEventsByJudgeId(id);
        // Finally delete the judge itself
        judgeRepository.deleteJudgeById(id);
    }
} 
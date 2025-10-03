package in.chill.main.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.chill.main.entity.Club;
import in.chill.main.repository.ClubRepository;

@Service
public class ClubServiceImplementation implements ClubService {

    @Autowired
    private ClubRepository clubRepository;
    
    @Override
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    @Override
    public Optional<Club> getClubById(int id) {
        return clubRepository.findById(id);
    }

    @Override
    public Club createClub(Club club) {
        return clubRepository.save(club);
    }

    @Override
    public Club updateClub(int id, Club club) {
        Club existingClub = clubRepository.findById(id).orElse(null);
        if (existingClub != null) {
            existingClub.setName(club.getName());
            existingClub.setPresidentName(club.getPresidentName());
            existingClub.setPresidentContact(club.getPresidentContact());
            existingClub.setPresidentEmail(club.getPresidentEmail());
            return clubRepository.save(existingClub);
        } else {
            throw new RuntimeException("Club not found with id: " + id);
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteClub(int id) {
        if (!clubRepository.existsById(id)) {
            throw new RuntimeException("Club not found with id: " + id);
        }
        // Delete in order: child records before parent
        // First delete records from tables that reference events created by this club
        clubRepository.deleteResultsByClubId(id);
        clubRepository.deleteParticipationsByClubId(id);
        clubRepository.deleteBudgetsByClubId(id);
        // Then delete records directly referencing this club
        clubRepository.deleteVolunteersByClubId(id);
        clubRepository.deleteSponsorsByClubId(id);
        clubRepository.deleteEventsByClubId(id);
        // Finally delete the club itself
        clubRepository.deleteClubById(id);
    }
} 
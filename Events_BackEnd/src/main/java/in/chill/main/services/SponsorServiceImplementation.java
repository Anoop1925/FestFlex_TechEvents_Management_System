package in.chill.main.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.chill.main.entity.Sponsor;
import in.chill.main.repository.SponsorRepository;

@Service
public class SponsorServiceImplementation implements SponsorService {

    @Autowired
    private SponsorRepository sponsorRepository;
    
    @Override
    public List<Sponsor> getAllSponsors() {
        return sponsorRepository.findAll();
    }

    @Override
    public Optional<Sponsor> getSponsorById(int id) {
        return sponsorRepository.findById(id);
    }

    @Override
    public Sponsor createSponsor(Sponsor sponsor) {
        return sponsorRepository.save(sponsor);
    }

    @Override
    public Sponsor updateSponsor(int id, Sponsor sponsor) {
        Sponsor existingSponsor = sponsorRepository.findById(id).orElse(null);
        if (existingSponsor != null) {
            existingSponsor.setName(sponsor.getName());
            existingSponsor.setIsCash(sponsor.getIsCash());
            existingSponsor.setMode(sponsor.getMode());
            existingSponsor.setContact(sponsor.getContact());
            existingSponsor.setClub(sponsor.getClub());
            return sponsorRepository.save(existingSponsor);
        } else {
            throw new RuntimeException("Sponsor not found with id: " + id);
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteSponsor(int id) {
        if (!sponsorRepository.existsById(id)) {
            throw new RuntimeException("Sponsor not found with id: " + id);
        }
        // Delete in order: child records before parent
        sponsorRepository.deleteBudgetsBySponsorId(id);
        // Finally delete the sponsor itself
        sponsorRepository.deleteSponsorById(id);
    }
}

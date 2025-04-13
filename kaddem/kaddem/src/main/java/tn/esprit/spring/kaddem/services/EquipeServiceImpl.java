package tn.esprit.spring.kaddem.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;

import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class EquipeServiceImpl implements IEquipeService {
    private final EquipeRepository equipeRepository;

    public List<Equipe> retrieveAllEquipes() {
        log.info("Retrieving all equipes");
        List<Equipe> equipes = (List<Equipe>) equipeRepository.findAll();
        log.info("Found {} equipes", equipes.size());
        return equipes;
    }

    public Equipe addEquipe(Equipe e) {
        log.info("Adding equipe: {}", e.getNomEquipe());
        Equipe savedEquipe = equipeRepository.save(e);
        log.info("Equipe added with ID: {}", savedEquipe.getIdEquipe());
        return savedEquipe;
    }

    public void deleteEquipe(Integer idEquipe) {
        log.info("Deleting equipe with ID: {}", idEquipe);
        Equipe e = retrieveEquipe(idEquipe);
        equipeRepository.delete(e);
        log.info("Equipe with ID: {} deleted", idEquipe);
    }

    public Equipe retrieveEquipe(Integer equipeId) {
        log.info("Retrieving equipe with ID: {}", equipeId);
        return equipeRepository.findById(equipeId).orElseThrow(() -> {
            log.error("Equipe with ID {} not found", equipeId);
            return new NoSuchElementException("Equipe not found with ID: " + equipeId);
        });
    }

    public Equipe updateEquipe(Equipe e) {
        log.info("Updating equipe with ID: {}", e.getIdEquipe());
        Equipe updatedEquipe = equipeRepository.save(e);
        log.info("Equipe with ID: {} updated", updatedEquipe.getIdEquipe());
        return updatedEquipe;
    }

    public void evoluerEquipes() {
        log.info("Running scheduled evolution for equipes...");
        List<Equipe> equipes = (List<Equipe>) equipeRepository.findAll();
        for (Equipe equipe : equipes) {
            evoluerEquipe(equipe);
        }
    }

    public void evoluerEquipe(Equipe equipe) {
        if (equipe.getNiveau() == null) {
            log.warn("Equipe {} has no level set, skipping evolution", equipe.getNomEquipe());
            return;
        }

        log.info("Evaluating equipe: {} with current level: {}", equipe.getNomEquipe(), equipe.getNiveau());

        List<Etudiant> etudiants = equipe.getEtudiants() != null ? new ArrayList<>(equipe.getEtudiants()) : new ArrayList<>();
        int nbEtudiantsAvecContratsActifs = 0;

        for (Etudiant etudiant : etudiants) {
            Set<Contrat> contrats = etudiant.getContrats() != null ? etudiant.getContrats() : new HashSet<>();

            for (Contrat contrat : contrats) {
                if (contrat.getDateFinContrat() != null && !contrat.getArchive()) {
                    Date dateSysteme = new Date();
                    long diffInTime = dateSysteme.getTime() - contrat.getDateFinContrat().getTime();
                    long diffInYears = diffInTime / (1000L * 60 * 60 * 24 * 365);

                    if (diffInYears > 1) {
                        nbEtudiantsAvecContratsActifs++;
                        log.info("Student {} has an active contract older than 1 year", etudiant.getNomE());
                        break;
                    }
                }
            }

            if (nbEtudiantsAvecContratsActifs >= 3) {
                log.info("Equipe {} has sufficient students with active contracts older than 1 year, triggering evolution", equipe.getNomEquipe());
                break;
            }
        }

        // Evolving the team based on the conditions
        if (nbEtudiantsAvecContratsActifs >= 3) {
            if (equipe.getNiveau() == Niveau.JUNIOR) {
                equipe.setNiveau(Niveau.SENIOR);
                equipeRepository.save(equipe);
                log.info("Equipe ID {} evolved from JUNIOR to SENIOR", equipe.getIdEquipe());
            } else if (equipe.getNiveau() == Niveau.SENIOR) {
                equipe.setNiveau(Niveau.EXPERT);
                equipeRepository.save(equipe);
                log.info("Equipe ID {} evolved from SENIOR to EXPERT", equipe.getIdEquipe());
            }
        }
    }
}

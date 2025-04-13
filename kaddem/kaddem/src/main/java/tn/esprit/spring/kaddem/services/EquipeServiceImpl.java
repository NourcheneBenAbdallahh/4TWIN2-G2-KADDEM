package tn.esprit.spring.kaddem.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class EquipeServiceImpl implements IEquipeService {
    EquipeRepository equipeRepository;

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
        Equipe equipe = equipeRepository.findById(equipeId).orElse(null);
        if (equipe != null) {
            log.info("Equipe retrieved: {}", equipe.getNomEquipe());
        } else {
            log.error("Equipe with ID: {} not found", equipeId);
        }
        return equipe;
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
            if ((equipe.getNiveau().equals(Niveau.JUNIOR)) || (equipe.getNiveau().equals(Niveau.SENIOR))) {
                log.info("Evaluating equipe: {} with current level: {}", equipe.getNomEquipe(), equipe.getNiveau());

                List<Etudiant> etudiants = (List<Etudiant>) equipe.getEtudiants();
                Integer nbEtudiantsAvecContratsActifs = 0;

                for (Etudiant etudiant : etudiants) {
                    Set<Contrat> contrats = etudiant.getContrats();
                    for (Contrat contrat : contrats) {
                        Date dateSysteme = new Date();
                        long difference_In_Time = dateSysteme.getTime() - contrat.getDateFinContrat().getTime();
                        long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));

                        if ((contrat.getArchive() == false) && (difference_In_Years > 1)) {
                            nbEtudiantsAvecContratsActifs++;
                            log.info("Student {} has an active contract older than 1 year", etudiant.getNomE());
                            break;
                        }
                    }
                    if (nbEtudiantsAvecContratsActifs >= 3) break;
                }

                if (nbEtudiantsAvecContratsActifs >= 3) {
                    if (equipe.getNiveau().equals(Niveau.JUNIOR)) {
                        equipe.setNiveau(Niveau.SENIOR);
                        equipeRepository.save(equipe);
                        log.info("Equipe ID {} evolved from JUNIOR to SENIOR", equipe.getIdEquipe());
                        break;
                    }
                    if (equipe.getNiveau().equals(Niveau.SENIOR)) {
                        equipe.setNiveau(Niveau.EXPERT);
                        equipeRepository.save(equipe);
                        log.info("Equipe ID {} evolved from SENIOR to EXPERT", equipe.getIdEquipe());
                        break;
                    }
                }
            }
        }
    }
}

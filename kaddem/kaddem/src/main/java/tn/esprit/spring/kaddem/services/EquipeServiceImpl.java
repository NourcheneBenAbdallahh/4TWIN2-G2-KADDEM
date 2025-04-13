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

	@Override
	public List<Equipe> retrieveAllEquipes() {
		log.info("Retrieving all equipes...");
		return (List<Equipe>) equipeRepository.findAll();
	}

	@Override
	public Equipe addEquipe(Equipe e) {
		log.info("Adding new equipe: {}", e.getNomEquipe());
		return equipeRepository.save(e);
	}

	@Override
	public void deleteEquipe(Integer idEquipe) {
		log.warn("Deleting equipe with ID: {}", idEquipe);
		Optional<Equipe> equipeOpt = equipeRepository.findById(idEquipe);
		if (equipeOpt.isPresent()) {
			equipeRepository.delete(equipeOpt.get());
			log.info("Equipe deleted.");
		} else {
			log.error("Equipe not found with ID: {}", idEquipe);
		}
	}

	@Override
	public Equipe retrieveEquipe(Integer equipeId) {
		log.info("Retrieving equipe with ID: {}", equipeId);
		return equipeRepository.findById(equipeId).orElse(null);
	}

	@Override
	public Equipe updateEquipe(Equipe e) {
		log.info("Updating equipe ID: {}", e.getIdEquipe());
		return equipeRepository.save(e);
	}

	@Override
	public void evoluerEquipes() {
		log.info("Running scheduled evolution for equipes...");
		List<Equipe> equipes = (List<Equipe>) equipeRepository.findAll();
		Date now = new Date();

		for (Equipe equipe : equipes) {
			if (equipe.getNiveau() == Niveau.JUNIOR || equipe.getNiveau() == Niveau.SENIOR) {

				Set<Etudiant> etudiants = equipe.getEtudiants();
				if (etudiants == null || etudiants.isEmpty()) continue;

				int nbEtudiantsAvecContratsActifs = 0;

				for (Etudiant etudiant : etudiants) {
					Set<Contrat> contrats = etudiant.getContrats();
					if (contrats == null) continue;

					for (Contrat contrat : contrats) {
						long differenceInYears = (now.getTime() - contrat.getDateFinContrat().getTime()) / (1000L * 60 * 60 * 24 * 365);
						if (!contrat.getArchive() && differenceInYears > 1) {
							nbEtudiantsAvecContratsActifs++;
							break; // break inner loop to count the student only once
						}
					}

					if (nbEtudiantsAvecContratsActifs >= 3) break;
				}

				if (nbEtudiantsAvecContratsActifs >= 3) {
					if (equipe.getNiveau() == Niveau.JUNIOR) {
						equipe.setNiveau(Niveau.SENIOR);
						log.info("Equipe ID {} evolved from JUNIOR to SENIOR", equipe.getIdEquipe());
					} else if (equipe.getNiveau() == Niveau.SENIOR) {
						equipe.setNiveau(Niveau.EXPERT);
						log.info("Equipe ID {} evolved from SENIOR to EXPERT", equipe.getIdEquipe());
					}
					equipeRepository.save(equipe);
				}
			}
		}
	}
}

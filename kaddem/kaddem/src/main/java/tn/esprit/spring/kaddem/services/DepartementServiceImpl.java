package tn.esprit.spring.kaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j

@Service
public class DepartementServiceImpl implements IDepartementService{
	@Autowired
	DepartementRepository departementRepository;


	public List<Departement> retrieveAllDepartements(){
		log.info("Appel de retrieveAllDepartements()");
		List<Departement> list = (List<Departement>) departementRepository.findAll();
		log.debug("Liste récupérée : {}", list);
		return list;
	}


	public Departement addDepartement(Departement d) {
		log.info("Ajout d'un département");

		log.debug("Détails du département reçu : {}", d);

		try {
			Departement saved = departementRepository.save(d);

			log.debug("Département sauvegardé avec succès : {}", saved);

			return saved;
		} catch (Exception e) {
			log.error("Erreur lors de l’ajout du département : {}", e.getMessage(), e);
			throw e;
		}
	}

	public Departement updateDepartement(Departement d) {
		if (d.getIdDepart() == null) {
			log.warn("Tentative de mise à jour avec un ID null !");
		} else {
			log.info("Mise à jour du département ID {}", d.getIdDepart());
		}

		log.debug("Détails du département avant mise à jour : {}", d);

		Departement updated = departementRepository.save(d);

		log.debug("Département mis à jour : {}", updated);

		return updated;
	}



	public Departement retrieveDepartement(Integer idDepart) {
		log.info("Récupération du département avec ID {}", idDepart);

		Departement d = departementRepository.findById(idDepart).orElse(null);

		if (d != null) {
			log.debug("Département trouvé : {}", d);
		} else {
			log.error("Aucun département trouvé avec l'ID {}", idDepart);
		}

		return d;
	}


	public void deleteDepartement(Integer idDepartement){
		log.warn("Suppression du département avec ID {}", idDepartement);
		Departement d = retrieveDepartement(idDepartement);
		departementRepository.delete(d);
		log.info("Département supprimé");
	}
}
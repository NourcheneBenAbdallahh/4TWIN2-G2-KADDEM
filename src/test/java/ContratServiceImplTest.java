import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.ContratServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContratServiceImplTest {
    @Mock
    private ContratRepository contratRepository;
    @Mock
    private EtudiantRepository etudiantRepository;
    @InjectMocks
    private ContratServiceImpl contratService;

    private Contrat contrat;
    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        contrat = new Contrat();
        contrat.setIdContrat(1);
        contrat.setSpecialite(tn.esprit.spring.kaddem.entities.Specialite.IA);
        contrat.setArchive(false);
        contrat.setDateFinContrat(new Date());

        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1);
        etudiant.setNomE("Doe");
        etudiant.setPrenomE("John");
        etudiant.setContrats(new HashSet<>());
    }

    @Test
    void addContrat() {
        // Given
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);
        // When
        Contrat result = contratService.addContrat(contrat);
        // Then
        assertNotNull(result);
        assertEquals(contrat.getSpecialite(), result.getSpecialite());
        assertEquals(contrat.getIdContrat(), result.getIdContrat());
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void updateContrat() {
        // Given
        contrat.setArchive(true);
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);
        // When
        Contrat result = contratService.updateContrat(contrat);
        // Then
        assertNotNull(result);
        assertTrue(result.getArchive());
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void retrieveContrat() {
        // Given
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));
        // When
        Contrat result = contratService.retrieveContrat(1);
        // Then
        assertNotNull(result);
        assertEquals(contrat.getIdContrat(), result.getIdContrat());
        verify(contratRepository, times(1)).findById(1);
    }

    @Test
    void retrieveContrat_NotFound() {
        // Given
        when(contratRepository.findById(999)).thenReturn(Optional.empty());
        // Then
        assertThrows(NoSuchElementException.class, () -> {
            contratService.retrieveContrat(999);
        });
        verify(contratRepository, times(1)).findById(999);
    }

    @Test
    void deleteContrat() {
        // Given
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));
        // When
        contratService.removeContrat(1);
        // Then
        verify(contratRepository, times(1)).findById(1);
        verify(contratRepository, times(1)).delete(contrat);
    }

    @Test
    void affectContratToEtudiant() {
        // Given
        when(etudiantRepository.findByNomEAndPrenomE("Doe", "John")).thenReturn(etudiant);
        when(contratRepository.findByIdContrat(1)).thenReturn(contrat);
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);
        // When
        Contrat result = contratService.affectContratToEtudiant(1, "Doe", "John");
        // Then
        verify(contratRepository, times(1)).save(contrat);
        assertEquals(etudiant, result.getEtudiant());
    }

    @Test
    void retrieveAllContrats() {
        // Given
        Contrat contrat2 = new Contrat();
        contrat2.setIdContrat(2);
        contrat2.setSpecialite(tn.esprit.spring.kaddem.entities.Specialite.CLOUD);
        List<Contrat> contrats = Arrays.asList(contrat, contrat2);
        when(contratRepository.findAll()).thenReturn(contrats);
        // When
        List<Contrat> result = contratService.retrieveAllContrats();
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(contrat));
        assertTrue(result.contains(contrat2));
        assertEquals(contrat.getSpecialite(), result.get(0).getSpecialite());
        assertEquals(contrat2.getSpecialite(), result.get(1).getSpecialite());
        verify(contratRepository, times(1)).findAll();
    }

    @Test
    void nbContratsValides() {
        // Given
        Date start = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 30); // 30 jours
        Date end = new Date();
        when(contratRepository.getnbContratsValides(start, end)).thenReturn(3);
        // When
        Integer count = contratService.nbContratsValides(start, end);
        // Then
        assertEquals(3, count);
    }

    @Test
    void getChiffreAffaireEntreDeuxDates() {
        // Given
        Contrat contrat1 = new Contrat();
        contrat1.setSpecialite(tn.esprit.spring.kaddem.entities.Specialite.IA);

        Contrat contrat2 = new Contrat();
        contrat2.setSpecialite(tn.esprit.spring.kaddem.entities.Specialite.CLOUD);

        List<Contrat> contrats = Arrays.asList(contrat1, contrat2);

        when(contratRepository.findAll()).thenReturn(contrats);

        Date start = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 60); // 2 mois
        Date end = new Date();

        float result = contratService.getChiffreAffaireEntreDeuxDates(start, end);
        // 2 months × 300 (IA) + 2 months × 400 (CLOUD) = 1400
        assertTrue(result >= 1390 && result <= 1410); // tolérance
    }
}

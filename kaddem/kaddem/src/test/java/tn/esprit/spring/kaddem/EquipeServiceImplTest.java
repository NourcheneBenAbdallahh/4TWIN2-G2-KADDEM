package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipeServiceImplTest {

    @Mock
    private EquipeRepository equipeRepository;

    @InjectMocks
    private EquipeServiceImpl equipeService;

    private Equipe equipe;
    private Etudiant etudiant;
    private Contrat contrat;

    @BeforeEach
    void setUp() {
        equipe = new Equipe();
        equipe.setIdEquipe(1);
        equipe.setNomEquipe("Alpha Team");
        equipe.setNiveau(Niveau.JUNIOR);

        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1);
        etudiant.setNomE("John Doe");

        contrat = new Contrat();
        contrat.setDateFinContrat(new Date(System.currentTimeMillis() - 10000000000L));  // A contract older than 1 year
        contrat.setArchive(false);
    }

    @Test
    void testRetrieveAllEquipes() {
        Equipe e2 = new Equipe();
        e2.setIdEquipe(2);
        e2.setNomEquipe("Beta Team");

        List<Equipe> equipes = Arrays.asList(equipe, e2);
        when(equipeRepository.findAll()).thenReturn(equipes);

        List<Equipe> result = equipeService.retrieveAllEquipes();

        assertEquals(2, result.size());
        assertTrue(result.contains(equipe));
        verify(equipeRepository, times(1)).findAll();
    }

    @Test
    void testAddEquipe() {
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);

        Equipe result = equipeService.addEquipe(equipe);

        assertNotNull(result);
        assertEquals(equipe.getNomEquipe(), result.getNomEquipe());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void testDeleteEquipe() {
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        equipeService.deleteEquipe(1);

        verify(equipeRepository, times(1)).delete(equipe);
    }

    @Test
    void testRetrieveEquipe() {
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        Equipe result = equipeService.retrieveEquipe(1);

        assertNotNull(result);
        assertEquals("Alpha Team", result.getNomEquipe());
        verify(equipeRepository, times(1)).findById(1);
    }

    @Test
    void testRetrieveEquipe_NotFound() {
        when(equipeRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            equipeService.retrieveEquipe(999);
        });

        verify(equipeRepository, times(1)).findById(999);
    }

    @Test
    void testUpdateEquipe() {
        equipe.setNomEquipe("Updated Team");
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);

        Equipe result = equipeService.updateEquipe(equipe);

        assertEquals("Updated Team", result.getNomEquipe());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void testEvoluerEquipes() {
        Set<Contrat> contrats = new HashSet<>();
        contrats.add(contrat);
        etudiant.setContrats(contrats);

        Set<Etudiant> etudiants = new HashSet<>();
        etudiants.add(etudiant);

        equipe.setEtudiants(etudiants);

        when(equipeRepository.findAll()).thenReturn(Collections.singletonList(equipe));
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);

        equipeService.evoluerEquipes();

        assertEquals(Niveau.SENIOR, equipe.getNiveau());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void testEvoluerEquipes_NoEvolution() {
        contrat.setDateFinContrat(new Date(System.currentTimeMillis()));  // Contract too recent to trigger evolution
        Set<Contrat> contrats = new HashSet<>();
        contrats.add(contrat);
        etudiant.setContrats(contrats);

        Set<Etudiant> etudiants = new HashSet<>();
        etudiants.add(etudiant);

        equipe.setEtudiants(etudiants);

        when(equipeRepository.findAll()).thenReturn(Collections.singletonList(equipe));

        equipeService.evoluerEquipes();

        assertEquals(Niveau.JUNIOR, equipe.getNiveau());  // No evolution should happen
        verify(equipeRepository, never()).save(equipe);
    }
}

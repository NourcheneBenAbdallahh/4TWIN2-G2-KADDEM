import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.EtudiantServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EtudiantServiceImplTest {

    @Mock
    private EtudiantRepository etudiantRepository;
    @Mock
    private ContratRepository contratRepository;
    @Mock
    private EquipeRepository equipeRepository;
    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRetrieveAllEtudiants() {
        List<Etudiant> list = new ArrayList<>();
        list.add(new Etudiant("Ali", "Ben Ali"));
        when(etudiantRepository.findAll()).thenReturn(list);
        List<Etudiant> result = etudiantService.retrieveAllEtudiants();
        assertEquals(1, result.size());
    }

    @Test
    void testAddEtudiant() {
        Etudiant e = new Etudiant("Ali", "Ben Ali");
        when(etudiantRepository.save(e)).thenReturn(e);
        Etudiant result = etudiantService.addEtudiant(e);
        assertEquals("Ali", result.getNomE());
    }

    @Test
    void testUpdateEtudiant() {
        Etudiant e = new Etudiant("Ali", "Ben Ali");
        when(etudiantRepository.save(e)).thenReturn(e);
        Etudiant result = etudiantService.updateEtudiant(e);
        assertEquals("Ben Ali", result.getPrenomE());
    }

    @Test
    void testRetrieveEtudiant() {
        Etudiant e = new Etudiant("Ali", "Ben Ali");
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(e));
        Etudiant result = etudiantService.retrieveEtudiant(1);
        assertNotNull(result);
    }

    @Test
    void testRemoveEtudiant() {
        Etudiant e = new Etudiant("Ali", "Ben Ali");
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(e));
        etudiantService.removeEtudiant(1);
        verify(etudiantRepository).delete(e);
    }

    @Test
    void testAssignEtudiantToDepartement() {
        Etudiant e = new Etudiant("Ali", "Ben Ali");
        Departement d = new Departement();
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(e));
        when(departementRepository.findById(2)).thenReturn(Optional.of(d));
        etudiantService.assignEtudiantToDepartement(1, 2);
        verify(etudiantRepository).save(e);
    }

    @Test
    void testAddAndAssignEtudiantToEquipeAndContract() {
        Etudiant e = new Etudiant("Ali", "Ben Ali");
        Contrat c = new Contrat();
        Equipe eq = new Equipe();
        Set<Etudiant> etudiants = new HashSet<>();
        eq.setEtudiants(etudiants);

        when(contratRepository.findById(1)).thenReturn(Optional.of(c));
        when(equipeRepository.findById(2)).thenReturn(Optional.of(eq));

        Etudiant result = etudiantService.addAndAssignEtudiantToEquipeAndContract(e, 1, 2);

        assertTrue(eq.getEtudiants().contains(e));
        assertEquals(e, c.getEtudiant());
    }

    @Test
    void testGetEtudiantsByDepartement() {
        List<Etudiant> list = new ArrayList<>();
        list.add(new Etudiant("Ali", "Ben Ali"));
        when(etudiantRepository.findEtudiantsByDepartement_IdDepart(1)).thenReturn(list);
        List<Etudiant> result = etudiantService.getEtudiantsByDepartement(1);
        assertEquals(1, result.size());
    }
}


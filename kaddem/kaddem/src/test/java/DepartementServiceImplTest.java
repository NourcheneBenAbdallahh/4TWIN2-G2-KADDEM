
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.services.DepartementServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DepartementServiceImplTest {

    @InjectMocks
    private DepartementServiceImpl departementService;

    @Mock
    private DepartementRepository departementRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllDepartements() {
        Departement d1 = new Departement();
        d1.setNomDepart("Informatique");
        d1.setIdDepart(1);
        Departement d2 = new Departement();
        d2.setNomDepart("Finance");
        d2.setIdDepart(2);

        when(departementRepository.findAll()).thenReturn(Arrays.asList(d1, d2));

        List<Departement> result = departementService.retrieveAllDepartements();

        assertEquals(2, result.size());
        verify(departementRepository, times(1)).findAll();
    }

    @Test
    void testAddDepartement() {
        Departement d = new Departement();
        d.setNomDepart("Informatique");
        d.setIdDepart(23);


        when(departementRepository.save(any(Departement.class))).thenReturn(d);

        Departement result = departementService.addDepartement(d);

        assertNotNull(result);
        assertEquals("Informatique", result.getNomDepart());
    }

    @Test
    void testRetrieveDepartement() {
        Departement d = new Departement();
        d.setIdDepart(1);
        d.setNomDepart("RH");

        when(departementRepository.findById(1)).thenReturn(Optional.of(d));

        Departement result = departementService.retrieveDepartement(1);

        assertEquals("RH", result.getNomDepart());
    }

    @Test
    void testDeleteDepartement() {
        Departement d = new Departement();
        d.setIdDepart(1);
        d.setNomDepart("Finance");

        when(departementRepository.findById(1)).thenReturn(Optional.of(d));

        departementService.deleteDepartement(1);

        verify(departementRepository, times(1)).delete(d);
    }
}

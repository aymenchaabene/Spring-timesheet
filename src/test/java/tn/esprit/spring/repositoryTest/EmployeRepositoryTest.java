package tn.esprit.spring.repositoryTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.EntrepriseRepository;
import tn.esprit.spring.services.EmployeServiceImpl;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class EmployeRepositoryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeServiceImpl.class);

	@Autowired
	EmployeRepository repo;
	@Autowired
	ContratRepository repoC;
	@Autowired
	EntrepriseRepository repoE;
	@Autowired
	DepartementRepository repoD;

	java.util.List<String> eml;

	@Test
	@Rollback(false)
	public void ajouterEmployerTest() {
		Employe employe = getEmploye();

		assertNotNull(employe);
	}

	@Test
	@Rollback(false)
	public void countempTest() {
		LOGGER.info("{La methode countemp dans EmployeRepository retourne la valeur :  }" + " " + repo.countemp(),
				+repo.countemp());

		assertThat(repo.countemp()).isPositive().isNotZero();

	}

	@Test
	@Rollback(false)
	public void employeNamesTest() {
		java.util.List<String> names = repo.employeNames();
		LOGGER.info("{La methode employeNames dans EmployeRepository contient :  }" + " " + repo.employeNames().size());
		assertThat(names.size()).isPositive();

	}

	@Test
	@Rollback(false)
	public void mettreAjourEmailByEmployeIdJPQLTest() {
		Employe employe = new Employe("Sabeh", "Kchock", "sabeh@gmail.com", true, Role.CHEF_DEPARTEMENT);

		repo.save(employe);

		repo.mettreAjourEmailByEmployeIdJPQL("mohamedHedi@esprit.com", employe.getId());
		LOGGER.info(
				"{La methode mettreAjourEmailByEmployeIdJPQL dans EmployeRepository  : Succes  }" + employe.getEmail());

		assertThat(employe.getEmail()).isNotSameAs("mohamedHedi@esprit.com");
	}

	@Test
	@Rollback(false)
	public void getSalaireByEmployeIdJPQLTest() {
		Employe employe = getEmploye();
		Contrat contrat = getContrat(employe);
		float salaire = repo.getSalaireByEmployeIdJPQL(contrat.getEmploye().getId());

		LOGGER.info("{La methode getSalaireByEmployeIdJPQL dans EmployeRepository return :   }" + " "
				+ repo.getSalaireByEmployeIdJPQL(contrat.getEmploye().getId()));
		// assertNotNull(salaire);
		assertThat(salaire).isPositive().isNotZero();

	}

	@Test
	@Rollback(false)
	public void deleteAllContratJPQLTest() {
		Employe employe = getEmploye();
		Contrat contrat = getContrat(employe);
		// boolean
		// isExistBeforeDelete=repoC.findById(contrat.getReference()).isPresent();

		repo.deleteAllContratJPQL();
		boolean notExistAfterDelete = repoC.findById(contrat.getReference()).isPresent();
		LOGGER.info("{La methode deleteAllContratJPQL dans EmployeRepository return :  Succes }");
		// assertTrue(isExistBeforeDelete);
		assertFalse(notExistAfterDelete);
	}

	@Test
	@Rollback(false)
	public void getSalaireMoyenByDepartementIdTest() {
		Departement departemen = new Departement("Informatique");
		java.util.List<Departement> departements = new ArrayList<>();
		repoD.save(departemen);

		departements.add(departemen);

		Contrat contrat1 = getContrat(getEmploye());
		Contrat contrat2 = getContrat2(getEmploye());
		Contrat contrat3 = getContrat2(getEmploye());

		java.util.List<Employe> employes = new ArrayList<>();

		employes.add(contrat1.getEmploye());
		employes.add(contrat2.getEmploye());
		employes.add(contrat3.getEmploye());

		departemen.setEmployes(employes);

		double d = repo.getSalaireMoyenByDepartementId(departemen.getId());
		LOGGER.info("{La methode getSalaireMoyenByDepartementId dans EmployeRepository return :  Succes }" + d);

		assertThat(d).isPositive().isNotZero();
	}

	@Test
	@Rollback(false)
	public void getAllEmployeByEntreprisecTest() {
		Employe employe1 = getEmploye();
		Employe employe2 = getEmploye();
		Employe employe3 = getEmploye();
		java.util.List<Employe> employes = new ArrayList<>();
		employes.add(employe1);
		employes.add(employe2);
		employes.add(employe3);

		Entreprise entreprise = getEntreprise();

		Departement departement = new Departement("Informatique");
		departement.setEmployes(employes);
		repoD.save(departement);
		entreprise.addDepartement(departement);

		repo.getAllEmployeByEntreprisec(entreprise);
		LOGGER.info("{La methode getAllEmployeByEntreprisec dans EmployeRepository return :  Succes }"
				+ repo.getAllEmployeByEntreprisec(entreprise).size());
		assertNotNull(repo.getAllEmployeByEntreprisec(entreprise));
		assertThat(repo.getAllEmployeByEntreprisec(entreprise).size()).isPositive();
	}

	private Employe getEmploye() {
		Employe employe = new Employe("Med", "BEN KHOUDJA", "med@esprit.tn", true, Role.ADMINISTRATEUR);

		repo.save(employe);
		return employe;
	}

	private Entreprise getEntreprise() {
		Entreprise entreprise = new Entreprise("Esprit", "Commercial");
		Departement departement = new Departement("Financiere");
		repoD.save(departement);
		entreprise.addDepartement(departement);
		repoE.save(entreprise);
		return entreprise;
	}

	private Contrat getContrat(Employe employe) {
		Contrat contrat = new Contrat();
		employe = getEmploye();

		contrat.setEmploye(employe);
		contrat.setReference(12);
		contrat.setSalaire(1236);
		contrat.setDateDebut(new Date());
		contrat.setTypeContrat("CDI");
		repoC.save(contrat);

		return contrat;
	}

	private Contrat getContrat2(Employe employe) {
		Contrat contrat = new Contrat();
		employe = getEmploye();

		contrat.setEmploye(employe);
		contrat.setReference(12);
		contrat.setSalaire(1436);
		contrat.setDateDebut(new Date());
		contrat.setTypeContrat("CDI");
		repoC.save(contrat);

		return contrat;
	}
}

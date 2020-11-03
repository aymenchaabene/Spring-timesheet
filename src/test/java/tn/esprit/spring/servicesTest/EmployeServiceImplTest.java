package tn.esprit.spring.servicesTest;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.entities.TimesheetPK;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.EntrepriseRepository;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.repository.TimesheetRepository;
import tn.esprit.spring.services.EmployeServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
/*
 * @SpringBootTest Annotation qui peut être spécifiée sur une classe de test qui
 * exécute des tests basés sur Spring Boot.
 */
public class EmployeServiceImplTest {

	@Autowired
	EmployeServiceImpl controller;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	ContratRepository contratRepoistory;
	@Autowired
	EmployeRepository employeRepository;
	@Autowired
	EntrepriseRepository entrepriseRepository;
	@Autowired
	MissionRepository missionRepository;
	@Autowired
	TimesheetRepository timesheetRepository;

	@Test
	public void ajouterEmployeTest() {
		Employe employe = new Employe("Mohamed Hedi", "BEN KHOUDJA", "mohamedhedi.benkhoudja@esprit.tn", true,
				Role.ADMINISTRATEUR);

		int i = controller.ajouterEmploye(employe);

		assertThat(i).isNotNegative();
	}

	@Test
	public void mettreAjourEmailByEmployeIdTest() {
		Employe employe = new Employe("Kawthar", "BEN KHOUDJA", "kawthaar.benkhoudja@esprit.tn", true,
				Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe);

		controller.mettreAjourEmailByEmployeId("kawthaar.benkhoudja@gmail.com", employe.getId());
		Employe employe2 = employeRepository.findById(employe.getId()).orElse(null);

		assertThat(employe.getEmail()).isNotSameAs(employe2.getEmail());
	}

	@Test
	public void affecterEmployeADepartementTest() {

		Employe employe = new Employe("Kawthar", "BEN KHOUDJA", "kawthaar.benkhoudja@esprit.tn", true,
				Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe);
		Departement departement = new Departement("R.H");
		deptRepoistory.save(departement);
		controller.affecterEmployeADepartement(employe.getId(), departement.getId());

		assertNotNull(deptRepoistory.findById(departement.getId()));

	}

	@Test
	public void desaffecterEmployeDuDepartementTest() {
		Employe employe = new Employe("Imen", "BEN KHOUDJA", "kawthaar.benkhoudja@esprit.tn", true,
				Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe);
		Departement departement = new Departement("R.D");
		deptRepoistory.save(departement);
		controller.desaffecterEmployeDuDepartement(employe.getId(), departement.getId());



}

	@Test
	public void deleteEmployeByIdTest() {
		Employe employe = new Employe("Supprimer", "Supprimer", "Supprimer.benkhoudja@esprit.tn", true,
				Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe);
		Employe employeManagedEntity = null;
		boolean b = employeRepository.findById(employe.getId()).isPresent();
		if (b) {
			employeManagedEntity = employeRepository.findById(employe.getId()).get();

		}
		controller.deleteEmployeById(employeManagedEntity.getId());

	}

	@Test
	public void deleteContratByIdTest() {
		Contrat contrat = new Contrat(new Date(), "CDI", 2000);
		controller.ajouterContrat(contrat);
		assertNotNull(contrat);
		controller.deleteContratById(contrat.getReference());

	}

	@Test
	public void ajouterContratTest() {
		Contrat contrat = new Contrat(new Date(), "CDI", 1500);
		controller.ajouterContrat(contrat);
		assertNotNull(contrat);

	}

	@Test(expected = Test.None.class /* no exception expected */)
	public void affecterContratAEmployeTest() {
		Contrat contrat = new Contrat(new Date(), "CDI", 1500);
		contratRepoistory.save(contrat);
		Employe employe = new Employe("Imen", "BEN KHOUDJA", "kawthaar.benkhoudja@esprit.tn", true,
				Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe);
		controller.affecterContratAEmploye(contrat.getReference(), employe.getId());

	}

	@Test
	public void getEmployePrenomByIdTest() {
		Employe employe = new Employe("Imen", "SAHLI", "Imen.SAHLI@esprit.tn", true, Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe);

		String name = controller.getEmployePrenomById(employe.getId());

		assertThat(name).isEqualTo(employe.getPrenom());
	}

	@Test
	public void getNombreEmployeJPQLTest() {
		int i = controller.getNombreEmployeJPQL();
		assertThat(i).isNotEqualTo(-1);

	}

	@Test
	public void getAllEmployeNamesJPQLTest() {
		Employe employe = new Employe("Khalil", "SEKMA", "Khalil.SEKMA@esprit.tn", true, Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe);

		java.util.List<String> names = controller.getAllEmployeNamesJPQL();

		assertThat(names.size()).isPositive();

	}

	@Test
	public void getAllEmployesTest() {
		java.util.List<Employe> employes = controller.getAllEmployes();

		assertThat(employes.size()).isPositive();

	}

	@Test
	public void getSalaireMoyenByDepartementIdTest() {
		Departement departemen = new Departement("Informatique");

		Employe employe = new Employe("Imen", "SAHLI", "Imen.SAHLI@esprit.tn", true, Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe);
		Employe employe1 = new Employe("Souad", "Mahasen", "Souad.SAHLI@esprit.tn", true, Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe1);
		Employe employe2 = new Employe("Bilel", "Hedhli", "Bilel.HEdhli@esprit.tn", true, Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe2);

		Contrat contrat = new Contrat(new Date(), "CDI", 2000);
		Contrat contrat1 = new Contrat(new Date(), "CDI", 2345);
		Contrat contrat2 = new Contrat(new Date(), "CDI", 1234);

		contrat.setEmploye(employe);
		contrat1.setEmploye(employe1);
		contrat2.setEmploye(employe2);

		java.util.List<Employe> employes = new ArrayList<>();
		employes.add(contrat.getEmploye());
		employes.add(contrat1.getEmploye());
		employes.add(contrat2.getEmploye());

		departemen.setEmployes(employes);

		controller.ajouterContrat(contrat);
		controller.ajouterContrat(contrat1);
		controller.ajouterContrat(contrat2);
		deptRepoistory.save(departemen);

		double d = controller.getSalaireMoyenByDepartementId(departemen.getId());

		assertThat(d).isPositive().isNotZero();
	}

	@Test
	public void getSalaireByEmployeIdJPQLTest() {
		Employe employe = new Employe("Imen", "SAHLI", "Imen.SAHLI@esprit.tn", true, Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe);

		Contrat contrat = new Contrat(new Date(), "CDI", 2000);
		contrat.setEmploye(employe);
		controller.ajouterContrat(contrat);
		float salaire = controller.getSalaireByEmployeIdJPQL(employe.getId());

		assertThat(salaire).isPositive().isGreaterThan(0);
	}

	@Test
	public void deleteAllContratJPQLTest() {
		Contrat contrat = new Contrat(new Date(), "CDI", 6000);
		controller.ajouterContrat(contrat);
		controller.deleteAllContratJPQL();
		boolean notExistAfterDelete = contratRepoistory.findById(contrat.getReference()).isPresent();

		assertFalse(notExistAfterDelete);

	}

	@Test
	public void getAllEmployeByEntrepriseTest() {
		Employe employe = new Employe("Imen", "SAHLI", "Imen.SAHLI@esprit.tn", true, Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe);
		Employe employe1 = new Employe("Souad", "Mahasen", "Souad.SAHLI@esprit.tn", true, Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe1);
		Employe employe2 = new Employe("Bilel", "Hedhli", "Bilel.HEdhli@esprit.tn", true, Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe2);

		Entreprise entreprise = new Entreprise("Esprit", "Commercial");

		Departement departemen = new Departement("Informatique");
		Departement departemen2 = new Departement("R.H");
		List<Departement> departements = new ArrayList<>();
		departements.add(departemen);
		departements.add(departemen2);

		entreprise.setDepartements(departements);
		java.util.List<Employe> employes = new ArrayList<>();
		java.util.List<Employe> employes2 = new ArrayList<>();
		employes.add(employe);
		employes.add(employe1);
		employes2.add(employe2);

		departemen.setEmployes(employes);
		departemen2.setEmployes(employes2);

		departemen.setEntreprise(entreprise);
		departemen2.setEntreprise(entreprise);
		entrepriseRepository.save(entreprise);

		deptRepoistory.save(departemen);
		deptRepoistory.save(departemen2);

		controller.getAllEmployeByEntreprise(entreprise);
		java.util.List<Employe> Entreprise_employe = controller.getAllEmployeByEntreprise(entreprise);

		assertThat(Entreprise_employe.size()).isPositive();

	}

	@Test
	public void mettreAjourEmailByEmployeIdJPQLTest() {
		Employe employe = new Employe("Sawsen", "MACRON", "SAWSAN.MACROM@esprit.tn", true, Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe);
		controller.mettreAjourEmailByEmployeIdJPQL("sawsen@esprit.com", employe.getId());
		Employe employeManagedEntity = employeRepository.findById(employe.getId()).get();

		assertThat(employe.getEmail()).isNotSameAs(employeManagedEntity.getEmail());

	}

	@Test
	public void getTimesheetsByMissionAndDateTest() {

		Employe employe = new Employe("Sawsen", "MACRON", "SAWSAN.MACROM@esprit.tn", true, Role.ADMINISTRATEUR);
		controller.ajouterEmploye(employe);

		Mission mission = new Mission("Dev", "Spring junit");
		missionRepository.save(mission);
		Mission mission1 = new Mission("Dev", "Spring junit");
		missionRepository.save(mission1);

		TimesheetPK timesheetPK = new TimesheetPK(mission.getId(), employe.getId(), new Date(),
				new Date(2020 - 12 - 27));

		Timesheet timesheet = new Timesheet();

		timesheet.setTimesheetPK(timesheetPK);
		timesheet.setEmploye(employe);
		timesheet.setMission(mission);
		timesheet.setValide(true);

		List<Timesheet> timesheets = new ArrayList<Timesheet>();

		timesheets.add(timesheet);

		mission.setTimesheets(timesheets);
		mission1.setTimesheets(timesheets);

		employe.setTimesheets(timesheets);

		timesheetRepository.save(timesheet);

		java.util.List<Timesheet> timesheets_Res = controller.getTimesheetsByMissionAndDate(employe, mission,
				new Date(), new Date(2020 - 12 - 27));

		assertThat(timesheets_Res.size()).isPositive();

	}
}

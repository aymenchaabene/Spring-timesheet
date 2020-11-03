package tn.esprit.spring.services;


import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.entities.TimesheetPK;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class TimesheetServiceImpl implements ITimesheetService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimesheetServiceImpl.class);

	@Autowired
	MissionRepository missionRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;
	@Autowired
	EmployeRepository employeRepository;

	public int ajouterMission(Mission mission) {
		missionRepository.save(mission);
		return mission.getId();
	}

	public void affecterMissionADepartement(int missionId, int depId) {

		Mission mission = missionRepository.findById(missionId).orElse(null);

		Departement dep = deptRepoistory.findById(depId).orElse(null);
		if (mission != null && dep != null) {
			mission.setDepartement(dep);
			missionRepository.save(mission);

		}

	}

	public void ajouterTimesheet(int missionId, int employeId, Date dateDebut, Date dateFin) {
		TimesheetPK timesheetPK = new TimesheetPK();
		timesheetPK.setDateDebut(dateDebut);
		timesheetPK.setDateFin(dateFin);
		timesheetPK.setIdEmploye(employeId);
		timesheetPK.setIdMission(missionId);

		Timesheet timesheet = new Timesheet();
		timesheet.setTimesheetPK(timesheetPK);
		timesheet.setValide(false); // par defaut non valide
		timesheetRepository.save(timesheet);

	}

	public void validerTimesheet(int missionId, int employeId, Date dateDebut, Date dateFin, int validateurId) {
		LOGGER.info("In valider Timesheet");
		Employe validateur = employeRepository.findById(validateurId).orElse(null);
		Mission mission = missionRepository.findById(missionId).orElse(null);
		if (validateur != null && mission != null) {

			if (!validateur.getRole().equals(Role.CHEF_DEPARTEMENT)) {
				LOGGER.info("l'employe doit etre chef de departement pour valider une feuille de temps !");
				return;
			}
			boolean chefDeLaMission = false;
			for (Departement dep : validateur.getDepartements()) {
				if (dep.getId() == mission.getDepartement().getId()) {
					chefDeLaMission = true;
					break;
				}
			}
			if (!chefDeLaMission) {
				LOGGER.warn("l'employe doit etre chef de departement de la mission en question");
				return;
			}
		}

		TimesheetPK timesheetPK = new TimesheetPK(missionId, employeId, dateDebut, dateFin);
		Timesheet timesheet = timesheetRepository.findBytimesheetPK(timesheetPK);
		timesheet.setValide(true);

	}

	public List<Mission> findAllMissionByEmployeJPQL(int employeId) {
		return timesheetRepository.findAllMissionByEmployeJPQL(employeId);
	}

	public List<Employe> getAllEmployeByMission(int missionId) {
		return timesheetRepository.getAllEmployeByMission(missionId);
	}

}

package com.feuji.employeeservice.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.feuji.employeeservice.bean.EmployeeBean;
import com.feuji.employeeservice.constants.CommonConstants;
import com.feuji.employeeservice.dto.AddEmployee;
import com.feuji.employeeservice.dto.EmployeeDisplayDto;
import com.feuji.employeeservice.dto.EmployeeDto;
import com.feuji.employeeservice.dto.SaveEmployeeDto;
import com.feuji.employeeservice.dto.SaveEmployeeUserDto;
import com.feuji.employeeservice.dto.UpadteEmployeeDto;
import com.feuji.employeeservice.entity.EmployeeEntity;
import com.feuji.employeeservice.entity.UserLoginEntity;
import com.feuji.employeeservice.exception.RecordsNotFoundException;
import com.feuji.employeeservice.repository.EmployeeRepository;
import com.feuji.employeeservice.repository.UserLoginRepo;
import com.feuji.employeeservice.service.EmployeeService;
import com.feuji.employeeservice.util.EmailSender;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	public EmployeeRepository employeeRepository;

	@Autowired
	private EmailSender emailSender;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserLoginRepo userLoginRepo;

	@Override
	public EmployeeEntity saveEmployeeAndUser(SaveEmployeeUserDto employeeUserDTO) {

		EmployeeEntity employeeEntity = new EmployeeEntity();

		employeeEntity.setEmployeeCode(employeeUserDTO.getEmployeeCode());
		employeeEntity.setFirstName(employeeUserDTO.getFirstName());
		employeeEntity.setMiddleName(employeeUserDTO.getMiddleName());
		employeeEntity.setLastName(employeeUserDTO.getLastName());
		employeeEntity.setDesignation(employeeUserDTO.getDesignation());
		employeeEntity.setEmail(employeeUserDTO.getEmail());
		employeeEntity.setGender(employeeUserDTO.getGender());
		employeeEntity.setDateOfJoining(employeeUserDTO.getDateOfJoining());
		employeeEntity.setReportingManagerId(employeeUserDTO.getReportingManagerId());
		employeeEntity.setEmploymentType(employeeUserDTO.getEmploymentType());
		employeeEntity.setStatus(employeeUserDTO.getStatus());
		employeeEntity.setIsDeleted(CommonConstants.FALSE);
		employeeEntity.setDeliveryUnitId(employeeUserDTO.getDeliveryUnitId());
		employeeEntity.setBusinessUnitId(employeeUserDTO.getBusinessUnitId());

		employeeEntity = employeeRepository.save(employeeEntity);
		String password = generatePassword(employeeUserDTO.getFirstName(), employeeUserDTO.getEmployeeCode());

		UserLoginEntity userLoginEntity = new UserLoginEntity();
		userLoginEntity.setUserEmpId(employeeEntity.getEmployeeId());
		userLoginEntity.setUserName(employeeUserDTO.getFirstName());
		userLoginEntity.setUserPassword(passwordEncoder.encode(password));
		userLoginEntity.setUserEmail(employeeUserDTO.getEmail());
		userLoginEntity.setDesignation(employeeUserDTO.getDesignation());
		userLoginEntity.setEmployeeStatus(employeeUserDTO.getStatus());

		userLoginEntity = userLoginRepo.save(userLoginEntity);

		userLoginEntity.setFlag(true);
		userLoginRepo.save(userLoginEntity);

		try {
			log.info("Sending email to: " + employeeUserDTO.getEmail());
			emailSender.sendSimpleEmail(employeeUserDTO.getEmail(), "Your Password",
					"Your password is: " + password + "You can also click on forgot password to change your password");
			log.info("Email sent successfully to: " + employeeUserDTO.getEmail());
		} catch (Exception e) {
			log.error("Error sending email to: " + employeeUserDTO.getEmail(), e);
		}
		return employeeEntity;
	}

	private String generatePassword(String firstName, String employeeCode) {
		String password = employeeCode + "@123";
		return password;
	}

	@Override
	public boolean isEmailUnique(String email) {
		Optional<EmployeeEntity> employee = employeeRepository.findByEmail(email);
		return employee.isEmpty();
	}

	@Override
	public List<EmployeeEntity> getAllEmployees() {
	    log.debug("Attempting to retrieve all employees");
        try {
            List<EmployeeEntity> employees = employeeRepository.findAll();
            log.debug("Successfully retrieved {} employees", employees.size());
            return employees;
        } catch (Exception e) {
            log.error("Failed to retrieve employees", e);
           
            throw new RuntimeException("Failed to retrieve all employees", e);
        }
	}

	@Override
	public List<SaveEmployeeDto> getByReferenceTypeId(Integer referenceTypeId) {
		
		log.info("EmployeeServiceImpl.getByReferenceTypeId()  started " );
		return employeeRepository.getByReferenceTypeId(referenceTypeId);
		
	}

	@Override
	public EmployeeEntity getById(Integer id) {
		log.info("EmployeeServiceImpl.getById()  started " );
		return employeeRepository.findById(id).orElse(null);
	}

	@Override
	public EmployeeEntity getByUuid(String uuid) {
		return employeeRepository.findByUuid(uuid);
	}

	@Override
	public List<EmployeeDto> getByUserEmpId(Integer id) {
		return employeeRepository.getEmployeeDetailsByUserEmpId(id);
	}

	@Override
	public boolean isEmployeeCodeUnique(String empCode) {
		return !employeeRepository.existsByEmployeeCode(empCode);
	}

	@Override
	public EmployeeBean getReportingMngIdByEmpId(Integer employeeId) {
		
		log.info("EmployeeServiceImpl.getReportingMngIdByEmpId()  started " + employeeId);
		EmployeeEntity entity = employeeRepository.findById(employeeId).orElseThrow();
		log.info("EmployeeServiceImpl.getReportingMngIdByEmpId()  ended and fetched " + entity);
		return entityToBean(entity);
	}

	public List<AddEmployee> getAllReportingManager() {
		log.info("EmployeeServiceImpl.getAllReportingManager()  started " );
		List<AddEmployee> employees = employeeRepository.findDesignationsContainingManager();
		log.info("EmployeeServiceImpl.getAllReportingManager()  ended and fetched  employees"+employees );
		return employees;
	}

	@Override
	public List<EmployeeEntity> searchEmployeesByFirstName(String firstName) {
		return employeeRepository.findByFirstNameContainingIgnoreCase(firstName);
	}

	public EmployeeBean entityToBean(EmployeeEntity entity) {
		EmployeeBean employeeBean = new EmployeeBean();

		employeeBean.setEmployeeId(entity.getEmployeeId());
		employeeBean.setEmployeeCode(entity.getEmployeeCode());
		employeeBean.setFirstName(entity.getFirstName());
		employeeBean.setMiddleName(entity.getMiddleName());
		employeeBean.setLastName(entity.getLastName());
		employeeBean.setDesignation(entity.getDesignation());
		employeeBean.setEmail(entity.getEmail());
		employeeBean.setGender(entity.getGender());
		employeeBean.setDateOfJoining(entity.getDateOfJoining());
		employeeBean.setReportingManagerId(entity.getReportingManagerId());
		employeeBean.setEmploymentType(entity.getEmploymentType());
		employeeBean.setStatus(entity.getStatus());
		employeeBean.setDeliveryUnitId(entity.getDeliveryUnitId());
		employeeBean.setBusinessUnitId(entity.getBusinessUnitId());
		employeeBean.setExitDate(entity.getExitDate());
		employeeBean.setExitRemarks(entity.getExitRemarks());
		employeeBean.setIsDeleted(entity.getIsDeleted());
		employeeBean.setUuid(entity.getUuid());
		employeeBean.setCreatedBy(entity.getCreatedBy());
		employeeBean.setCreatedOn(entity.getCreatedOn());
		employeeBean.setModifiedBy(entity.getModifiedBy());
		employeeBean.setModifiedOn(entity.getModifiedOn());

		return employeeBean;
	}

	public EmployeeEntity beanToEntity(EmployeeBean employeeBean) {
		EmployeeEntity entity = new EmployeeEntity();

		entity.setEmployeeId(employeeBean.getEmployeeId());
		entity.setEmployeeCode(employeeBean.getEmployeeCode());
		entity.setFirstName(employeeBean.getFirstName());
		entity.setMiddleName(employeeBean.getMiddleName());
		entity.setLastName(employeeBean.getLastName());
		entity.setDesignation(employeeBean.getDesignation());
		entity.setEmail(employeeBean.getEmail());
		entity.setGender(employeeBean.getGender());
		entity.setDateOfJoining(employeeBean.getDateOfJoining());
		entity.setReportingManagerId(employeeBean.getReportingManagerId());
		entity.setEmploymentType(employeeBean.getEmploymentType());
		entity.setStatus(employeeBean.getStatus());
		entity.setDeliveryUnitId(employeeBean.getDeliveryUnitId());
		entity.setBusinessUnitId(employeeBean.getBusinessUnitId());
		entity.setExitDate(employeeBean.getExitDate());
		entity.setExitRemarks(employeeBean.getExitRemarks());
		entity.setIsDeleted(employeeBean.getIsDeleted());
		entity.setUuid(employeeBean.getUuid());
		entity.setCreatedBy(employeeBean.getCreatedBy());
		entity.setCreatedOn(employeeBean.getCreatedOn());
		entity.setModifiedBy(employeeBean.getModifiedBy());
		entity.setModifiedOn(employeeBean.getModifiedOn());

		return entity;
	}

	@Override
	public void updateEmployeeDetails(EmployeeEntity updateEmployee, Integer id) throws Throwable {
		EmployeeEntity existingEmployee = employeeRepository.findById(id)
				.orElseThrow(() -> new Exception("Employee not found with id: " + id));

		existingEmployee.setFirstName(updateEmployee.getFirstName());
		existingEmployee.setMiddleName(updateEmployee.getMiddleName());
		existingEmployee.setLastName(updateEmployee.getLastName());
		existingEmployee.setDesignation(updateEmployee.getDesignation());
		existingEmployee.setEmail(updateEmployee.getEmail());
		existingEmployee.setGender(updateEmployee.getGender());
		existingEmployee.setDateOfJoining(updateEmployee.getDateOfJoining());
		existingEmployee.setReportingManagerId(updateEmployee.getReportingManagerId());
		existingEmployee.setEmploymentType(updateEmployee.getEmploymentType());
		existingEmployee.setStatus(updateEmployee.getStatus());
		existingEmployee.setDeliveryUnitId(updateEmployee.getDeliveryUnitId());
		existingEmployee.setBusinessUnitId(updateEmployee.getBusinessUnitId());
		existingEmployee.setExitDate(updateEmployee.getExitDate());
		existingEmployee.setExitRemarks(updateEmployee.getExitRemarks());
		existingEmployee.setIsDeleted(updateEmployee.getIsDeleted());
		existingEmployee.setUuid(updateEmployee.getUuid());
		existingEmployee.setCreatedBy(updateEmployee.getCreatedBy());
		existingEmployee.setCreatedOn(updateEmployee.getCreatedOn());
		existingEmployee.setModifiedBy(updateEmployee.getModifiedBy());
		existingEmployee.setModifiedOn(updateEmployee.getModifiedOn());

		employeeRepository.save(existingEmployee);
	}

	@Override
	public List<EmployeeDisplayDto> getEmployeeDetails() {
		try {
			log.info("EmployeeServiceImpl.getEmployeeDetails()  started " );

			List<EmployeeDisplayDto> empdetails = employeeRepository.getEmployeeDetails();
			log.info("EmployeeServiceImpl.getEmployeeDetails()  retrived and ended " );
			return empdetails;
		} catch (Exception e) {
			log.info(e.getMessage());

		}
		return null;
	}

	@Override
	public List<UpadteEmployeeDto> getEmployeeDetailByUUiD(String uuid) {
		try {
			log.info("EmployeeServiceImpl.getEmployeeDetailByUUiD1()  started " + uuid);
			List<UpadteEmployeeDto> empdetails = employeeRepository.getEmployeeDetailByUUiD(uuid);

			log.info("EmployeeServiceImpl.getEmployeeDetailByUUiD1()  fetched Employee details" + empdetails);

			return empdetails;
		} catch (Exception e) {
			log.info("EmployeeServiceImpl.getEmployeeDetailByUUiD1()  catch block" + e.getMessage());

		}
		return null;
	}

	@Override
	public EmployeeEntity updateEmployee(EmployeeBean employeeBean) {
		log.info("EMployeeServiceimpl.updateEmployee()  started {}", employeeBean);
		EmployeeEntity accountEntity1 = beanToEntity(employeeBean);
		if (employeeBean == null) {
			throw new IllegalArgumentException("Account bean object is null");
		}

		EmployeeEntity savedEntity = employeeRepository.save(accountEntity1);
		log.info("EMployeeServiceimpl.updateEmployee()  ended and fetched {}", savedEntity);
		return savedEntity;

	}

	@Override
	public EmployeeEntity delete(Integer employeeId) {
		log.info("EMployeeServiceimpl.delete()  started {}", employeeId);
		EmployeeEntity optional = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new IllegalArgumentException("id not found"));
		optional.setIsDeleted(CommonConstants.TRUE);
		EmployeeBean entityToBean = entityToBean(optional);
		log.info("EMployeeServiceimpl.delete()  deleted {}", entityToBean);
		EmployeeEntity deletedEmployee = updateEmployee(entityToBean);
		log.info("EMployeeServiceimpl.delete()  ended {}", entityToBean);

		return deletedEmployee;

	}

	@Override
	public EmployeeBean findByEmail(String employeeEmail) throws RecordsNotFoundException {
		log.info("Service findByEmail Method Start");

		Optional<EmployeeEntity> optionalEntity = employeeRepository.findByEmail(employeeEmail);
		if (optionalEntity.isPresent()) {
			EmployeeEntity entity = optionalEntity.get();
			log.info("Service findByEmail Method End");
			return entityToBean(entity);
		} else {
			throw new RecordsNotFoundException("employee not found with this email :" + employeeEmail);
		}
	}

}

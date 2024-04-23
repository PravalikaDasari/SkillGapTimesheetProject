package com.feuji.timesheetentryservice.serviceimpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feuji.timesheetentryservice.constants.CommonConstants;
import com.feuji.timesheetentryservice.controller.HolidayController;
import com.feuji.timesheetentryservice.entity.HolidayEntity;
import com.feuji.timesheetentryservice.exception.HolidayDateExistsException;
import com.feuji.timesheetentryservice.exception.HolidayNameAndHolidayDateExistException;
import com.feuji.timesheetentryservice.exception.HolidayNameExistException;
import com.feuji.timesheetentryservice.exception.HolidayNotFoundException;
import com.feuji.timesheetentryservice.repository.HolidayRepository;
import com.feuji.timesheetentryservice.service.HolidayService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class HolidayServiceImpl implements HolidayService {
	private static Logger log = LoggerFactory.getLogger(HolidayController.class);
	@Autowired
	private HolidayRepository holidayRepository;

	@Override
	public void save(HolidayEntity holidayEntity) {
		if (holidayEntity != null) {
			List<HolidayEntity> holidayDetails = holidayRepository
					.findByHolidayNameOrHolidayDate(holidayEntity.getHolidayName(), holidayEntity.getHolidayDate());
			Optional<HolidayEntity> holiday = holidayDetails.stream().findFirst();
			if (holidayDetails.size() > 1) {
				throw new HolidayNameAndHolidayDateExistException();
			} else if (holiday.isPresent()) {
				if (holiday.get().getHolidayName().equalsIgnoreCase(holidayEntity.getHolidayName())) {
					throw new HolidayNameExistException("Holiday name is exists");
				}

				if (holiday.get().getHolidayDate().equals(holidayEntity.getHolidayDate())) {
					throw new HolidayDateExistsException("Holiday date is exists");
				}
			} else {
				holidayRepository.save(holidayEntity);
				log.info("Holiday details are save", holidayEntity);
			}

		} else {
			throw new HolidayNotFoundException("Holiday details Not found");
		}
	}

	@Override
	public List<HolidayEntity> getAll() {
		return holidayRepository.findAll().stream().filter(holiday -> (holiday.getIsDeleted()==CommonConstants.FALSE))
				.collect(Collectors.toList());
	}

	@Override
	public List<Integer> getWeekHolidaysDayIds(String startweekofDate) {

		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		LocalDate startDate = LocalDate.parse(startweekofDate, formatter1);

		LocalDate endDate = startDate.plusDays(6);

		List<HolidayEntity> holdayList = holidayRepository.findAll().stream()
				.filter(holiday -> holiday.getHolidayDate().isAfter(startDate.minusDays(1))
						&& holiday.getHolidayDate().isBefore(endDate.plusDays(1)))
				.collect(Collectors.toList());

		List<Integer> weekHolidaysDayIdList = new ArrayList<>();
		for (HolidayEntity holidayEntity : holdayList) {

			LocalDate holidayDate = holidayEntity.getHolidayDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

			int dayOfWeekValue = holidayDate.getDayOfWeek().getValue() - 1;
			weekHolidaysDayIdList.add(dayOfWeekValue);

		}

		return weekHolidaysDayIdList;
	}

	@Override
	public void update(HolidayEntity holidayEntity) throws HolidayNotFoundException {
		Optional<HolidayEntity> optional = holidayRepository.findById(holidayEntity.getHolidayId());
		if (optional.isPresent()) {
			holidayRepository.save(holidayEntity);

		} else {
			throw new HolidayNotFoundException("HolidayNotFoundExceptionbyholidayId-" + holidayEntity.getHolidayId());
		}
	}

	@Override
	public HolidayEntity get(Integer holidayId) {

		return holidayRepository.findById(holidayId).get();
	}

	@Override
	public HolidayEntity delete(Integer holidayId) {
		log.info("service method{}", holidayId);
		HolidayEntity optional = holidayRepository.findById(holidayId)
				.orElseThrow(() -> new IllegalArgumentException("id not found"));
		optional.setIsDeleted(CommonConstants.TRUE);
		update(optional);

		return optional;
	}

	@Override
	public List<HolidayEntity> getHolidayByYear(int year) {
		List<HolidayEntity> holidayList = holidayRepository.findHolidaysByYear(year).stream()
				.filter(holiday -> (holiday.getIsDeleted()==CommonConstants.FALSE)).collect(Collectors.toList());
		;
		if (holidayList != null) {
			return holidayList;

		} else {
			return null;
		}
	}

}
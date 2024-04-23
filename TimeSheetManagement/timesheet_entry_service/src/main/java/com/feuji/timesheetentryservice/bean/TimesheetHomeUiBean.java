package com.feuji.timesheetentryservice.bean;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TimesheetHomeUiBean {
	private Integer timesheetWeekId;
	private Integer accountId;
	private Integer accountProjectId;
	private Integer employeeId;
	private Integer weekNumber;
	@JsonFormat(pattern = "dd-MMM-yyyy")
	private Date weekStartDate;
	@JsonFormat(pattern = "dd-MMM-yyyy")
	private Date weekEndDate;
	private String comments;

	private Integer timesheetStatusId;
	private Integer approvedBy;
	private Boolean isactive;
	private Boolean isDeleted;
	private String uuid;

	private String createdBy;
	@JsonFormat(pattern = "dd-MMM-yyyy")
	private Date createdOn;
	private String modifiedBy;
	@JsonFormat(pattern = "dd-MMM-yyyy")
	private Date modifiedOn;


}

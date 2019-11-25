package com.example.mtpv.eticketcourt.Pojos;

import java.io.Serializable;

public class CourtCaseDisposal implements Serializable{
	private String eticketNo,regnNo,pidCD,pidName,releaseDT,challanType,violations,unitCode,psCode,officerPid,offenceDate,vehRelease,ddRemarks,minorFlag;

	private String driver_dlNO,driver_dlDob,driver_aadhaarNO,driver_mobileNo,driver_stcNO,driver_courtCD,driver_courtDispCD,driver_imprisFrom,driver_imprisTo,driver_imprisDays,driver_courtFine,driver_risingDetails,driver_coutrAttnDT,driver_dlRelease,
	driver_dlSusp,driver_dlCancel,driver_SuspensionFromDate,driver_SuspensionToDate,driver_noOfDaysSuspended,driver_imgMegistrateCopy,driver_imgDlCopy;

	private String owner_COURT_FINE,owner_CONVICTION_DETAILS,owner_CONVICTION_FROM_DT,owner_CONVICTION_TO_DT,owner_RISING_DETAILS,owner_COURT_CASE_NO,
	owner_COURT_CD,owner_DISPOSAL_CD,owner_COURT_PC,owner_D_OF_PAY,owner_LICENSE_NO,owner_CONTACT_NO;

	
	public String getMinorFlag() {
		return minorFlag;
	}

	public void setMinorFlag(String minorFlag) {
		this.minorFlag = minorFlag;
	}

	public String getEticketNo() {
		return eticketNo;
	}

	public void setEticketNo(String eticketNo) {
		this.eticketNo = eticketNo;
	}

	public String getRegnNo() {
		return regnNo;
	}

	public void setRegnNo(String regnNo) {
		this.regnNo = regnNo;
	}

	public String getPidCD() {
		return pidCD;
	}

	public void setPidCD(String pidCD) {
		this.pidCD = pidCD;
	}

	public String getPidName() {
		return pidName;
	}

	public void setPidName(String pidName) {
		this.pidName = pidName;
	}

	public String getReleaseDT() {
		return releaseDT;
	}

	public void setReleaseDT(String releaseDT) {
		this.releaseDT = releaseDT;
	}

	public String getChallanType() {
		return challanType;
	}

	public void setChallanType(String challanType) {
		this.challanType = challanType;
	}

	public String getViolations() {
		return violations;
	}

	public void setViolations(String violations) {
		this.violations = violations;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getPsCode() {
		return psCode;
	}

	public void setPsCode(String psCode) {
		this.psCode = psCode;
	}

	public String getOfficerPid() {
		return officerPid;
	}

	public void setOfficerPid(String officerPid) {
		this.officerPid = officerPid;
	}

	public String getOffenceDate() {
		return offenceDate;
	}

	public void setOffenceDate(String offenceDate) {
		this.offenceDate = offenceDate;
	}

	public String getVehRelease() {
		return vehRelease;
	}

	public void setVehRelease(String vehRelease) {
		this.vehRelease = vehRelease;
	}

	public String getDdRemarks() {
		return ddRemarks;
	}

	public void setDdRemarks(String ddRemarks) {
		this.ddRemarks = ddRemarks;
	}

	public String getDriver_dlNO() {
		return driver_dlNO;
	}

	public void setDriver_dlNO(String driver_dlNO) {
		this.driver_dlNO = driver_dlNO;
	}

	public String getDriver_dlDob() {
		return driver_dlDob;
	}

	public void setDriver_dlDob(String driver_dlDob) {
		this.driver_dlDob = driver_dlDob;
	}

	public String getDriver_aadhaarNO() {
		return driver_aadhaarNO;
	}

	public void setDriver_aadhaarNO(String driver_aadhaarNO) {
		this.driver_aadhaarNO = driver_aadhaarNO;
	}

	public String getDriver_mobileNo() {
		return driver_mobileNo;
	}

	public void setDriver_mobileNo(String driver_mobileNo) {
		this.driver_mobileNo = driver_mobileNo;
	}

	public String getDriver_stcNO() {
		return driver_stcNO;
	}

	public void setDriver_stcNO(String driver_stcNO) {
		this.driver_stcNO = driver_stcNO;
	}

	public String getDriver_courtCD() {
		return driver_courtCD;
	}

	public void setDriver_courtCD(String driver_courtCD) {
		this.driver_courtCD = driver_courtCD;
	}

	public String getDriver_courtDispCD() {
		return driver_courtDispCD;
	}

	public void setDriver_courtDispCD(String driver_courtDispCD) {
		this.driver_courtDispCD = driver_courtDispCD;
	}

	public String getDriver_imprisFrom() {
		return driver_imprisFrom;
	}

	public void setDriver_imprisFrom(String driver_imprisFrom) {
		this.driver_imprisFrom = driver_imprisFrom;
	}

	public String getDriver_imprisTo() {
		return driver_imprisTo;
	}

	public void setDriver_imprisTo(String driver_imprisTo) {
		this.driver_imprisTo = driver_imprisTo;
	}

	public String getDriver_imprisDays() {
		return driver_imprisDays;
	}

	public void setDriver_imprisDays(String driver_imprisDays) {
		this.driver_imprisDays = driver_imprisDays;
	}

	public String getDriver_courtFine() {
		return driver_courtFine;
	}

	public void setDriver_courtFine(String driver_courtFine) {
		this.driver_courtFine = driver_courtFine;
	}

	public String getDriver_risingDetails() {
		return driver_risingDetails;
	}

	public void setDriver_risingDetails(String driver_risingDetails) {
		this.driver_risingDetails = driver_risingDetails;
	}

	public String getDriver_coutrAttnDT() {
		return driver_coutrAttnDT;
	}

	public void setDriver_coutrAttnDT(String driver_coutrAttnDT) {
		this.driver_coutrAttnDT = driver_coutrAttnDT;
	}

	public String getDriver_dlRelease() {
		return driver_dlRelease;
	}

	public void setDriver_dlRelease(String driver_dlRelease) {
		this.driver_dlRelease = driver_dlRelease;
	}

	public String getDriver_dlSusp() {
		return driver_dlSusp;
	}

	public void setDriver_dlSusp(String driver_dlSusp) {
		this.driver_dlSusp = driver_dlSusp;
	}

	public String getDriver_dlCancel() {
		return driver_dlCancel;
	}

	public void setDriver_dlCancel(String driver_dlCancel) {
		this.driver_dlCancel = driver_dlCancel;
	}

	public String getDriver_SuspensionFromDate() {
		return driver_SuspensionFromDate;
	}

	public void setDriver_SuspensionFromDate(String driver_SuspensionFromDate) {
		this.driver_SuspensionFromDate = driver_SuspensionFromDate;
	}

	public String getDriver_SuspensionToDate() {
		return driver_SuspensionToDate;
	}

	public void setDriver_SuspensionToDate(String driver_SuspensionToDate) {
		this.driver_SuspensionToDate = driver_SuspensionToDate;
	}

	public String getDriver_noOfDaysSuspended() {
		return driver_noOfDaysSuspended;
	}

	public void setDriver_noOfDaysSuspended(String driver_noOfDaysSuspended) {
		this.driver_noOfDaysSuspended = driver_noOfDaysSuspended;
	}

	public String getDriver_imgMegistrateCopy() {
		return driver_imgMegistrateCopy;
	}

	public void setDriver_imgMegistrateCopy(String driver_imgMegistrateCopy) {
		this.driver_imgMegistrateCopy = driver_imgMegistrateCopy;
	}

	public String getDriver_imgDlCopy() {
		return driver_imgDlCopy;
	}

	public void setDriver_imgDlCopy(String driver_imgDlCopy) {
		this.driver_imgDlCopy = driver_imgDlCopy;
	}

	public String getOwner_COURT_FINE() {
		return owner_COURT_FINE;
	}

	public void setOwner_COURT_FINE(String owner_COURT_FINE) {
		this.owner_COURT_FINE = owner_COURT_FINE;
	}

	public String getOwner_CONVICTION_DETAILS() {
		return owner_CONVICTION_DETAILS;
	}

	public void setOwner_CONVICTION_DETAILS(String owner_CONVICTION_DETAILS) {
		this.owner_CONVICTION_DETAILS = owner_CONVICTION_DETAILS;
	}

	public String getOwner_CONVICTION_FROM_DT() {
		return owner_CONVICTION_FROM_DT;
	}

	public void setOwner_CONVICTION_FROM_DT(String owner_CONVICTION_FROM_DT) {
		this.owner_CONVICTION_FROM_DT = owner_CONVICTION_FROM_DT;
	}

	public String getOwner_CONVICTION_TO_DT() {
		return owner_CONVICTION_TO_DT;
	}

	public void setOwner_CONVICTION_TO_DT(String owner_CONVICTION_TO_DT) {
		this.owner_CONVICTION_TO_DT = owner_CONVICTION_TO_DT;
	}

	public String getOwner_RISING_DETAILS() {
		return owner_RISING_DETAILS;
	}

	public void setOwner_RISING_DETAILS(String owner_RISING_DETAILS) {
		this.owner_RISING_DETAILS = owner_RISING_DETAILS;
	}

	public String getOwner_COURT_CASE_NO() {
		return owner_COURT_CASE_NO;
	}

	public void setOwner_COURT_CASE_NO(String owner_COURT_CASE_NO) {
		this.owner_COURT_CASE_NO = owner_COURT_CASE_NO;
	}

	public String getOwner_COURT_CD() {
		return owner_COURT_CD;
	}

	public void setOwner_COURT_CD(String owner_COURT_CD) {
		this.owner_COURT_CD = owner_COURT_CD;
	}

	public String getOwner_DISPOSAL_CD() {
		return owner_DISPOSAL_CD;
	}

	public void setOwner_DISPOSAL_CD(String owner_DISPOSAL_CD) {
		this.owner_DISPOSAL_CD = owner_DISPOSAL_CD;
	}

	public String getOwner_COURT_PC() {
		return owner_COURT_PC;
	}

	public void setOwner_COURT_PC(String owner_COURT_PC) {
		this.owner_COURT_PC = owner_COURT_PC;
	}

	public String getOwner_D_OF_PAY() {
		return owner_D_OF_PAY;
	}

	public void setOwner_D_OF_PAY(String owner_D_OF_PAY) {
		this.owner_D_OF_PAY = owner_D_OF_PAY;
	}

	public String getOwner_LICENSE_NO() {
		return owner_LICENSE_NO;
	}

	public void setOwner_LICENSE_NO(String owner_LICENSE_NO) {
		this.owner_LICENSE_NO = owner_LICENSE_NO;
	}

	public String getOwner_CONTACT_NO() {
		return owner_CONTACT_NO;
	}

	public void setOwner_CONTACT_NO(String owner_CONTACT_NO) {
		this.owner_CONTACT_NO = owner_CONTACT_NO;
	}
	
	
	
}

package com.bd.data.model;

public class Metric {

	private String scrumTeam;
	private int unitTestBackEnd;
	private int unitTestFrontEnd;
	private int nDependCritical;
	private int nDependNonCritical;
	private int checkMarxHigh;
	private int checkMarxMed;
	private int checkMarxLow;
	private int fodCritical;
	private int fodHigh;
	private int fodMed;
	private int fodLow;
	private String manualCodeReviews;
	private String automationStats;
	private String comments;

	public String getScrumTeam() {
		return scrumTeam;
	}

	public void setScrumTeam(String scrumTeam) {
		this.scrumTeam = scrumTeam;
	}

	public int getUnitTestBackEnd() {
		return unitTestBackEnd;
	}

	public void setUnitTestBackEnd(int unitTestBackEnd) {
		this.unitTestBackEnd = unitTestBackEnd;
	}

	public int getUnitTestFrontEnd() {
		return unitTestFrontEnd;
	}

	public void setUnitTestFrontEnd(int unitTestFrontEnd) {
		this.unitTestFrontEnd = unitTestFrontEnd;
	}

	public int getnDependCritical() {
		return nDependCritical;
	}

	public void setnDependCritical(int nDependCritical) {
		this.nDependCritical = nDependCritical;
	}

	public int getnDependNonCritical() {
		return nDependNonCritical;
	}

	public void setnDependNonCritical(int nDependNonCritical) {
		this.nDependNonCritical = nDependNonCritical;
	}

	public int getCheckMarxHigh() {
		return checkMarxHigh;
	}

	public void setCheckMarxHigh(int checkMarxHigh) {
		this.checkMarxHigh = checkMarxHigh;
	}

	public int getCheckMarxMed() {
		return checkMarxMed;
	}

	public void setCheckMarxMed(int checkMarxMed) {
		this.checkMarxMed = checkMarxMed;
	}

	public int getCheckMarxLow() {
		return checkMarxLow;
	}

	public void setCheckMarxLow(int checkMarxLow) {
		this.checkMarxLow = checkMarxLow;
	}

	public int getFodCritical() {
		return fodCritical;
	}

	public void setFodCritical(int fodCritical) {
		this.fodCritical = fodCritical;
	}

	public int getFodHigh() {
		return fodHigh;
	}

	public void setFodHigh(int fodHigh) {
		this.fodHigh = fodHigh;
	}

	public int getFodMed() {
		return fodMed;
	}

	public void setFodMed(int fodMed) {
		this.fodMed = fodMed;
	}

	public int getFodLow() {
		return fodLow;
	}

	public void setFodLow(int fodLow) {
		this.fodLow = fodLow;
	}

	public String getManualCodeReviews() {
		return manualCodeReviews;
	}

	public void setManualCodeReviews(String manualCodeReviews) {
		this.manualCodeReviews = manualCodeReviews;
	}

	public String getAutomationStats() {
		return automationStats;
	}

	public void setAutomationStats(String automationStats) {
		this.automationStats = automationStats;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Metric(String scrumTeam, int unitTestBackEnd, int unitTestFrontEnd, int nDependCritical,
			int nDependNonCritical, int checkMarxHigh, int checkMarxMed, int checkMarxLow, int fodCritical, int fodHigh,
			int fodMed, int fodLow, String manualCodeReviews, String automationStats, String comments) {
		super();
		this.scrumTeam = scrumTeam;
		this.unitTestBackEnd = unitTestBackEnd;
		this.unitTestFrontEnd = unitTestFrontEnd;
		this.nDependCritical = nDependCritical;
		this.nDependNonCritical = nDependNonCritical;
		this.checkMarxHigh = checkMarxHigh;
		this.checkMarxMed = checkMarxMed;
		this.checkMarxLow = checkMarxLow;
		this.fodCritical = fodCritical;
		this.fodHigh = fodHigh;
		this.fodMed = fodMed;
		this.fodLow = fodLow;
		this.manualCodeReviews = manualCodeReviews;
		this.automationStats = automationStats;
		this.comments = comments;
	}
}

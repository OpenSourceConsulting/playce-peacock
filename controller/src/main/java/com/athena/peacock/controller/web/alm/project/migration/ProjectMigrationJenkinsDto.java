package com.athena.peacock.controller.web.alm.project.migration;

public class ProjectMigrationJenkinsDto {

	private String PROJECT_CODE;
	private String MAPPING_CODE;
	private String CHECKFLAG;

	public String getPROJECT_CODE() {
		return PROJECT_CODE;
	}

	public void setPROJECT_CODE(String pROJECT_CODE) {
		PROJECT_CODE = pROJECT_CODE;
	}

	public String getMAPPING_CODE() {
		return MAPPING_CODE;
	}

	public void setMAPPING_CODE(String mAPPING_CODE) {
		MAPPING_CODE = mAPPING_CODE;
	}

	public String getCHECKFLAG() {
		return CHECKFLAG;
	}

	public void setCHECKFLAG(String cHECKFLAG) {
		CHECKFLAG = cHECKFLAG;
	}

}

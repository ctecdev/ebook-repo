package rs.ac.uns.ftn.esd.ctecdev.search.model;

public final class QueryAttribute {
	
	private String fieldName;
	private String kw;  
	private String st;
	private String occ;
	
	
	public QueryAttribute() {
		super();
	}

	public QueryAttribute(String fieldName, String kw, String st, String occ) {
		super();
		this.fieldName = fieldName;
		this.kw = kw;
		this.st = st;
		this.occ = occ;
	}

	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getKw() {
		return kw;
	}

	public void setKw(String kw) {
		this.kw = kw;
	}

	public String getSt() {
		return st;
	}

	public void setSt(String st) {
		this.st = st;
	}

	public String getOcc() {
		return occ;
	}

	public void setOcc(String occ) {
		this.occ = occ;
	}



}

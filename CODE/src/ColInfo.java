public class ColInfo {
	public String nomCol; 
	public String typeCol; 
	
	public ColInfo(String nom, String type) {
		nomCol= nom; 
		typeCol = type; 
	}
	
	public ColInfo() {
		this("","");
	}
	
	public String getTypeCol(String nomCol) {
		if(this.nomCol.equals(nomCol)) {
			return this.typeCol; 
		}
		return null; 
	}
	
	public String toString() {
		return "nom col:"+ nomCol +"type col :"+typeCol; 
	}
}

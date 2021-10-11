
public class ColInfo {
	public String nomCol; 
	public String typeCol; 
	
	public ColInfo(String nom, String type) {
		nomCol= nom; 
		typeCol = type; //On doit s'assurer que le type est soit int, soit float soit StringT (str de taille fixe)
		//exception? assertion? 
	}
}

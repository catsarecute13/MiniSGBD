import java.util.ArrayList; 
public class RelationInfo {
	public String nomRelation; 
	public int nbColonnes; 
	public ArrayList<ColInfo> infoCol; 
	
	public RelationInfo(String nom, int nb, ArrayList<ColInfo> tab) {
		nomRelation = nom; 
		nbColonnes = nb; 
		infoCol = new ArrayList<ColInfo>(tab); 
	}
}

import java.util.ArrayList; 
public class Catalog {
	private static Catalog catalog = new Catalog(); 
	public ArrayList<RelationInfo> relationTab; 
	public int compteur; 
	
	public Catalog() {
	}
	
	public void Init() {
		relationTab = new ArrayList<RelationInfo>(0); 
		compteur = 0; //compteur = relationTab.size(); 
	}
	
	public void Finish() {
		relationTab.clear();
		compteur = 0; 
	}
	
	public void AddRelation(RelationInfo relation) {
		if (!relationTab.contains(relation)){
			relationTab.add(relation);
			compteur += 1; 
			
		}
		
	}
	
	static public Catalog getCatalog() {
		return catalog;}
}

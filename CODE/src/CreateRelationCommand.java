import java.util.ArrayList;
import java.util.StringTokenizer;

public class CreateRelationCommand {

	public String nomRelation; 
	public int nbColonnes; 
	public ArrayList<String> nomColonne;
	public ArrayList<String> typeColonne; 
	
	
	//constructeur
	public CreateRelationCommand(String Chaine) {
		
		nomColonne= new ArrayList<String>();
		typeColonne= new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(Chaine, " ");
		st.nextToken(); //CREATE
		st.nextToken(); //TABLE
		nomRelation=st.nextToken(); //nom de la relation
		String ch= st.nextToken();  //on prend la parenthèse entière
		StringBuffer buff = new StringBuffer(ch);
		buff.deleteCharAt(0);  //on enlève parenthèse debut
		buff.deleteCharAt(buff.length()-1); //on enleve la parenthese de fin
		
		st = new StringTokenizer(buff.toString(), ",");
		
		ArrayList<String> liste = new ArrayList<String>();
		
		while(st.hasMoreElements()) {
			liste.add(st.nextToken());
		}
		
		for(String c: liste) {
			st = new StringTokenizer(c.toString(), ":");
			nomColonne.add(st.nextToken());
			typeColonne.add(st.nextToken());
		}
	}
	
	
	//methode
	public void Execute() throws Exception {
		PageId pageId= FileManager.getFileManager().createHeaderPage();
		ColInfo colinf= new ColInfo();
		ArrayList<ColInfo> tab= new ArrayList<ColInfo>();
		
		for(int i=0; i<nomColonne.size(); i++) {
			colinf.nomCol= nomColonne.get(i);
			colinf.typeCol= typeColonne.get(i);
			tab.add(colinf);
		}
		nbColonnes=tab.size();
		RelationInfo rel =new RelationInfo(nomRelation, nbColonnes,  tab, pageId);
		Catalog.getCatalog().AddRelation(rel);
	}
	
	
}

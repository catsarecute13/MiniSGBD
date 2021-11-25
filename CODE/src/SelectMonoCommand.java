import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.HashMap; 
import java.util.ArrayList; 
import java.util.Arrays; 

public class SelectMonoCommand {
//	public ArrayList<String> conditions; //HashMap nomCol,op valeur ? 
	public HashMap<String[], Object> conditions; // redefinir equals! [nomCol, op], Valeur
	public ArrayList<String> op; 
	public String nomRelation; 
	public RelationInfo relation; 
	public Record record;
	public boolean verifWhere; 
	public ArrayList<Record> upd;

	public SelectMonoCommand(String ch) {
		//conditions = new ArrayList<String>(); 
		conditions = new HashMap<String[],Object>(); 
		StringTokenizer st = new StringTokenizer(ch); 
		//ArrayList<String> op = new ArrayList<String>(); 
		st.nextToken(); //SELECTMONO 
		st.nextToken(); 
		st.nextToken(); 
		nomRelation = st.nextToken(); 
		relation = Catalog.getCatalog().getRelation(nomRelation); 
		String [] nomOp = new String[2];
		if(verifWhere = st.hasMoreTokens()) {
			while(st.hasMoreTokens()) {
				String s= st.nextToken("AND").trim(); 
				if(s.contains("="))
						nomOp[1]="=";  
					else if(s.contains(">"))
						nomOp[1]=">"; 
					else if(s.contains("<"))
						nomOp[1]="<"; 
					else if(s.contains(">="))
						nomOp[1]="<="; 
					else if(s.contains("<>"))
						nomOp[1]="<>"; 

				//conditions.add(st.nextToken("AND").trim());
				String [] tmp = s.split("=|<=|>=|<>|<|>"); //nomCol, valeur
				nomOp[0]=tmp[0]; //On met le nom de la colonne dans nomOp
				String typeCol =null; 
				for(ColInfo c : relation.infoCol) {
					while(typeCol ==null) {//Tant qu'on a pas trouve la colonne
						typeCol = c.getTypeCol(tmp[0]);}
				}
				switch(typeCol) { 
				case "Integer": 
					conditions.put(nomOp, Integer.valueOf(tmp[1])); 
					break; 
				case "Float": 
					conditions.put(nomOp, Float.valueOf(tmp[1])); 
					break; 
				default: 
					conditions.put(nomOp, tmp[1]); 
				}
			}
		}
	}
	
	
	public void Execute() {
		ArrayList<Record> res =new ArrayList<Record>(Arrays.asList(FileManager.getFileManager().getAllRecords(relation))); 
		if(!verifWhere) {
			for(Record r: res) {
				System.out.println(r);
			}
			System.out.println("Total record :"+res.size());
		}
		else {
			//verifier criteres des condition <1 ou >20 
			int nbConditions = conditions.keySet().size() ; 
			if(nbConditions <1 || nbConditions>20 ) {
				System.out.println("Veuillez entrer un nombre de conditions entre 1 et 20 (inclus)!"); 
				return; 
			}
				/**
				 * Pour chaque record on verifie toutes les conditions
				 * 		Si une condition n'est pas satisfaite, on supprime le record des resultats; 
				 * 			
				 * */
			for(Record r : res) {
				for(String [] k : conditions.keySet()) {
					int i =relation.getIdxInfoCol(k[0]); 
					int retourCompare = compareTo(r.values[i],conditions.get(k)); 
					switch(k[1]) { //k[1] est l'operateur //Vu qu'on utilise compareTo on peut imaginer donner le resultat avec des AND
					case ">": // Faire selon le type 
						if(retourCompare <=0){
							res.remove(r); 
							break;}
					case ">=": 
						if(retourCompare<0) {
							res.remove(r); 
							break;}
					case "<": 
						if(retourCompare>= 0){
							res.remove(r); 
							break;}
					case "<=": 
						if(retourCompare >0) {
							res.remove(r);
							break; 
						}
					case "<>": 
						if(retourCompare ==0) {
							res.remove(r); 
							break; 
						}
					case "=": 
						if(retourCompare != 0) {
							res.remove(r); 
							break; 
						}
					}
					
				}
			}
		}
	}
	
	private int compareTo(Object obj1, Object obj2) {
		if(obj1 instanceof Integer) {
			((Integer)obj1).compareTo((Integer)obj2); 
		}
		else if (obj1 instanceof Float ) {
			((Float)obj1).compareTo((Float)obj2); 
		}
		else {
			((String)obj1).compareTo((String)obj2); 
		}
		return 0; 
	}


	public void Update() {
		upd=new ArrayList<Record>(Arrays.asList(FileManager.getFileManager().getAllRecords(relation)));
		//verifier criteres des condition <1 ou >20 
		int nbConditions = conditions.keySet().size() ; 
			if(nbConditions <1 || nbConditions>20 ) {
				System.out.println("Veuillez entrer un nombre de conditions entre 1 et 20 (inclus)!"); 
				return ; 
			}
			/**
			 * Pour chaque record on verifie toutes les conditions
			 * 		Si une condition n'est pas satisfaite, on supprime le record des resultats; 
			 * 			
			 * */
		for(Record r : upd) {
			for(String [] k : conditions.keySet()) {
				int i =relation.getIdxInfoCol(k[0]); 
				int retourCompare = compareTo(r.values[i],conditions.get(k)); 
				switch(k[1]) { //k[1] est l'operateur //Vu qu'on utilise compareTo on peut imaginer donner le resultat avec des AND
					case ">": // Faire selon le type 
						if(retourCompare <=0){
							upd.remove(r); 
							break;}
					case ">=": 
						if(retourCompare<0) {
							upd.remove(r); 
							break;}
					case "<": 
						if(retourCompare>= 0){
							upd.remove(r); 
							break;}
					case "<=": 
						if(retourCompare >0) {
							upd.remove(r);
							break; 
						}
					case "<>": 
						if(retourCompare ==0) {
							upd.remove(r); 
							break; 
						}
					case "=": 
						if(retourCompare != 0) {
							upd.remove(r); 
							break; 
						}
				}
	
			}
		}
	}
}

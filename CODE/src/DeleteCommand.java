import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

public class DeleteCommand {
	public HashMap<String[], Object> conditions; // redefinir equals! [nomCol, op], Valeur
	//public ArrayList<String> op; 
	public String nomRelation; 
	public RelationInfo relation; 
	public Record record;

	public DeleteCommand(String ch) {
		//conditions = new ArrayList<String>(); 
		conditions = new HashMap<String[],Object>(); 
		StringTokenizer st = new StringTokenizer(ch); 
		//ArrayList<String> op = new ArrayList<String>(); 
		st.nextToken(); //DELETE
		st.nextToken(); //FROM
		nomRelation = st.nextToken(); //NomRelation
		relation = Catalog.getCatalog().getRelation(nomRelation); 
		st.nextToken(); //WHERE
		String [] nomOp;
		while(st.hasMoreTokens()) {
			nomOp = new String[2];
			String s= st.nextToken().trim(); 
			//System.out.println(s);
			if(s.contains("<="))
				nomOp[1]="<="; 
			else if(s.contains("<>"))
				nomOp[1]="<>"; 
			else if(s.contains(">="))
				nomOp[1]=">="; 
			else if(s.contains("="))
					nomOp[1]="=";  
			else if(s.contains(">"))
				nomOp[1]=">"; 
			else if(s.contains("<"))
				nomOp[1]="<"; 

			//conditions.add(st.nextToken("AND").trim());
			String [] tmp = s.split("=|<=|>=|<>|<|>"); //nomCol, valeur
			//System.out.println(Arrays.toString(tmp));
			nomOp[0]=tmp[0]; //On met le nom de la colonne dans nomOp
			//System.out.println(nomOp[0]);
			String typeCol = null; 
			for(ColInfo c : relation.infoCol) {
				//System.out.println(c); 
				if(c.nomCol.equals(nomOp[0])) {//Tant qu'on a pas trouve la colonne
					typeCol = c.getTypeCol(tmp[0]);
					}
			}
			switch(typeCol) { 
			case "int": 
				conditions.put(nomOp, Integer.valueOf(tmp[1])); 
				break; 
			case "float": 
				conditions.put(nomOp, Float.valueOf(tmp[1])); 
				break; 
			default: 
				conditions.put(nomOp, tmp[1]); 
			}
			//System.out.println(conditionsToString());
			
			if(st.hasMoreTokens()) {
				st.nextToken(); //AND 	
			}
		}
	}
	
	private int compareTo(Object obj1, Object obj2) {
		if(obj1 instanceof Integer) {
			return ((Integer)obj1).compareTo((Integer)obj2); 
		}
		else if (obj1 instanceof Float ) {
			return ((Float)obj1).compareTo((Float)obj2); 
		}
		else if (obj1 instanceof String){
			return ((String)obj1).compareTo((String)obj2); 
		}
		
		throw new RuntimeException("Erreur dans le compareTo"); //juste pour le plaisir de throw une runtimeexception :) 
	}

		//Trouver les tuples � supprimer
		//Les supprimer sur disque 
		//Les compter 
		
		public void Execute() {
			ArrayList<Record> res =new ArrayList<Record>(Arrays.asList(FileManager.getFileManager().getAllRecords(relation))); 
			ArrayList<CreateIndexCommand> index=CreateIndexCommand.getIndex(nomRelation); //INDEX
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
				int taille = res.size(); 
				for(int j = 0; j<taille; j++) {
					boolean removed = false; 
					Record r = res.get(j);
					for(String [] k : conditions.keySet()) {
						int i =relation.getIdxInfoCol(k[0]); 
						int retourCompare = compareTo(r.values[i],conditions.get(k));
						//System.out.println(Arrays.toString(k)+ ": retour compareTo= "+ retourCompare);
						switch(k[1]) { //k[1] est l'operateur //Vu qu'on utilise compareTo on peut imaginer donner le resultat avec des AND
						case ">": // Faire selon le type 
							if(retourCompare <=0){
								removed = res.remove(r); 
								taille --; 
								j=j-1; 
							}
							break;
						case ">=": 
							if(retourCompare<0) {
								removed = res.remove(r);
								taille--;
								j--;
							}
							break;
						case "<": 
							if(retourCompare>= 0){
								removed = res.remove(r);
								taille--; 
								j--;
							}
							break;
						case "<=": 
							if(retourCompare >0) {
								removed = res.remove(r);
								taille--; 
								j--; 
							}
							break; 
						case "<>": 
							if(retourCompare ==0) {
								removed = res.remove(r); 
								taille--; 
								j--; 
							}
							break; 
						case "=": 
							if(retourCompare != 0) {
								removed = res.remove(r); 
								taille--; 
								j--;
							}
							break; 
						}
					if(removed) {
						break;
					}
						
					}
				}
				//suppression des records 
				for(Record r: res) {
					FileManager.getFileManager().deleteRecordFromRelation(r);
					for(CreateIndexCommand ind:index){ //Delete Records from Index
						ind.Delete(r);
					}
				}
				System.out.println("Total deleted records :"+res.size());
			
			}
		
            public void Update() {
                ArrayList<Record> res =new ArrayList<Record>(Arrays.asList(FileManager.getFileManager().getAllRecords(relation))); 
                ArrayList<CreateIndexCommand> index=CreateIndexCommand.getIndex(nomRelation); //INDEX
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
                    int taille = res.size(); 
                    for(int j = 0; j<taille; j++) {
                        boolean removed = false; 
                        Record r = res.get(j);
                        for(String [] k : conditions.keySet()) {
                            int i =relation.getIdxInfoCol(k[0]); 
                            int retourCompare = compareTo(r.values[i],conditions.get(k));
                            //System.out.println(Arrays.toString(k)+ ": retour compareTo= "+ retourCompare);
                            switch(k[1]) { //k[1] est l'operateur //Vu qu'on utilise compareTo on peut imaginer donner le resultat avec des AND
                            case ">": // Faire selon le type 
                                if(retourCompare <=0){
                                    removed = res.remove(r); 
                                    taille --; 
                                    j=j-1; 
                                }
                                break;
                            case ">=": 
                                if(retourCompare<0) {
                                    removed = res.remove(r);
                                    taille--;
                                    j--;
                                }
                                break;
                            case "<": 
                                if(retourCompare>= 0){
                                    removed = res.remove(r);
                                    taille--; 
                                    j--;
                                }
                                break;
                            case "<=": 
                                if(retourCompare >0) {
                                    removed = res.remove(r);
                                    taille--; 
                                    j--; 
                                }
                                break; 
                            case "<>": 
                                if(retourCompare ==0) {
                                    removed = res.remove(r); 
                                    taille--; 
                                    j--; 
                                }
                                break; 
                            case "=": 
                                if(retourCompare != 0) {
                                    removed = res.remove(r); 
                                    taille--; 
                                    j--;
                                }
                                break; 
                            }
                        if(removed) {
                            break;
                        }
                            
                        }
                    }
                    //suppression des records 
                    for(Record r: res) {
                        FileManager.getFileManager().deleteRecordFromRelation(r);
                        for(CreateIndexCommand ind:index){ //Delete Records from Index
                            ind.Delete(r);
                        }
                    }
                
                }

	}
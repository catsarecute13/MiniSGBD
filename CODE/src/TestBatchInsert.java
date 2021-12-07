
public class TestBatchInsert {
	public static void main(String[] args) {
		try {
			DROPDBCommand drop = new DROPDBCommand("DROPDB");
			drop.Execute();
			
			CreateRelationCommand create = new CreateRelationCommand("CREATE RELATION S (X:int,C2:float,BLA:string10)");
			create.Execute();
			//System.out.println(create);  
			//System.out.println("************Catalog***********"); 
			//System.out.println("compteur :" + Catalog.getCatalog().compteur);
			/**for(int i = 0; i<Catalog.getCatalog().relationTab.size(); i++) {
				System.out.println(Catalog.getCatalog().relationTab.get(i).toString()); 
			}*/
			BatchInsertCommand batch = new BatchInsertCommand("")
			
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

}

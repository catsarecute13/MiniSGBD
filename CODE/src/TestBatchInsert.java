
public class TestBatchInsert {
	public static void main(String[] args) {
		try {
			DROPDBCommand drop = new DROPDBCommand("DROPDB");
			drop.Execute();
			
			CreateRelationCommand create = new CreateRelationCommand("CREATE RELATION S (A:String2,B:int,C:String4,D:float,E:String5,F:int,G:int,H:int)");
			create.Execute();
			//System.out.println(create);  
			//System.out.println("************Catalog***********"); 
			//System.out.println("compteur :" + Catalog.getCatalog().compteur);
			/**for(int i = 0; i<Catalog.getCatalog().relationTab.size(); i++) {
				System.out.println(Catalog.getCatalog().relationTab.get(i).toString()); 
			}*/
			BatchInsertCommand batch = new BatchInsertCommand("BATCHINSERT INTO S FROM FILE S1.csv");
			batch.Execute();
			SelectMonoCommand select = new SelectMonoCommand("SELECTMONO * FROM S"); 
			select.Execute();
			
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

}

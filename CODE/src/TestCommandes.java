
public class TestCommandes {

	public static void main(String[] args) {
		try {
			DROPDBCommand drop = new DROPDBCommand("DROPDB");
			drop.Execute();
			CreateRelationCommand create = new CreateRelationCommand("CREATE RELATION R (X:int,C2:float,BLA:string10)");
			create.Execute();
			//System.out.println(create);  
			//System.out.println("************Catalog***********"); 
			//System.out.println("compteur :" + Catalog.getCatalog().compteur);
			/**for(int i = 0; i<Catalog.getCatalog().relationTab.size(); i++) {
				System.out.println(Catalog.getCatalog().relationTab.get(i).toString()); 
			}*/
			
			for (int i=0;i<300;i++) {
				InsertCommand insert = new InsertCommand("INSERT INTO R RECORD ("+i+",2.0,0123456789)");
				insert.Execute();
			}
			
			SelectMonoCommand select = new SelectMonoCommand("SELECTMONO * FROM R"); 
			select.Execute();
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

}

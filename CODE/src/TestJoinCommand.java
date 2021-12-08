
public class TestJoinCommand {

	public static void main(String[] args) {
		try {
			DROPDBCommand drop = new DROPDBCommand("DROPDB");
			drop.Execute();
			CreateRelationCommand create = new CreateRelationCommand("CREATE RELATION R (C1:int,C2:string3,C3:int)");
			create.Execute();
			//System.out.println(create);  
			//System.out.println("************Catalog***********"); 
			//System.out.println("compteur :" + Catalog.getCatalog().compteur);
			/**for(int i = 0; i<Catalog.getCatalog().relationTab.size(); i++) {
				System.out.println(Catalog.getCatalog().relationTab.get(i).toString()); 
			}*/
			
			/*for (int i=0;i<300;i++) {
				InsertCommand insert = new InsertCommand("INSERT INTO R RECORD ("+i+",2.0,0123456789)");
				insert.Execute();
			}*/
      		InsertCommand inser = new InsertCommand("INSERT INTO R RECORD (1,aab,2)");
		    inser.Execute();
		    inser = new InsertCommand("INSERT INTO R RECORD (2,abc,2)");
		    inser.Execute();
		    
		    CreateRelationCommand createS = new CreateRelationCommand("CREATE RELATION S (AA:int,BB:int)");
			createS.Execute();
		    
		    inser = new InsertCommand("INSERT INTO S RECORD (1,2)");
		    inser.Execute();
		    
		    JoinCommand join = new JoinCommand("SELECTJOIN * FROM R,S WHERE R.C1=S.AA");
		    join.Execute(); 
		}catch(Exception e) {
			e.getStackTrace();
		}

	

}
}

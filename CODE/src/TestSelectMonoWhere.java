
public class TestSelectMonoWhere {

	public static void main(String[] args) {
		DROPDBCommand drop = new DROPDBCommand("DROPDB");
		drop.Execute();
		
		CreateRelationCommand create = new CreateRelationCommand("CREATE RELATION S (A:String2,B:int,C:String4,D:float,E:String5,F:int,G:int,H:int)");
		try {
			create.Execute();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//System.out.println(create);  
		//System.out.println("************Catalog***********"); 
		//System.out.println("compteur :" + Catalog.getCatalog().compteur);
		/**for(int i = 0; i<Catalog.getCatalog().relationTab.size(); i++) {
			System.out.println(Catalog.getCatalog().relationTab.get(i).toString()); 
		}*/
		BatchInsertCommand batch = new BatchInsertCommand("BATCHINSERT INTO S FROM FILE S1.csv");
		try {
			batch.Execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SelectMonoCommand select = new SelectMonoCommand("SELECTMONO * FROM S WHERE B=19");// AND A=KJ AND C>=ABCD AND C<>CSMA"); 
		select.Execute();
		SelectMonoCommand select2 = new SelectMonoCommand("SELECTMONO * FROM S WHERE C=Nati");// AND A=KJ AND C>=ABCD AND C<>CSMA"); 
		select2.Execute();
		SelectMonoCommand select3 = new SelectMonoCommand("SELECTMONO * FROM S WHERE B=167 AND A=GR AND F=12");
		select3.Execute();
		

	}

}

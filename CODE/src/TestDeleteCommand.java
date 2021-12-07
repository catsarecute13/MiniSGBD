
public class TestDeleteCommand {

	public static void main(String[] args) {
		DROPDBCommand drop = new DROPDBCommand("DROPDB");
		drop.Execute();
		
		CreateRelationCommand create = new CreateRelationCommand("CREATE RELATION S (A:String2,B:int,C:String4,D:float,E:String5,F:int,G:int,H:int)");
		try {
			create.Execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BatchInsertCommand batch = new BatchInsertCommand("BATCHINSERT INTO S FROM FILE S1.csv");
		batch.Execute();
		
		DeleteCommand delete = new DeleteCommand("DELETE FROM S WHERE C=Nati ");
		delete.Execute();
				

	}

}

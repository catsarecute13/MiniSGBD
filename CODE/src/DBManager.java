import java.util.StringTokenizer;

public class DBManager {
    private static DBManager DBManager = new DBManager();

    public void Init(){
        Catalog.getCatalog().Init();
    }

    public void Finish(){
        Catalog.getCatalog().Finish();
        BufferManager.getBufferManager().FlushAll();
    }

    public void ProcessCommand(String ch) throws Exception{
    	StringTokenizer st = new StringTokenizer(ch, " ");
		if(st.nextToken().equals("CREATE") && st.nextToken().equals("TABLE") ){
			CreateRelationCommand createRel = new CreateRelationCommand(ch);
			createRel.Execute();
		}
	}
		
    public static DBManager getDBManager(){
        return DBManager;
    }
}
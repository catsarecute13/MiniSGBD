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
        StringTokenizer tok=new StringTokenizer(ch);
        String first=tok.nextToken();
        switch (first) {
            case "CREATE":
                CreateRelationCommand createRel = new CreateRelationCommand(ch);
                createRel.Execute();
                break;
            case "DROPDB":
                DROPDBCommand drop=new DROPDBCommand(ch);
                drop.Execute();
                break;
            case "INSERT":
                InsertCommand insert=new InsertCommand(ch);
                insert.Execute();
                break;
            case "BATCHINSERT":
                BatchInsertCommand batch=new BatchInsertCommand(ch);
                batch.Execute();
                break;
            case "SELECTMONO":
                //Le nom que Mayel va choisir
        }
    }

    public static DBManager getDBManager(){
        return DBManager;
    }

    public static void setDBManager(){
        DBManager=new DBManager();
    }
}
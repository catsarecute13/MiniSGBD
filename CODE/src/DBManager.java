import java.util.StringTokenizer;

public class DBManager {
    private static DBManager DBManager = new DBManager();

    public void Init(){
        Catalog.Init();
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
            	SelectMonoCommand selectMono = new SelectMonoCommand(ch); 
            	selectMono.Execute(); 
            	break;
            case "DELETE":
            	DeleteCommand delete = new DeleteCommand(ch);
            	delete.Execute();
            	break;
            case "SELECTJOIN":
            	JoinCommand join = new JoinCommand(ch);
            	join.Execute();
            	break;
            case "UPDATE":
            	UpdateCommand update = new UpdateCommand(ch);
            	update.Execute();
            	break;
            case "CREATEINDEX": 
            	CreateIndexCommand index = new CreateIndexCommand(ch);
            	index.Execute();    
            	break;
            case "SELECTINDEX": 
            	SelectIndexCommand selectIndex = new SelectIndexCommand(ch);
            	selectIndex.Execute();
            default:
            	System.out.println("Aucune commande de ce type.");
        }
    }

    public static DBManager getDBManager(){
        return DBManager;
    }

    public static void setDBManager(){
        DBManager=new DBManager();
    }
}
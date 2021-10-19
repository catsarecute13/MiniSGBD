public class DBManager {
    private static DBManager DBManager = new DBManager();

    public void Init(){
        Catalog.getCatalog().Init();
    }

    public void Finish(){
        Catalog.getCatalog().Finish();
        BufferManager.getBufferManager().FlushAll();
    }

    public void ProcessCommand(String ch){
        //PASS
    }

    public static DBManager getDBManager(){
        return DBManager;
    }
}
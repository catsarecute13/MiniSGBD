import org.apache.commons.io.FileUtils;

public class DROPDBCommand{
    public DROPDBCommand(String chaine){

    }

    public void Execute(){
        FileUtils.cleanDirectory(DBParams.DBPath); 
        BufferManager.getBufferManager().FlushAll();
        BufferManager.getBufferManager().File_FrameMRU=new ListeChainee();
        Catalog.setCatalog();
        DiskManager.setDiskManager();
        FileManager.setFileManager();
        //DBManager.setDBManager();  //NOT SO SURE ABOUT
    }
}
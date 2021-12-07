import java.io.File;

public class DROPDBCommand{
    public DROPDBCommand(String chaine){

    	
    }

    public void Execute(){
        cleanDirectory(DBParams.DBPath); 
        BufferManager.getBufferManager().resetBufferManager();
        //BufferManager.getBufferManager().File_FrameMRU=new ListeChainee();
        Catalog.getCatalog().setCatalog(); //Voir si c'est bien ce qu'on doit faire 
        DiskManager.getDiskManager().setDiskManager();
        FileManager.getFileManager().setFileManager();
        DBManager.setDBManager();  //NOT SO SURE ABOUT
    }
    
    private void cleanDirectory(String path) {
    	File dir = new File(path); 
    	File [] dirList = dir.listFiles();
    	if (dirList != null){
    		for(File file : dirList) {
    			//System.out.println(file.getName());
        		if(file.isDirectory()) {
        			cleanDirectory(file.getPath()); //verfier que ce n'est pas la methode getAbsolutePath qu'on doit utilisers
        		}
        		else {
        			file.delete(); 
        		}
        	}
    	}
    	 
    	
    }
}
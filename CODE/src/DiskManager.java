import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
public class DiskManager { //singleton
	private static DiskManager diskManager = new DiskManager();
	
	public static PageId AllocPage() throws Exception{
		File dir = new File(DBParams.DBPath); 
		File[] dirList = dir.listFiles(); 
		
		
		if(dirList != null) { 
			if (dirList.length == 0) { 
				File fichier = new File(DBParams.DBPath+"/F"+dirList.length+".df"); 
				fichier.createNewFile(); 
				PageId id = new PageId(0,0); 
				return id; }
			
			else {
				if (dirList[dirList.length-1].length() > 12288) {
					File fichier = new File(DBParams.DBPath+"/F"+ dirList.length+".df"); 
					fichier.createNewFile();
					PageId id = new PageId(dirList.length , 0);
					return id;}
				
				else if(dirList[dirList.length-1].length() == 0) {
					PageId id = new PageId(dirList.length -1, 0);
					return id;}
				
				else if(dirList[dirList.length-1].length() <= 4096) {		
						PageId id = new PageId(dirList.length-1, 1); 
						return id;}
				
				else if (dirList[dirList.length-1].length() <= 8192) {
						PageId id = new PageId(dirList.length-1, 2);
						return id;}
				
				else if(dirList[dirList.length-1].length() <= 12288) {
						PageId id = new PageId(dirList.length-1, 3); 
						return id;}
				}	
			
		}
		return null;
	}
	
	
	//Changer par ByteBuffer 
	public static void readPage(PageId pageID, ByteBuffer buff) {
		//Écrire sur buffer le contenu disque de la page identifiée par l'argument pageId. 
		try {
			RandomAccessFile file = new RandomAccessFile(DBParams.DBPath+"/F"+pageID.FileIdx+".df", "r" );
			file.seek((pageID.PageIdx)*4096);
			file.read(buff.array(), 0, 4096); 
			file.close();
		}catch (IOException e) {
			System.out.println("Erreur E/S");
			e.printStackTrace();
		}
	}
	
	public static void writePage(PageId pageID, ByteBuffer buff) {	
		try {
			RandomAccessFile file = new RandomAccessFile(DBParams.DBPath+"/F"+pageID.FileIdx+".df", "rw");
			file.seek((pageID.PageIdx)*4096);
			file.write(buff.array(), 0, 4096);
			file.close();
		}catch (IOException e) {
			System.out.println("Erreur E/S");
		}
		
	}
	public static DiskManager getDiskManager() {
		return diskManager; 
	}

}
	


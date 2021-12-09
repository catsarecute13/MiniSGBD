import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;
public class DiskManager { //singleton
	private static DiskManager diskManager = new DiskManager();
	private final static ByteBuffer BUFFERDEZERO = ByteBuffer.allocate(DBParams.pageSize);

	public static PageId AllocPage() {
		File dir = new File(DBParams.DBPath); 
		File[] dirList = dir.listFiles(); 
		int ce = new File(DBParams.DBPath+File.separator+"Catalog.def").exists() ? 1 : 0; 
		
		Arrays.sort(dirList, new Comparator<File>(){ 
			@Override
			public int compare(File f1, File f2) {
				return f1.getName().compareTo(f2.getName()); 
			}
		});
		if(dirList != null) { 
			if (dirList.length - ce ==0 ) { 
				File fichier = new File(DBParams.DBPath+File.separator+"F"+dirList.length+".df"); 
				try {
					fichier.createNewFile();
				} catch (IOException e) {
					System.out.println("Impossible de créer le fichier: "+ DBParams.DBPath+File.separator+"F"+dirList.length+".df");
					System.exit(-1);
				} 
				PageId id = new PageId(0,0);
				DiskManager.writePage(id, BUFFERDEZERO);
				return id; }
			
			else {
				if (dirList[dirList.length-1].length() > 12288) {
					File fichier = new File(DBParams.DBPath+File.separator+"F"+ dirList.length+".df"); 
					try {
						fichier.createNewFile();
					} catch (IOException e) {
						System.out.println("Impossible de créer le fichier: "+ DBParams.DBPath+File.separator+"F"+dirList.length+".df");
						System.exit(-1);
					}
					PageId id = new PageId(dirList.length -ce , 0);
					DiskManager.writePage(id, BUFFERDEZERO);
					return id;}
				
				else if(dirList[dirList.length-1].length() == 0) {
					PageId id = new PageId(dirList.length -1 -ce, 0);
					DiskManager.writePage(id, BUFFERDEZERO);
					return id;}
				
				else if(dirList[dirList.length-1].length() <= 4096) {		
						PageId id = new PageId(dirList.length-1 -ce, 1); 
						DiskManager.writePage(id, BUFFERDEZERO);
						return id;}
				
				else if (dirList[dirList.length-1].length() <= 8192) {
						PageId id = new PageId(dirList.length-1 -ce, 2);
						DiskManager.writePage(id, BUFFERDEZERO);
						return id;}
				
				else if(dirList[dirList.length-1].length() <= 12288) {
						PageId id = new PageId(dirList.length-1 -ce, 3); 
						DiskManager.writePage(id, BUFFERDEZERO);
						return id;}
				}	
			
		}
		
		return null;
	}
	
	
	//Changer par ByteBuffer 
	public static void readPage(PageId pageID, ByteBuffer buff) { //si pageID == -1 0, on throws un nullPointerException
		//Ecrire sur buffer le contenu disque de la page identifiee par l'argument pageId. 
		try {
			RandomAccessFile file = new RandomAccessFile(DBParams.DBPath+File.separator+"F"+pageID.FileIdx+".df", "r" );
			file.seek((pageID.PageIdx)*DBParams.pageSize);
			file.read(buff.array(), 0, DBParams.pageSize); 
			file.close();
		}catch (IOException e) {
			System.out.println("Erreur E/S");
			e.printStackTrace();
		}
	}
	
	public static void writePage(PageId pageID, ByteBuffer buff) {	
		try {
			RandomAccessFile file = new RandomAccessFile(DBParams.DBPath+File.separator+"F"+pageID.FileIdx+".df", "rw");
			file.seek((pageID.PageIdx)*DBParams.pageSize);
			file.write(buff.array(), 0, DBParams.pageSize);
			file.close();
		}catch (IOException e) {
			//System.out.println("Erreur E/S");
			e.printStackTrace();
		}
		
	}
	public static DiskManager getDiskManager() {
		return diskManager; 
	}


	public void setDiskManager() {
		diskManager = new DiskManager(); 
		
	}

}
	


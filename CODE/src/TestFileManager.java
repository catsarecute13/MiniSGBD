import java.nio.ByteBuffer;
import java.util.ArrayList;

public class TestFileManager {

	public static void main(String[] args)  {
		try {
			System.out.println("Hello");
			ColInfo colInfo = new ColInfo("code", "integer"); 
			ColInfo colInfo2 = new ColInfo("nom", "String4"); 
			ArrayList<ColInfo>listeColInfo = new ArrayList<ColInfo>(2); 
			listeColInfo.add(colInfo); 
			listeColInfo.add(colInfo2); 
			System.out.println("hello2");
			PageId headerPageId= FileManager.getFileManager().createHeaderPage(); 
			System.out.println("header_pid:" + headerPageId);
		   	RelationInfo relation = new RelationInfo("Test", 2, listeColInfo, headerPageId); 
			System.out.println("création raltion: fait");
		   	Record record = new Record(relation); 
		   	System.out.println("Création record : fait");
		   	Integer code =121;
		   	String nom = "Toto";
		   	ByteBuffer buffer =ByteBuffer.allocate(relation.recordSize);
		   	buffer.putInt(code); 
		   	buffer.put(nom.getBytes()); 
		    System.out.println("après écriture dans buffer");
		    System.out.println(buffer.getInt());
		   	record.readFromBuffer(buffer,0);
		   	PageId id_dataPage = FileManager.getFileManager().addDataPage(relation); 
		    Rid rid =	FileManager.getFileManager().writeRecordToDataPage(relation, record, id_dataPage); 
		    
		    Record [] recordsInDataPage=FileManager.getFileManager().getRecordsInDataPage(relation, id_dataPage);	
		    System.out.println(recordsInDataPage.length);
		    for(int i = 0; i<recordsInDataPage.length; i++) {
		    	//System.out.println(recordsInDataPage[i].toString());
		    }
		   
		}catch( Exception e){
			e.printStackTrace();
			System.out.println("erreur");
			
		}
	}
}

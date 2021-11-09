import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class TestFileManager {

	public static void main(String[] args)  {
		try {
			ColInfo colInfo = new ColInfo("code", "integer"); 
			ColInfo colInfo2 = new ColInfo("nom", "String4"); 
			ArrayList<ColInfo>listeColInfo = new ArrayList<ColInfo>(2); 
			listeColInfo.add(colInfo); 
			listeColInfo.add(colInfo2); 
			PageId headerPageId= FileManager.getFileManager().createHeaderPage(); 
			System.out.println("header_pid:" + headerPageId);
		   	RelationInfo relation = new RelationInfo("Test", 2, listeColInfo, headerPageId); 
		   	Record record = new Record(relation); 
		
		   	Integer code =121;
		   	String nom = "Toto";
		   	ByteBuffer buffer =ByteBuffer.allocate(relation.recordSize);
		   	buffer.position(0); 
		   	buffer.putInt(code); 
		   	buffer.put(nom.getBytes(StandardCharsets.UTF_16));
		   	//System.out.println(buffer.getInt(0));
		   	record.readFromBuffer(buffer,0); //J'ecris le contenu du buffer dans le record 
		   	System.out.println("RECORD: " +record.toString()); 
		   	System.out.println(Arrays.toString(buffer.array())); 
		   	PageId id_dataPage = FileManager.getFileManager().addDataPage(relation); 
		    Rid rid =	FileManager.getFileManager().writeRecordToDataPage(relation, record, id_dataPage);
		    Record [] recordsInDataPage=FileManager.getFileManager().getRecordsInDataPage(relation, id_dataPage);	
		    //System.out.println("NB record :" +recordsInDataPage.length);
		    for(int i = 0; recordsInDataPage[i] !=null; i++) {
		    		System.out.println("Record : "+ recordsInDataPage[i]);
		    }
		   
		}catch(Exception e){
			System.out.println("EXCEPTION !");
			e.printStackTrace();
		}
	}
}

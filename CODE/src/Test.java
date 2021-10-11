import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList; 
public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException {
			ColInfo colInfo = new ColInfo("code", "integer"); 
			ColInfo colInfo2 = new ColInfo("nom", "String4"); 
			ArrayList<ColInfo>listeColInfo = new ArrayList(2); 
			listeColInfo.add(colInfo); 
			listeColInfo.add(colInfo2); 
		   	RelationInfo relation = new RelationInfo("Test", 2, listeColInfo); 
		   	Record record = new Record(relation); 
	        ByteBuffer buffer = ByteBuffer.allocate(16); 
	        buffer.putInt(7); 
	        //buffer.putFloat(4,(float)0.0);
	        String toto = new String("toto"); 
	        byte tab [] = new byte[toto.getBytes().length];
	        tab = toto.getBytes(); 
	        buffer.put(tab, 0, tab.length); 
		   	record.readFromBuffer(buffer, 0); 
		   	for(int i =0; i<relation.nbColonnes; i++){
		   			System.out.println((record.values[i].toString())); 
		   		
		   		}
	
		   		}	
}



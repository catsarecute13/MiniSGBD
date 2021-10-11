import java.nio.ByteBuffer;
import java.util.ArrayList; 
public class Record {
	public RelationInfo relation; 
	public Object [] values; 
	
	
	public Record(RelationInfo rel) { 
		relation = rel; 
		values = new Object [relation.nbColonnes]; 
	}
	
	public void writeToBuffer(ByteBuffer buffer, int position) {
		for(int i = 0; i < relation.nbColonnes; i++) {
			Object tmp= values[i]; 
			if(tmp instanceof Integer) {
				buffer.putInt(position, ((Integer) tmp).intValue());
				}
			else if(tmp instanceof Float) {
				buffer.putFloat(position, ((Float) tmp).floatValue()); 
			}
			else {
				buffer.put(((String) tmp).getBytes(), position, ((String) tmp).length()); 
			}
		}
	}
	/*Ecris le contenu du buffer dans record */
	public void readFromBuffer(ByteBuffer buffer, int position) {
		buffer.position(position); 
		for(int i = 0; i< relation.nbColonnes; i++) {
			if (relation.infoCol.get(i).typeCol.trim().toLowerCase().equals("integer")) {
				values[i] =buffer.getInt();
			}
			else if (relation.infoCol.get(i).typeCol.trim().toLowerCase().equals("float")) {
				values[i] = buffer.getFloat();
			}
			else {
				int length = Integer.valueOf(relation.infoCol.get(i).typeCol.substring(6)) * Character.BYTES; 
				byte [] tab = new byte[length]; 
				buffer.get(tab, 0, length);
				values[i]= new String(tab); 
			}
		}
		
	}
	
	

}

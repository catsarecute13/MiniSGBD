import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
public class Record {
	public RelationInfo relation; 
	public Object [] values; 
	
	
	public Record(RelationInfo rel) { 
		relation = rel; 
		values = new Object [relation.nbColonnes]; 
	}
	
	/**
	 * Ecrit contenu d'un record dans le buffer
	 * */
	public void writeToBuffer(ByteBuffer buffer, int position) {
		buffer.position(position); 
		for(int i = 0; i < relation.nbColonnes; i++) {
			Object tmp= values[i]; 
			if(tmp instanceof Integer) {
				buffer.putInt(((Integer) tmp).intValue());
				}
			else if(tmp instanceof Float) {
				buffer.putFloat(((Float) tmp).floatValue()); 
			}
			else {
				buffer.put(((String) tmp).getBytes(StandardCharsets.UTF_16)); 
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
				int length = Integer.valueOf(relation.infoCol.get(i).typeCol.substring(6)) * Character.BYTES +2; 
				byte [] tab = new byte[length]; 
				buffer.get(tab, 0, length);
				values[i]= new String(tab, StandardCharsets.UTF_16); 
			}
		}
		
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("(");
		for(int i = 0; i <relation.nbColonnes; i++) {
			Object tmp= values[i]; 
			if(tmp instanceof Integer) {
				buffer.append(((Integer) tmp).intValue()).append(",");
				}
			else if(tmp instanceof Float) {
				buffer.append(((Float)tmp).floatValue()).append(","); 
			}
			else {
				buffer.append((String)tmp).append(","); 
			}
		}
		
		return buffer.deleteCharAt(buffer.length()-1).append(")").toString();
	}
}

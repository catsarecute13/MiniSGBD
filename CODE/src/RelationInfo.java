import java.util.ArrayList; 
public class RelationInfo implements java.io.Serializable {

	private static final long serialVersionUID = 6931476315267173873L;
	
	public String nomRelation; 
	public int nbColonnes; 
	public ArrayList<ColInfo> infoCol; 
	public PageId headerPageId;
	public int recordSize;
	public int slotCount;
	
	public RelationInfo(String nom, int nb, ArrayList<ColInfo> tab, PageId headerPageId) {
		nomRelation = nom; 
		nbColonnes = nb; 
		infoCol = new ArrayList<ColInfo>(tab); 
		this.headerPageId=headerPageId;
		recordSize=0;
		for(int i=0;i<nbColonnes;i++) {
			if (infoCol.get(i).typeCol.trim().toLowerCase().compareTo("int")==0) { //ou integer
				recordSize+=Integer.BYTES;
			}
			else if (infoCol.get(i).typeCol.trim().toLowerCase().compareTo("float")==0) {
				recordSize+=Float.BYTES;
			}
			else {
				recordSize+=Integer.valueOf(infoCol.get(i).typeCol.substring(6))*Character.BYTES +2;
			}
		}
		
		slotCount= (DBParams.pageSize - 4*Integer.BYTES)/ (recordSize + (byte) 1);
	}
	
	public int getIdxInfoCol(String nomCol) {
		for(int i = 0; i <infoCol.size(); i++) {
			if (infoCol.get(i).nomCol.equals(nomCol)) {
				return i; 
			}
		}
		return -1; 
	}
	
	public String toString() { 
		StringBuffer buffer = new StringBuffer("Nom relation: "+ nomRelation +"\nnb colonnes :"+ nbColonnes); 
		buffer.append("\nNom colonne | Type colonne\n"); 
		System.out.println("infoCol size" +infoCol.size()); 
		for(int i= 0; i<infoCol.size() ;i++) {
			buffer.append(infoCol.get(i).toString()).append("\n"); 
		}
		buffer.append("headerPage Id: ").append(headerPageId).append("\n"); 
		buffer.append("recordSize: ").append(recordSize).append("\n"); 
		buffer.append("slotCount: ").append(slotCount).append("\n");
		return buffer.toString(); 
	} 
}

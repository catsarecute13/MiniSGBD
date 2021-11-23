import java.util.ArrayList; 
public class RelationInfo {
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
			if (infoCol.get(i).typeCol.trim().toLowerCase().compareTo("integer")==0) {
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
	
}

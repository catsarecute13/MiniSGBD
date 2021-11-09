

public class PageId {
	public int FileIdx; //Fx.df, donc le x qui est le numéro du fichier
	public int PageIdx; // Page appartient = {0,1,2,3}
	 //PageID (x, page)
	public PageId(int file, int page) {
		FileIdx=file; 
		PageIdx= page; 
	}
	
	@Override
	public boolean equals(Object o) {
		return FileIdx== ((PageId)o).FileIdx && PageIdx== ((PageId)o).PageIdx; 
	}
	
	@Override
	public String toString() {
		return "("+FileIdx+","+PageIdx+")"; 
	}

}

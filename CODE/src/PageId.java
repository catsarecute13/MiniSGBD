

public class PageId implements java.io.Serializable {
	private static final long serialVersionUID = -3864480858698481273L;
	public int FileIdx; //Fx.df, donc le x qui est le numéro du fichier
	public int PageIdx; // Page appartient = {0,1,2,3}
	public static final PageId pidFactice= new PageId(-1,0);
	 //PageID (x, page)
	
	public PageId(int file, int page) {
		FileIdx=file; 
		PageIdx= page; 
	}
	
	public PageId(PageId pid) {
		this.FileIdx= pid.FileIdx; 
		this.PageIdx=pid.PageIdx;
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

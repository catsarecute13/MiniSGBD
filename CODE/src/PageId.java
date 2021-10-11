

public class PageId {
	public int FileIdx; //Fx.df, donc le x qui est le numéro du fichier
	public int PageIdx; // Page appartient = {0,1,2,3}
	 //PageID (x, page)
	public PageId(int file, int page) {
		FileIdx=file; 
		PageIdx= page; 
	}

}

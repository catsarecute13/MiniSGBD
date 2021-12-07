public class Frame {
	public PageId id; 
	public int pin_count; 
	public boolean dirty; 
	public byte [] buffer; 
	public ListeChainee chaine; 
	
	public Frame (PageId id) {
		this.id = id; 
		pin_count = 0; 
		dirty = false;
		buffer = new byte[DBParams.pageSize];
		chaine = null; 
	}
	
	public String toString() {
		return "(id: )" +id+ "pin_count:"+pin_count; 
	}
}

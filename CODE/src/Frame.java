public class Frame {
	public PageId id; 
	public int pin_count; 
	public boolean dirty; 
	public byte [] buffer; 
	public int temps; 
	public ListeChainee chaine; 
	
	public Frame (PageId id) {
		this.id = id; 
		pin_count = 0; 
		dirty = false;
		buffer = new byte[4096];
		temps = 0;
		chaine = null; 
	}

}

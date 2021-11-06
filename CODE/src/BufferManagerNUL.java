import java.nio.ByteBuffer;
import java.util.ArrayList; 
@Deprecated

public class BufferManagerNUL {
	public ArrayList<Frame> pool; 
	public int mru;
	public BufferManagerNUL() {
		pool = new ArrayList<Frame>(DBParams.frameCount);
	}
	
	public byte [] getPage(PageId id) {
		int tmp = contient(id);
		if (tmp == -1) {
			if (pool.size()==DBParams.frameCount) {
				int index = remplacementMRU(id); 
				return pool.get(index).buffer;
			}
			else {
				pool.set(pool.size(), new Frame(id));
				pool.get(pool.size()-1).pin_count++; 
				DiskManager.readPage(pool.get(pool.size()-1).id, ByteBuffer.wrap(pool.get(pool.size()-1).buffer));
				mru = pool.size() - 1; 
				return pool.get(pool.size()-1).buffer; 
			}
			
		}
		pool.get(tmp).pin_count++; 
		//pool.get(tmp).temps = 0; 
		return pool.get(tmp).buffer;	
	}
	private int remplacementMRU(PageId id) {
		int min =DBParams.frameCount;
		int index=-1; //attention � �a 
		for(int i = 0; i<DBParams.frameCount; i++){
			if (pool.get(i).pin_count == 0 && pool.get(i).temps <min) {
				index = i; 
			}
		}
		if(pool.get(index).dirty) {
			DiskManager.writePage(id,ByteBuffer.wrap(pool.get(index).buffer));
		}
		pool.get(index).pin_count++; 
		DiskManager.readPage(pool.get(index).id,ByteBuffer.wrap(pool.get(index).buffer));
		return index ;
		
	}
	
	public void freePage(PageId id, boolean dirty) {
		
	}
	
	private int contient(PageId id) {
		for(int i = 0; i < DBParams.frameCount; i++) {
			if (pool.get(i).id.equals(id)){
				return i; 
			}
		}
		return -1; 
	}
	
	
}
	


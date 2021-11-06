import java.nio.ByteBuffer; 
public class BufferManager {
	public Frame [] pool; 
	public ListeChainee File_FrameMRU; 
	private static BufferManager bufferManager = new BufferManager(); 
	
	private BufferManager(){
		pool = new Frame [DBParams.frameCount];
		File_FrameMRU = new ListeChainee();
	}
	
	public ByteBuffer getpage(PageId id) {
		
		for(int i=0; i< DBParams.frameCount; i++) {
			if(pool[i]==null) {
				//System.out.println("AAAH");
				pool[i] = new Frame(id); 
				DiskManager.readPage(id, ByteBuffer.wrap(pool[i].buffer)); 
				pool[i].pin_count++; 
				return ByteBuffer.wrap(pool[i].buffer, 0, pool[i].buffer.length); 
			}
			else if (pool[i].id.equals(id)) {
				//System.out.println("BBBH");
				pool[i].pin_count ++; 
				if (pool[i].pin_count == 1) {
					pool[i].chaine.supprimer();

				}
				return ByteBuffer.wrap(pool[i].buffer, 0, pool[i].buffer.length); 
			}
		}
		if (File_FrameMRU.frameSuiv != null) {
			//System.out.println("CCCH");
			if(pool[File_FrameMRU.frameSuiv.index].dirty) {
				DiskManager.writePage(pool[File_FrameMRU.frameSuiv.index].id, ByteBuffer.wrap(pool[File_FrameMRU.frameSuiv.index].buffer));
			}
				Frame tmp = new Frame(id); 
				DiskManager.readPage(id, ByteBuffer.wrap(tmp.buffer));
				pool[File_FrameMRU.frameSuiv.index] = tmp; 
				tmp.pin_count++; 
				File_FrameMRU.frameSuiv.supprimer();
				return ByteBuffer.wrap(tmp.buffer, 0, tmp.buffer.length); 
		}
		System.out.println("va retourner null");
		return null;
		 
	}
	
	void freePage(PageId id,boolean valdirty) {
		for(int i = 0; i<DBParams.frameCount; i++) {
			if(pool[i].id.equals(id)) {
				if(pool[i].dirty != true) {
					pool[i].dirty=valdirty; 
				}
				pool[i].pin_count--;
				if (pool[i].pin_count== 0) {
					ListeChainee tmp = new ListeChainee(i);
					pool[i].chaine = tmp; 
					File_FrameMRU.ajouter(tmp);
				}
				
			}
			 
		}
	}
	
	public void FlushAll(){
        for (Frame element:pool){
            if (element.dirty) {
                DiskManager.writePage(element.id, ByteBuffer.wrap(element.buffer)); //changer type
            }
            pool.clear();
        }
    }

	static public BufferManager getBufferManager() {
		return bufferManager; 
	}
	
}

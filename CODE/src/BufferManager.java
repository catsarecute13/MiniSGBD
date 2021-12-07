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
		int g = 0;
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
		int g = 0;
		for(int i = 0; i<DBParams.frameCount; i++) {
			//System.out.println("pool["+i+"]" + pool[i]); 
			//System.out.println("id "+ pool[i].id); 
			//System.out.println("pageId donne en param"+ id); 
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
				break; 
			}
			 
		}
	}
	
	public void FlushAll(){
        for (Frame element:pool){
            if (element != null && element.dirty) {
                DiskManager.writePage(element.id, ByteBuffer.wrap(element.buffer)); 

            }
            
        }
	}

	static public BufferManager getBufferManager() {
		return bufferManager; 
	}

	public void resetBufferManager() {
		pool = new Frame [DBParams.frameCount];
		File_FrameMRU = new ListeChainee();
		
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer("[");
		for(int i=0; i<DBParams.frameCount; i++) {
			if(pool[i] == null)
				buffer.append("VIDE, ");
			else 
				buffer.append(pool[i].id).append(" pin_count=").append(pool[i].pin_count).append(", ");
		}
		return buffer.append("]").toString();
	}
	
	
}

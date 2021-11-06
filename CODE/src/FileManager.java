import java.nio.ByteBuffer;

public class FileManager{
	private static FileManager fileManager= new FileManager(); 
	
	public PageId readPageIdFromPageBuffer(ByteBuffer buffer, boolean pointeurPageVide) {
		if(!pointeurPageVide) { 
			buffer.position(0);
			return new PageId(buffer.getInt(), buffer.getInt()); 
		}
		else {
			buffer.position(Integer.BYTES*2); 
			return new PageId(buffer.getInt(), buffer.getInt()); 
		}
	}
	
	public void writePageIdToPageBuffer(PageId id, ByteBuffer buffer, boolean first) {
		if(!first) {
			buffer.position(0); 
			buffer.putInt(id.FileIdx); 
			buffer.putInt(id.PageIdx); 
		}
		else {
			buffer.position(Integer.BYTES*2); 
			buffer.putInt(id.FileIdx); 
			buffer.putInt(id.PageIdx); 
		}
	}
	
    public  PageId createHeaderPage() throws Exception{
        PageId nouvPage=DiskManager.AllocPage();
        PageId pIdFactice=new PageId(-1,0);
        ByteBuffer page=BufferManager.getBufferManager().getpage(nouvPage);
        writePageIdToPageBuffer(pIdFactice,page,false);//premier pid (pages pleines)
        writePageIdToPageBuffer(pIdFactice,page,true); //deuxième pid (pages vides)
        BufferManager.getBufferManager().freePage(nouvPage, true);
        
        return nouvPage;
    }

    public PageId addDataPage(RelationInfo relInfo) throws Exception{
        PageId nouvPage=DiskManager.AllocPage();
        PageId idPrevious=relInfo.headerPageId;

        ByteBuffer headerPage=BufferManager.getBufferManager().getpage(idPrevious);
        PageId idNext=readPageIdFromPageBuffer(headerPage,true);
        writePageIdToPageBuffer(nouvPage,headerPage,true); //TRUE c'est la page suivante, FALSE c'est la page précédente
        BufferManager.getBufferManager().freePage(idPrevious, true);

        ByteBuffer nextPage=BufferManager.getBufferManager().getpage(idNext);
        writePageIdToPageBuffer(nouvPage,nextPage,false);
        BufferManager.getBufferManager().freePage(idNext, true);

        ByteBuffer dataPage=BufferManager.getBufferManager().getpage(nouvPage);
        writePageIdToPageBuffer(idPrevious,dataPage,false);
        writePageIdToPageBuffer(idNext,dataPage,true);
        BufferManager.getBufferManager().freePage(nouvPage, true);
        return nouvPage;
    }

    public PageId getFreeDataPageId(RelationInfo relInfo) throws Exception{
        PageId pIdFactice=new PageId(-1,0);
        PageId header=relInfo.headerPageId;
        ByteBuffer headerPage=BufferManager.getBufferManager().getpage(header);
        PageId idPage=readPageIdFromPageBuffer(headerPage,true);
        BufferManager.getBufferManager().freePage(header, false);
        if (idPage.equals(pIdFactice)){
            return addDataPage(relInfo);
        } else{
            return idPage;
        }
    }

    public Rid writeRecordToDataPage(RelationInfo relInfo, Record record, PageId pageId){
        ByteBuffer page=BufferManager.getBufferManager().getpage(pageId);
        int nbSlots=relInfo.slotCount;
        int nbLibre=0;
        int slotIdx=-1;
        page.position(Integer.BYTES*4);
        for(int i=0;i<nbSlots;i++){
            if (page.getInt(i)==0){
                nbLibre+=1; //Nb de slots libres: si nbLibre>1, on ne deplace pas la page vers les pages pleines.
                if (nbLibre==1){
                    slotIdx=i; //Le slot qu'on va utiliser
                }
            }
        } 

        //On aura jamais nbLibre ==0 car quand c'est egal a 1, on ecrit le record puis on d�place directement 
        //le record dans les pages pleines 
        if (nbLibre ==1){
        	//On ecrit le record dans le slot libre 
        	record.writeToBuffer(page, Integer.BYTES*4 + nbSlots* + record.relation.recordSize*slotIdx);
        	page.put(slotIdx, (byte)1); //on update la bytemap
        
        	//On deplace la page vers les pages pleines
        	ByteBuffer headerPage = BufferManager.getBufferManager().getpage(relInfo.headerPageId); 
        	
        	//Precdent dans page devient headerPage 
        	//Suivant devient pageVide headerPage 
        	page.putInt(headerPage.getInt()); 
        	page.putInt(headerPage.getInt()); 
        	
        	//PageVide headerPage devient page
        	headerPage.position(Integer.BYTES*2); //Update la position au pid de la page suivante
        	headerPage.putInt(pageId.FileIdx); 
        	headerPage.putInt(pageId.PageIdx); 
        	
        	//On libere la page aupres de BufferManager avec dirty 
        	BufferManager.getBufferManager().freePage(pageId, true);
        	return new Rid(pageId, slotIdx); 
        }
        else  {
        	//On ecrit le record dans le slot lib
        	//On libere la page aupres de BufferManager avec dirty
        	record.writeToBuffer(page, Integer.BYTES*4 + nbSlots + record.relation.recordSize*slotIdx);
        	page.put(slotIdx, (byte)1); //on update la bytemap
        	BufferManager.getBufferManager().freePage(pageId, true);
        	return new Rid(pageId, slotIdx); 
        }
   

    }
    
    public Record [] getRecordsInDataPage(RelationInfo relInfo, PageId pageId) {
    	Record [] recordList = new Record[relInfo.slotCount]; 
    	ByteBuffer page = BufferManager.getBufferManager().getpage(pageId); 
    	Record tmpRecord = new Record(relInfo); 
    	page.position(Integer.BYTES*4); 
    	for(int i = 0; i< recordList.length; i++) {
    		if(page.get(i)==1) { //On n'ecrit que s'il y a des records
    			tmpRecord.readFromBuffer(page, relInfo.slotCount + i*relInfo.recordSize); 
        		recordList[i]=tmpRecord; 
    		}
    		
    	}
    	return recordList; 
    }
    
    public Rid InsertRecordIntoRelation(RelationInfo relInfo, Record record) {
    	//On recupere le headerFile
    	ByteBuffer headerPage = BufferManager.getBufferManager().getpage(relInfo.headerPageId); 
    	//On ecrit le record dans le premier slot libre 
    	return writeRecordToDataPage(relInfo, record, new PageId(headerPage.getInt(), headerPage.getInt()));
    
    }
    
    public Record [] getAllRecords(RelationInfo relInfo) {
    	//On recupere le headerFile
    	//On getRecords de toutes les pages
    	return null; 
    }
    public static FileManager getFileManager() {
    	return fileManager; 
    }
}
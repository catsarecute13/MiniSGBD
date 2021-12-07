import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class FileManager{
	private static FileManager fileManager= new FileManager(); 
	
	/**
	 * Lire un ID 
	 * */
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
		//System.out.println(buffer);
		if(!first) {
			//System.out.println(buffer); 
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
        //System.out.println(nouvPage);
        PageId pIdFactice=new PageId(-1,0);
        //System.out.println("getPage"+nouvPage);
        ByteBuffer page=BufferManager.getBufferManager().getpage(nouvPage);
        //System.out.println(page);
        writePageIdToPageBuffer(pIdFactice,page,false);//premier pid (pages pleines)
        writePageIdToPageBuffer(pIdFactice,page,true); //deuxieme pid (pages vides)
        //System.out.println("freePage"+nouvPage);
        BufferManager.getBufferManager().freePage(nouvPage, true);
        
        return nouvPage;
    }

    public PageId addDataPage(RelationInfo relInfo){ //on peut considerer -1 0 comme un nullPointerException
        PageId pidFactice = new PageId(-1, 0); 
    	PageId nouvPage=DiskManager.AllocPage(); //	ID de la nouvelleDataPage 
    	//System.out.println("id alloc page : "+nouvPage);
        PageId idPrevious=relInfo.headerPageId; //Le precedent de la nouvelle dataPage sera headerPageID parce qu'on
        										//ajoute au debut
        //System.out.println("getPage"+idPrevious);
        ByteBuffer headerPage=BufferManager.getBufferManager().getpage(idPrevious); //on recupere le buffer de HeaderPage
        PageId idNext=readPageIdFromPageBuffer(headerPage,true);
        writePageIdToPageBuffer(nouvPage,headerPage,true); //TRUE c'est la page suivante, FALSE c'est la page precedente
        //System.out.println("freePage"+idPrevious); 
        BufferManager.getBufferManager().freePage(idPrevious, true); //Quand on finit on free la headerPage 
        if(!idNext.equals(pidFactice)) {//Si la page suivante existe reellement (donc le pidNext est different du pidFactice) 
        	 //System.out.println("getPage"+idNext);
        	 ByteBuffer nextPage=BufferManager.getBufferManager().getpage(idNext); 
        	 writePageIdToPageBuffer(nouvPage,nextPage,false); 
        	 //System.out.println("freePage"+idNext);
        	 BufferManager.getBufferManager().freePage(idNext, true);
           
        }
        //System.out.println("getPage"+nouvPage);
        ByteBuffer dataPage=BufferManager.getBufferManager().getpage(nouvPage); //On cree la nouvelle page (avec buffer)
        if(!idNext.equals(pidFactice)) {
        	  writePageIdToPageBuffer(idNext,dataPage,true);
        }
        else {
        	writePageIdToPageBuffer(pidFactice, dataPage, true); 
        }
        writePageIdToPageBuffer(idPrevious,dataPage,false); //On ecrit les id dans le buffer 
        //System.out.println("taille buffer page : "+ dataPage.capacity()); 
        // System.out.println("Position 0 dans byteMap: "+ Integer.BYTES*4); 
        //System.out.println("Nombre de slot (taille byteMap) : "+ relInfo.slotCount); 
        dataPage.position(Integer.BYTES*4); 
        dataPage.put(new byte[relInfo.slotCount], 0,  relInfo.slotCount); //On initialise la bytemap a 0
        //System.out.println("freePage"+nouvPage);
        BufferManager.getBufferManager().freePage(nouvPage, true);
        return nouvPage;
    }

    public PageId getFreeDataPageId(RelationInfo relInfo) {
    	PageId pIdFactice=new PageId(-1,0);
        PageId header=relInfo.headerPageId;
        
        //System.out.println("getPage"+relInfo.headerPageId);
        ByteBuffer headerPage=BufferManager.getBufferManager().getpage(header);
        PageId idPage=readPageIdFromPageBuffer(headerPage,true);//pages non pleines == pages suivantes
        //System.out.println("pid lu: "+ idPage);
        //System.out.println("freePage"+relInfo.headerPageId);
        BufferManager.getBufferManager().freePage(header, false);
        if (idPage.equals(pIdFactice)){ //Il n'y a pas de pages
				return addDataPage(relInfo);
				//On ajoute une page 
        } else{
            return idPage; //sinon on renvoie la page existante
        }
    }

    public Rid writeRecordToDataPage(RelationInfo relInfo, Record record, PageId pageId){
     	//System.out.println(BufferManager.getBufferManager());
     	//System.out.println("WriteRecordToDataPageFile : "+BufferManager.getBufferManager().File_FrameMRU);
    	PageId pIdFactice=new PageId(-1,0);
    	//System.out.println("getPage"+pageId);
        ByteBuffer page=BufferManager.getBufferManager().getpage(pageId); //DATAPAGES
        int nbSlots=relInfo.slotCount;
        int nbLibre=0;
        int slotIdx=-1;
        for(int i=0;i<nbSlots;i++){
            if (page.get(Integer.BYTES*4 + i)==(byte)0){
                nbLibre+=1; //Nb de slots libres: si nbLibre>1, on ne deplace pas la page vers les pages pleines.
                if (nbLibre==1){
                    slotIdx=i; //Le slot qu'on va utiliser
                }
            }
        } 
        //System.out.println("J'ecris dans le slot "+slotIdx);
        //On aura jamais nbLibre ==0 car quand c'est egal a 1, on ecrit le record puis on deplace directement 
        //la page dans les pages pleines 
        if (nbLibre ==1){
        	//System.out.println(BufferManager.getBufferManager());
        	
        	//On ecrit le record dans le slot libre 
        	record.writeToBuffer(page, Integer.BYTES*4 + nbSlots + record.relation.recordSize*slotIdx);
        	page.put(Integer.BYTES*4 +slotIdx, (byte)1); //on update la bytemap
        
        	//JUSQUE LA TOUT VA BIEN 
        	
        	
        	//Suppression de la pages des pages non pleines 
        	PageId pidSuivPage = readPageIdFromPageBuffer(page, true);
        	PageId pidPrePage = readPageIdFromPageBuffer(page, false); 
        	
        	if(!pidSuivPage.equals(pIdFactice)) {//Si ce n'est pas la derniere page
        		//System.out.println("getPage"+pidSuivPage);
        		ByteBuffer buffPidSuivPage = BufferManager.getBufferManager().getpage(pidSuivPage); 
        		writePageIdToPageBuffer(pidPrePage, buffPidSuivPage,false);  //update du pre dans la pageSuiv
        		//System.out.println("freePage"+pidSuivPage);
        		BufferManager.getBufferManager().freePage(pidSuivPage, true);
        	}
        	//System.out.println("getPage"+pidPrePage); 
        	ByteBuffer buffPidPrePage = BufferManager.getBufferManager().getpage(pidPrePage); 
        	writePageIdToPageBuffer(pidSuivPage, buffPidPrePage, true); 
        	//System.out.println("freePage"+pidPrePage);
    		BufferManager.getBufferManager().freePage(pidPrePage, true);

        	
        	//INSERTION DANS LES PAGES PLEINES
        	//On deplace la page vers les pages pleines
        	//Je recupere headerPage 
    		//System.out.println("getPage"+relInfo.headerPageId);
        	ByteBuffer headerPage = BufferManager.getBufferManager().getpage(relInfo.headerPageId); 
        	PageId pidPrecedent = readPageIdFromPageBuffer(headerPage, false); 
        	writePageIdToPageBuffer(relInfo.headerPageId, page, false); //update precedant de page
        	writePageIdToPageBuffer(pageId, headerPage, false); //update precedant dans buffer
        	writePageIdToPageBuffer(pidPrecedent, page, true); //update suivant dans la page
    		//System.out.println("freePage"+relInfo.headerPageId);
        	BufferManager.getBufferManager().freePage(relInfo.headerPageId, true);

        	if(!pidPrecedent.equals(pIdFactice)){ //S'il existe une page pleine alors avant insertion
        		//System.out.println("getPage"+pidPrecedent);
        		ByteBuffer pagePleine = BufferManager.getBufferManager().getpage(pidPrecedent); 
        		writePageIdToPageBuffer(pageId, pagePleine, false); //update precedant dans la pagePleine
        		//System.out.println("freePage"+pidPrecedent);
        		BufferManager.getBufferManager().freePage(pidPrecedent, true);
        	}
        	
        	//On libere la page aupres de BufferManager avec dirty 
        	//System.out.println("freePage"+pageId); 
        	BufferManager.getBufferManager().freePage(pageId, true);
        	//System.out.println(BufferManager.getBufferManager());
        	return new Rid(pageId, slotIdx); 
        }
        else  {
        	//On ecrit le record dans le slot lib
        	//On libere la page aupres de BufferManager avec dirty
        	record.writeToBuffer(page, Integer.BYTES*4 + nbSlots + record.relation.recordSize*slotIdx);
        	page.put(Integer.BYTES*4+ slotIdx, (byte)1); //on update la bytemap
        	//System.out.println("freePage"+pageId);
        	BufferManager.getBufferManager().freePage(pageId, true);
        	return new Rid(pageId, slotIdx); 
        }

    }
    
    public void deleteRecordFromRelation(Rid rid) {
    	//System.out.println("getPage"+rid.pageid);
    	ByteBuffer pageBuffer=BufferManager.getBufferManager().getpage(rid.pageid);
    	pageBuffer.position(Integer.BYTES*4 +rid.slotIdx);
    	pageBuffer.put(new byte[] {0}, 0, 1);
    	BufferManager.getBufferManager().freePage(rid.pageid, true);
    	 
    }
    public Record [] getRecordsInDataPage(RelationInfo relInfo, PageId pageId) {
    	Record [] recordList = new Record[relInfo.slotCount]; 
    	//System.out.println("getpage"+pageId); 
    	ByteBuffer page = BufferManager.getBufferManager().getpage(pageId); 
    	Record tmpRecord = new Record(relInfo); 
    	page.position(Integer.BYTES*4); 
    	for(int i = 0; i< recordList.length; i++) {
    		byte tmp = page.get(Integer.BYTES*4 +i); 
    		if(tmp==(byte)1) { //On verifie la bytemap 
    			tmpRecord.readFromBuffer(page, Integer.BYTES*4 +relInfo.slotCount + i*relInfo.recordSize); 
        		recordList[i]=tmpRecord; 
        		recordList[i].rid = new Rid(pageId, i);
    		}
    		
    	}
    	//System.out.println("freePage"+pageId);
    	BufferManager.getBufferManager().freePage(pageId, false);
    	return recordList; 
    }
    
    public Rid InsertRecordIntoRelation(RelationInfo relInfo, Record record) {
    	//On ecrit le record dans le premier slot libre 
    	PageId prochainLibre = this.getFreeDataPageId(relInfo);
    	//System.out.println("getFreeDataPageId :"+ prochainLibre);
    	return writeRecordToDataPage(relInfo, record, prochainLibre);
    }
    
    public Record [] getAllRecords(RelationInfo relInfo) {
    	//System.out.println("Debut de getAllRecords "+BufferManager.getBufferManager());
    	PageId pidFactice = new PageId(-1, 0); 
    	ArrayList<Record> allRecords = new ArrayList<Record>(); //la liste ou on va mettre tous les records 
    	//On recupere le headerFile
    	//System.out.println("getPage"+relInfo.headerPageId);
    	ByteBuffer headerPage = BufferManager.getBufferManager().getpage(relInfo.headerPageId); 
    	//On getRecords de toutes les pages
    		//On get les record des pages pleines, puis les records des pages nonPleines 
    	Record tmpRecord = new Record(relInfo);
    	PageId pageId = readPageIdFromPageBuffer(headerPage, false);
    	while(!pageId.equals(pidFactice)) {
    		//System.out.println("getPage"+pageId);
    		ByteBuffer bufferPage = BufferManager.getBufferManager().getpage(pageId); 
        	//bufferPage.position(Integer.BYTES*4); 
	    	for(int i = 0; i< relInfo.slotCount; i++) { //ici on est dans les pages pleines, pas la peine de verifier la bytemap
	    		//byte tmp = bufferPage.get(Integer.BYTES*4 +i); 
	    		tmpRecord = new Record(relInfo);	
	    		tmpRecord.readFromBuffer(bufferPage, Integer.BYTES*4 +relInfo.slotCount + i*relInfo.recordSize); 
	    		//System.out.println("Dans les pages pleines : " + tmpRecord);
	    		tmpRecord.rid = new Rid(pageId, i); 
    			allRecords.add(tmpRecord);
	        }
	    	//System.out.println("freePage"+pageId);
	    	BufferManager.getBufferManager().freePage(pageId, false);
	    	pageId = readPageIdFromPageBuffer(bufferPage, true); 
    	}
    	//On recupere les record des pages non pleines 
    	pageId = readPageIdFromPageBuffer(headerPage, true); 

    	//System.out.println("freePage"+relInfo.headerPageId);
    	BufferManager.getBufferManager().freePage(relInfo.headerPageId, false);
    	while(!pageId.equals(pidFactice)) {
    		//System.out.println("Pid page non pleine actuelle : " + pageId + "ET SLOT COUNT " + relInfo.slotCount);
    		//System.in.read()
    		//System.out.println(BufferManager.getBufferManager());
    		//System.out.println("getPage"+pageId);
    		ByteBuffer bufferPage = BufferManager.getBufferManager().getpage(pageId);
    		bufferPage.position(Integer.BYTES*4); 
    		for(int i = 0; i< relInfo.slotCount; i++) {
    			tmpRecord = new Record(relInfo);
        		byte tmp = bufferPage.get(Integer.BYTES*4 +i); 
        		if(tmp==(byte)1) { //On verifie la bytemap parce qu'on ne prend que les slots remplis 
        			tmpRecord.readFromBuffer(bufferPage, Integer.BYTES*4 +relInfo.slotCount + i*relInfo.recordSize); 
        			//System.out.println("Dans les pages pas pleines : " + tmpRecord);
        			tmpRecord.rid = new Rid(pageId, i); 
        			allRecords.add(tmpRecord); 
        		}
    		}
    		//System.out.println("freePage"+pageId);
    		BufferManager.getBufferManager().freePage(pageId, false);
	    	pageId = readPageIdFromPageBuffer(bufferPage, true); 
    	}
    //System.out.println(allRecords);
    //System.out.println("******************************************************************");
    //System.out.println(Arrays.toString(allRecords.toArray(Record []::new)));
    return allRecords.toArray(Record []::new); //VERFIER QUE CA MARCHE BIEN 
    }
    
    
    public static FileManager getFileManager() {
    	return fileManager; 
    }

	public void setFileManager() {
		fileManager= new FileManager();
		
	}
}
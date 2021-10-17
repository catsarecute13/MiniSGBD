import java.nio.ByteBuffer;

public class FileManager{
    public PageId createHeaderPage(){
        PageId nouvPage=DiskManager.getDiskManager().AllocPage();
        PageId pIdFactice=new PageId(-1,0);
        ByteBuffer page=BufferManager.getBufferManager().getpage(nouvPage);
        writePageIdToPageBuffer(pIdFactice,page,false);
        writePageIdToPageBuffer(pIdFactice,page,true);
        BufferManager.getBufferManager().FreePage(nouvPage, true);
        return nouvPage;
    }

    public PageId addDataPage(RelationInfo relInfo){
        PageId nouvPage=DiskManager.getDiskManager().AllocPage();
        PageId idPrevious=relInfo.headerPageId;

        ByteBuffer headerPage=BufferManager.getBufferManager().getpage(idPrevious);
        PageId idNext=readPageIdFromPageBuffer(headerPage,true);
        writePageIdToPageBuffer(nouvPage,headerPage,true); //TRUE c'est la page suivante, FALSE c'est la page précédente
        BufferManager.getBufferManager().FreePage(idPrevious, true);

        ByteBuffer nextPage=BufferManager.getBufferManager().getpage(idNext);
        writePageIdToPageBuffer(nouvPage,nextPage,false);
        BufferManager.getBufferManager().FreePage(idNext, true);

        ByteBuffer dataPage=BufferManager.getBufferManager().getpage(nouvPage);
        writePageIdToPageBuffer(idPrevious,dataPage,false);
        writePageIdToPageBuffer(idNext,dataPage,true);
        BufferManager.getBufferManager().FreePage(nouvPage, true);
        return nouvPage;
    }

    public PageId getFreeDataPageId(RelationInfo relInfo){
        PageId pIdFactice=new PageId(-1,0);
        PageId header=relInfo.headerPageId;
        ByteBuffer headerPage=BufferManager.getBufferManager().getpage(header);
        PageId idPage=readPageIdFromPageBuffer(headerPage,true);
        BufferManager.getBufferManager().FreePage(header, false);
        if (idPage.equals(pIdFactice)){
            return addDataPage(relInfo);
        } else{
            return idPage;
        }
    }

    public rid writeRecordToDataPage(RelationInfo relInfo, Record record, PageId pageId){
        ByteBuffer page=BufferManager.getBufferManager().getpage(pageId);
        int nbSlots=relInfo.slotCount;
        int nbLibre=0;
        int slotIdx=-1;
        for(int i=0;i<nbSlots;i++){
            if (page.get(Integer.BYTES*4+i)==0){
                nbLibre+=1; //Nb de slots libres: si nbLibre>1, on ne déplace pas la page vers les pages pleines.
                if (nbLibre==1){
                    slotIdx=i; //Le slot qu'on va utiliser
                }
            }
        } 

        if (nbLibre!=1){
        }

    }
}
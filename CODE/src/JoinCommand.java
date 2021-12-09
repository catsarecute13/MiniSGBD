import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class JoinCommand {
	ArrayList<Record> resultat;
    RelationInfo rel1,rel2;
    String nomRel1,nomRel2;
    String [] champ1, champ2;
    public JoinCommand(String commande){
        try{
            resultat=new ArrayList<Record>();
            StringTokenizer st=new StringTokenizer(commande);
            st.nextToken();
            st.nextToken();
            st.nextToken();
            String rels=st.nextToken();
            st.nextToken();
            String cond=st.nextToken();
            st=new StringTokenizer(rels,",");
            nomRel1=st.nextToken();
            nomRel2=st.nextToken();
            rel1=Catalog.getCatalog().getRelation(nomRel1);
            rel2=Catalog.getCatalog().getRelation(nomRel2);
            st=new StringTokenizer(cond,"=");
            String part1=st.nextToken();
            String part2=st.nextToken();
            champ1=new String[2];
            champ2=new String[2];
            st=new StringTokenizer(part1,"."); 
            champ1[0]=st.nextToken(); //NomRel1
            champ1[1]=st.nextToken(); //NomCol1
            st=new StringTokenizer(part2,".");
            champ2[0]=st.nextToken(); //NomRel2
            champ2[1]=st.nextToken(); //NomCol2
        }
        catch(Exception e){ //On peut aussi stocker l'exception dans e et Raise Exception dans la méthode execute..
            System.out.println("Format Incorrect de la commande SelectJoin");
        }
    }
    
    public void Execute() {
    	//je dois parcourir toutes les pages de la relation, pour chaque page, je compare les tuples de la pages, à tous les tuples de la relation 2
    	//pages pleines
    	int nb_merge=0;  
    	RelationInfo rel1 = Catalog.getCatalog().getRelation(champ1[0]); //on récupère la première relation
    	RelationInfo rel2 = Catalog.getCatalog().getRelation(champ2[0]);//on récupère la seconde relation
    	Record [] records_r2= FileManager.getFileManager().getAllRecords(rel2); //je récupère tous les record de la seconde relation
    	//System.out.println("Les records de"+ rel2.nomRelation); 
    	//for(int k=0; k<records_r2.length;k++) {
    	//	System.out.println(records_r2[k]);
    	//}
    	//System.out.println("---------------------Debut des comparaisons------------------------");
    	int indexCol_r1 = rel1.getIdxInfoCol(champ1[1]); 
    	int indexCol_r2 = rel2.getIdxInfoCol(champ2[1]);
    	ByteBuffer headerPagebuff= BufferManager.getBufferManager().getpage(rel1.headerPageId); 
    	PageId PidSuiv = FileManager.getFileManager().readPageIdFromPageBuffer(headerPagebuff, false); //false --> page pleines 
    	//System.out.println(PidSuiv);
    	BufferManager.getBufferManager().freePage(rel1.headerPageId, false);
    	PageId factice = new PageId(-1,0);
    	 while(!PidSuiv.equals(factice)) {
    		  //je récupère la page 
    		 Record [] record_page_rel1 = FileManager.getFileManager().getRecordsInDataPage(rel1, PidSuiv);
    		 //System.out.println("Les records de la page "+ PidSuiv.PageIdx+ " de"+ rel1.nomRelation); 
    	    	//for(int k=0; k<record_page_rel1.length;k++) {
    	    		//System.out.println(record_page_rel1[k]);
    	    	//}
    	   // System.out.println("-------------");
    		 //Je compare les tuples de la page avec tous ceux de rel2
    		 for(int i=0; i<record_page_rel1.length; i++) {
    			 for(int j=0; j<records_r2.length; j++) {
    				// System.out.println("record de "+ rel1.nomRelation+":"+ record_page_rel1[i]);
    				 //System.out.println("record de "+rel2.nomRelation+": "+records_r2[j]);
    				 boolean egaux = record_page_rel1[i].compareTo(indexCol_r1, records_r2[j], indexCol_r2);
    				 //System.out.println("egaux :" + (egaux ? "oui" : "non"));
    				 //System.out.println("******************");
    				 if(egaux) {
    					// System.out.println("merging...");
    					 System.out.println(record_page_rel1[i].merge(records_r2[j]));
    					 //System.out.println("****************");
    					 nb_merge++; 
    				 }
    			 }
    		 } 
    		 PageId tmp = new PageId(PidSuiv); //le pid à free 
    		 ByteBuffer pageSuiv= BufferManager.getBufferManager().getpage(PidSuiv);
    		 PidSuiv= FileManager.getFileManager().readPageIdFromPageBuffer(pageSuiv, true);//la prochaine page à lire
    		 BufferManager.getBufferManager().freePage(tmp, false);
    	 }
    	 //pages non pleines
    	headerPagebuff= BufferManager.getBufferManager().getpage(rel1.headerPageId); //headerPage
     	PageId PidPre= FileManager.getFileManager().readPageIdFromPageBuffer(headerPagebuff, true); //true --> pages non pleines 
     	BufferManager.getBufferManager().freePage(rel1.headerPageId, false);//je free la headerPage
     	while(!PidPre.equals(factice)) {
  		  //je récupère la page 
  		 Record [] record_page_rel1 = FileManager.getFileManager().getRecordsInDataPage(rel1, PidPre); 
  		//System.out.println("Les records de la page "+ PidPre.PageIdx+ " de"+ rel1.nomRelation); 
    	//for(int k=0; k<record_page_rel1.length;k++) {
    		//System.out.println(record_page_rel1[k]);
    	//}
    	//System.out.println("-------------");
  		 //Je compare les tuples de la page avec tous ceux de rel2
  		 for(int i=0; i<record_page_rel1.length; i++) {
  			 for(int j=0; j<records_r2.length; j++) {
  				//System.out.println("record de " +rel1.nomRelation +": " + record_page_rel1[i]);
				 //System.out.println("record de " +rel2.nomRelation +": "+ records_r2[j]);
				 boolean egaux = record_page_rel1[i].compareTo(indexCol_r1, records_r2[j], indexCol_r2);
				// System.out.println("egaux :" + (egaux ? "oui" : "non"));
				 //System.out.println("******************");
  				 if(egaux) {
  					// System.out.println("merging...");
					 System.out.println(record_page_rel1[i].merge(records_r2[j]));
					 //System.out.println("****************");
  					 //System.out.println(record_page_rel1[i].merge(records_r2[j]));
  					 nb_merge++; 
  				 }
  			 }
  		 }
  		 PageId tmp = new PageId(PidPre); //le pid à free 
  		 ByteBuffer pageSuiv= BufferManager.getBufferManager().getpage(PidPre);//buffer
  		 PidPre= FileManager.getFileManager().readPageIdFromPageBuffer(pageSuiv, true);//la prochaine page à lire
  		 //System.out.println(PidPre);
  		 BufferManager.getBufferManager().freePage(tmp, false);
  	 }
     System.out.println("Total records :"+ nb_merge);
     
    }


}

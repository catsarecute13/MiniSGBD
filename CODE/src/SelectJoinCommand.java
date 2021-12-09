import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class SelectJoinCommand{
    ArrayList<Record> resultat;
    RelationInfo rel1,rel2;
    String nomRel1,nomRel2;
    String [] champ1, champ2;
    public SelectJoinCommand(String commande){
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

    public void Execute(){
        ArrayList<Record> res =new ArrayList<Record>(Arrays.asList(FileManager.getFileManager().getAllRecords(rel1))); //Les records de la Rel1
        int count=0;
        for(Record r:res){ // Parcourir la rel1
            int index;
            SelectMonoCommand smc;
            if (champ1[0].equals(nomRel1)){ // Verifier le champs 
                index=rel1.getIdxInfoCol(champ1[1]);
                smc=new SelectMonoCommand("SELECTMONO * FROM "+champ2[0]+" WHERE "+champ2[1]+"="+r.values[index]); // SELECTMONO FROM REL2 WHERE COL2=Valeur de COL1
            }else {
                index=rel1.getIdxInfoCol(champ2[1]);
                smc=new SelectMonoCommand("SELECTMONO * FROM "+champ1[0]+" WHERE "+champ1[1]+"="+r.values[index]);
            }
            smc.Update(); //Cette méthode nous donne la liste des elements de Rel2 stockés dans la variable upd
            for(Record s:smc.upd){
                System.out.println(s.merge(r)); //Méthode ajoutée à la classe Record qui donne une chaine de la forme "(record1,record2)"
                count++;
            }
        }
        System.out.println("Nombre TOTAL: "+count);
    }
}
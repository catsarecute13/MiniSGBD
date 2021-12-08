import java.util.StringTokenizer;
import java.util.ArrayList;

public class SelectIndexCommand{
    public String nomRelation;
    public RelationInfo relation;
    public String nomColonne;
    public int value;

    public SelectIndexCommand(String commande){
        StringTokenizer st=new StringTokenizer(commande);
        st.nextToken();
        st.nextToken();
        st.nextToken();
        nomRelation=st.nextToken();
        relation = Catalog.getCatalog().getRelation(nomRelation);
        st.nextToken();
        String tmp=st.nextToken();
        st=new StringTokenizer(tmp,"=");
        nomColonne=st.nextToken();
        value=Integer.parseInt(st.nextToken());
    }

    public void Execute(){
        CreateIndexCommand resultat=null;
        for(CreateIndexCommand ind: CreateIndexCommand.indexes){
            if (ind.key.equals(nomColonne)){
                resultat=ind;
                break;
            }
        }
        if (resultat==null){
            return;
        }
        ArrayList<Record> res=resultat.index.search(value);
        for (Record r:res){
            System.out.println(r);
        }
    }
}
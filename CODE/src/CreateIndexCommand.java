import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateIndexCommand{
    public String nomRelation;
    public RelationInfo relation;
    public String key;
    public int order;
    public BTree index;
    public static ArrayList<CreateIndexCommand> indexes=new ArrayList<CreateIndexCommand>();

    public CreateIndexCommand(String commande){
        StringTokenizer st=new StringTokenizer(commande);
        st.nextToken();
        st.nextToken();
        nomRelation=st.nextToken();
        relation = Catalog.getCatalog().getRelation(nomRelation);
        key=st.nextToken();
        String tmp=st.nextToken();
        st=new StringTokenizer(key,"=");
        st.nextToken();
        key=st.nextToken();
        st=new StringTokenizer(tmp,"=");
        st.nextToken();
        order=Integer.parseInt(st.nextToken());
        index=new BTree(order);
    }

    public void Execute(){
        //if index existe dans indexes où key alors system.out.println("Probleme")
        for (CreateIndexCommand index: indexes){
            if (index.key.equals(key)){
                System.out.println("Index sur la colonne "+key+" existe déjà.");
                return;
            }
        }
        ArrayList<Record> rec =new ArrayList<Record>(Arrays.asList(FileManager.getFileManager().getAllRecords(relation)));
        for(Record r:rec){
            int col=relation.getIdxInfoCol(key);
            index.insert((int)r.values[col],r);
        }
        indexes.add(this);
    }

    public void Insert(Record r){
        int col=relation.getIdxInfoCol(key);
        index.insert((int)r.values[col],r);
    }

    public void Delete(Record r){
        //Delete record r into BTree
        int col=relation.getIdxInfoCol(key);
        index=index.Remove((int)r.values[col]);
        
    }
}
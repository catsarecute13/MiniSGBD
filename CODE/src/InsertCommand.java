import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer; 

public class InsertCommand{
    public RelationInfo relation;
    public String nomRelation;
    public Record record;
    public List<String> valuesRecord ; 
    public InsertCommand(String chaine){
        StringTokenizer ch=new StringTokenizer(chaine);
        ch.nextToken();
        ch.nextToken();
        nomRelation=ch.nextToken();
        ch.nextToken();
        String avDelim=ch.nextToken();
        StringBuffer av=new StringBuffer(avDelim);
        av.deleteCharAt(0);
        av.deleteCharAt(av.length()-1);
        ch=new StringTokenizer(av.toString(),",");
        valuesRecord=new ArrayList<String>();
        while(ch.hasMoreTokens()){
            valuesRecord.add(ch.nextToken());
        }
        //Chercher dans le catalog la relation
        for(RelationInfo e:Catalog.getCatalog().relationTab){
            if (e.nomRelation.equals(nomRelation)){
                relation=e;
            }
        }
        //creer un record
        record=new Record(relation);
    }

    public void Execute(){
        //comparer les types entre infoCol(RelationInfo) et le tableau l.
        ByteBuffer buff=ByteBuffer.allocate(relation.recordSize); //FAUX ALLOCATE
        int taille = valuesRecord.size(); 
        for(int i=0;i<taille;i++){
            if (relation.infoCol.get(i).equals("int")){
                buff.putInt(Integer.parseInt(valuesRecord.get(i)));
            }
            else if (relation.infoCol.get(i).equals("float")){
                buff.putFloat(Float.parseFloat(valuesRecord.get(i)));
            }
            else {
                buff.put(valuesRecord.get(i).getBytes());
            }
        }
        //mettre dans record
        record.readFromBuffer(buff,0);
    }
}
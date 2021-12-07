import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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
                //System.out.println("relation :" +relation + "e : "+ e);
            }
        }
        //creer un record
        //System.out.println(relation); 
        record=new Record(relation);
        
    }

    public void Execute(){
        //comparer les types entre infoCol(RelationInfo) et le tableau l.
        ByteBuffer buff=ByteBuffer.allocate(relation.recordSize); 
        int taille = valuesRecord.size(); 
        for(int i=0;i<taille;i++){
            if (relation.infoCol.get(i).typeCol.equals("int")){
                buff.putInt(Integer.parseInt(valuesRecord.get(i)));
            }
            else if (relation.infoCol.get(i).typeCol.equals("float")){
                buff.putFloat(Float.parseFloat(valuesRecord.get(i)));
            }
            else {
                buff.put(valuesRecord.get(i).getBytes(StandardCharsets.UTF_16));
            }
        }
        //mettre dans record
        record.readFromBuffer(buff,0);
        FileManager.getFileManager().InsertRecordIntoRelation(relation, record);
    }
}
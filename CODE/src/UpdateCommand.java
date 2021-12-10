import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class UpdateCommand {
    String nomRelation;
    RelationInfo relation;
    ArrayList<String> colonnes;
    String where;
    public UpdateCommand(String commande){
        StringTokenizer ch=new StringTokenizer(commande);
        ch.nextToken();
        nomRelation=ch.nextToken();
        relation = Catalog.getCatalog().getRelation(nomRelation);
        ch.nextToken();
        colonnes=new ArrayList<String>();
        String champsColonne=ch.nextToken();
        StringTokenizer st=new StringTokenizer(champsColonne,",");
        while(st.hasMoreTokens()){
            colonnes.add(st.nextToken());
        }
        where=ch.nextToken("");
    }

    public void Execute(){
        SelectMonoCommand commande=new SelectMonoCommand("SELECTMONO * FROM "+nomRelation+where); //oubli des espaces? 
        commande.Update();
        ArrayList<Record> res=commande.upd;
        ArrayList<Record> comp=commande.upd;
        HashMap<String,String> colonne=new HashMap<String,String>();
        if (colonnes.size()==0){
            System.out.println("Il n'y a aucune colonne dans la partie SET");
            return;
        }
        for(String c:colonnes){
            StringTokenizer ch=new StringTokenizer(c,"=");
            String key=ch.nextToken();
            String valeur=ch.nextToken();
            if (colonne.containsKey(key)){
                System.out.println("Une colonne de la relation apparaît plusieurs fois");
                return;
            }
            colonne.put(key,valeur);
        }
        //Pour chaque record
        for(Record r: res){
            //Pour chaque colonne de record, on vérifie si le record doit être changé
            for(int i=0;i<relation.infoCol.size();i++){
                if (colonne.containsKey(relation.infoCol.get(i).nomCol)){
                    try{
                        if (relation.infoCol.get(i).typeCol.trim().toLowerCase().equals("int")) {
                            r.values[i]=Integer.parseInt(colonne.get(relation.infoCol.get(i).nomCol));
                        }
                        else if (relation.infoCol.get(i).typeCol.trim().toLowerCase().equals("float")) {
                            r.values[i]=Float.parseFloat(colonne.get(relation.infoCol.get(i).nomCol));
                        }
                        else {
                            r.values[i]=colonne.get(relation.infoCol.get(i).nomCol);
                        }
                    }
                    catch(Exception e){
                        System.out.println("Une des valeurs n'est pas compatible avec le type de la colonne");
                        return;
                    }
                }
                else {
                    //Si la colonne n'est pas dans la liste de colonnes à changer 
                    //DO NOTHING
                }
            }
        }
        DeleteCommand del=new DeleteCommand("DELETE FROM "+nomRelation+where);
        del.Update();
        int tupCount=0;
        for (int i=0;i<res.size();i++){
            StringBuffer buff=new StringBuffer();
            if (res.get(i).equals(comp.get(i))){
                tupCount++;
            }
            InsertCommand insert=new InsertCommand("INSERT INTO "+nomRelation+" RECORD "+res.get(i).toString());
            insert.Execute();
        }
        System.out.println("Total updated records: "+tupCount);
    }
}
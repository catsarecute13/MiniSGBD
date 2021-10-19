import java.util.StringTokenizer;

public class BatchInsertCommand{
    String nomRelation;
    String nomFichier;
    public BatchInsertCommand(String chaine){
        StringTokenizer ch=new StringTokenizer(chaine);
        for(int i=0;ch.hasMoreElements();i++){
            if (i==2){
                nomRelation=ch.nextToken();
            } else if (i==5){
                nomFichier=ch.nextToken();
            } else {
                ch.nextToken();
            }
        }
    }

    public void Execute(){
        File file = new File(DBParams.dossierPrincipale+nomFichier);    //Ouverture
        FileReader fileReader = new FileReader(file);                   //du fichier
        BufferedReader bufferedReader = new BufferedReader(fileReader); //CSV
        String line;
        while ((line = bufferedReader.readLine()) != null) { //On lit ligne par ligne
            String chaine="INSERT INTO "+nomRelation+" RECORD ("+line+")"; // on crée une chaine de la forme "INSERT INTO nomRelation RECORD (val1,val2, … ,valn)" 
            InsertCommand insert=new InsertCommand(ch); //insérer la ligne
            insert.Execute(); //Executer
        }
    }
}
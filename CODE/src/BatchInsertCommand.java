import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    //il faut ajouter un try catch
    public void Execute() throws Exception{
        File file = new File(DBParams.DBPath+nomFichier);    //Ouverture
        FileReader fileReader = new FileReader(file);                   //du fichier
        BufferedReader bufferedReader = new BufferedReader(fileReader); //CSV
        String line;
        while ((line = bufferedReader.readLine()) != null) { //On lit ligne par ligne
            String chaine="INSERT INTO "+nomRelation+" RECORD ("+line+")"; // on crée une chaine de la forme "INSERT INTO nomRelation RECORD (val1,val2, … ,valn)" 
            InsertCommand insert=new InsertCommand(chaine); //insérer la ligne
            insert.Execute(); //Executer
        }
    }
}
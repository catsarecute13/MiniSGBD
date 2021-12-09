import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList; 

public class Catalog {
	private static Catalog catalog = Init(); 
	public  ArrayList<RelationInfo> relationTab; 
	public int compteur; 
	
	public Catalog() {
		relationTab = new ArrayList<RelationInfo>(0); 
		compteur = 0; 
	}
	
	public static Catalog Init() {
		try {
            FileInputStream f = new FileInputStream (DBParams.DBPath+"/Catalog.def");
            ObjectInputStream s = new ObjectInputStream(f);
			//relationTab =((Catalog) s).readObject().relationTab; 
			//compteur = (Catalog) s.readObject().compteur;
			catalog = (Catalog)s.readObject(); 
			s.close();
			f.close();
        }catch (FileNotFoundException e){
        	return new Catalog(); 
        }
		
        catch (IOException e){
			System.out.println(" Erreur E/S ");
			e.printStackTrace();
		    return new Catalog(); 
		}
        catch (ClassNotFoundException e){
			System.out.println(" Pb classe ");
			return new Catalog(); 
		}
		
		return catalog;
		
		
	}
	public void Finish() {
		try {
			FileOutputStream f = new FileOutputStream (DBParams.DBPath+"/Catalog.def");
			ObjectOutputStream s = new ObjectOutputStream(f);
			s.writeObject(this);
			s.flush();
			s.close();
			f.close();
		}
		catch (IOException e){
			System.out.println(" Erreur E/S ");
			e.printStackTrace();
		}
		relationTab.clear();
		compteur = 0; 
	}
	
	public void AddRelation(RelationInfo relation) {
		if (!relationTab.contains(relation)){
			relationTab.add(relation);
			compteur += 1; 
		}
		
	}
	
	public RelationInfo getRelation(String nomRel) {
		for(RelationInfo e : relationTab) {
			if(e.nomRelation.equals(nomRel)) {
				return e; 
			}
		}
		return null; 
	}
	
	static public Catalog getCatalog() {
		return catalog;}
	
	public void setCatalog() {
		catalog = new Catalog(); 
		
	}
}

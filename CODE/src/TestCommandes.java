
public class TestCommandes {

	public static void main(String[] args) {
		try {
			CreateRelationCommand create = new CreateRelationCommand("CREATE RELATION R (X:int,C2:float,BLA:string10)");
			create.Execute();
			System.out.println(create);  
			System.out.println("************Catalog***********"); 
			System.out.println("compteur :" + Catalog.getCatalog().compteur);
			for(int i = 0; i<Catalog.getCatalog().relationTab.size(); i++) {
				System.out.println(Catalog.getCatalog().relationTab.get(i).toString()); 
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

}

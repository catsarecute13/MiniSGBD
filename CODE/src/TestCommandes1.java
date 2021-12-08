
public class TestCommandes1 {

	public static void main(String[] args) {
		try {
			DROPDBCommand drop = new DROPDBCommand("DROPDB");
			drop.Execute();
			CreateRelationCommand create = new CreateRelationCommand("CREATE RELATION R (X:int,C2:float,BLA:string10)");
			create.Execute();
			//System.out.println(create);  
			//System.out.println("************Catalog***********"); 
			//System.out.println("compteur :" + Catalog.getCatalog().compteur);
			/**for(int i = 0; i<Catalog.getCatalog().relationTab.size(); i++) {
				System.out.println(Catalog.getCatalog().relationTab.get(i).toString()); 
			}*/
			
			/*for (int i=0;i<300;i++) {
				InsertCommand insert = new InsertCommand("INSERT INTO R RECORD ("+i+",2.0,0123456789)");
				insert.Execute();
			}*/

			InsertCommand inser = new InsertCommand("INSERT INTO R RECORD (1,9.0,0123456789)");
			inser.Execute();
      		inser = new InsertCommand("INSERT INTO R RECORD (10,3.0,0123456789)");
		    inser.Execute();
		    inser = new InsertCommand("INSERT INTO R RECORD (3,2.0,0123456789)");
		    inser.Execute();
		    inser = new InsertCommand("INSERT INTO R RECORD (10,5.0,0123456789)");
		    inser.Execute();
		    inser = new InsertCommand("INSERT INTO R RECORD (5,4.0,0123456789)");
			inser.Execute();
		    inser = new InsertCommand("INSERT INTO R RECORD (6,10.0,0123456789)");
		    inser.Execute();
		    inser = new InsertCommand("INSERT INTO R RECORD (5,0.0,0123456789)");
		    inser.Execute();

			CreateIndexCommand creat=new CreateIndexCommand("CreateIndex on R Key=X ORDER=2");
			creat.Execute();
			SelectIndexCommand selec=new SelectIndexCommand("SELECTINDEX * FROM R WHERE X=6");
			selec.Execute();

			System.out.println("*****************************************");
			inser = new InsertCommand("INSERT INTO R RECORD (6,10.0,34657)");
		    inser.Execute();

			selec=new SelectIndexCommand("SELECTINDEX * FROM R WHERE X=6");
			selec.Execute();

			System.out.println("*****************************************");

			DeleteCommand del=new DeleteCommand("DELETE FROM R WHERE X=6");
			del.Execute();

			selec=new SelectIndexCommand("SELECTINDEX * FROM R WHERE X=6");
			selec.Execute();


			UpdateCommand upda=new UpdateCommand("Update R SET X=6 Where X>=7");
			upda.Execute();
			
			selec=new SelectIndexCommand("SELECTINDEX * FROM R WHERE X=6");
			selec.Execute();
			
			/*SelectMonoCommand select = new SelectMonoCommand("SELECTMONO * FROM R"); 
			select.Execute();

			/*select.Update();
			
     		ArrayList<Record> up=select.upd;
      		System.out.println(up);
      		BTree b = new BTree(2);
      		b.insert(3,up.get(0));
      		b.insert(2,up.get(1));
      		b.insert(5,up.get(2));
      		b.insert(5,up.get(3));
    		b.insert(10,up.get(4));
    		b.insert(7,up.get(5));

      		b.display();
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			System.out.println(b.search(5));
			b=b.Remove(5);
			System.out.println("********************");
			System.out.println(b.search(5));
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			b.display();*/

			
			
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

}
